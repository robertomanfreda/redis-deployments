package io.github.robertomanfreda.connectiontest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test/runtime/failover")
@RequiredArgsConstructor
@RestController
public class TestController {

    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/write")
    public String test() {
        redisTemplate.opsForValue().set("foo", "bar");
        return redisTemplate.opsForValue().get("foo");
    }
}
