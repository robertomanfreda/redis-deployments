package io.github.robertomanfreda.connectiontest.layout;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/layout")
@RequiredArgsConstructor
@RestController
public class LayoutController {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Returns the Set containing all the seats in a stadium
    @GetMapping("/{stadiumName}")
    public ResponseEntity<Set<LayoutSeat>> getAllSeats(@PathVariable String stadiumName) {
        // REDIS COMMAND -> SMEMBERS key
        Set<String> members = redisTemplate.opsForSet().members("stadium:" + stadiumName + ":seats:layout");

        if (members != null) {
            Set<LayoutSeat> collect = members.stream()
                    .map(s -> {
                        try {
                            return objectMapper.readValue(s, LayoutSeat.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());

            return ResponseEntity.ok(collect);
        }

        return ResponseEntity.notFound().build();
    }


    @PostConstruct
    private void fillSanSiro() {
        List<LayoutSeat> layoutSeats = List.of(
                new LayoutSeat("maradona", "VIP", 0),
                new LayoutSeat("maradona", "VIP", 1),
                new LayoutSeat("maradona", "VIP", 2),
                new LayoutSeat("maradona", "VIP", 3),
                new LayoutSeat("maradona", "VIP", 4),
                new LayoutSeat("maradona", "VIP", 5),
                new LayoutSeat("maradona", "VIP", 6),
                new LayoutSeat("maradona", "VIP", 7),
                new LayoutSeat("maradona", "VIP", 8),
                new LayoutSeat("maradona", "VIP", 9),
                new LayoutSeat("maradona", "VIP", 10),
                new LayoutSeat("maradona", "VIP", 11),
                new LayoutSeat("maradona", "VIP", 12),
                new LayoutSeat("maradona", "VIP", 13),
                new LayoutSeat("maradona", "VIP", 14),
                new LayoutSeat("maradona", "VIP", 15),
                new LayoutSeat("maradona", "VIP", 16),
                new LayoutSeat("maradona", "NORMAL", 17),
                new LayoutSeat("maradona", "NORMAL", 18),
                new LayoutSeat("maradona", "NORMAL", 19),
                new LayoutSeat("maradona", "NORMAL", 20)
        );

        layoutSeats.forEach(ls -> {
            try {
                // REDIS COMMAND -> SADD key value
                redisTemplate.opsForSet().add("stadium:maradona:seats:layout", objectMapper.writeValueAsString(ls));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
