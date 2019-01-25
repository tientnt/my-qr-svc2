package com.am.common.utils;

import play.Logger;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;

public class AESHelper {

    private static final Logger.ALogger logger = Logger.of(AESHelper.class);

    private static int pswdIterations = 65536;

    private static int keySize = 256;

    private static String fixedSalt;

    private static String password;

    private static SecretKeySpec fixedSecretKeySpec;

    private static String iv;

    static {
        try {
            password = Singleton.config.getString("app.aes_password");
            fixedSalt = Singleton.config.getString("app.aes_salt");
            byte[] saltBytes = org.apache.commons.codec.binary.Base64.decodeBase64(fixedSalt);
            SecretKeyFactory pBKDF2WithHmacSHA256Factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, pswdIterations, keySize);
            SecretKey secretKey = pBKDF2WithHmacSHA256Factory.generateSecret(spec);
            fixedSecretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception e) {
            logger.error("Static init AES error: ", e);
        }
    }

    public static String encryptWithFixedSalt(String plainText) throws Exception {
        long startTime = System.currentTimeMillis();
        iv = null;

        //encrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, fixedSecretKeySpec);
        AlgorithmParameters params = cipher.getParameters();

        byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
        iv = org.apache.commons.codec.binary.Base64.encodeBase64String(ivBytes);
        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        long endTime = System.currentTimeMillis();
        logger.debug("Encryption of {} takes {}ms", plainText, (endTime - startTime));
        return org.apache.commons.codec.binary.Base64.encodeBase64String(encryptedTextBytes);
    }

    public static String decryptWithFixedSalt(String encryptedText) throws Exception {
        long startTime = System.currentTimeMillis();
        byte[] encryptedTextBytes = org.apache.commons.codec.binary.Base64.decodeBase64(encryptedText);
        // Decrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,
                    fixedSecretKeySpec,
                    new IvParameterSpec(org.apache.commons.codec.binary.Base64.decodeBase64(iv)));

        byte[] decryptedTextBytes = null;
        try {
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        } catch (IllegalBlockSizeException e) {
            logger.error("decryptWithFixedSalt error - IllegalBlockSizeException ", e);
        } catch (BadPaddingException e) {
            logger.error("decryptWithFixedSalt error - BadPaddingException ", e);
        }

        long endTime = System.currentTimeMillis();
        logger.debug("Decryption of {} takes {}ms", encryptedText, (endTime - startTime));

        return new String(decryptedTextBytes);
    }

}
