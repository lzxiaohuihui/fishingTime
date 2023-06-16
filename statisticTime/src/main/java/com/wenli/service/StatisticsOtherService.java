package com.wenli.service;

import com.wenli.entity.dto.AppTimeRunning;
import com.wenli.entity.po.StatisticsTime;

import java.util.List;

/**
 * @description:
 * @date: 6/16/23 11:16 AM
 * @author: lzh
 */
public interface StatisticsOtherService {
    List<AppTimeRunning> findVsCodeRunningTime(String startDay, String endDay);

    List<AppTimeRunning> findIdeaRunningTime(String startDay, String endDay);

    List<StatisticsTime> findAllChromeRecords(String startDay, String endDay);
}
