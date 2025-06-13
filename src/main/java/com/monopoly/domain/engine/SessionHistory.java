package com.monopoly.domain.engine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SessionHistory {
    private final List<String> historyHandlers;
}
