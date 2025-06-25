package com.monopoly.constructor;

import com.monopoly.domain.engine.Board;
import com.monopoly.domain.engine.card.BoardCard;
import com.monopoly.domain.engine.card.ChanceCard;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.domain.engine.card.TreasuryCard;
import com.monopoly.domain.engine.card.RentOfCard;
import com.monopoly.domain.engine.enums.ChanceCardType;
import com.monopoly.domain.engine.enums.TreasuryCardType;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BoardConstructor {
    

    public Board createClassicBoard() {
        List<ChanceCard> chanceCards = createChanceCards();

        List<TreasuryCard> treasuryCards = createTreasuryCards();

        List<BoardCard> cardsOnBoard = createClassicBoardCards();

        return new Board(
                UUID.randomUUID(),
                chanceCards,
                treasuryCards,
                40,
                cardsOnBoard
        );
    }

    public Board createCustomBoard() {
        List<ChanceCard> chanceCards = createCustomChanceCards();

        List<TreasuryCard> treasuryCards = createCustomTreasuryCards();

        List<BoardCard> cardsOnBoard = createCustomBoardCards();

        return new Board(
                UUID.randomUUID(),
                chanceCards,
                treasuryCards,
                32,
                cardsOnBoard
        );
    }

    private List<ChanceCard> createChanceCards() {
        List<ChanceCard> chanceCards = new ArrayList<>();

        chanceCards.add(new ChanceCard(UUID.randomUUID(), "Банковская ошибка в вашу пользу", "Получите 200$", ChanceCardType.GO_TO_PRISON));
        chanceCards.add(new ChanceCard(UUID.randomUUID(), "Штраф за превышение скорости", "Заплатите 15$", ChanceCardType.FREE_PRISON_CARD));
        chanceCards.add(new ChanceCard(UUID.randomUUID(), "Выигрыш в лотерею", "Получите 100$", ChanceCardType.FREE_PRISON_CARD));
        
        return chanceCards;
    }

    private List<TreasuryCard> createTreasuryCards() {
        List<TreasuryCard> treasuryCards = new ArrayList<>();

        treasuryCards.add(new TreasuryCard(UUID.randomUUID(), "Возврат налога", "Получите 20$", TreasuryCardType.MONEY_REWARD, 20));
        treasuryCards.add(new TreasuryCard(UUID.randomUUID(), "Оплата страховки", "Заплатите 50$", TreasuryCardType.MONEY_PENALTY, 50));
        treasuryCards.add(new TreasuryCard(UUID.randomUUID(), "Наследство", "Получите 100$", TreasuryCardType.MONEY_REWARD, 100));

        return treasuryCards;
    }

    private List<BoardCard> createClassicBoardCards() {
        List<BoardCard> cardsOnBoard = new ArrayList<>();

        cardsOnBoard.add(new BoardCard(UUID.randomUUID(), "Старт", "Получите 200$ при прохождении", new ArrayList<>(), 0));

        cardsOnBoard.add(createPropertyCard("Старая дорога", 60, 2, "brown", 1));
        cardsOnBoard.add(new BoardCard(UUID.randomUUID(), "Казна", "Возьмите карту из казны", new ArrayList<>(), 2));
        cardsOnBoard.add(createPropertyCard("Главная улица", 60, 4, "brown", 3));
        cardsOnBoard.add(new BoardCard(UUID.randomUUID(), "Подоходный налог", "Заплатите 200$", new ArrayList<>(), 4));
        
        return cardsOnBoard;
    }

    private List<BoardCard> createCustomBoardCards() {
        List<BoardCard> cardsOnBoard = new ArrayList<>();
        return cardsOnBoard;
    }

    private List<ChanceCard> createCustomChanceCards() {
        return new ArrayList<>();
    }

    private List<TreasuryCard> createCustomTreasuryCards() {
        return new ArrayList<>();
    }

    private PropertyCard createPropertyCard(String title, int price, int baseRent, String group, int position) {
        Map<Integer, Integer> rentLevels = new HashMap<>();
        rentLevels.put(0, baseRent);
        rentLevels.put(1, baseRent * 5);
        rentLevels.put(2, baseRent * 15);
        rentLevels.put(3, baseRent * 45);
        
        RentOfCard rentOfCard = new RentOfCard(rentLevels);
        
        return new PropertyCard(
                UUID.randomUUID(),
                title,
                "Собственность группы " + group,
                new ArrayList<>(),
                position,
                price,
                rentOfCard,
                group
        );
    }
}