package org.ivipa.ratel.rockie.server.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.time.Duration;

@Slf4j
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800, redisNamespace = "ratel-system-server")
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.database}")
    private int database;

    @Value("${spring.data.redis.jedis.pool.max-active}")
    private int maxTotal;

    @Value("${spring.data.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.data.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.data.redis.jedis.pool.max-wait}")
    private int maxWaitMillis;


    @Value("${ratel.system.redis.port}")
    private int systemRedisPort;

    @Value("${ratel.system.redis.host}")
    private String systemRedisHost;

    @Value("${ratel.system.redis.password}")
    private String systemRedisPassword;

    @Value("${ratel.system.redis.database}")
    private int systemRedisDatabase;

    @Value("${ratel.system.redis.jedis.pool.max-active}")
    private int systemRedisMaxTotal;

    @Value("${ratel.system.redis.jedis.pool.max-idle}")
    private int systemRedisMaxIdle;

    @Value("${ratel.system.redis.jedis.pool.min-idle}")
    private int systemRedisMinIdle;

    @Value("${ratel.system.redis.jedis.pool.max-wait}")
    private int systemRedisMaxWaitMillis;


    @Value("${ratel.rockie.redis.port}")
    private int rockieRedisPort;

    @Value("${ratel.rockie.redis.host}")
    private String rockieRedisHost;

    @Value("${ratel.rockie.redis.password}")
    private String rockieRedisPassword;

    @Value("${ratel.rockie.redis.database}")
    private int rockieRedisDatabase;

    @Value("${ratel.rockie.redis.jedis.pool.max-active}")
    private int rockieRedisMaxTotal;

    @Value("${ratel.rockie.redis.jedis.pool.max-idle}")
    private int rockieRedisMaxIdle;

    @Value("${ratel.rockie.redis.jedis.pool.min-idle}")
    private int rockieRedisMinIdle;

    @Value("${ratel.rockie.redis.jedis.pool.max-wait}")
    private int rockieRedisMaxWaitMillis;

    @Bean("redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory(@Qualifier("redisPoolConfig")JedisPoolConfig poolConfig) {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(poolConfig);
        redisConnectionFactory.getStandaloneConfiguration().setHostName(host);
        redisConnectionFactory.getStandaloneConfiguration().setPort(port);
        redisConnectionFactory.getStandaloneConfiguration().setPassword(password);
        redisConnectionFactory.getStandaloneConfiguration().setDatabase(database);
        return redisConnectionFactory;
    }

    @Primary
    @Bean("redisTemplate")
    public RedisTemplate<String, Serializable> redisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory factory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<String, Serializable>();
        redisTemplate.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        StringRedisSerializer stringRedisSerializer =  new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }


    @Bean("redisPoolConfig")
    public JedisPoolConfig redisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWait(Duration.ofMillis(maxWaitMillis));
        return config;
    }


    @Bean("systemRedisConnectionFactory")
    public RedisConnectionFactory systemRedisConnectionFactory(@Qualifier("systemRedisPoolConfig")JedisPoolConfig poolConfig) {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(poolConfig);
        redisConnectionFactory.getStandaloneConfiguration().setHostName(systemRedisHost);
        redisConnectionFactory.getStandaloneConfiguration().setPort(systemRedisPort);
        redisConnectionFactory.getStandaloneConfiguration().setPassword(systemRedisPassword);
        redisConnectionFactory.getStandaloneConfiguration().setDatabase(systemRedisDatabase);
        return redisConnectionFactory;
    }

    @Bean("systemRedisTemplate")
    public RedisTemplate<String, Serializable> systemRedisTemplate(@Qualifier("systemRedisConnectionFactory") RedisConnectionFactory factory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<String, Serializable>();
        redisTemplate.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        StringRedisSerializer stringRedisSerializer =  new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }


    @Bean("systemRedisPoolConfig")
    public JedisPoolConfig systemRedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(systemRedisMaxTotal);
        config.setMaxIdle(systemRedisMaxIdle);
        config.setMinIdle(systemRedisMinIdle);
        config.setMaxWait(Duration.ofMillis(systemRedisMaxWaitMillis));
        return config;
    }



    @Bean("rockieRedisConnectionFactory")
    public RedisConnectionFactory rockieRedisConnectionFactory(@Qualifier("rockieRedisPoolConfig")JedisPoolConfig poolConfig) {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(poolConfig);
        redisConnectionFactory.getStandaloneConfiguration().setHostName(rockieRedisHost);
        redisConnectionFactory.getStandaloneConfiguration().setPort(rockieRedisPort);
        redisConnectionFactory.getStandaloneConfiguration().setPassword(rockieRedisPassword);
        redisConnectionFactory.getStandaloneConfiguration().setDatabase(rockieRedisDatabase);
        return redisConnectionFactory;
    }

    @Bean("rockieRedisTemplate")
    public RedisTemplate<String, Serializable> rockieRedisTemplate(@Qualifier("rockieRedisConnectionFactory") RedisConnectionFactory factory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<String, Serializable>();
        redisTemplate.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        StringRedisSerializer stringRedisSerializer =  new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }


    @Bean("rockieRedisPoolConfig")
    public JedisPoolConfig rockieRedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(rockieRedisMaxTotal);
        config.setMaxIdle(rockieRedisMaxIdle);
        config.setMinIdle(rockieRedisMinIdle);
        config.setMaxWait(Duration.ofMillis(rockieRedisMaxWaitMillis));
        return config;
    }



}

