package com.monopoly.domain.engine.card;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PropertyGroup {
    private final String name;
    private final Set<PropertyCard> propertyCards;
}
