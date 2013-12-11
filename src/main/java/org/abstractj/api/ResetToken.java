package org.abstractj.api;

import org.abstractj.util.Configuration;
import org.bouncycastle.util.encoders.Base64;
import org.jboss.aerogear.AeroGearCrypto;
import org.jboss.aerogear.crypto.Random;
import org.jboss.aerogear.crypto.password.Pbkdf2;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ResetToken {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    //Generate an unique ID to the token, probably will be moved to ag-crypto-java
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

}
