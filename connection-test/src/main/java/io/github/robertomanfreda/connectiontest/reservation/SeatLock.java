package io.github.robertomanfreda.connectiontest.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SeatLock {
    private String stadiumName;
    private String seatType;
    private Integer seatNumber;
    private String username;
}