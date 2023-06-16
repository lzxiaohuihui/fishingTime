package com.wenli.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName segments
 */
@TableName(value ="segments")
@Data
public class Segment implements Serializable {
    @TableId
    private Long id;

    private String name;

    private Long urlId;

    private static final long serialVersionUID = 1L;

    public Segment(String name) {
        this.name = name;
    }
}