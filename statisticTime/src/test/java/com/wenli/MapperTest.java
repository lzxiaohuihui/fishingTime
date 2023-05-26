package com.wenli;


import cn.hutool.Hutool;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Week;
import cn.hutool.core.date.format.FastDateFormat;
import com.wenli.mapper.StatisticsTimeMapper;
import com.wenli.service.StatisticsTimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PutMapping;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest(classes = TimeApplication.class)
@RunWith(SpringRunner.class)
public class MapperTest {

    @Resource
    private StatisticsTimeMapper statisticsTimeMapper;

    @Resource
    private StatisticsTimeService statisticsTimeService;


    @Test
    public void testTimeFormat() {
        long StartTime = 1685004118;
        long endTime = 1685011373;
//        System.out.println(statisticsTimeMapper.findRunningTime(StartTime, endTime));
//        System.out.println(statisticsTimeMapper.findAppRunningTime(StartTime, endTime));
//        System.out.println(statisticsTimeMapper.findRunningTimeByHour("2023-05-22"));
        System.out.println(statisticsTimeMapper.findAppRunningTimeOneDay("2023-05-22"));
    }

    @Test
    public void testHutools() {
        String oneDay = "2023-05-21";
        String[] split = oneDay.split("-");
        int year = Integer.parseInt(split[0]);
        int mouth = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        DateTime date = DateUtil.date(new Date(year - 1900, mouth - 1, day));
        int i = date.dayOfWeek();
        i = i == 1 ? 8 : i;
        DateTime last = DateUtil.offsetDay(date, -1);
        DateTime mon = DateUtil.offsetDay(date, 2 - i);
        DateTime tue = DateUtil.offsetDay(mon, 1);
        DateTime wed = DateUtil.offsetDay(tue, 1);
        DateTime thu = DateUtil.offsetDay(wed, 1);
        DateTime fri = DateUtil.offsetDay(thu, 1);
        DateTime sat = DateUtil.offsetDay(fri, 1);
        DateTime sun = DateUtil.offsetDay(sat, 1);
        System.out.println(sun.toString("YYYY-MM-dd"));
    }


}
