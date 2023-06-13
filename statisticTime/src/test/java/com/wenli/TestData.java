package com.wenli;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenli.entity.po.ChromeUrls;
import com.wenli.entity.po.StatisticsTime;
import com.wenli.mapper.ChromeUrlMapper;
import com.wenli.mapper.StatisticsTimeMapper;
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

        try (InputStream in = Files.newInputStream(source.toPath());
             OutputStream out = Files.newOutputStream(dest.toPath())) {
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
        QueryWrapper<ChromeUrls> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("last_visit_time", 13323186463309130L);
        List<ChromeUrls> chromeHistories = chromeHistoryMapper.selectList(queryWrapper);
        for (ChromeUrls chromeHistory : chromeHistories) {
            System.out.println(new Date(chromeHistory.getLastVisitTime() / 1000 - 11644473600000L));
        }
        System.out.println(chromeHistories.size());
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
        path.add(Arrays.asList(0,0));
        int[] preStep = {0, 0};
        findAllGraph(paths, visited, path, 9, preStep);
        for (List<List<Integer>> lists : paths) {
            System.out.println(lists);
        }
    }

    int[][] direction = new int[][]{
            {0, 1}, {0, -1}, {1, 0}, {-1, 0}
    };

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
            path.remove(path.size()-1);
            visited[nextX][nextY] = false;
        }

    }

    @Test
    public void testUrl(){
        List<String> titleByUrl = chromeHistoryMapper.findTitleByUrl("https://www.bilibili.com%");
        System.out.println(titleByUrl);
    }
}
