package com.dean.course1.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端
 * Created by dean on 14/12/7.
 */
public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8787);

            while (true) {
                Socket socket = serverSocket.accept();

                InputStream inputStream = socket.getInputStream();
                Request request = ProtocolUtil.readRequest(inputStream);
                socket.shutdownInput();

                OutputStream outputStream = socket.getOutputStream();

                Response response = new Response();
                response.setEncode(Encode.UTF8.getValue());

                if ("HELLO".equalsIgnoreCase(request.getCommond())) {
                    response.setResponse("Hello!");
                } else {
                    response.setResponse("see you!");
                }

                response.setResponseLength(response.getResponse().length());

                ProtocolUtil.writeResponse(outputStream, response);

                socket.shutdownOutput();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
