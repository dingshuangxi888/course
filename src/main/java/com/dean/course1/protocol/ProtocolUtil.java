package com.dean.course1.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 协议解析工具类
 * Created by dean on 14/12/7.
 */
public class ProtocolUtil {

    public static void writeRequest(OutputStream outputStream, Request request) throws IOException {
        outputStream.write(request.getEncode());
        outputStream.write(ByteUtil.int2Bytes(request.getCommondLength()));
        if (Encode.UTF8.getValue() == request.getEncode()) {
            outputStream.write(request.getCommond().getBytes("UTF8"));
        } else {
            outputStream.write(request.getCommond().getBytes("GBK"));
        }
        outputStream.flush();
    }

    public static Response readResponse(InputStream inputStream) throws IOException {
        byte[] encodeByte = new byte[1];
        inputStream.read(encodeByte);
        byte encode = encodeByte[0];

        byte[] responseLengthBytes = new byte[4];
        inputStream.read(responseLengthBytes);
        int responseLength = ByteUtil.bytes2Int(responseLengthBytes);

        byte[] responseBytes = new byte[responseLength];
        inputStream.read(responseBytes);
        String response = "";
        if (Encode.UTF8.getValue() == encode) {
            response = new String(responseBytes, "UTF8");
        } else {
            response = new String(responseBytes, "GBK");
        }

        Response result = new Response();
        result.setResponse(response);
        result.setResponseLength(responseLength);
        result.setEncode(encode);

        return result;
    }

    public static Request readRequest(InputStream inputStream) throws IOException {

        byte[] encodeByte = new byte[1];
        inputStream.read(encodeByte);
        byte encode = encodeByte[0];

        byte[] commondLengthBytes = new byte[4];
        inputStream.read(commondLengthBytes);
        int commondLength = ByteUtil.bytes2Int(commondLengthBytes);

        byte[] commondBytes = new byte[commondLength];
        inputStream.read(commondBytes);
        String commond = "";
        if (Encode.UTF8.getValue() == encode) {
            commond = new String(commondBytes, "UTF-8");
        } else {
            commond = new String(commondBytes, "GBK");
        }

        Request request = new Request();
        request.setCommond(commond);
        request.setCommondLength(commondLength);
        request.setEncode(encode);
        return request;
    }

    public static void writeResponse(OutputStream outputStream, Response response) throws IOException {
        outputStream.write(response.getEncode());
        outputStream.write(ByteUtil.int2Bytes(response.getResponseLength()));
        if (Encode.UTF8.getValue() == response.getEncode()) {
            outputStream.write(response.getResponse().getBytes("UTF8"));
        } else {
            outputStream.write(response.getResponse().getBytes("GBK"));
        }
        outputStream.flush();
    }
}
