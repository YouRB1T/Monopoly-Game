package com.monopoly.domain.engine.card;

import java.util.Map;

public interface IRent {
    Map<Integer, Integer> getRentLevels();
    Integer getCurrentRent();
}
