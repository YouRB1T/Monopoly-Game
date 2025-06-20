package com.monopoly.domain.engine.card;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class RentOfCard {
    private final Map<Integer, Integer> rentLevels;
    private Integer currentRentLevel;
    private Integer currentRent;

    private void upgrade() {
        currentRentLevel++;
        currentRent = rentLevels.get(currentRentLevel);
    }

    public Integer getCurrentRent() {
        if (currentRentLevel == null) return 0;
        return rentLevels.getOrDefault(currentRentLevel, 0);
    }

}
