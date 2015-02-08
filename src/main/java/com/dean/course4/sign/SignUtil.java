package com.dean.course4.sign;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by dean on 14/12/27.
 */
public class SignUtil {
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

    public static byte[] publicDecrypt(byte[] digestBytes, PublicKey publicKey) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryBytes = cipher.doFinal(digestBytes);
        return decryBytes;
    }

    public static byte[] privateEncrypt(byte[] content, PrivateKey privateKey) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] bytes = messageDigest.digest(content);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptBytes = cipher.doFinal(bytes);
        return encryptBytes;
    }
}
