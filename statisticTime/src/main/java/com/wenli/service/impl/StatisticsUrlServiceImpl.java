package com.wenli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenli.entity.po.StatisticsUrls;
import com.wenli.mapper.StatisticsUrlsMapper;
import com.wenli.service.StatisticsUrlService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @date: 2023-06-13 5:56 p.m.
 * @author: lzh
 */
@Service
public class StatisticsUrlServiceImpl implements StatisticsUrlService {

    @Resource
    private StatisticsUrlsMapper statisticsUrlsMapper;

    @Override
    public boolean addStatisticsUrl(String url) {
        StatisticsUrls statisticsUrls = statisticsUrlsMapper.selectOne(new QueryWrapper<StatisticsUrls>().eq("url", url));
        if (statisticsUrls != null) {
            return false;
        }
        StatisticsUrls tmp = new StatisticsUrls();
        tmp.setUrl(url);
        statisticsUrlsMapper.insert(tmp);
        return true;
    }
}
