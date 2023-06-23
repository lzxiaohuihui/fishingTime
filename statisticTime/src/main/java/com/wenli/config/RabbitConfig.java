package com.wenli.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    private static final String EXCHANGE_NAME = "statistics_direct_exchange";
    private static final String LINUX_QUEUE_NAME = "statistics_linux_queue";

    private static final String WINDOWS_QUEUE_NAME = "statistics_windows_queue";
    private static final String DLX_QUEUE_NAME = "statistics_dlx_queue";
    private static final String DLX_EXCHANGE_NAME = "statistics_dlx_exchange";

    @Bean("statisticsExchange")
    public Exchange statisticsExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("statisticsLinuxQueue")
    public Queue statisticsLinuxQueue() {
        return QueueBuilder.durable(LINUX_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", "statistics_dlx")
                .build();
    }

    @Bean("statisticsWindowsQueue")
    public Queue statisticsWindowsQueue() {
        return QueueBuilder.durable(WINDOWS_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", "statistics_dlx")
                .build();
    }


    @Bean("statisticsDlxExchange")
    public Exchange statisticsDlxExchange() {
        return ExchangeBuilder.directExchange(DLX_EXCHANGE_NAME).durable(true).build();
    }

    @Bean("statisticsDlxQueue")
    public Queue statisticsDlxQueue() {
        return QueueBuilder.durable(DLX_QUEUE_NAME).build();
    }

    @Bean
    public Binding bindLinuxQueueExchange(@Qualifier("statisticsLinuxQueue") Queue queue, @Qualifier(
            "statisticsExchange") Exchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with("linux").noargs();
    }


    @Bean
    public Binding bindWindowsQueueExchange(@Qualifier("statisticsWindowsQueue") Queue queue, @Qualifier(
            "statisticsExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("windows").noargs();
    }

    @Bean
    public Binding bindDlxQueueExchange(@Qualifier("statisticsDlxQueue") Queue queue, @Qualifier(
            "statisticsDlxExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("statistics_dlx").noargs();
    }


}