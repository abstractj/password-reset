package org.abstractj.model;

import org.abstractj.api.ExpirationTime;
import org.bouncycastle.util.encoders.Base64;
import org.jboss.aerogear.AeroGearCrypto;
import org.jboss.aerogear.crypto.Random;
import org.jboss.aerogear.crypto.password.Pbkdf2;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Properties;

import static org.jboss.aerogear.AeroGearCrypto.DERIVED_KEY_LENGTH;

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
        String secret = loadProperties().getProperty("config.secret");

        Mac mac = null;
        try {
            SecretKey secretKey = pbkdf2.generateSecretKey(secret);
            mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
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


    private Properties loadProperties() {
        Properties props = new Properties();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("META-INF/config.properties");
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return props;
    }

    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA256";
    private static final int ITERATIONS = 20000;
    private static final byte[] salt = new Random().randomBytes();


}
