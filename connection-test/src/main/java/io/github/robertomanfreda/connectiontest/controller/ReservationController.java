package io.github.robertomanfreda.connectiontest.controller;

import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.ArrayOutput;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/reserve")
@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final RedisTemplate<String, String> redisTemplate;
    private final LettuceConnectionFactory lettuceConnectionFactory;

    @Data
    public static class LockRequest {
        private String username;
        private String stadiumName;
        private String seatType;
        private Integer seatNumber;
    }

    @PostMapping("/lock")
    public ResponseEntity<?> lock(@RequestBody LockRequest lockRequest) {
        String key = lockRequest.getStadiumName().toLowerCase().concat(":").concat("seats");
        String hashKey = lockRequest.getSeatType().concat(":").concat(String.valueOf(lockRequest.getSeatNumber()));

        Boolean saved = redisTemplate.opsForHash().putIfAbsent(key, hashKey, lockRequest);
        hexpire(key, hashKey, HexpireOption.NX, 30);

        return saved
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().body("Key already present");
    }

    /*
     * Manually releases the seat.
     */
    public ResponseEntity<?> unlock() {
        return null;
    }

    /*
     * Makes the reservation permanent by extending the TTL until the end of ticket sales.
     */
    public ResponseEntity<?> confirm() {
        return null;
    }


    /*
     * Get all locked seats for a specific stadium
     */
    @GetMapping("/locked-seats/{stadiumName}")
    public ResponseEntity<?> getLockedSeats(@PathVariable String stadiumName) {
        return null;
    }

    /*
     * Get all confirmed reservations for a specific stadium
     */
    @GetMapping("/confirmed-seats/{stadiumName}")
    public ResponseEntity<?> getConfirmedSeats(@PathVariable String stadiumName) {
        return null;
    }

    /*
     * Check the status of a specific seat (locked, confirmed, or available)
     */
    @GetMapping("/seat-status/{stadiumName}/{seatType}/{seatNumber}")
    public ResponseEntity<?> getSeatStatus(
            @PathVariable String stadiumName,
            @PathVariable String seatType,
            @PathVariable Integer seatNumber) {
        return null;
    }

    /*
     * Temporary custom implementation for HEXPIRE Operation, RedisTemplate is working on the support
     */
    private List<Long> hexpire(String key, String hashKey, HexpireOption hexpireOption, int seconds) {
        LettuceConnection lettuceConnection = (LettuceConnection) lettuceConnectionFactory.getConnection();

        /* Array reply. For each field:
            Integer reply: -2 if no such field exists in the provided hash key, or the provided key does not exist.
            Integer reply: 0 if the specified NX | XX | GT | LT condition has not been met.
            Integer reply: 1 if the expiration time was set/updated.
            Integer reply: 2 when HEXPIRE/HPEXPIRE is called with 0 seconds/milliseconds or when HEXPIREAT/HPEXPIREAT is called with a past Unix time in seconds/milliseconds.
         */

        //noinspection unchecked
        return (List<Long>) lettuceConnection.execute(
                "HEXPIRE",
                new ArrayOutput<>(StringCodec.UTF8),
                key.getBytes(), String.valueOf(seconds).getBytes(), hexpireOption.toString().getBytes(),
                "FIELDS".getBytes(), "1".getBytes(), hashKey.getBytes()
        );
    }

    private enum HexpireOption {
        NX, XX, GT, LT
    }
}
