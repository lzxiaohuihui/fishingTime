package com.wenli.service;

import com.wenli.entity.po.StatisticsTimeDoc;

import java.util.List;

/**
 * @description:
 * @date: 7/5/23 10:20 AM
 * @author: lzh
 */
public interface StatisticsESService {

    List<StatisticsTimeDoc> findByTermQuery(String indices, String[] args);

    void addTerm(StatisticsTimeDoc statisticsTimeDoc);
}
