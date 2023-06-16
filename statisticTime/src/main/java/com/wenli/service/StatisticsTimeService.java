package com.wenli.service;

import com.wenli.entity.dto.TimeRunning;
import com.wenli.entity.dto.AppTimeRunning;
import com.wenli.entity.po.StatisticsTime;

import java.text.ParseException;
import java.util.List;

public interface StatisticsTimeService {
    void addStatisticsTime(StatisticsTime statisticsTime);

    List<TimeRunning> findRunningTimeSevenDays(String oneDay);
    List<AppTimeRunning> findAppRunningTimeOneDay(String oneDay);

    List<TimeRunning> findRunningTimeByHourOneDay(String oneDay);


}
