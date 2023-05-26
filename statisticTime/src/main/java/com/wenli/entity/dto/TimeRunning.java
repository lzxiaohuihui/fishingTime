package com.wenli.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class TimeRunning {
    private String timeStamp;
    private Integer totalRunningTime;
}
