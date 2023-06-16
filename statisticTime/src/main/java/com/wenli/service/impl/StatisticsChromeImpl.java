package com.wenli.service.impl;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.wenli.entity.po.StatisticsUrls;
import com.wenli.mapper.ChromeUrlMapper;
import com.wenli.mapper.StatisticsUrlsMapper;
import com.wenli.service.StatisticsBrowser;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @date: 2023-06-13 9:57 a.m.
 * @author: lzh
 */

@Service
@Slf4j
public class StatisticsChromeImpl implements StatisticsBrowser {
    @Resource
    private ChromeUrlMapper chromeUrlMapper;

    @Resource
    private StatisticsUrlsMapper statisticsUrlsMapper;


    private LoadingCache<String, String> titlesCache;

    @PostConstruct
    public void init() {
        titlesCache = Caffeine.newBuilder()
                .maximumSize(Integer.MAX_VALUE)
                .expireAfterWrite(Long.MAX_VALUE, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
                    @Override
                    public String load(@NonNull String key) {
                        return "None";
                    }
                });
    }

    @Override
    public Map<String, String> getChromeTitle() {
        return titlesCache.asMap();
    }

    @Override
    public String getChromeUrl(String key){
        return titlesCache.get(key);
    }

    @Override
    @Scheduled(initialDelay = 1000, fixedDelay = 10 * 60 * 1000)
    public void HistoryCopy() {
        String ChromeHistoryPath = System.getProperty("user.home") + "/.config/google-chrome/Default/History";
        String targetPath = System.getProperty("user.dir") + "/chromeHistory.db";

        File source = new File(ChromeHistoryPath);
        File dest = new File(targetPath);

        try (InputStream in = Files.newInputStream(source.toPath()); OutputStream out =
                Files.newOutputStream(dest.toPath())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            log.info("Chrome history file copied successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<StatisticsUrls> statisticsUrls = statisticsUrlsMapper.selectList(null);
        for (StatisticsUrls statisticsUrl : statisticsUrls) {
            List<String> titles = chromeUrlMapper.findTitleByUrl(statisticsUrl.getUrl() + "%");
            for (String title : titles) {
                titlesCache.put(title, statisticsUrl.getUrl());
            }
        }

    }
}
