package com.chenyj.moredatasource.datasource.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Author chenyj
 * @Description
 * @Date create by 2020/4/18 0018
 * 陈银杰专属测试
 */
@Configuration
public class DoubleRedisConfig  {



    /**
     * 配置lettuce连接池
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    public GenericObjectPoolConfig redisPool() {
        return new GenericObjectPoolConfig<>();
    }


    /**
     * 配置第一个数据源的
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisStandaloneConfiguration redisConfig() {
        return new RedisStandaloneConfiguration();
    }

    /**
     * 配置第二个数据源
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis-token")
    public RedisStandaloneConfiguration redisTokenConfig() {
        return new RedisStandaloneConfiguration();
    }

    /**
     * 配置第一个数据源的连接工厂
     * 这里注意：需要添加@Primary 指定bean的名称，目的是为了创建两个不同名称的LettuceConnectionFactory
     *
     * @param config
     * @param redisConfig
     * @return
     */
    @Bean("factory")
    @Primary
    public LettuceConnectionFactory factory(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfig) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisConfig, clientConfiguration);
    }

    @Bean("tokenfactory")
    public LettuceConnectionFactory tokenfactory(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisTokenConfig) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisTokenConfig, clientConfiguration);
    }

    /**
     * 配置第一个数据源的RedisTemplate
     * 注意：这里指定使用名称=factory 的 RedisConnectionFactory
     * 并且标识第一个数据源是默认数据源 @Primary
     *
     * @param factory
     * @return
     */
    @Bean("redisTemplate")
    @Primary
    public RedisTemplate<String, String> redisTemplate(@Qualifier("factory") RedisConnectionFactory factory) {
        return getStringStringRedisTemplate(factory);
    }

    /**
     * 配置第一个数据源的RedisTemplate
     * 注意：这里指定使用名称=factory2 的 RedisConnectionFactory
     *
     * @param tokenfactory
     * @return
     */
    @Bean("redisTokenTemplate")
    public RedisTemplate<String, String> redisTokenTemplate(@Qualifier("tokenfactory") RedisConnectionFactory tokenfactory) {
        return getStringStringRedisTemplate(tokenfactory);
    }

    /**
     * 设置序列化方式 （这一步不是必须的）
     *
     * @param factory
     * @return
     */
    private RedisTemplate<String, String> getStringStringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        return template;
    }
}
