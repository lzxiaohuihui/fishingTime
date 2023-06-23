package com.wenli.consumer;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.wenli.entity.dto.WindowDto;
import com.wenli.entity.po.ErrorData;
import com.wenli.entity.po.StatisticsTime;
import com.wenli.service.ErrorDataService;
import com.wenli.service.StatisticsTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @description:
 * @date: 6/21/23 9:33 AM
 * @author: lzh
 */
@Component
@Slf4j
public class StatisticsLinuxConsumer {

    private WindowDto lastSeen;

    @Resource
    private StatisticsTimeService statisticsTimeService;


    @Resource
    private ErrorDataService errorDataService;


    private final long maxTime = 300;


    public StatisticsLinuxConsumer(){
        lastSeen = new WindowDto(0, null, null, System.currentTimeMillis()/1000);
    }

    @RabbitListener(queues = "statistics_linux_queue")
    public void LinuxQueueConsumer(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String s = new String(message.getBody());
            WindowDto windowDto = JSON.parseObject(s, WindowDto.class);
            statisticsTimeService.addStatisticsTime(lastSeen, windowDto);
            lastSeen = windowDto;
            channel.basicAck(deliveryTag, false);
            log.info("消费者签收消息成功 [{}]", deliveryTag);
        }catch (RuntimeException e){
            channel.basicNack(deliveryTag, false, true);
            log.error("消费者签收消息失败 [{}], 放回原队列", deliveryTag);
        }catch (Exception e){
            channel.basicNack(deliveryTag, false, false);
            log.error("消费者签收消息失败 [{}], 放入死信队列", deliveryTag);
        }
    }

    @RabbitListener(queues = {"statistics_dlx_queue"})
    public void dlxQueueConsumer(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String s = new String(message.getBody());
            WindowDto windowDto = JSON.parseObject(s, WindowDto.class);
            ErrorData errorData = new ErrorData();
            BeanUtil.copyProperties(windowDto, errorData);
            errorDataService.addErrorDataRecord(errorData);
        }finally {
            channel.basicAck(deliveryTag, false);
        }
    }


}
