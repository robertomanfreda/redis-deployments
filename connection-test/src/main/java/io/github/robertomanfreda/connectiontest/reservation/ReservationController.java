package io.github.robertomanfreda.connectiontest.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.robertomanfreda.connectiontest.lettuce.CustomLettuceOperations;
import io.github.robertomanfreda.connectiontest.lettuce.HexpireOption;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/reservation")
@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final CustomLettuceOperations customLettuceOperations;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/lock")
    public ResponseEntity<?> lock(@RequestBody SeatLock seatLock) {
        // TODO Check if seat is not already confirmed
        // then
        String key = "stadium:" + seatLock.getStadiumName().toLowerCase() + ":seats:lock";
        String hashKey = seatLock.getSeatType() + ":" + seatLock.getSeatNumber();

        try {
            // REDIS COMMAND -> HSET key hashKey fields
            Boolean saved = redisTemplate.opsForHash().putIfAbsent(key, hashKey, objectMapper.writeValueAsString(seatLock));

            if (!saved) {
                ResponseEntity.badRequest().body("Reservation already present");
            }

            // TODO Check return code >
            customLettuceOperations.hexpire(key, hashKey, HexpireOption.NX, 900);

            return ResponseEntity.ok().build();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Get all locked seats for a specific stadium
     */
    @GetMapping("/locked/{stadiumName}")
    public ResponseEntity<List<SeatLock>> getLockedSeats(@PathVariable String stadiumName) {
        String key = "stadium:" + stadiumName.toLowerCase() + ":seats:lock";

        List<SeatLock> list = redisTemplate.opsForHash()
                .values(key)
                .stream().map(seat -> {
                    try {
                        return objectMapper.readValue((String) seat, SeatLock.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        return ResponseEntity.ok(list);
    }

    /*
     * Get all locked seats for a specific stadium by a specific user
     */
    @GetMapping("/locked/{stadiumName}/user/{userName}")
    public ResponseEntity<?> getLockedSeatsByUser(@PathVariable String stadiumName, @PathVariable String userName) {
        String key = "stadium:" + stadiumName.toLowerCase() + ":seats:lock";

        List<SeatLock> list = redisTemplate.opsForHash()
                .values(key)
                .stream().map(seat -> {
                    try {
                        return objectMapper.readValue((String) seat, SeatLock.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(seat -> seat.getUsername().equals(userName))
                .toList();

        return ResponseEntity.ok(list);
    }
}
