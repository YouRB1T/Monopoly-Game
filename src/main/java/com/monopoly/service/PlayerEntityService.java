package com.monopoly.service;

import com.monopoly.domain.engine.Player;
import com.monopoly.domain.entity.PlayerEntity;
import com.monopoly.repository.PlayerEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PlayerEntityService {
    
    private final PlayerEntityRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public PlayerEntityService(PlayerEntityRepository playerRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PlayerEntity registerPlayer(String nickname, String password) {
        if (playerRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("Player with nickname " + nickname + " already exists");
        }
        
        PlayerEntity playerEntity = new PlayerEntity(nickname, passwordEncoder.encode(password));
        log.info("Registered player: {}", playerEntity);
        return playerRepository.save(playerEntity);
    }

    public Optional<PlayerEntity> authenticatePlayer(String nickname, String password) {
        Optional<PlayerEntity> playerOpt = playerRepository.findByNickname(nickname);
        
        if (playerOpt.isPresent() && passwordEncoder.matches(password, playerOpt.get().getPassword())) {
            log.info("Authenticated player: {}", playerOpt.get());
            return playerOpt;
        }
        
        return Optional.empty();
    }

    public Player convertToPlayer(PlayerEntity playerEntity, int initialMoney) {
        Player player = new Player(playerEntity.getId(), initialMoney, new HashSet<>());
        player.setName(playerEntity.getNickname());
        return player;
    }

    public void updatePlayerStats(UUID playerId, boolean isWinner) {
        log.info("Updating player stats for player: {}", playerId);
        playerRepository.findById(playerId).ifPresent(player -> {
            player.setGamesPlayed(player.getGamesPlayed() + 1);
            if (isWinner) {
                player.setGamesWon(player.getGamesWon() + 1);
            }
            playerRepository.save(player);
        });
    }

    public Optional<PlayerEntity> getPlayerById(UUID id) {
        log.info("Getting player by id: {}", id);
        return playerRepository.findById(id);
    }

    public Optional<PlayerEntity> getPlayerByNickname(String nickname) {
        log.info("Getting player by nickname: {}", nickname);
        return playerRepository.findByNickname(nickname);
    }
}