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
        List<TimeRunning> runningTimeByHour = statisticsTimeService.findRunningTimeByHourOneDay(day);
        boolean[] flag = new boolean[24];
        for (TimeRunning timeRunning : runningTimeByHour) {
            flag[Integer.parseInt(timeRunning.getTimeStamp().substring(0, 2))] = true;
        }
        for (int i = 0; i < flag.length; i++) {
            if (flag[i]) continue;
            if (i < 10) {
                runningTimeByHour.add(new TimeRunning("0" + i + ":00", 0));
            } else {
                runningTimeByHour.add(new TimeRunning(i + ":00", 0));
            }
        }
        Collections.sort(runningTimeByHour, new Comparator<TimeRunning>() {
            @Override
            public int compare(TimeRunning o1, TimeRunning o2) {
                return Integer.parseInt(o1.getTimeStamp().substring(0, 2))-Integer.parseInt(o2.getTimeStamp().substring(0, 2));
            }
        });
        return runningTimeByHour;

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