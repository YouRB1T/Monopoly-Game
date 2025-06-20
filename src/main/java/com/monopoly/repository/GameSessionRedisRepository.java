package com.monopoly.repository;

import com.monopoly.domain.engine.GameSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public class GameSessionRedisRepository implements ActiveGameSessionRepository{

    private final RedisTemplate<String, GameSession> redisTemplate;
    private static final String KEY_PREFIX = "GameSession:";

    public GameSessionRedisRepository(RedisTemplate<String, GameSession> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String buildKey(UUID id) {
        return KEY_PREFIX + id.toString();
    }

    @Override
    public GameSession create(GameSession gameSession) {
        String key = buildKey(gameSession.getId());
        redisTemplate.opsForValue().set(key, gameSession);
        return gameSession;
    }

    @Override
    public Optional<GameSession> findById(UUID id) {
        String key = buildKey(id);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    @Override
    public GameSession deleteById(UUID id) {
        String key = buildKey(id);
        GameSession gameSession = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return gameSession;
    }

    @Override
    public boolean existsByID(UUID id) {
        String key = buildKey(id);
        return Boolean.TRUE.equals(
                redisTemplate.hasKey(key)
        );
    }
}
