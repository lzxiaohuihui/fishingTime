package com.wenli.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description:
 * @date: 7/5/23 11:27 AM
 * @author: lzh
 */

@Data
@AllArgsConstructor
public class ItemDto {
    private String app;
    private String title;
    private Long startTime;
    private String runningTime;
}
