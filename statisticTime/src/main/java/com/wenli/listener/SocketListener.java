package com.wenli.listener;

import com.wenli.service.StatisticsTimeService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// implements ApplicationRunner 启动时执行
@Component
public class SocketListener implements ApplicationRunner {
    private final Integer PORT = 1413;

    @Resource
    private StatisticsTimeService statisticsTimeService;

    public void run(ApplicationArguments args) throws Exception {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {

                System.out.println("Server is listening on port " + PORT);
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected");
                    new Client(socket, statisticsTimeService).start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        /**
        Thread.sleep(1000);
        String projectDir = System.getProperty("user.dir");
        String scriptPath = projectDir + "/resume.sh"; // 替换为您的脚本路径
        ProcessBuilder processBuilder = new ProcessBuilder(scriptPath);
        processBuilder.directory(new File(projectDir));
        Process process = processBuilder.start();
        */
    }
}

