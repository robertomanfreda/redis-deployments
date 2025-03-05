package io.github.robertomanfreda.connectiontest.layout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LayoutSeat {
    private String stadiumName;
    private String seatType;
    private Integer seatNumber;
}