package org.abstractj.service;

import org.abstractj.api.ExpirationTime;
import org.abstractj.api.service.TokenService;
import org.abstractj.api.util.Configuration;
import org.abstractj.fixture.FakeService;
import org.abstractj.model.Token;
import org.jboss.aerogear.crypto.Hmac;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Logger;

@Stateless
public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = Logger.getLogger(TokenServiceImpl.class.getSimpleName());

    @Inject
    private EntityManager em;

    @Inject
    private ExpirationTime expirationTime;

    @Override
    public void destroy(String id) {
        try {
            Token token = em.find(Token.class, id);
            em.remove(token);
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isValid(String id) {

        Token token = null;
        try {
            token = em.createQuery("SELECT t FROM Token t WHERE t.id = :id", Token.class)
                    .setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            //Do nothing atm because we don't want to give any clue to an attacker
        }

        return (token != null && !expirationTime.isExpired(token.getExpiration()));
    }

    //Send to some place the url for password reset
    @Override
    public void send(String email) {

        Token token;

        //Here of course we need to validate the e-mail against the database or PicketLink
        if (FakeService.userExists(email)) {

            String secret = Configuration.getSecret();
            try {
                Hmac hmac = new Hmac(secret);
                token = save(hmac.digest());
                LOGGER.info("Sending password reset instructions");
                LOGGER.info("===================================");
                LOGGER.info(Configuration.uri(token.getId()));
                LOGGER.info("===================================");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
    }

    //Private method because it' up to the implementer
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
