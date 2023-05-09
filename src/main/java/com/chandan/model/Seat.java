package com.chandan.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Seat {
    private final String id;
    private final int rowNo;
    private final int seatNo;
}
