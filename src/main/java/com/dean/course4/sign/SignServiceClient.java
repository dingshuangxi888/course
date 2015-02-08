package com.dean.course4.sign;

import com.dean.course4.cert.TestSignature;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

/**
 * Created by dean on 14/12/27.
 */
public class SignServiceClient {

    private static String consumerPrivateKey = "abc";

    private static String providePublicKey = "cba";

    public static void main(String[] args) {

        try {
            String service = "com.dean.testSign";
            String format = "json";
            String arg1 = "hello";

            Map<String, String> params = new HashMap<>();
            params.put("service", service);
            params.put("format", format);
            params.put("arg1", arg1);

            String digest = getSign(params);

            CloseableHttpClient httpClient = HttpClients.createDefault();

            String url = "http://127.0.0.1/testSign?service=" + service + "&format=" + format + "&arg1=" + arg1;
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("digest", digest);

            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity httpEntity = httpResponse.getEntity();
            byte[] bytes = EntityUtils.toByteArray(httpEntity);
            String jsonresult = new String(bytes);

            String serverResponseDigest = httpResponse.getLastHeader("digest").getValue();

            boolean validateResult = validate(jsonresult, serverResponseDigest);

            System.out.println("¼ìÑé½á¹û£º" + validateResult);

            httpResponse.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean validate(String responseContent, String digest) throws Exception {

        byte[] bytes = getMd5(responseContent);
        String responseDigest = bytes2hex(bytes);

        PublicKey publicKey = SignUtil.string2PublicKey(providePublicKey);
        byte[] decryptBytes = SignUtil.publicDecrypt(hex2bytes(digest), publicKey);
        String decryptDigest = bytes2hex(decryptBytes);
        if (responseDigest.equals(decryptDigest)) {
            return true;
        } else {
            return false;
        }
    }

    private static byte[] hex2bytes(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        str = str.toUpperCase();
        int length = str.length() / 2;
        char[] hexChars = str.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private static String bytes2hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private static byte[] getMd5(String content) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] bytes = messageDigest.digest(content.getBytes("UTF-8"));
        return bytes;
    }

    private static String getSign(Map<String, String> params) throws Exception {
        Set<String> keySet = params.keySet();
        TreeSet<String> sortSet = new TreeSet<>();
        sortSet.addAll(keySet);

        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = sortSet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = params.get(key);
            stringBuilder.append(key).append(value);
        }

        byte[] md5bytes = getMd5(stringBuilder.toString());

        PrivateKey privateKey = SignUtil.string2PrivateKey(consumerPrivateKey);

        byte[] encryptBytes = SignUtil.privateEncrypt(md5bytes, privateKey);

        String hexStr = bytes2hex(encryptBytes);
        return hexStr;
    }
}
