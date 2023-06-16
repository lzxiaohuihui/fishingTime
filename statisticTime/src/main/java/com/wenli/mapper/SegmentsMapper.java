package com.wenli.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wenli.entity.po.Segment;

/**
 * @description:
 * @date: 6/15/23 6:49 PM
 * @author: lzh
 */
@DS("sqlite")
public interface SegmentsMapper extends BaseMapper<Segment> {
}
