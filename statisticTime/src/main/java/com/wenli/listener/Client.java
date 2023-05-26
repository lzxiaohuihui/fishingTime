package com.wenli.listener;

import com.alibaba.fastjson.JSON;
import com.wenli.entity.dto.WindowDto;
import com.wenli.entity.po.StatisticsTime;
import com.wenli.service.StatisticsTimeService;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class Client extends Thread {

    private final long maxTime = 300;
    private final StatisticsTimeService statisticsTimeService;
    private final Socket socket;

    private WindowDto lastSeen;


    public Client(Socket socket, StatisticsTimeService statisticsTimeService) {
        this.socket = socket;
        this.statisticsTimeService = statisticsTimeService;
        lastSeen = new WindowDto(0, null, null, 0);
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String newLine = StringEscapeUtils.unescapeJava(line);
                WindowDto windowDto = JSON.parseObject(newLine, WindowDto.class);
                StatisticsTime statisticsTime = new StatisticsTime();
                statisticsTime.setApp(lastSeen.getApp());
                statisticsTime.setTitle(lastSeen.getTitle());
                statisticsTime.setStartTime(lastSeen.getTimestamp());
                statisticsTime.setEndTime(windowDto.getTimestamp());
                statisticsTime.setRunningTime(Math.min(statisticsTime.getEndTime()-statisticsTime.getStartTime(), maxTime));
                if(lastSeen.getXid() != 0){
                    statisticsTimeService.addStatisticsTime(statisticsTime);
                }
                lastSeen = windowDto;
                System.out.println(windowDto);
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Client disconnected");
        } catch (Exception e1){
            this.run();
            e1.printStackTrace();
        }
    }
}