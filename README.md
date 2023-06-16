## 2023/6/16 日志

优化了数据库查询性能，从原来的全表扫描，到都用上了索引。

单个接口性能  **20** QPS  --->    **400**  QPS

使用caffeine后缓存后

单个接口性能 **400** QPS  --->    **6000**  QPS。

### 使用本地缓存

|                接口                |      返回数据      |
| :--------------------------------: | :----------------: |
|      getSevenTimes/2023-06-15      |    总的运行时间    |
| getAppRunningTimeOneDay/2023-06-15 |   app的运行时间    |
|  getRunningTimeByHour/2023-06-15   | 各个小时的运行时间 |

这里都有重叠的地方，不需要都建立缓存。**保存每个app每个小时的运行时间**。

app的个数，我不超过10个，按10个算。按一天休息8小时，最多有16个小时是运行了电脑，要保存每个app每个小时的运行时间即可，一天的数据量是160，每个数据是app名称（假设31字节），时间（1字节），一个数据32字节，一天数据5.12KB，一年的数据不到2MB。

```json
// 8点，app1运行120秒
appHoursCache = {
    "date1": {
        "hour1": {{"app1": 120}, {"app2", 350}, {"app3", 23}},
        "hour2": {{"app1": 120}, {"app2", 350}, {"app3", 23}}
    } 
    "date2": {
        "hour1": {{"app1": 120}, {"app2", 350}, {"app3", 23}},
        "hour2": {{"app1": 120}, {"app2", 350}, {"app3", 23}}
    } 
}
```





|                    接口                    |           返回数据           |
| :----------------------------------------: | :--------------------------: |
| getVscodeRunningTime/2023-06-08_2023-06-15 | 返回VScode的各个项目运行时间 |
|  getIdeaRunningTime/2023-06-08_2023-06-15  |  返回Idea各个项目的运行时间  |
|    getChromeTime/2023-06-08_2023-06-15     | 返回Chrome各个网址的浏览时间 |

这三个数据都没有重叠的地方，需要分别建立缓存。

分别保存每一天各个项目的运行时间，或者各个网址。

对于当天的缓存数据每十分钟更新一次，凡是过去，皆为序章，对于过往的缓存数据不作处理。

```json
vsCodeCache = {
    "date1": {"project1": "time1", "project2": "time2"},
    "date2": {"project1": "time1", "project2": "time2"},
    ...
}
    
ideaCache = {
    date1: {"project1": "time1", "project2": "time2"},
    date2: {"project1": "time1", "project2": "time2"},
    ...
}


titles = {
    "date1": ["title1", "title2", "title3",...],
    "date2": ["title1", "title2", "title3",...],
    ...
}

chromeTitleToUrl = {
    "title1": "url1",
    "title2": "url1",
    "title3": "url4",
    ...
}		
```



::: danger 更新当日缓存

每1分钟更新当日数据。线程池，定时任务

:::

::: note 更新最新数据（待完成）

使用netty监听Python端发送过来的数据，将其汇总，每隔一段时间使用消息队列发布消息，消费者拿到消息后，存到MySQL，和添加到缓存。

:::

::: note 当缓存中没数据，所有请求都会访问MySQL

加锁，只让一个请求去写入缓存

:::



## 2023/6/12 更新

使用Chrome api来获取网站的图标

https://www.google.com/s2/favicons?domain=google.com

读取Chrome浏览器的History文件

![image-20230613185205634](https://lzh-images.oss-cn-hangzhou.aliyuncs.com/images/image-20230613185205634.png)



## 项目架构

![image-20230525164134513](https://lzh-images.oss-cn-hangzhou.aliyuncs.com/images/image-20230525164134513.png)

Python发送数据格式

- xid	  X11窗口id
- title	 窗口标题
- app	 激活窗口的软件名称
- time    激活时间戳



## 展示

![image-20230616171612194](https://lzh-images.oss-cn-hangzhou.aliyuncs.com/images/image-20230616171612194.png)

![image-20230616171640246](https://lzh-images.oss-cn-hangzhou.aliyuncs.com/images/image-20230616171640246.png)

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



## 优化查询

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

