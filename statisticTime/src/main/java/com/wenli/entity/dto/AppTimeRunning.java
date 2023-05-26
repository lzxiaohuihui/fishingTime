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
    private Integer totalRunningTime;
}
