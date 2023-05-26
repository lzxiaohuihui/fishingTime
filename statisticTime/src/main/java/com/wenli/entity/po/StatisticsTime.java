package com.wenli.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("statistics_time")
public class StatisticsTime {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String app;
    private String title;
    private Long startTime;
    private Long endTime;
    private Long runningTime;
}