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

