package com.wenli.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("statistics_time")
//@Document(indexName = "statistics")
public class StatisticsTime {
    @TableId(type = IdType.AUTO)
//    @Id
    private Long id;

//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String app;

//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;

//    @Field(type = FieldType.Long)
    private Long startTime;

//    @Field(type = FieldType.Long)
    private Long endTime;

//    @Field(type = FieldType.Long)
    private Long runningTime;
}