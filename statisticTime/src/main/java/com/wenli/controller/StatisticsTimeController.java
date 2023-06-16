package com.wenli.controller;

import com.wenli.entity.dto.TimeRunning;
import com.wenli.entity.dto.AppTimeRunning;
import com.wenli.service.StatisticsTimeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
public class StatisticsTimeController {
    @Resource
    private StatisticsTimeService statisticsTimeService;

    @CrossOrigin
    @GetMapping("/getRunningTimeByHour/{day}")
    public List<TimeRunning> getRunningTimeByHour(@PathVariable String day) {
        return statisticsTimeService.findRunningTimeByHourOneDay(day);
    }

    @CrossOrigin
    @GetMapping("/getSevenTimes/{day}")
    public List<TimeRunning> getSevenTimes(@PathVariable String day){
        return statisticsTimeService.findRunningTimeSevenDays(day);
    }

    @CrossOrigin
    @GetMapping("/getAppRunningTimeOneDay/{day}")
    public List<AppTimeRunning> getAppRunningTimeOneDay(@PathVariable String day) {
        return statisticsTimeService.findAppRunningTimeOneDay(day);
    }


}