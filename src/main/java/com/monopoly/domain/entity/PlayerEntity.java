package com.monopoly.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
public class PlayerEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(unique = true, nullable = false)
    private String nickname;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "games_played")
    private int gamesPlayed = 0;
    
    @Column(name = "games_won")
    private int gamesWon = 0;
    
    public PlayerEntity(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }
}