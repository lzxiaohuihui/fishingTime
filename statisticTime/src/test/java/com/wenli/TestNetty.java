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

        String s1 = "abc";
        String s2 = "abc";
        String s3 = new String("abc");
        String s4 = new String("abc");
        System.out.println(s1 == s2);
        System.out.println(s3 == s4);
        System.out.println(s1 == s3);
        String s5 = s3.intern();
        System.out.println(s1 == s5);
    }
}
