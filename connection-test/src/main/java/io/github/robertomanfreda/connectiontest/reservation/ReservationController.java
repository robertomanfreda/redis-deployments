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

    /*
     * Here, the idea is that for each seat lock, we insert the seat into a hash using putIfAbsent.
     * This approach ensures an automatic locking mechanism, even when attempting to perform the same operation
     * concurrently. Redis guarantees that the operation will be atomic.
     *
     * We then implement a custom solution (since Spring Data Redis doesn't support this functionality yet) using
     * HEXPIRE to set a TTL on the key within the hash.
     */
    @PostMapping("/lock")
    public ResponseEntity<?> lock(@RequestBody SeatLock seatLock) {
        String key = "stadium:" + seatLock.getStadiumName().toLowerCase() + ":seats:lock";
        String hashKey = seatLock.getSeatType() + ":" + seatLock.getSeatNumber();

        try {
            // TODO HERE YOU SHOULD OPTIMIZE ADDING ONLY field+value, probably you don't need the entire obj
            // REDIS COMMAND -> HSET key hashKey fields
            Boolean saved = redisTemplate.opsForHash().putIfAbsent(key, hashKey,
                    objectMapper.writeValueAsString(seatLock));

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
     * Here, we ensure that HEXPIRE is set until the end of the ticket sale, to prevent further locking of the
     * seat. This might be unnecessary if, before locking a seat in the /lock endpoint, we check the relational database
     * to see if the seat has already been sold. Alternatively, we can decide to create a separate key to finalize the
     * sale.
     * Doing this, we offload data from the cache, but we would be forced to perform a union of multiple datasets
     * when requesting the list of all occupied seats from the /locked/{stadiumName} endpoint.
     */
    @PostMapping("/confirm/{stadiumName}/{seatType}/{seatNumber}")
    public ResponseEntity<?> confirm(@PathVariable String stadiumName, @PathVariable String seatType,
                                     @PathVariable Integer seatNumber) {

        String key = "stadium:" + stadiumName.toLowerCase() + ":seats:lock";
        String hashKey = seatType + ":" + seatNumber;

        // TODO Check return code >
        customLettuceOperations.hexpire(key, hashKey, HexpireOption.GT, 1000000);

        return ResponseEntity.ok().build();
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
