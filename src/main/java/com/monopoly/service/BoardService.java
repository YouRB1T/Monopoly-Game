package com.monopoly.service;

import com.monopoly.constructor.BoardConstructor;
import com.monopoly.domain.engine.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    
    private final BoardConstructor boardConstructor;
    
    @Autowired
    public BoardService(BoardConstructor boardConstructor) {
        this.boardConstructor = boardConstructor;
    }

    public List<Board> createBoards(boolean isClassic) {
        List<Board> boards = new ArrayList<>();
        
        if (isClassic) {
            boards.add(boardConstructor.createClassicBoard());
        } else {
            boards.add(boardConstructor.createCustomBoard());
        }
        
        return boards;
    }
}
