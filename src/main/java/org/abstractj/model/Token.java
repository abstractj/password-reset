package org.abstractj.model;

import org.abstractj.api.ExpirationTime;
import org.abstractj.util.Configuration;
import org.bouncycastle.util.encoders.Base64;
import org.jboss.aerogear.AeroGearCrypto;
import org.jboss.aerogear.crypto.Random;
import org.jboss.aerogear.crypto.password.Pbkdf2;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Entity
public class Token {

    @Id
    private String id;
    private Long sentAt;
    private Long expiration;
    private Boolean used = false;

    public Token(){}

    public Token(String id, ExpirationTime expirationTime) {
        this.id = id;
        this.sentAt = expirationTime.getCurrentTime();
        this.expiration = expirationTime.getExpirationDate();
    }

    public Token(String id) {
        this(id, new ExpirationTime());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSentAt() {
        return sentAt;
    }

    public void setSentAt(Long sentAt) {
        this.sentAt = sentAt;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = true;
    }
}
