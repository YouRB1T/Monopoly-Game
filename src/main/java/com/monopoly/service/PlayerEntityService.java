package com.monopoly.service;

import com.monopoly.domain.engine.Player;
import com.monopoly.domain.entity.PlayerEntity;
import com.monopoly.repository.PlayerEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerEntityService {
    
    private final PlayerEntityRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public PlayerEntityService(PlayerEntityRepository playerRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Регистрирует нового игрока в системе
     * @param nickname никнейм игрока
     * @param password пароль игрока
     * @return сущность игрока
     */
    public PlayerEntity registerPlayer(String nickname, String password) {
        if (playerRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("Игрок с никнеймом " + nickname + " уже существует");
        }
        
        PlayerEntity playerEntity = new PlayerEntity(nickname, passwordEncoder.encode(password));
        return playerRepository.save(playerEntity);
    }
    
    /**
     * Аутентифицирует игрока
     * @param nickname никнейм игрока
     * @param password пароль игрока
     * @return сущность игрока, если аутентификация успешна
     */
    public Optional<PlayerEntity> authenticatePlayer(String nickname, String password) {
        Optional<PlayerEntity> playerOpt = playerRepository.findByNickname(nickname);
        
        if (playerOpt.isPresent() && passwordEncoder.matches(password, playerOpt.get().getPassword())) {
            return playerOpt;
        }
        
        return Optional.empty();
    }
    
    /**
     * Преобразует сущность игрока в игрока для игровой сессии
     * @param playerEntity сущность игрока
     * @param initialMoney начальное количество денег
     * @return игрок для игровой сессии
     */
    public Player convertToPlayer(PlayerEntity playerEntity, int initialMoney) {
        Player player = new Player(playerEntity.getId(), initialMoney, new HashSet<>());
        player.setName(playerEntity.getNickname());
        return player;
    }
    
    /**
     * Обновляет статистику игрока после игры
     * @param playerId ID игрока
     * @param isWinner флаг, указывающий выиграл ли игрок
     */
    public void updatePlayerStats(UUID playerId, boolean isWinner) {
        playerRepository.findById(playerId).ifPresent(player -> {
            player.setGamesPlayed(player.getGamesPlayed() + 1);
            if (isWinner) {
                player.setGamesWon(player.getGamesWon() + 1);
            }
            playerRepository.save(player);
        });
    }
    
    /**
     * Получает сущность игрока по ID
     * @param id ID игрока
     * @return сущность игрока
     */
    public Optional<PlayerEntity> getPlayerById(UUID id) {
        return playerRepository.findById(id);
    }
    
    /**
     * Получает сущность игрока по никнейму
     * @param nickname никнейм игрока
     * @return сущность игрока
     */
    public Optional<PlayerEntity> getPlayerByNickname(String nickname) {
        return playerRepository.findByNickname(nickname);
    }
}