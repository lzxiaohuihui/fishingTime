package com.wenli.service.impl;


import cn.hutool.core.date.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.wenli.entity.dto.AppTimeRunning;
import com.wenli.entity.dto.TimeRunning;
import com.wenli.entity.po.StatisticsTime;
import com.wenli.mapper.StatisticsMysqlMapper;
import com.wenli.service.StatisticsTimeService;
import com.wenli.utils.MyDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StatisticsTimeServiceImpl implements StatisticsTimeService {

    @Resource
    private StatisticsMysqlMapper statisticsMysqlMapper;

    private LoadingCache<String, Map<String, List<AppTimeRunning>>> appHoursCache;

    @PostConstruct
    public void init() {
        appHoursCache = Caffeine.newBuilder()
                .maximumSize(Integer.MAX_VALUE)
                .expireAfterWrite(Long.MAX_VALUE, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Map<String, List<AppTimeRunning>>>() {
                    @Override
                    public @Nullable Map<String, List<AppTimeRunning>> load(@NonNull String day) {
                        return getAppHoursRunningTimes(day);
                    }
                });
    }

    private Map<String, List<AppTimeRunning>> getAppHoursRunningTimes(String day){
        log.info("load data appHoursRunningTimes from mysql.");
        Date date = MyDateUtils.getDateFromDay(day);
        DateRange range = DateUtil.range(DateUtil.beginOfDay(date), DateUtil.endOfDay(date), DateField.HOUR_OF_DAY);
        Map<String, List<AppTimeRunning>> res = new HashMap<>();
        for (DateTime dateTime : range) {
            long start = DateUtil.beginOfHour(dateTime).getTime()/1000;
            long end = DateUtil.endOfHour(dateTime).getTime()/1000;
            String hour = String.valueOf(DateUtil.hour(dateTime, true));
            List<AppTimeRunning> appRunningTime = statisticsMysqlMapper.findAppRunningTime(start, end);
            res.put(hour, appRunningTime);
        }
        return res;
    }

    @Override
    public void addStatisticsTime(StatisticsTime statisticsTime) {
        statisticsMysqlMapper.insert(statisticsTime);
    }

    @Override
    public List<TimeRunning> findRunningTimeSevenDays(String oneDay) {
        DateTime date = MyDateUtils.getDateTimeFromDay(oneDay);
        int diff = date.dayOfWeek();
        diff = diff == 1 ? 8 : diff;
        List<TimeRunning> res = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
            DateTime dateTime = DateUtil.offsetDay(date, 2 - diff + i);
            String day = dateTime.toString("YYYY-MM-dd");
            long total = getTotalRunningTimeByCache(DateUtil.date(dateTime));
            res.add(new TimeRunning(day, (int)total));
        }
        return res;
    }

    private Long getTotalRunningTimeByCache(Date date){
        String day = DateUtil.formatDate(date);
        Map<String, List<AppTimeRunning>> stringListMap = appHoursCache.get(day);
        if (stringListMap == null){
            return 0L;
        }
        long total = 0;
        for (List<AppTimeRunning> value : stringListMap.values()) {
            for (AppTimeRunning appTimeRunning : value) {
                total += appTimeRunning.getTotalRunningTime();
            }
        }
        return total;
    }

    @Override
    public List<AppTimeRunning> findAppRunningTimeOneDay(String oneDay) {
        Map<String, List<AppTimeRunning>> stringListMap = appHoursCache.get(oneDay);
        if (stringListMap == null) {
            return null;
        }
        Map<String, Long> appTimes = new HashMap<>();
        for (List<AppTimeRunning> value : stringListMap.values()) {
            for (AppTimeRunning appTimeRunning : value) {
                String app = appTimeRunning.getApp();
                appTimes.put(app, appTimes.getOrDefault(app, 0L)+appTimeRunning.getTotalRunningTime());
            }
        }
        List<AppTimeRunning> res = new ArrayList<>();
        appTimes.forEach((k, v)->{
            res.add(new AppTimeRunning(k, v));
        });
        return res;
    }


    @Override
    public List<TimeRunning> findRunningTimeByHourOneDay(String oneDay) {

        Map<String, List<AppTimeRunning>> stringListMap = appHoursCache.get(oneDay);
        if (stringListMap == null) {
            return null;
        }
        List<TimeRunning> res = new ArrayList<>();
        for(int i = 0; i < 24; i++){
            String hour = String.valueOf(i);
            List<AppTimeRunning> hourData = stringListMap.getOrDefault(hour, null);
            String timeStamp = hour + ":00";
            int total = 0;
            if (hourData != null) {
                for (AppTimeRunning hourDatum : hourData) {
                    total += hourDatum.getTotalRunningTime();
                }
            }
            res.add(new TimeRunning(timeStamp, total));
        }
        return res;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 60 * 1000)
    public void updateTodayCache(){
        DateTime date = DateUtil.date();
        String today = date.toString("yyyy-MM-dd");
        appHoursCache.refresh(today);
    }
}
