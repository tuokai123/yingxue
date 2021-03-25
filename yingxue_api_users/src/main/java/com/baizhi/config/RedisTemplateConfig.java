package com.baizhi.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


//注解:代表这是一个配置类   spring.xml
@Configuration
public class RedisTemplateConfig {



    @Bean  //bean  工厂中创建对象是谁 RedisTemplate 注意: 方法名必须为redisTemplate
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //1.创建对象
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        //2.指定redis连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //3.使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        //4.设置key序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //5.设置hash key序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //6.设置value序列化方式为json
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        //7.工厂创建redisTemplate对象之后在进行配置
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}