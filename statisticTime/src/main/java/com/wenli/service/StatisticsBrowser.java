package com.wenli.service;

import java.util.Map;

/**
 * @description:
 * @date: 2023-06-13 9:57 a.m.
 * @author: lzh
 */
public interface StatisticsBrowser {

    Map<String, String> getChromeTitle();

    String getChromeUrl(String key);

    void HistoryCopy();
}
