package com.am.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.joda.time.DateTime;
import play.Logger;
import play.libs.Json;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.*;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SecurityHelper {

    private static final Logger.ALogger logger = Logger.of(SecurityHelper.class);

    public static String generateUID() {
        // generate 32 characters uid
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateRandomString(int length){
        String rnd = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        return rnd.substring(0, length);
    }


    public static Claims verifyJWTToken(String token, String secretKey) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception ex) {
            logger.error("verifyJWTToken: {}", ex.getMessage());
        }
        return null;
    }

    public static JsonNode getJWTClaim(String token, String claim) {
        try {
            String jsonClaim = new String(Base64.getDecoder().decode(token.split("\\.")[1]));
            JsonNode jsonNode = Json.parse(jsonClaim);
            return jsonNode.get(claim);
        } catch (Exception ex) {
            logger.error("getJWTClaim: {}", ex.getMessage());
        }
        return null;
    }

    public static Date getExpiredAt(int expiresIn) {
        DateTime now = DateTime.now();
        Date expiredAt = now.plusSeconds(expiresIn).toDate();
        return expiredAt;
    }

    public static String generateSecureString() {
        String clientSecret = null;
        try {
            //Initialize SecureRandom
            //This is a lengthy operation, to be done only upon
            //initialization of the application
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");

            //generate a random number
            String randomNum = new Integer(prng.nextInt()).toString();

            //get its digest
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] result = sha.digest(randomNum.getBytes());
            clientSecret = Base64.getEncoder().encodeToString(result);

        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
        }
        return clientSecret;

    }

    private static SecretKey getAESKey(String keyValue) throws Exception {
        SecretKey secretKey = new SecretKeySpec(keyValue.getBytes(UTF_8), "AES");
        return secretKey;
        /*
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey;
        */
    }

    public static String encryptAES(String data, String keyValue) throws Exception {
        SecretKey secretKey = getAESKey(keyValue);

        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        byte[] iv = new byte[aesCipher.getBlockSize()];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] byteCipherText = aesCipher.doFinal(data.getBytes(UTF_8));

        Cipher aesDe = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        aesDe.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] combined = new byte[iv.length + byteCipherText.length];
        for (int i = 0; i < combined.length; i++) {
            combined[i] = i < iv.length ? iv[i] : byteCipherText[i - iv.length];
        }

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decryptAES(String cipherText, String keyValue) throws Exception {
        SecretKey secretKey = getAESKey(keyValue);

        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        byte[] iv = new byte[aesCipher.getBlockSize()];

        byte[] combined = Base64.getDecoder().decode(cipherText);
        byte[] byteCipherText = new byte[combined.length - iv.length];
        for (int i = 0; i < combined.length; i++) {
            if (i < iv.length) {
                iv[i] = combined[i];
            } else {
                byteCipherText[i - iv.length] = combined[i];
            }
        }

        aesCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] byteDecryptedText = aesCipher.doFinal(byteCipherText);
        return new String(byteDecryptedText, UTF_8);
    }

    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }

    public static KeyPair getKeyPairFromKeyStore(String keyName,
                                                 String keyStorePath,
                                                 String keypass,
                                                 String keystore) throws Exception {
        //Generated with:
        //  keytool -genkeypair -alias mykey -storepass s3cr3t -keypass s3cr3t -keyalg RSA -keystore keystore.jks

        //InputStream ins = Secure.class.getResourceAsStream(keyStorePath);
        InputStream ins = new FileInputStream(keyStorePath);

        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(ins, keystore.toCharArray());   //Keystore password
        KeyStore.PasswordProtection keyPassword =       //Key password
                new KeyStore.PasswordProtection(keypass.toCharArray());

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(keyName, keyPassword);

        java.security.cert.Certificate cert = keyStore.getCertificate(keyName);
        PublicKey publicKey = cert.getPublicKey();
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();

        return new KeyPair(publicKey, privateKey);
    }

    public static String encryptRSA(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decryptRSA(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decryptCipher.doFinal(bytes), UTF_8);
    }

    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes("UTF-8"));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes("UTF-8"));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return publicSignature.verify(signatureBytes);
    }

}
