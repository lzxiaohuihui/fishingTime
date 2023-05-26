---
title: 在Ubuntu上统计软件使用时长
date: 2023-05-25 16:26:13
permalink: /pages/361d44/
---

## FishingTime

在Ubuntu X11桌面上统计**软件**使用时长和分析**VS Code**和**IDEA**项目情况。

统计的时长计算方式为，窗口获得焦点，到失去焦点这段时间，并且每次最大统计5分钟。

## 界面展示

### 主视图

![image-20230526202748669](https://lzh-images.oss-cn-hangzhou.aliyuncs.com/images/image-20230526202748669.png)

### 其它视图

![image-20230526202849891](https://lzh-images.oss-cn-hangzhou.aliyuncs.com/images/image-20230526202849891.png)

## 开始使用

```shell
# 终端1
cd statisticTime
mvn package
java -jar /home/lzh/Documents/IdeaProjects/statistic/statisticTime/target/statisticTime-1.0-SNAPSHOT.jar

# 终端2
cd statisticWeb
yarn
yarn dev
```



## 项目架构

![image-20230525164134513](https://lzh-images.oss-cn-hangzhou.aliyuncs.com/images/image-20230525164134513.png)

Python发送数据格式

- xid	  X11窗口id
- title	 窗口标题
- app	 激活窗口的软件名称
- time    激活时间戳



## MySQL表结构

```sql
CREATE TABLE statistics_time
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    app          VARCHAR(255) NOT NULL,
    title        VARCHAR(255),
    start_time   INT          NOT NULL,
    end_time     INT          NOT NULL,
    running_time INT
);
CREATE INDEX idx_app
    ON statistics_time (app);

CREATE INDEX idx_start_time
    ON statistics_time (start_time);
```





## 查询

这种查询方式虽然简单，但是对于数据库而言，查询语句均为All，全表扫描。

查询范围天的运行时间

```sql
SELECT DATE(DATE_FORMAT(FROM_UNIXTIME(start_time), '%Y-%m-%d')) AS time_hour,
       SUM(running_time)                                        AS total_running_time
FROM statistics_time
WHERE DATE(DATE_FORMAT(FROM_UNIXTIME(start_time), '%Y-%m-%d')) >= '2023-04-25'
  AND DATE(DATE_FORMAT(FROM_UNIXTIME(start_time), '%Y-%m-%d')) <= '2023-04-27'
GROUP BY time_hour;
```



查询范围天的各app运行时间

```sql
SELECT DATE(DATE_FORMAT(FROM_UNIXTIME(start_time), '%Y-%m-%d')) AS time_hour,
       app,
       SUM(running_time)                                        AS total_running_time
FROM statistics_time
WHERE DATE(DATE_FORMAT(FROM_UNIXTIME(start_time), '%Y-%m-%d')) >= '2023-04-25'
  AND DATE(DATE_FORMAT(FROM_UNIXTIME(start_time), '%Y-%m-%d')) <= '2023-04-27'
GROUP BY time_hour, app;
```



## 优化查询(待比较)

在业务层进行一下数据处理，仅根据时间戳进行查询，不进行日期格式转换，执行的单表查询类型为range。

查询范围时间内总的运行时间

```sql
SELECT running_time as total_running_time
FROM statistics_time
WHERE start_time >= 1685004118
  AND end_time <= 1685011373;
```



查询范围天的各app运行时间

```sql
SELECT app, sum(running_time) as total_running_time
FROM statistics_time
WHERE start_time >= 1685000118
  AND end_time <= 1685011373
group by app;
```

