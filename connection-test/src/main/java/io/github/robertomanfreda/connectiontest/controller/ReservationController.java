package io.github.robertomanfreda.connectiontest.controller;

//@RequestMapping("/reserve")
//@RequiredArgsConstructor
//@RestController
public class ReservationController {

//    private final RedisTemplate<String, String> redisTemplate;
//    private final LettuceConnectionFactory lettuceConnectionFactory;
//
//    @AllArgsConstructor
//    @Data
//    @NoArgsConstructor
//    public static class Seat {
//        private String stadiumName;
//        private String seatType;
//        private Integer seatNumber;
//    }
//
//    @Data
//    @EqualsAndHashCode(callSuper = true)
//    public static class SeatLockRequest extends Seat {
//        private String username;
//    }
//
//    // Returns the Set containing all the seats in a stadium
//    @GetMapping("/seats/all/{stadiumName}")
//    public ResponseEntity<Set<Seat>> getAllSeats(@PathVariable String stadiumName) {
//        // REDIS COMMAND -> SMEMBERS key
//        Set<String> range = redisTemplate.opsForSet().members(stadiumName);
//
//        if (range != null) {
//            Set<Seat> collect = range.stream().map(s -> objectMapper.convertValue(s, Seat.class)).collect(Collectors.toSet());
//            return ResponseEntity.ok(collect);
//        }
//
//        return ResponseEntity.notFound().build();
//    }
//
//    @PostMapping("/seat/lock")
//    public ResponseEntity<?> lock(@RequestBody SeatLockRequest seatLockRequest) {
//        String key = "stadium:" + seatLockRequest.getStadiumName().toLowerCase() + ":seats:lock";
//        String hashKey = seatLockRequest.getSeatType().concat(":").concat(String.valueOf(seatLockRequest.getSeatNumber()));
//
//        try {
//            // REDIS COMMAND -> HSET key hashKey fields
//            Boolean saved = redisTemplate.opsForHash().putIfAbsent(key, hashKey, objectMapper.writeValueAsString(seatLockRequest));
//
//            hexpire(key, hashKey, HexpireOption.NX, 900);
//
//            return saved
//                    ? ResponseEntity.ok().build()
//                    : ResponseEntity.badRequest().body("Key already present");
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /*
//     * Manually releases the seat by removing the hashKey.
//     */
//    @PostMapping("/unlock")
//    public ResponseEntity<?> unlock() {
//        return null;
//    }
//
//    /*
//     * Makes the reservation permanent by extending the TTL until the end of ticket sales or by moving into another
//     * collection.
//     */
//    @PostMapping("/confirm")
//    public ResponseEntity<?> confirm() {
//        return null;
//    }
//
//
//    /*
//     * Get all locked seats for a specific stadium
//     */
//    @GetMapping("/locked-seats/{stadiumName}")
//    public ResponseEntity<?> getLockedSeats(@PathVariable String stadiumName) {
//        String key = "stadium:" + stadiumName.toLowerCase().concat(":seats:lock");
//        Map<Object, Object> lockedSeats = redisTemplate.opsForHash().entries(key);
//        if (lockedSeats.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No locked seats found.");
//        }
//        return ResponseEntity.ok(lockedSeats);
//    }
//
//    /*
//     * Get the count of locked seats for a specific stadium
//     */
//    @GetMapping("/locked-seats/count/{stadiumName}")
//    public ResponseEntity<?> getLockedSeatsCount(@PathVariable String stadiumName) {
//        String key = stadiumName.toLowerCase().concat(":seats");
//        Long lockedSeatsCount = redisTemplate.opsForHash().size(key);
//        return ResponseEntity.ok().body("Number of locked seats: " + lockedSeatsCount);
//    }
//
//    /*
//     * Get all confirmed reservations for a specific stadium
//     */
//    @GetMapping("/confirmed-seats/{stadiumName}")
//    public ResponseEntity<?> getConfirmedSeats(@PathVariable String stadiumName) {
//        return null;
//    }
//
//    /*
//     * Check the status of a specific seat (locked, confirmed, or available)
//     */
//    @GetMapping("/seat-status/{stadiumName}/{seatType}/{seatNumber}")
//    public ResponseEntity<?> getSeatStatus(
//            @PathVariable String stadiumName,
//            @PathVariable String seatType,
//            @PathVariable Integer seatNumber) {
//
//        String key = stadiumName.toLowerCase().concat(":seat");
//        String hashKey = seatType.concat(":").concat(String.valueOf(seatNumber));
//
//        Object o = redisTemplate.opsForHash().get(key, hashKey);
//
//        if (o == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seat not locked.");
//        }
//
//        return ResponseEntity.ok(o);
//    }
//
//    /*
//     * Temporary custom implementation for HEXPIRE Operation, RedisTemplate is working on the support
//     */
//    private List<Long> hexpire(String key, String hashKey, HexpireOption hexpireOption, int seconds) {
//        LettuceConnection lettuceConnection = (LettuceConnection) lettuceConnectionFactory.getConnection();
//
//        /* Array reply. For each field:
//            Integer reply:  -2 if no such field exists in the provided hash key, or the provided key does not exist.
//            Integer reply:  0 if the specified NX | XX | GT | LT condition has not been met.
//            Integer reply:  1 if the expiration time was set/updated.
//            Integer reply:  2 when HEXPIRE/HPEXPIRE is called with 0 seconds/milliseconds or when HEXPIREAT/HPEXPIREAT
//                            is called with a past Unix time in seconds/milliseconds.
//         */
//
//        //noinspection unchecked
//        return (List<Long>) lettuceConnection.execute(
//                "HEXPIRE",
//                new ArrayOutput<>(StringCodec.UTF8),
//                key.getBytes(), String.valueOf(seconds).getBytes(), hexpireOption.toString().getBytes(),
//                "FIELDS".getBytes(), "1".getBytes(), hashKey.getBytes()
//        );
//    }
//
//    private enum HexpireOption {
//        NX, XX, GT, LT
//    }
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @PostConstruct
//    private void fillSanSiro() {
//        List<Seat> seats = List.of(
//                new Seat("sansiro", "VIP", 0),
//                new Seat("sansiro", "VIP", 1),
//                new Seat("sansiro", "VIP", 2),
//                new Seat("sansiro", "VIP", 3),
//                new Seat("sansiro", "VIP", 4),
//                new Seat("sansiro", "VIP", 5),
//                new Seat("sansiro", "VIP", 6),
//                new Seat("sansiro", "VIP", 7),
//                new Seat("sansiro", "VIP", 8),
//                new Seat("sansiro", "VIP", 9),
//                new Seat("sansiro", "VIP", 10),
//                new Seat("sansiro", "VIP", 11),
//                new Seat("sansiro", "VIP", 12),
//                new Seat("sansiro", "VIP", 13),
//                new Seat("sansiro", "VIP", 14),
//                new Seat("sansiro", "VIP", 15),
//                new Seat("sansiro", "VIP", 16),
//                new Seat("sansiro", "NORMAL", 17),
//                new Seat("sansiro", "NORMAL", 18),
//                new Seat("sansiro", "NORMAL", 19),
//                new Seat("sansiro", "NORMAL", 20)
//        );
//
//        seats.forEach(seat -> {
//            try {
//                // REDIS COMMAND -> SADD key value
//                redisTemplate.opsForSet().add("stadium:sansiro:seats:all", objectMapper.writeValueAsString(seat));
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
}
