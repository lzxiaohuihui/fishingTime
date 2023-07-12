package com.wenli;// package com.wenli;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.wenli.entity.dto.TimeRunning;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

/**
 * @description:
 * @date: 2023-06-13 4:20 p.m.
 * @author: lzh
 */
// @Ignore
public class TestDownload {
    @Test
    public void testImage() throws Exception {
        String imageUrl = "https://t3.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE," +
                "URL&url=https://google.com&size=32";
        String destinationFile = "image.png";

        String proxyHost = "127.0.0.1";
        int proxyPort = 7890;

        URL url = new URL(imageUrl);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
        InputStream inputStream = connection.getInputStream();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        System.out.println(base64Image);


        inputStream.close();
        connection.disconnect();
    }

    @Test
    public void testLeetcodeK() {
        String[] words = {"i", "love", "leetcode", "i", "love", "coding"};
        int k = 2;
        System.out.println(topKFrequent(words, k));

    }

    public List<String> topKFrequent(String[] words, int k) {

        Map<String, Integer> count = new HashMap<>();
        for (String word : words) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }

        PriorityQueue<String> pq = new PriorityQueue<>(k, (o1, o2) -> {
            if (count.get(o2) != count.get(o1)) {
                return count.get(o2) - count.get(o1);
            }
            return o2.compareTo(o1);
        });

        count.forEach((key, value) -> pq.add(key));
        int i = 0;
        List<String> res = new ArrayList<>();
        for (String s : pq) {
            if (i++ < k) {
                res.add(s);
            }
        }
        return res;


    }

    @Test
    public void testDateTime() {
        // 获取当前日期
        Date date = new Date();

        // 生成一天当中每一个小时的区间
        DateRange rangeList = DateUtil.range(DateUtil.beginOfDay(date), DateUtil.endOfDay(date), DateField.HOUR_OF_DAY);

        // 输出每个小时的区间
        for (Date range : rangeList) {
            System.out.println(DateUtil.formatDateTime(range) + " - " + DateUtil.formatDateTime(DateUtil.endOfHour(range)));
        }

        System.out.println(DateUtil.offsetDay(date, -7));
        System.out.println(DateUtil.beginOfWeek(date));
        System.out.println(DateUtil.endOfWeek(date));

        File source = new File("/home/lzh/.config/google-chrome/Default/History");
//        File dest = new File(System.getProperty("user.dir")+"/chromeHistory.db");
        File dest = new File("/home/lzh/history.db");

        try (InputStream in = Files.newInputStream(source.toPath());
             OutputStream out = Files.newOutputStream(dest.toPath())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            System.out.println("File copied successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWeek(){
        String oneDay = "2023-05-24";
        String[] split = oneDay.split("-");
        int year = Integer.parseInt(split[0]);
        int mouth = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        DateTime date = DateUtil.date(new Date(year - 1900, mouth - 1, day));
        int diff = date.dayOfWeek();
        diff = diff == 1 ? 8 : diff;
        String[] calendars = new String[7];
        List<TimeRunning> res = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
//            calendars[i] = DateUtil.offsetDay(date, 2 - diff + i).toString("YYYY-MM-dd");
            DateTime dateTime = DateUtil.offsetDay(date, 2 - diff + i);
            System.out.println(DateUtil.beginOfDay(dateTime).getTime() + "-" + DateUtil.endOfDay(dateTime).getTime());

//            TimeRunning runningTimeOneDay = statisticsTimeMapper.findRunningTimeOneDay(calendars[i]);
//            if (runningTimeOneDay != null) {
//                res.add(runningTimeOneDay);
//            } else {
//                res.add(new TimeRunning(calendars[i], 0));
//            }
        }
    }

    @Test
    public void testHome(){
        System.out.println(System.getProperty("user.home"));
        String ChromeHistoryPath = System.getProperty("user.home") + "/.config/google-chrome/Default/History";
        System.out.println(ChromeHistoryPath);

        System.out.println(System.getProperty("user.dir"));
        System.out.println(DateUtil.formatDate(new Date()));

    }


    public int compress(char[] chars) {
        int index = 0;
        char cur = chars[0];
        int curLen = 1;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == cur){
                curLen += 1;
            }
            else{
                chars[index++] = cur;
                if (curLen > 1) {
                    String s = String.valueOf(curLen);
                    for(char c: s.toCharArray()){
                        chars[index++] =  c;
                    }
                }
                curLen = 1;
            }
            cur = chars[i];
        }
        chars[index++] = cur;
        if (curLen > 1) {
            String s = String.valueOf(curLen);
            for(char c: s.toCharArray()){
                chars[index++] =  c;
            }
        }
        return index;
    }



    @Test
    public void testLc(){
        char[] nums = {'a','b','b','b','b','b','b','b','b','b','b','b','b'};
        compress(nums);
        System.out.println(nums);
    }

    public boolean increasingTriplet(int[] nums) {
        int n = nums.length;
        int[] flag = new int[n];
        int minValue = nums[0];
        for(int i = 1; i < n; i++){
            if(nums[i] > minValue) {
                flag[i] = 1;
            }
            minValue = Math.min(nums[i], minValue);
        }
        int index = 0;
        for(int i = 1; i < n; i++){
            if(flag[i] == 1){
                minValue = nums[i];
                index = i;
                break;
            }
        }

        if(index == 0) return false;

        for(int i = index+1; i < n; i++){
            if(nums[i] > minValue) return true;
            if(flag[i] == 1){
                minValue = Math.min(nums[i], minValue);
            }
        }

        return false;

    }

    @Test
    public void testN(){
        System.out.println(nHelp("nowcoder", 3));
    }

    public String nHelp(String s, int numRows){
        char[][] res = new char[numRows][s.length()];
        for (int i = 0; i < numRows; i++) Arrays.fill(res[i], ' ');
        res[0][0] = s.charAt(0);

        // 0: down, 1: up
        int flag = 0;
        int x = 0, y = 0;
        for (int i = 1; i < s.length(); i++) {
            if (flag == 0) x += 1;
            else{
                x -= 1;
                y += 1;
            }
            if (x == numRows-1) flag = 1;
            if (x == 0) flag = 0;
            res[x][y] = s.charAt(i);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < numRows; i++){
            for(int j = 0; j < s.length(); j++){
                if (res[i][j] != ' ') sb.append(res[i][j]);
            }
        }
        return sb.toString();

    }


}
