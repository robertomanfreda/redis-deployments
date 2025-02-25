package io.github.robertomanfreda.connectiontest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-master-replica-sentinel.yml")
public class RedisMasterReplicaSentinelConnectionTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ValueOperations<String, String> valueOps;

    @BeforeEach
    public void setUp() {
        valueOps = redisTemplate.opsForValue();
    }

    @Test
    public void testWriteReadDelete() {
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