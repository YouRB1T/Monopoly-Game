package com.monopoly.domain.engine.card;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class RentOfCard {
    private final Map<Integer, Integer> rentLevels;
    private Integer currentRentLevel;
    private Integer currentRent;

    private void upgrade() {
        currentRentLevel++;
        currentRent = rentLevels.get(currentRentLevel);
    }
}
