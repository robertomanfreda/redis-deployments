package io.github.robertomanfreda.connectiontest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static io.lettuce.core.ReadFrom.REPLICA_PREFERRED;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisSentinelConfiguration redisSentinelConfiguration() {
        RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration()
                .master("mymaster")
                .sentinel("127.0.0.1", 26379)
                .sentinel("127.0.0.1", 26380)
                .sentinel("127.0.0.1", 26381);
        redisConfig.setPassword("foobar");
        redisConfig.setSentinelPassword("foobarbaz");

        return redisConfig;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(@Autowired RedisSentinelConfiguration
                                                                     redisSentinelConfiguration) {
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .readFrom(REPLICA_PREFERRED)
                .build();

        return new LettuceConnectionFactory(redisSentinelConfiguration, lettuceClientConfiguration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(@Autowired LettuceConnectionFactory
                                                                   lettuceConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}