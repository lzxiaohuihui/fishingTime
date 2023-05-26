package com.wenli.service.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.wenli.entity.dto.TimeRunning;
import com.wenli.entity.dto.AppTimeRunning;
import com.wenli.entity.po.StatisticsTime;
import com.wenli.mapper.StatisticsTimeMapper;
import com.wenli.service.StatisticsTimeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static cn.hutool.poi.excel.sax.ElementName.v;

@Service
public class StatisticsTimeServiceImpl implements StatisticsTimeService {

    @Resource
    private StatisticsTimeMapper statisticsTimeMapper;

    @Override
    public void addStatisticsTime(StatisticsTime statisticsTime) {
        statisticsTimeMapper.insert(statisticsTime);
    }

    @Override
    public List<TimeRunning> findRunningTimeSevenDays(String oneDay) {
        String[] split = oneDay.split("-");
        int year = Integer.parseInt(split[0]);
        int mouth = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        DateTime date = DateUtil.date(new Date(year - 1900, mouth - 1, day));
        int diff = date.dayOfWeek();
        diff = diff == 1 ? 8 : diff;
        String[] calendars = new String[7];
        List<TimeRunning> res = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
            calendars[i] = DateUtil.offsetDay(date, 2 - diff + i).toString("YYYY-MM-dd");
            TimeRunning runningTimeOneDay = statisticsTimeMapper.findRunningTimeOneDay(calendars[i]);
            if (runningTimeOneDay != null) {
                res.add(runningTimeOneDay);
            } else {
                res.add(new TimeRunning(calendars[i], 0));
            }
        }

        return res;
    }

    @Override
    public List<AppTimeRunning> findAppRunningTimeOneDay(String oneDay) {
        return statisticsTimeMapper.findAppRunningTimeOneDay(oneDay);
    }

    @Override
    public List<TimeRunning> findRunningTimeByHourOneDay(String oneDay) {
        return statisticsTimeMapper.findRunningTimeByHourOneDay(oneDay);
    }

    @Override
    public List<AppTimeRunning> findVsCodeRunningTime(String startDay, String endDay) {
        List<AppTimeRunning> vscodeRunningTime = statisticsTimeMapper.findVscodeRunningTime(startDay, endDay);
        Map<String, Integer> appTimes = new HashMap<>();
        for (AppTimeRunning appTimeRunning : vscodeRunningTime) {
            String[] split = appTimeRunning.getApp().split("-");
            if (split.length<3) {
                continue;
            }
            String app = split[1].trim();
            appTimes.put(app, appTimes.getOrDefault(app, 0)+appTimeRunning.getTotalRunningTime());
        }
        List<AppTimeRunning> res = new ArrayList<>();
        appTimes.forEach((k,v)->{
            res.add(new AppTimeRunning(null, k, v));
        });
        return res;
    }

    @Override
    public List<AppTimeRunning> findIdeaRunningTime(String startDay, String endDay) {
        List<AppTimeRunning> vscodeRunningTime = statisticsTimeMapper.findIdeaRunningTime(startDay, endDay);
        Map<String, Integer> appTimes = new HashMap<>();
        for (AppTimeRunning appTimeRunning : vscodeRunningTime) {
            String[] split = appTimeRunning.getApp().split("–");
            if (split.length != 2) {
                continue;
            }
            // "statisticTime [~/Documents/IdeaProjects/statistic/statisticTime]"
            int index = split[0].indexOf('[');
            index = index==-1?split[0].length():index;
            String app = split[0].substring(0, index).trim();
            appTimes.put(app, appTimes.getOrDefault(app, 0)+appTimeRunning.getTotalRunningTime());
        }
        List<AppTimeRunning> res = new ArrayList<>();
        appTimes.forEach((k,v)->{
            res.add(new AppTimeRunning(null, k, v));
        });
        return res;
    }

}
