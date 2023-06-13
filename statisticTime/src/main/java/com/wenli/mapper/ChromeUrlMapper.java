package com.wenli.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wenli.entity.po.ChromeUrls;

import java.util.List;

/**
 * @description:
 * @date: 2023-06-07 8:36 p.m.
 * @author: lzh
 */

@DS("sqlite")
public interface ChromeUrlMapper extends BaseMapper<ChromeUrls> {

    List<String> findTitleByUrl(String url);
}
