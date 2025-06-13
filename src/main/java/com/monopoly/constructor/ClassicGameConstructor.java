package com.monopoly.constructor;

import com.monopoly.domain.engine.Board;
import com.monopoly.domain.engine.GameSession;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.UUID;

public class ClassicGameConstructor implements GameSessionConstructor{
    @Override
    public GameSession create() {
        Board board = new Board(
                new UUID(13242342, 342324343),
                new ArrayList<>(),
                new ArrayList<>(),
                40,
                new ArrayList<>()
        );


        return new GameSession(
                new UUID(234324, 23434),
                new ArrayList<>(),
                new ArrayList<>(),
                new TreeMap<>(),
                new TreeMap<>(),
                new TreeMap<>(),
                null);
    }
}
