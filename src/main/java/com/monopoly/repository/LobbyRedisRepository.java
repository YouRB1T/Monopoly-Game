package com.monopoly.repository;

import com.monopoly.domain.engine.Lobby;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LobbyRedisRepository implements LobbyRepository {
    
    private final RedisTemplate<String, Lobby> redisTemplate;
    private static final String KEY_PREFIX = "Lobby:";
    
    public LobbyRedisRepository(RedisTemplate<String, Lobby> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    private String buildKey(UUID id) {
        return KEY_PREFIX + id.toString();
    }
    
    @Override
    public Lobby create(Lobby lobby) {
        String key = buildKey(lobby.getId());
        redisTemplate.opsForValue().set(key, lobby);
        return lobby;
    }
    
    @Override
    public Optional<Lobby> findById(UUID id) {
        String key = buildKey(id);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }
    
    @Override
    public Lobby update(Lobby lobby) {
        String key = buildKey(lobby.getId());
        redisTemplate.opsForValue().set(key, lobby);
        return lobby;
    }
    
    @Override
    public Lobby deleteById(UUID id) {
        String key = buildKey(id);
        Lobby lobby = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return lobby;
    }
    
    @Override
    public boolean existsById(UUID id) {
        String key = buildKey(id);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public List<Lobby> findAll() {
        Set<String> keys = redisTemplate.keys(KEY_PREFIX + "*");
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }

        return keys.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}