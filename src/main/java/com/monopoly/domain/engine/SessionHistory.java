package com.monopoly.domain.engine;

import lombok.*;

import java.util.List;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class SessionHistory {
    private final List<String> historyHandlers;
}
