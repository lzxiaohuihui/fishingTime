package com.wenli.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class AppTimeRunning{
    private String timeStamp;
    private String app;
    private Long totalRunningTime;

    public AppTimeRunning(String app, Long totalRunningTime) {
        this.app = app;
        this.totalRunningTime = totalRunningTime;
    }
}
