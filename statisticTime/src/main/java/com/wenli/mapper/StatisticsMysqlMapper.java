package com.wenli.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wenli.entity.dto.AppTimeRunning;
import com.wenli.entity.po.StatisticsTime;

import java.util.List;

/**
 * @description:
 * @date: 6/15/23 10:01 AM
 * @author: lzh
 */
public interface StatisticsMysqlMapper extends BaseMapper<StatisticsTime> {


    Integer findRunningTime(long start, long end);

    List<AppTimeRunning> findAppRunningTime(long start, long end);

    List<AppTimeRunning>  findVsCodeRunningTime(long start, long end);

    List<AppTimeRunning>  findIdeaRunningTime(long start, long end);

    List<StatisticsTime> findChromeRecords(long start, long end);


}
