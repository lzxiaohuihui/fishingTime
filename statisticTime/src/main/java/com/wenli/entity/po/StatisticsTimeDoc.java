package com.wenli.entity.po;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @date: 7/5/23 9:47 AM
 * @author: lzh
 */

@NoArgsConstructor
public class StatisticsTimeDoc extends StatisticsTime{
    public StatisticsTimeDoc(StatisticsTime statisticsTime){
        this.setId(statisticsTime.getId());
        this.setApp(statisticsTime.getApp());
        this.setTitle(statisticsTime.getTitle());
        this.setStartTime(statisticsTime.getStartTime());
        this.setEndTime(statisticsTime.getEndTime());
        this.setRunningTime(statisticsTime.getRunningTime());
    }
}
