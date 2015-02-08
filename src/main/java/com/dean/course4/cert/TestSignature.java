package com.dean.course4.cert;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by dean on 14/12/27.
 */
public class TestSignature {

    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return byte2base64(bytes);
    }

    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return  byte2base64(bytes);
    }

    public static PublicKey string2PublicKey(String pubStr) throws Exception {
        byte[] bytes = base642byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PrivateKey string2PrivateKey(String priStr) throws Exception {
        byte[] bytes = base642byte(priStr);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }


    private static String byte2base64(byte[] bytes) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(bytes);
    }

    private static byte[] base642byte(String str) throws Exception {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        return base64Decoder.decodeBuffer(str);
    }

    /**
     * Ç©Ãû
     * @param content
     * @param privateKey
     * @return
     * @throws Exception
     */
    private static byte[] sign(byte[] content, PrivateKey privateKey) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] bytes = messageDigest.digest(content);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptBytes = cipher.doFinal(bytes);
        return encryptBytes;
    }

    private static boolean verify(byte[] content, byte[] sign, PublicKey publicKey) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] bytes = messageDigest.digest(content);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryBytes = cipher.doFinal(sign);
        if (byte2base64(decryBytes).equals(byte2base64(bytes))) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = getKeyPair();
        String publicKey = getPublicKey(keyPair);
        String privateKey = getPrivateKey(keyPair);

        PublicKey publics = string2PublicKey(publicKey);
        PrivateKey privates = string2PrivateKey(privateKey);

        String str = "Hi, my name is Ding Shuangxi, How do you do!";

        byte[] sign = sign(str.getBytes(), privates);
        boolean verify = verify(str.getBytes(), sign, publics);
        System.out.println(verify);

    }
}
