package com.monopoly.engine.handler;

public interface GameHandler<R> {
    boolean canHandle(R request);

}
