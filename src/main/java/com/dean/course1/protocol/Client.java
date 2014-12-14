package com.dean.course1.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 客户端
 * Created by dean on 14/12/7.
 */
public class Client {

    public static void main(String[] args) {
        Request request = new Request();

        String commond = "HELLO";
        request.setCommond(commond);
        request.setCommondLength(commond.length());
        request.setEncode(Encode.UTF8.getValue());

        System.out.println("commond length:" + request.getCommondLength());
        System.out.println("commond:" + request.getCommond());

        try {
            Socket socket = new Socket("127.0.0.1", 8787);

            OutputStream outputStream = socket.getOutputStream();
            ProtocolUtil.writeRequest(outputStream, request);
            socket.shutdownOutput();

            InputStream inputStream = socket.getInputStream();
            Response response = ProtocolUtil.readResponse(inputStream);
            socket.shutdownInput();

            System.out.println("response length:" + response.getResponseLength());
            System.out.println("response:" + response.getResponse());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
