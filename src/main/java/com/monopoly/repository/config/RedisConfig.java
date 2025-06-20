package com.monopoly.repository.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Lobby;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.UUID;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(); //localhost:6379
    }

    @Bean
    public RedisTemplate<String, GameSession> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, GameSession> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        ObjectMapper objectMapper = JsonMapper.builder()
                .activateDefaultTyping(
                        LaissezFaireSubTypeValidator.instance,
                        ObjectMapper.DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY)
                .build();

        Jackson2JsonRedisSerializer<GameSession> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, GameSession.class);

        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, Lobby> lobbyRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Lobby> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        ObjectMapper objectMapper = JsonMapper.builder()
                .activateDefaultTyping(
                        LaissezFaireSubTypeValidator.instance,
                        ObjectMapper.DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY)
                .build();
        
        Jackson2JsonRedisSerializer<Lobby> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, Lobby.class);
        
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        
        return template;
    }

}

