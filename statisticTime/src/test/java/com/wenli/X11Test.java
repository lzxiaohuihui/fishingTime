package com.wenli;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @description:
 * @date: 2023-05-25 2:29 p.m.
 * @author: lzh
 */
public class X11Test {
    public static void main(String[] args) {
        System.out.println(DateUtil.beginOfWeek(DateUtil.date()));
        System.out.println(DateUtil.endOfWeek(DateUtil.date()).getTime());

    }
}
