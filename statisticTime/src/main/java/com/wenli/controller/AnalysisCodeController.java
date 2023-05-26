package com.wenli.controller;

import com.wenli.entity.dto.AppTimeRunning;
import com.wenli.service.StatisticsTimeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @date: 2023-05-26 6:28 p.m.
 * @author: lzh
 */
@RestController

public class AnalysisCodeController {

    @Resource
    private StatisticsTimeService statisticsTimeService;

    @CrossOrigin
    @GetMapping("/getVscodeRunningTime/{day}")
    public List<AppTimeRunning> getVscodeTime(@PathVariable String day){
        String[] split = day.split("_");
        if(split.length != 2) return null;
        return statisticsTimeService.findVsCodeRunningTime(split[0], split[1]);
    }

    @CrossOrigin
    @GetMapping("/getIdeaRunningTime/{day}")
    public List<AppTimeRunning> getIdeaTime(@PathVariable String day){
        String[] split = day.split("_");
        if(split.length != 2) return null;
        return statisticsTimeService.findIdeaRunningTime(split[0], split[1]);
    }
}
