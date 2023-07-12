package com.wenli.controller;

import cn.hutool.core.bean.BeanUtil;
import com.wenli.entity.dto.AppTimeRunning;
import com.wenli.entity.dto.ItemDto;
import com.wenli.entity.po.StatisticsTime;
import com.wenli.entity.po.StatisticsTimeDoc;
import com.wenli.service.StatisticsBrowser;
import com.wenli.service.StatisticsESService;
import com.wenli.service.StatisticsOtherService;
import com.wenli.service.StatisticsUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @date: 2023-05-26 6:28 p.m.
 * @author: lzh
 */
@RestController

public class StatisticsOtherController {

    @Resource
    private StatisticsOtherService statisticsOtherService;

    @Resource
    private StatisticsBrowser statisticsChrome;


    @Resource
    private StatisticsUrlService statisticsUrlService;

    @Autowired
    private StatisticsESService statisticsESService;

    @CrossOrigin
    @GetMapping("/getVscodeRunningTime/{day}")
    public List<AppTimeRunning> getVscodeTime(@PathVariable String day){
        String[] split = day.split("_");
        if(split.length != 2) return null;
        return statisticsOtherService.findVsCodeRunningTime(split[0], split[1]);
    }

    @CrossOrigin
    @GetMapping("/getIdeaRunningTime/{day}")
    public List<AppTimeRunning> getIdeaTime(@PathVariable String day){
        String[] split = day.split("_");
        if(split.length != 2) return null;
        return statisticsOtherService.findIdeaRunningTime(split[0], split[1]);
    }

    @CrossOrigin
    @GetMapping("/getChromeTime/{day}")
    public List<AppTimeRunning> getChromeTile(@PathVariable String day){
        String[] split = day.split("_");
        if(split.length != 2) return null;
        // sqlite 中的title
//        Map<String, String> chromeTitle = statisticsChrome.getChromeTitle();

        // mysql 中的title
        List<StatisticsTime> allChromeRecords = statisticsOtherService.findAllChromeRecords(split[0], split[1]);
        List<AppTimeRunning> res = new ArrayList<>();
        Map<String, Long> times = new HashMap<>();
        for (StatisticsTime s : allChromeRecords) {
            if (s.getTitle() == null || "Untitled".equals(s.getTitle())) continue;
            String app = statisticsChrome.getChromeUrl(s.getTitle());
            if ("None".equals(app)) continue;
            times.put(app, times.getOrDefault(app, 0L)+s.getRunningTime());
        }
        times.forEach((k, v)->{
            res.add(new AppTimeRunning("", k, v));
        });
        return res;

    }

    @CrossOrigin
    @GetMapping("/search/{text}")
    public List<ItemDto> getItems(@PathVariable String text){
        if (text == null ) return null;
        String[] split = text.split("_");
        if (split.length > 2) return null;

        String[] args = new String[2];
        args[0] = split[0];
        if (split.length == 1) args[1] = "";
        else args[1] = split[1];

        List<StatisticsTimeDoc> statistics = statisticsESService.findByTermQuery("statistics", args);
        List<ItemDto> res = new ArrayList<>();

        if (statistics == null) return null;
        for (StatisticsTimeDoc statistic : statistics) {
            res.add(BeanUtil.copyProperties(statistic, ItemDto.class));
        }

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
