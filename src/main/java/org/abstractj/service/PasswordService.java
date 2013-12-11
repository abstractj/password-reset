package org.abstractj.service;

import org.abstractj.api.ExpirationTime;
import org.abstractj.model.Token;

import javax.ejb.EJBException;
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

    public Token generate(ExpirationTime expiration) {

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

    public boolean isValid(Token token) {
        return (token != null && !expirationTime.isExpired(token.getExpiration()));
    }

    public Token findTokenById(String id) {

        Token token;
        try {
            token = em.createQuery("SELECT t FROM Token t WHERE t.id = :id and t.used = :used", Token.class)
                    .setParameter("id", id)
                    .setParameter("used", false)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw new RuntimeException("Not valid");
        }

        return token;
    }

    //Fake service to validate the user against the database
    public boolean userExists(String email){
        return (email != null || !email.isEmpty());
    }

    //Send to some place the url for password reset
    public void send(String uri) {
        LOGGER.info("Sending password reset instructions");
        LOGGER.info("===================================");
        LOGGER.info(uri);
        LOGGER.info("===================================");

    }
}
