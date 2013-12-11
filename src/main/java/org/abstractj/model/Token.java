package org.abstractj.model;

import org.abstractj.api.ExpirationTime;
import org.abstractj.util.Configuration;
import org.bouncycastle.util.encoders.Base64;
import org.jboss.aerogear.AeroGearCrypto;
import org.jboss.aerogear.crypto.Random;
import org.jboss.aerogear.crypto.password.Pbkdf2;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.persistence.Entity;
import javax.persistence.Id;
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

    public Token(ExpirationTime expirationTime) {
        this.id = generateToken();
        this.sentAt = expirationTime.getCurrentTime();
        this.expiration = expirationTime.getExpirationDate();
    }

    private String generateToken() {

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

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

}
