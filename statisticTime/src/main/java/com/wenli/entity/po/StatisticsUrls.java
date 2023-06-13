package com.wenli.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 统计网址的时间
 * @TableName statistics_urls
 */
@TableName(value ="statistics_urls")
@Data
public class StatisticsUrls implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 网址
     */
    private String url;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}