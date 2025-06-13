package com.monopoly.repository;

import com.monopoly.domain.engine.GameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static sun.net.www.protocol.http.AuthenticatorKeys.getKey;

@Repository
public class GameSessionRedisRepository implements ActiveGameSessionRepository{

    private final RedisTemplate<UUID, GameSession> redisTemplate;
    private final ValueOperations<UUID, GameSession> ops;
    private static final String KEY_PREFIX = "GameSession:";

    public GameSessionRedisRepository(RedisTemplate<UUID, GameSession> redisTemplate) {
        this.redisTemplate = redisTemplate;
        ops = redisTemplate.opsForValue();
    }

    private UUID buildKey(UUID id) {
        return UUID.fromString(KEY_PREFIX + id);
    }

    @Override
    public GameSession create(GameSession gameSession) {
        ops.set(getKey(gameSession.getId()), gameSession);
        return gameSession;
    }

    @Override
    public Optional<GameSession> findById(UUID id) {
        return ops.get(id);
    }

    @Override
    public boolean deleteById(UUID id) {
        return false;
    }

    @Override
    public boolean existsByID(UUID id) {
        return false;
    }
}
