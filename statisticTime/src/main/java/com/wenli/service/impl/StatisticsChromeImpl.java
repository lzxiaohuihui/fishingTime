package com.wenli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenli.entity.po.ChromeUrls;
import com.wenli.entity.po.StatisticsUrls;
import com.wenli.mapper.ChromeUrlMapper;
import com.wenli.mapper.StatisticsTimeMapper;
import com.wenli.mapper.StatisticsUrlsMapper;
import com.wenli.service.StatisticsChrome;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @date: 2023-06-13 9:57 a.m.
 * @author: lzh
 */

@Service
public class StatisticsChromeImpl implements StatisticsChrome {
    @Resource
    private ChromeUrlMapper chromeUrlMapper;

    @Resource
    private StatisticsUrlsMapper statisticsUrlsMapper;

    @Override
    public Map<String, String> getChromeTitle() {
        List<StatisticsUrls> statisticsUrls = statisticsUrlsMapper.selectList(null);
        Map<String, String> res = new HashMap<>();
        for (StatisticsUrls statisticsUrl : statisticsUrls) {
            List<String> titles = chromeUrlMapper.findTitleByUrl(statisticsUrl.getUrl() + "%");
            for (String title : titles) {
                res.put(title, statisticsUrl.getUrl());
            }
        }

        return res;
    }
}
