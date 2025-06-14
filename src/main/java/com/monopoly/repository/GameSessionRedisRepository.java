package com.monopoly.repository;

import com.monopoly.domain.engine.GameSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


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
        ops.set(buildKey(gameSession.getId()), gameSession);
        return gameSession;
    }

    @Override
    public Optional<GameSession> findById(UUID id) {
        return Optional.ofNullable(ops.get(buildKey(id)));
    }

    @Override
    public GameSession deleteById(UUID id) {
        GameSession gameSession = ops.get(buildKey(id));
        redisTemplate.delete(buildKey(id));
        return gameSession;
    }

    @Override
    public boolean existsByID(UUID id) {
        return Boolean.TRUE.equals(
                redisTemplate.hasKey(buildKey(id))
        );
    }
}
