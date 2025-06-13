package com.monopoly.domain.engine.card;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class PropertyGroup {
    private final String name;
    private final Set<PropertyCard> propertyCards;
}
