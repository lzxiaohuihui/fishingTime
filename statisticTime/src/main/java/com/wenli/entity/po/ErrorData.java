package com.wenli.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName t_error_data
 */
@TableName(value ="t_error_data")
@Data
public class ErrorData implements Serializable {
    private Integer id;

    private String xid;

    private String title;

    private String app;

    private String timestamp;

    private static final long serialVersionUID = 1L;
}