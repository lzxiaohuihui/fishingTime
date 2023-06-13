package com.wenli.utils;

/**
 * @description:
 * @date: 2023-06-08 8:51 a.m.
 * @author: lzh
 */
public class ChromeTimeUtil {
    private static final long CHROMETOUNIX = 11644473600000L;

    public static long chromeTimeToUnix(long chromeTime) {
        return chromeTime / 1000 - CHROMETOUNIX;
    }

    public static long unixTimeToChrome(long unixTime) {
        return (unixTime + CHROMETOUNIX) * 1000;
    }
}
