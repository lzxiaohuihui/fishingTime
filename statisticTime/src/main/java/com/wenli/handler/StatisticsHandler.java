package com.wenli.handler;

import com.wenli.utils.SequenceIdGenerator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ChannelHandler.Sharable
public class StatisticsHandler extends ChannelInboundHandlerAdapter {

    private final RabbitTemplate rabbitTemplate;

    public StatisticsHandler(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        String data = StringEscapeUtils.unescapeJava((String) msg);
        String confirmId = String.valueOf(SequenceIdGenerator.nextId());
        rabbitTemplate.convertAndSend("statistics_direct_exchange", "linux", data,
                new CorrelationData(confirmId));
        System.out.println(data + "-" + confirmId);
    }
}
