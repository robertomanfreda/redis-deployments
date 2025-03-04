package io.github.robertomanfreda.connectiontest.controller;

import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.ArrayOutput;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * This endpoint "locks" a specific seat in a stadium for a user.
     * <p>
     * The "lock" is implicitly done using Redis' `putIfAbsent` command, which ensures that the seat is not already occupied by another user.
     * If the seat is not already locked, it is added to the Redis hash with the user's information.
     * <p>
     * The lock is temporary, and the seat will automatically expire after the specified time (600 seconds = 15 minutes)
     * if not confirmed. The expiration is handled via the `HEXPIRE` command, which ensures that the seat is released
     * automatically if the user does not confirm or cancel the reservation within the given time.
     * <p>
     * If the seat is already locked by another user, the operation fails, and an error response is returned.
     *
     * @param lockRequest Contains the details of the user and the seat to be locked.
     * @return ResponseEntity with status 200 OK if the seat is successfully locked,
     * or status 400 Bad Request if the seat is already locked by another user.
     */
    @PostMapping("/lock")
    public ResponseEntity<?> lock(@RequestBody LockRequest lockRequest) {
        String key = lockRequest.getStadiumName().toLowerCase().concat(":").concat("seats");
        String hashKey = lockRequest.getSeatType().concat(":").concat(String.valueOf(lockRequest.getSeatNumber()));

        Boolean saved = redisTemplate.opsForHash().putIfAbsent(key, hashKey, lockRequest);
        hexpire(key, hashKey, HexpireOption.NX, 900);

        return saved
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().body("Key already present");
    }

    /*
     * Manually releases the seat by removing the hashKey.
     */
    @PostMapping("/unlock")
    public ResponseEntity<?> unlock() {
        return null;
    }

    /*
     * Makes the reservation permanent by extending the TTL until the end of ticket sales.
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirm() {
        return null;
    }


    /*
     * Get all locked seats for a specific stadium
     */
    @GetMapping("/locked-seats/{stadiumName}")
    public ResponseEntity<?> getLockedSeats(@PathVariable String stadiumName) {
        String key = stadiumName.toLowerCase().concat(":seats");
        Map<Object, Object> lockedSeats = redisTemplate.opsForHash().entries(key);
        if (lockedSeats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No locked seats found.");
        }
        return ResponseEntity.ok(lockedSeats);
    }

    /*
     * Get the count of locked seats for a specific stadium
     */
    @GetMapping("/locked-seats/count/{stadiumName}")
    public ResponseEntity<?> getLockedSeatsCount(@PathVariable String stadiumName) {
        String key = stadiumName.toLowerCase().concat(":seats");
        Long lockedSeatsCount = redisTemplate.opsForHash().size(key);
        return ResponseEntity.ok().body("Number of locked seats: " + lockedSeatsCount);
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

    private enum HexpireOption {
        NX, XX, GT, LT
    }
}
