package com.wenli.service.impl;

import com.wenli.entity.po.ErrorData;
import com.wenli.mapper.ErrorDataMapper;
import com.wenli.service.ErrorDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @date: 6/21/23 8:12 PM
 * @author: lzh
 */

@Service
public class ErrorDataServiceImpl implements ErrorDataService {

    @Resource
    private ErrorDataMapper errorDataMapper;


    @Override
    public void addErrorDataRecord(ErrorData errorData){
        errorDataMapper.insert(errorData);
    }
}
