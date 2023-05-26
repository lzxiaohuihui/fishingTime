package com.wenli.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wenli.entity.dto.TimeRunning;
import com.wenli.entity.dto.AppTimeRunning;
import com.wenli.entity.po.StatisticsTime;

import java.util.List;

public interface StatisticsTimeMapper extends BaseMapper<StatisticsTime> {
    TimeRunning findRunningTimeOneDay(String oneDay);

    List<AppTimeRunning> findAppRunningTimeOneDay(String oneDay);

    List<TimeRunning> findRunningTimeByHourOneDay(String oneDay);

    List<AppTimeRunning> findVscodeRunningTime(String startDay, String endDay);

    List<AppTimeRunning> findIdeaRunningTime(String startDay, String endDay);
}
