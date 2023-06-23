package com.wenli;


import com.alibaba.fastjson.JSON;
import org.apache.lucene.util.RamUsageEstimator;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

/**
 * @description:
 * @date: 6/20/23 9:02 PM
 * @author: lzh
 */

@Ignore
public class TestNetty {

    @Test
    public void testNetty(){
        Map<String, List<Integer>> res = new HashMap<>();
        res.put("test", Arrays.asList(1, 2, 3, 4, 5));
        res.put("test1", Arrays.asList(1, 2, 3, 4, 5));
        res.put("test2", Arrays.asList(1, 2, 3, 4, 5));
        res.put("test3", Arrays.asList(1, 2, 3, 4, 5));
        res.put("test4", Arrays.asList(1, 2, 3, 4, 5));
        res.put("test5", Arrays.asList(1, 2, 3, 4, 5));
        System.out.println(JSON.toJSONString(res).getBytes().length + "B");
    }
}
