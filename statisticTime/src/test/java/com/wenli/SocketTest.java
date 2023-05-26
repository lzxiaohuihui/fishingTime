package com.wenli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * SELECT SoftwareName, SUM(RunningTime) AS TotalTime
 * FROM SoftwareRunningTime
 * WHERE StartTime >= DATEADD(day, -1, GETDATE())
 * GROUP BY SoftwareName
 */

//public class SocketTest {
//    public static void main(String[] args) throws IOException {
//        int port = 1413;
//        try (ServerSocket serverSocket = new ServerSocket(port)){
//
//            System.out.println("Server is listening on port " + port);
//            while (true) {
//                Socket socket = serverSocket.accept();
//                System.out.println("Client connected");
//                new ClientHandler(socket).start();
//            }
//        }
//    }
//}
//class ClientHandler extends Thread {
//    private final Socket socket;
//
//    public ClientHandler(Socket socket) {
//        this.socket = socket;
//    }
//
//    public void run() {
//        try {
//            InputStream input = socket.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(input,StandardCharsets.UTF_8));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String newLine = StringEscapeUtils.unescapeJava(line);
//                WindowDto windowDto = JSON.parseObject(newLine, WindowDto.class);
//                System.out.println(windowDto);
////                System.out.println("Received: " + newLine);
//            }
//
//            socket.close();
//        } catch (IOException e) {
//            System.out.println("Client disconnected");
//        }
//    }
//}