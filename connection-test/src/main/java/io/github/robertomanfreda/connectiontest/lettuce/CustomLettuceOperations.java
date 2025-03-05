package io.github.robertomanfreda.connectiontest.lettuce;

import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.ArrayOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomLettuceOperations {

    private final LettuceConnectionFactory lettuceConnectionFactory;

    /*
     * Temporary custom implementation for HEXPIRE Operation, RedisTemplate is working on the support
     */
    public List<Long> hexpire(String key, String hashKey, HexpireOption hexpireOption, int seconds) {
        LettuceConnection lettuceConnection = (LettuceConnection) lettuceConnectionFactory.getConnection();

        /* Array reply. For each field:
            Integer reply:  -2 if no such field exists in the provided hash key, or the provided key does not exist.
            Integer reply:  0 if the specified NX | XX | GT | LT condition has not been met.
            Integer reply:  1 if the expiration time was set/updated.
            Integer reply:  2 when HEXPIRE/HPEXPIRE is called with 0 seconds/milliseconds or when HEXPIREAT/HPEXPIREAT
                            is called with a past Unix time in seconds/milliseconds.
         */

        //noinspection unchecked
        return (List<Long>) lettuceConnection.execute(
                "HEXPIRE",
                new ArrayOutput<>(StringCodec.UTF8),
                key.getBytes(), String.valueOf(seconds).getBytes(), hexpireOption.toString().getBytes(),
                "FIELDS".getBytes(), "1".getBytes(), hashKey.getBytes()
        );
    }
}
