package com.wenli.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.wenli.entity.dto.AppTimeRunning;
import com.wenli.entity.po.StatisticsTime;
import com.wenli.mapper.StatisticsMysqlMapper;
import com.wenli.service.StatisticsOtherService;
import com.wenli.utils.MyDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @date: 6/16/23 11:17 AM
 * @author: lzh
 */
@Slf4j
@Service
public class StatisticsOtherServiceImpl implements StatisticsOtherService {

    @Resource
    private StatisticsMysqlMapper statisticsMysqlMapper;

    private LoadingCache<String, List<StatisticsTime>> titlesFromMysqlCache;

    private LoadingCache<String, List<AppTimeRunning>> ideaRunningTimeCache;

    private LoadingCache<String, List<AppTimeRunning>> vsCodeRunningTimeCache;

    @PostConstruct
    public void init() {
        titlesFromMysqlCache = Caffeine.newBuilder()
                .maximumSize(Integer.MAX_VALUE)
                .expireAfterWrite(Long.MAX_VALUE, TimeUnit.SECONDS).build(new CacheLoader<String, List<StatisticsTime>>() {
                    @Override
                    public @Nullable List<StatisticsTime> load(@NonNull String day) {
                        Date date = MyDateUtils.getDateFromDay(day);
                        return findOneDayChromeRecords(date);
                    }
                });

        ideaRunningTimeCache = Caffeine.newBuilder()
                .maximumSize(Integer.MAX_VALUE)
                .expireAfterWrite(Long.MAX_VALUE, TimeUnit.SECONDS).build(new CacheLoader<String,
                        List<AppTimeRunning>>() {
                    @Override
                    public @Nullable List<AppTimeRunning> load(@NonNull String day) throws Exception {
                        return getIdeaRunningTime(day);
                    }
                });

        vsCodeRunningTimeCache = Caffeine.newBuilder()
                .maximumSize(Integer.MAX_VALUE)
                .expireAfterWrite(Long.MAX_VALUE, TimeUnit.SECONDS).build(new CacheLoader<String,
                        List<AppTimeRunning>>() {
                    @Override
                    public @Nullable List<AppTimeRunning> load(@NonNull String day) throws Exception {
                        return getVsCodeRunningTime(day);
                    }
                });
    }

    private List<AppTimeRunning> getDataFromCache(String startDay, String endDay, LoadingCache<String, List<AppTimeRunning>> vsCodeRunningTimeCache) {
        Date[] days = MyDateUtils.getDays(startDay, endDay);
        List<AppTimeRunning> res = new ArrayList<>();
        Map<String, Long> appTimes = new HashMap<>();
        for (Date day : days) {
            for (AppTimeRunning appTimeRunning :
                    Objects.requireNonNull(vsCodeRunningTimeCache.get(DateUtil.formatDate(day)))) {
                String app = appTimeRunning.getApp();
                appTimes.put(app, appTimes.getOrDefault(app, 0L) + appTimeRunning.getTotalRunningTime());
            }
        }
        appTimes.forEach((k, v) -> {
            res.add(new AppTimeRunning(null, k, v));
        });
        return res;
    }

    @Override
    public List<AppTimeRunning> findVsCodeRunningTime(String startDay, String endDay) {
        log.info("the size of vsCodeRunningTimeCache is: {} KB", RamUsageEstimator.sizeOfMap(vsCodeRunningTimeCache.asMap())/1000);
        return getDataFromCache(startDay, endDay, vsCodeRunningTimeCache);
    }

    private List<AppTimeRunning> getVsCodeRunningTime(String oneDay) {
        log.info("get vscode data from mysql.");
        Date date = MyDateUtils.getDateFromDay(oneDay);
        long start = DateUtil.beginOfDay(date).getTime()/1000;
        long end = DateUtil.endOfDay(date).getTime()/1000;
        List<AppTimeRunning> vscodeRunningTime = statisticsMysqlMapper.findVsCodeRunningTime(start, end);
        Map<String, Long> appTimes = new HashMap<>();
        for (AppTimeRunning appTimeRunning : vscodeRunningTime) {
            String[] split = appTimeRunning.getApp().split("-");
            if (split.length < 3) {
                continue;
            }
            String app = split[1].trim();
            appTimes.put(app, appTimes.getOrDefault(app, 0L) + appTimeRunning.getTotalRunningTime());
        }
        List<AppTimeRunning> res = new ArrayList<>();
        appTimes.forEach((k, v) -> {
            res.add(new AppTimeRunning(null, k, v));
        });
        return res;
    }

    @Override
    public List<AppTimeRunning> findIdeaRunningTime(String startDay, String endDay) {
        log.info("the size of ideaRunningTimeCache is: {} KB", RamUsageEstimator.sizeOfMap(ideaRunningTimeCache.asMap())/1000);
        return getDataFromCache(startDay, endDay, ideaRunningTimeCache);
    }

    private List<AppTimeRunning> getIdeaRunningTime(String oneDay) {
        log.info("get idea data from mysql.");

        Date date = MyDateUtils.getDateFromDay(oneDay);
        long start = DateUtil.beginOfDay(date).getTime()/1000;
        long end = DateUtil.endOfDay(date).getTime()/1000;
        List<AppTimeRunning> ideaRunningTime = statisticsMysqlMapper.findIdeaRunningTime(start, end);
        Map<String, Long> appTimes = new HashMap<>();
        for (AppTimeRunning appTimeRunning : ideaRunningTime) {
            if (appTimeRunning == null || appTimeRunning.getApp() == null) {
                continue;
            }
            String[] split = appTimeRunning.getApp().split("–");
            if (split.length != 2) {
                continue;
            }
            int index = split[0].indexOf('[');
            index = index == -1 ? split[0].length() : index;
            String app = split[0].substring(0, index).trim();
            appTimes.put(app, appTimes.getOrDefault(app, 0L) + appTimeRunning.getTotalRunningTime());
        }
        List<AppTimeRunning> res = new ArrayList<>();
        appTimes.forEach((k, v) -> {
            res.add(new AppTimeRunning(null, k, v));
        });
        return res;
    }


    @Override
    public List<StatisticsTime> findAllChromeRecords(String startDay, String endDay) {
        log.info("the size of titlesFromMysqlCache is: {} KB", RamUsageEstimator.sizeOfMap(titlesFromMysqlCache.asMap())/1000);
        Date[] days = MyDateUtils.getDays(startDay, endDay);
        List<StatisticsTime> res = new ArrayList<>();
        for (Date day: days){
            res.addAll(Objects.requireNonNull(titlesFromMysqlCache.get(DateUtil.formatDate(day))));
        }
        return res;
    }

    private List<StatisticsTime> findOneDayChromeRecords(Date date) {
        log.info("get chrome data from mysql.");

        long start = DateUtil.beginOfDay(date).getTime()/1000;
        long end = DateUtil.endOfDay(date).getTime()/1000;
        List<StatisticsTime> statisticsTimes = statisticsMysqlMapper.findChromeRecords(start, end);
        for (StatisticsTime statisticsTime : statisticsTimes) {
            if (statisticsTime == null || statisticsTime.getTitle() == null) continue;
            int len = statisticsTime.getTitle().length();
            if (len < 17) continue;
            statisticsTime.setTitle(statisticsTime.getTitle().substring(0, len - 16));
        }
        return statisticsTimes;
    }

}
