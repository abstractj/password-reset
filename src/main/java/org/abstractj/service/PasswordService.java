package org.abstractj.service;

import org.abstractj.api.ExpirationTime;
import org.abstractj.fixture.FakeUserService;
import org.abstractj.model.Token;
import org.abstractj.util.Configuration;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.logging.Logger;

@Stateless
public class PasswordService {

    private static final Logger LOGGER = Logger.getLogger(PasswordService.class.getSimpleName());

    @Inject
    private EntityManager em;

    @Inject
    private ExpirationTime expirationTime;

    private Token generate(ExpirationTime expiration) {

        Token token = null;
        try {
            token = new Token(expiration);
            em.merge(token);
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }


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
        Token token = findTokenById(id);
        return (token != null && !expirationTime.isExpired(token.getExpiration()));
    }

    //Send to some place the url for password reset
    public void send(String email) {

        Token token;

        //Here of course we need to validate the e-mail against the database or PicketLink
        if (FakeUserService.userExists(email)) {
            token = generate(new ExpirationTime());
            LOGGER.info("Sending password reset instructions");
            LOGGER.info("===================================");
            LOGGER.info(Configuration.uri(token.getId()));
            LOGGER.info("===================================");
        }
    }

    private Token findTokenById(String id) {

        Token token = null;
        try {
            token = em.createQuery("SELECT t FROM Token t WHERE t.id = :id and t.used = :used", Token.class)
                    .setParameter("id", id)
                    .setParameter("used", false)
                    .getSingleResult();

        } catch (NoResultException e) {
            //Do nothing atm because we don't want to give any clue to an attacker
        }

        return token;
    }


}
