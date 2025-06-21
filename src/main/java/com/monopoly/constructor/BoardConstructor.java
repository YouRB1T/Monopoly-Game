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
    
    /**
     * Создает классическую доску Монополии
     * @return Игровая доска с классическим набором карточек
     */
    public Board createClassicBoard() {
        // Создаем карты шанса
        List<ChanceCard> chanceCards = createChanceCards();
        
        // Создаем карты казны
        List<TreasuryCard> treasuryCards = createTreasuryCards();
        
        // Создаем карты на доске
        List<BoardCard> cardsOnBoard = createClassicBoardCards();
        
        // Создаем доску с размером 40 клеток (классическая Монополия)
        return new Board(
                UUID.randomUUID(),
                chanceCards,
                treasuryCards,
                40,
                cardsOnBoard
        );
    }
    
    /**
     * Создает кастомную доску Монополии
     * @return Игровая доска с кастомным набором карточек
     */
    public Board createCustomBoard() {
        // Создаем карты шанса
        List<ChanceCard> chanceCards = createCustomChanceCards();
        
        // Создаем карты казны
        List<TreasuryCard> treasuryCards = createCustomTreasuryCards();
        
        // Создаем карты на доске
        List<BoardCard> cardsOnBoard = createCustomBoardCards();
        
        // Создаем доску с размером 32 клетки (кастомная Монополия)
        return new Board(
                UUID.randomUUID(),
                chanceCards,
                treasuryCards,
                32,
                cardsOnBoard
        );
    }
    
    /**
     * Создает карты шанса для классической доски
     * @return Список карт шанса
     */
    private List<ChanceCard> createChanceCards() {
        List<ChanceCard> chanceCards = new ArrayList<>();
        
        // Добавляем карты шанса
        chanceCards.add(new ChanceCard(UUID.randomUUID(), "Банковская ошибка в вашу пользу", "Получите 200$", ChanceCardType.GO_TO_PRISON));
        chanceCards.add(new ChanceCard(UUID.randomUUID(), "Штраф за превышение скорости", "Заплатите 15$", ChanceCardType.FREE_PRISON_CARD));
        chanceCards.add(new ChanceCard(UUID.randomUUID(), "Выигрыш в лотерею", "Получите 100$", ChanceCardType.FREE_PRISON_CARD));
        // Добавьте остальные карты шанса
        
        return chanceCards;
    }
    
    /**
     * Создает карты казны для классической доски
     * @return Список карт казны
     */
    private List<TreasuryCard> createTreasuryCards() {
        List<TreasuryCard> treasuryCards = new ArrayList<>();
        
        // Добавляем карты казны
        treasuryCards.add(new TreasuryCard(UUID.randomUUID(), "Возврат налога", "Получите 20$", TreasuryCardType.MONEY_REWARD, 20));
        treasuryCards.add(new TreasuryCard(UUID.randomUUID(), "Оплата страховки", "Заплатите 50$", TreasuryCardType.MONEY_PENALTY, 50));
        treasuryCards.add(new TreasuryCard(UUID.randomUUID(), "Наследство", "Получите 100$", TreasuryCardType.MONEY_REWARD, 100));
        // Добавьте остальные карты казны
        
        return treasuryCards;
    }
    
    /**
     * Создает карты для классической доски
     * @return Список карт на доске
     */
    private List<BoardCard> createClassicBoardCards() {
        List<BoardCard> cardsOnBoard = new ArrayList<>();
        
        // Добавляем карты на доску
        // Старт
        cardsOnBoard.add(new BoardCard(UUID.randomUUID(), "Старт", "Получите 200$ при прохождении", new ArrayList<>(), 0));
        
        // Коричневые улицы
        cardsOnBoard.add(createPropertyCard("Старая дорога", 60, 2, "brown", 1));
        cardsOnBoard.add(new BoardCard(UUID.randomUUID(), "Казна", "Возьмите карту из казны", new ArrayList<>(), 2));
        cardsOnBoard.add(createPropertyCard("Главная улица", 60, 4, "brown", 3));
        cardsOnBoard.add(new BoardCard(UUID.randomUUID(), "Подоходный налог", "Заплатите 200$", new ArrayList<>(), 4));
        
        // Добавьте остальные карты доски
        
        return cardsOnBoard;
    }
    
    /**
     * Создает карты для кастомной доски
     * @return Список карт на доске
     */
    private List<BoardCard> createCustomBoardCards() {
        List<BoardCard> cardsOnBoard = new ArrayList<>();
        
        // Добавляем карты на кастомную доску
        // Реализуйте создание кастомных карт
        
        return cardsOnBoard;
    }
    
    /**
     * Создает карты шанса для кастомной доски
     * @return Список карт шанса
     */
    private List<ChanceCard> createCustomChanceCards() {
        // Реализуйте создание кастомных карт шанса
        return new ArrayList<>();
    }
    
    /**
     * Создает карты казны для кастомной доски
     * @return Список карт казны
     */
    private List<TreasuryCard> createCustomTreasuryCards() {
        // Реализуйте создание кастомных карт казны
        return new ArrayList<>();
    }
    
    /**
     * Создает карту собственности
     * @param title название собственности
     * @param price цена собственности
     * @param baseRent базовая рента
     * @param group группа собственности
     * @param position позиция на доске
     * @return Карта собственности
     */
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