package com.wenli;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenli.entity.dto.TimeRunning;
import com.wenli.entity.po.ChromeUrls;
import com.wenli.entity.po.StatisticsTime;
import com.wenli.mapper.ChromeUrlMapper;
import com.wenli.mapper.StatisticsMysqlMapper;
import com.wenli.mapper.StatisticsTimeMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @date: 2023-06-07 8:36 p.m.
 * @author: lzh
 */
@Ignore
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestData {
    @Resource
    private ChromeUrlMapper chromeHistoryMapper;

    @Resource
    private StatisticsTimeMapper statisticsTimeMapper;


    @Test
    public void testSqlite() {
        File source = new File("/home/lzh/.config/google-chrome/Default/History");
//        File dest = new File(System.getProperty("user.dir")+"/chromeHistory.db");
        File dest = new File("/home/lzh/history.db");

        try (InputStream in = Files.newInputStream(source.toPath()); OutputStream out =
                Files.newOutputStream(dest.toPath())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            System.out.println("File copied successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        long l = dest.lastModified();
//        Date date = new Date(l);
//        System.out.println(date);
//        QueryWrapper<ChromeUrls> queryWrapper = new QueryWrapper<>();
//        queryWrapper.gt("last_visit_time", 13323186463309130L);
//        List<ChromeUrls> chromeHistories = chromeHistoryMapper.selectList(queryWrapper);
//        for (ChromeUrls chromeHistory : chromeHistories) {
//            System.out.println(new Date(chromeHistory.getLastVisitTime() / 1000 - 11644473600000L));
//        }
//        System.out.println(chromeHistories.size());
    }


    @Test
    public void testMysql() {
        List<StatisticsTime> statisticsTimes = statisticsTimeMapper.selectList(null);
        System.out.println(statisticsTimes.size());
        System.out.println();
    }

    @Test
    public void testTime() {
        Date date = new Date(1686145917000L);
        System.out.println(date);

        String s = "绅士不在平行世界的个人空间_哔哩哔哩_bilibili - Google Chrome";
        String t = "绅士不在平行世界的个人空间_哔哩哔哩_bilibili";
        System.out.println(s);
    }

    @Test
    public void findGraph() {
        char[][] grid = new char[6][6];
        grid[0][0] = '#';
        grid[3][3] = '#';
        grid[4][4] = '#';
        grid[4][5] = '#';

        boolean[][] visited = new boolean[6][6];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '#') {
                    visited[i][j] = true;
                }
            }
        }

        // get the first graph
        List<List<List<Integer>>> paths = new ArrayList<>();
        List<List<Integer>> path = new ArrayList<>();
        path.add(Arrays.asList(0, 0));
        int[] preStep = {0, 0};
        findAllGraph(paths, visited, path, 9, preStep);
        for (List<List<Integer>> lists : paths) {
            System.out.println(lists);
        }
    }

    int[][] direction = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    void findAllGraph(List<List<List<Integer>>> paths, boolean[][] visited, List<List<Integer>> path, int area,
                      int[] preStep) {
        if (path.size() == area) {
            paths.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < direction.length; i++) {
            int nextX = preStep[0] + direction[i][0];
            int nextY = preStep[1] + direction[i][1];
            if (nextX < 0 || nextX >= visited.length) continue;
            if (nextY < 0 || nextY >= visited[0].length) continue;
            if (visited[nextX][nextY]) continue;
            visited[nextX][nextY] = true;
            preStep = new int[]{nextX, nextY};
            path.add(new ArrayList<>(Arrays.asList(nextX, nextY)));
            findAllGraph(paths, visited, path, area, preStep);
            path.remove(path.size() - 1);
            visited[nextX][nextY] = false;
        }

    }

    @Test
    public void testUrl() {
        List<String> titleByUrl = chromeHistoryMapper.findTitleByUrl("https://www.bilibili.com%");
//        System.out.println(titleByUrl);
    }

    @Resource
    private StatisticsMysqlMapper statisticsMysqlMapper;

    @Test
    public void testFindHourByIndex() {


        // 获取当前日期
        Date date = new Date();

        // 生成一天当中每一个小时的区间
        DateRange rangeList = DateUtil.range(DateUtil.beginOfDay(date), DateUtil.endOfDay(date), DateField.HOUR_OF_DAY);

        // 输出每个小时的区间
        List<TimeRunning> timeRunnings = new ArrayList<>();
        for (Date range : rangeList) {
            Integer res = statisticsMysqlMapper.findRunningTime(range.getTime() / 1000,
                    DateUtil.endOfHour(range).getTime() / 1000);
            if (res == null) continue;
            timeRunnings.add(new TimeRunning(range.toString(), res));
//            System.out.println(DateUtil.formatDateTime(range) + " - " + DateUtil.formatDateTime(DateUtil.endOfHour(range)));
//            System.out.println(timeRunnings);

        }
    }

    @Test
    public void testFindHourByFunc() {
        long l = System.currentTimeMillis();

        for (int i = 0; i < 100; ++i) {
            List<TimeRunning> runningTimeByHourOneDay = statisticsTimeMapper.findRunningTimeByHourOneDay("20230614");
        }
//        for (TimeRunning running : runningTimeByHourOneDay) {
//            System.out.println(running);
//        }

        long r = System.currentTimeMillis();
        System.out.println("耗时：" + (r - l) + "毫秒");
    }

    private StatisticsTime[] splitDateTime(StatisticsTime statisticsTime, DateTime date1, DateTime date2){
        DateTime dateTime = DateUtil.beginOfHour(date2);
        long between1 = DateUtil.between(date1, dateTime, DateUnit.SECOND);
        StatisticsTime statisticsTime1 = new StatisticsTime();
        StatisticsTime statisticsTime2 = new StatisticsTime();
        BeanUtil.copyProperties(statisticsTime, statisticsTime1);
        BeanUtil.copyProperties(statisticsTime, statisticsTime2);
        statisticsTime1.setStartTime(date1.getTime()/1000);
        statisticsTime1.setEndTime(dateTime.getTime()/1000);
        statisticsTime1.setRunningTime(between1);

        statisticsTime2.setStartTime(dateTime.getTime()/1000);
        statisticsTime2.setEndTime(date2.getTime()/1000);
        long between2 = DateUtil.between(dateTime, date2, DateUnit.SECOND);
        statisticsTime2.setRunningTime(between2);

        return new StatisticsTime[]{statisticsTime1, statisticsTime2};
    }
    @Test
    public void splitData(){
        List<StatisticsTime> statisticsTimes = statisticsTimeMapper.selectList(null);
        for (StatisticsTime statisticsTime : statisticsTimes) {
            DateTime date1 = DateUtil.date(statisticsTime.getStartTime() * 1000L);
            DateTime date2 = DateUtil.date((statisticsTime.getStartTime()+statisticsTime.getRunningTime()) * 1000L);
            boolean isSameHour = DateUtil.hour(date1, true) == DateUtil.hour(date2, true);
            if (isSameHour) continue;
            Long id = statisticsTime.getId();
            StatisticsTime[] p = splitDateTime(statisticsTime, date1, date2);
            statisticsTimeMapper.insert(p[0]);
            statisticsTimeMapper.insert(p[1]);
            statisticsTimeMapper.deleteById(id);

        }
    }
}
