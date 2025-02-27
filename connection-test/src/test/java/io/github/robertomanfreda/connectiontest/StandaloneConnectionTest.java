package io.github.robertomanfreda.connectiontest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@EnabledIfSystemProperty(named = "spring.profiles.active", matches = "standalone")
@SpringBootTest
public class StandaloneConnectionTest {

    private LettuceConnectionFactory factory;
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setUp() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("localhost", 6379);
        configuration.setPassword("foobar");

        factory = new LettuceConnectionFactory(configuration);

        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();

        factory.start();
    }

    @AfterEach
    void tearDown() {
        factory.stop();
    }

    @Test
    public void testWriteReadDelete() {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        // Write
        String key = "testKey";
        String value = "Hello Redis";
        valueOps.set(key, value);

        // Read
        String retrievedValue = valueOps.get(key);

        // Verify
        assertThat(retrievedValue).isEqualTo(value);

        // Clean
        redisTemplate.delete(key);
    }
}