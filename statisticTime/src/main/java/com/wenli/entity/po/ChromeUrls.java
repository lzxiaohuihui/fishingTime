package com.wenli.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description:
 * @date: 2023-06-07 8:38 p.m.
 * @author: lzh
 */
@Data
@TableName("urls")
public class ChromeUrls {
    @TableId
    private Long id;
    private String url;
    private String title;
    private Long visitCount;
    private Long typedCount;
    private Long lastVisitTime;
    private Long hidden;
}
