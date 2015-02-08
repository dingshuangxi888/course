package com.dean.course4.sign;

import com.alibaba.fastjson.JSON;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.*;

/**
 * Created by dean on 14/12/27.
 */
@RestController
public class SignServiceServer {

    @RequestMapping(value = "/testSign", method = RequestMethod.GET)
    public ModelAndView testSign(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map param = request.getParameterMap();
        String requestDigest = request.getHeader("digest");
        boolean validateResult = vaildate(param, requestDigest);

        String service = request.getParameter("service");
        String format = request.getParameter("format");
        String arg1 = request.getParameter("arg1");


        BaseResult baseResult = new BaseResult();
        baseResult.setResultCode("SUCESS");
        baseResult.setResultMessage("«Î«Û≥…π¶");
        String jsonReuslt = JSON.toJSONString(baseResult);
        String digest = getSign(jsonReuslt);
        response.addHeader("digest", digest);
        return new ModelAndView();
    }

    private String getSign(String content) throws Exception {
        PrivateKey privateKey = SignUtil.string2PrivateKey(content);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateKey);
        signature.update(content.getBytes());
        String hexStr = bytes2hex(signature.sign());
        return hexStr;
    }

    private boolean vaildate(Map params, String digest) throws Exception {
        Set<String> keySet = params.keySet();
        TreeSet<String> sortSet = new TreeSet<>();
        sortSet.addAll(keySet);

        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = sortSet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String[] values = (String[]) params.get(key);
            stringBuilder.append(key).append(values[0]);
        }

        String hexStr = bytes2hex(getMd5(stringBuilder.toString()));

        PublicKey publicKey = SignUtil.string2PublicKey(hexStr);

        byte[] decryptBytes = SignUtil.publicDecrypt(hex2bytes(digest), publicKey);
        String decryptDigest = bytes2hex(decryptBytes);
        if (hexStr.equals(decryptDigest)) {
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
}
