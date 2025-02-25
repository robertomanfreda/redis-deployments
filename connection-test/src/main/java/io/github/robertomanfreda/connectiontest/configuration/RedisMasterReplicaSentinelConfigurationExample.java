package io.github.robertomanfreda.connectiontest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static io.lettuce.core.ReadFrom.REPLICA_PREFERRED;

//@Configuration
public class RedisMasterReplicaSentinelConfigurationExample {

    @Bean
    public RedisSentinelConfiguration redisSentinelConfiguration() {
        RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration()
                .master("mymaster")
                .sentinel("192.168.1.145", 26379)
                .sentinel("192.168.1.146", 26379)
                .sentinel("192.168.1.147", 26379);
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
    public RedisTemplate<String, String> lettuceConnectionFactory(@Autowired LettuceConnectionFactory
                                                                          lettuceConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}