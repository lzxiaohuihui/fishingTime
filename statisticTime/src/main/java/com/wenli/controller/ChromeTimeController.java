package com.wenli.controller;

import com.wenli.entity.dto.AppTimeRunning;
import com.wenli.entity.po.StatisticsTime;
import com.wenli.service.StatisticsChrome;
import com.wenli.service.StatisticsTimeService;
import com.wenli.service.impl.StatisticsUrlService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @date: 2023-06-13 9:05 a.m.
 * @author: lzh
 */
@RestController
public class ChromeTimeController {

    @Resource
    private StatisticsChrome statisticsChrome;

    @Resource
    private StatisticsTimeService statisticsTimeService;

    @Resource
    private StatisticsUrlService statisticsUrlService;


    @CrossOrigin
    @GetMapping("/getChromeTime/{day}")
    public List<AppTimeRunning> getChromeTile(@PathVariable String day){
        String[] split = day.split("_");
        if(split.length != 2) return null;
        // sqlite 中的title
        Map<String, String> chromeTitle = statisticsChrome.getChromeTitle();

        // mysql 中的title
        List<StatisticsTime> allChromeRecords = statisticsTimeService.findAllChromeRecords(split[0], split[1]);

        List<AppTimeRunning> res = new ArrayList<>();
        Map<String, Long> times = new HashMap<>();
        for (StatisticsTime s : allChromeRecords) {
            if ("Untitled".equals(s.getTitle())) continue;
            String app = chromeTitle.getOrDefault(s.getTitle(), "None");
            if ("None".equals(app)) continue;
            times.put(app, times.getOrDefault(app, 0L)+s.getRunningTime());
        }
        times.forEach((k, v)->{
            res.add(new AppTimeRunning("", k, v));
        });

        return res;

    }

    @CrossOrigin
    @PostMapping("/addStatisticsUrl")
    public String addChromeUrl(@RequestParam("url") String url){
        System.out.println(url);
        if (statisticsUrlService.addStatisticsUrl(url)) {
            return "ok";
        }
        else{
            return "failed";
        }
    }
}
