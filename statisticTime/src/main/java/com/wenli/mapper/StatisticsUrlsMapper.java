package com.wenli.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wenli.entity.po.ChromeUrls;
import com.wenli.entity.po.StatisticsUrls;

import java.util.List;

/**
 * @description:
 * @date: 2023-06-07 8:36 p.m.
 * @author: lzh
 */

@DS("mysql")
public interface StatisticsUrlsMapper extends BaseMapper<StatisticsUrls> {

}
