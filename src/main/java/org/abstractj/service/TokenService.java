package org.abstractj.service;

import org.abstractj.api.ExpirationTime;
import org.abstractj.fixture.FakeUserService;
import org.abstractj.model.Token;
import org.abstractj.util.Configuration;
import org.bouncycastle.util.encoders.Base64;
import org.jboss.aerogear.AeroGearCrypto;
import org.jboss.aerogear.crypto.Random;
import org.jboss.aerogear.crypto.password.Pbkdf2;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Logger;

@Stateless
public class TokenService {

    private static final Logger LOGGER = Logger.getLogger(TokenService.class.getSimpleName());
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    @Inject
    private EntityManager em;

    @Inject
    private ExpirationTime expirationTime;

    public void destroy(String id) {
        try {
            Token token = em.find(Token.class, id);
            token.setUsed(true);
            em.merge(token);
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isValid(String id) {

        Token token = null;
        try {
            token = em.createQuery("SELECT t FROM Token t WHERE t.id = :id and t.used = :used", Token.class)
                    .setParameter("id", id)
                    .setParameter("used", false)
                    .getSingleResult();

        } catch (NoResultException e) {
            //Do nothing atm because we don't want to give any clue to an attacker
        }

        return (token != null && !expirationTime.isExpired(token.getExpiration()));
    }

    //Send to some place the url for password reset
    public void send(String email) {

        Token token;

        //Here of course we need to validate the e-mail against the database or PicketLink
        if (FakeUserService.userExists(email)) {
            token = save(create());
            LOGGER.info("Sending password reset instructions");
            LOGGER.info("===================================");
            LOGGER.info(Configuration.uri(token.getId()));
            LOGGER.info("===================================");
        }
    }

    public String create() {

        Pbkdf2 pbkdf2 = AeroGearCrypto.pbkdf2();
        String secret = Configuration.getSecret();

        Mac mac = null;
        try {
            SecretKey secretKey = pbkdf2.generateSecretKey(secret);
            mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(secretKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        byte[] rawHmac = mac.doFinal(new Random().randomBytes());

        return new String(Base64.encode(rawHmac));

    }

    private Token save(String id) {

        Token token = null;
        try {
            token = new Token(id);
            em.merge(token);
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }

}
