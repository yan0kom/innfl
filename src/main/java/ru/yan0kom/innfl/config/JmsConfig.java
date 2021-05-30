package ru.yan0kom.innfl.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsConfig {
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        val jt = new JmsTemplate(connectionFactory);
        jt.setMessageConverter(jacksonJmsMessageConverter());
        return jt;
    }

    @Bean
    public JmsTemplate jmsTemplateWithDeliveryDelay(
            ConnectionFactory connectionFactory,
            @Value("${fns-inn-fio-dr.retry-get-on-52-delay}") Long delay) {
        val jt = new JmsTemplate(connectionFactory);
        jt.setMessageConverter(jacksonJmsMessageConverter());
        jt.setDeliveryDelay(delay);
        return jt;
    }

    @Bean
    public DefaultJmsListenerContainerFactory containerFactory() {
        val factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionTransacted(true);
        factory.setMaxMessagesPerTask(1);
        factory.setConcurrency("1-5");
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        val bean = new Jackson2ObjectMapperFactoryBean();
        bean.setSimpleDateFormat("yyyy-MM-dd");
        bean.afterPropertiesSet();
        ObjectMapper objectMapper = bean.getObject();

        val converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
