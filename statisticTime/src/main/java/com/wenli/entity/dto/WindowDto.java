package com.wenli.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class WindowDto {
    private long xid;
    private String title;
    private String app;
    private long timestamp;

}
