# Monopoly Game Server

Серверная часть онлайн игры Монополия, построенная на Spring Boot с поддержкой WebSocket для реального времени.

## Технологический стек

### Backend
- **Java**: 17+
- **Spring Boot**: 3.5.0
- **Spring WebSocket**: для real-time коммуникации
- **Spring Data JPA**: 3.4.2 для работы с базой данных
- **Spring Security Core**: 6.5.0

### База данных
- **PostgreSQL**: 42.7.2 (основная БД)
- **Redis**: для кэширования и сессий лобби

### Дополнительные библиотеки
- **Hibernate Core**: 6.2.0.Final
- **Lombok**: 1.18.32 для упрощения кода
- **Jackson**: для JSON сериализации
- **SLF4J**: 2.0.16 для логирования

### Тестирование
- **JUnit**: 5.10.0 / 4.13.1
- **Spring Boot Test**

## Функционал

### Управление лобби
- Создание игровых лобби с настраиваемыми правилами
- Присоединение к лобби с проверкой пароля
- Исключение игроков из лобби (только создатель)
- Автоматическая передача прав создателя при выходе
- Real-time обновления состояния лобби через WebSocket

### Игровая механика
- Создание игровых сессий из лобби
- Обработка ходов игроков (бросок кубиков, движение)
- Система карточек и недвижимости
- Расчет арендной платы
- Завершение игры с определением победителя

### WebSocket коммуникация
- Отдельные каналы для лобби и игровых сессий
- Broadcast сообщения для всех участников
- Обработка подключений и отключений

## API Endpoints

### Лобби

#### Создание лобби
```http
POST /lobby/create
Content-Type: application/json

{
  "creator": {
    "id": "uuid",
    "name": "string",
    "avatar": "string",
    "balance": 1500
  },
  "lobbyName": "string",
  "maxPlayers": 4,
  "password": "string",
  "gameRules": {
    "startingMoney": 1500,
    "salaryAmount": 200,
    "freeParkingBonus": true
  }
}
```

#### Получение всех лобби
```http
GET /lobby
```

#### Присоединение к лобби
```http
POST /lobby/{lobbyId}/join
Content-Type: application/json

{
  "playerName": "string",
  "password": "string"
}
```

#### Подключение к лобби
```http
GET /lobby/{lobbyId}/connect
```



## Архитектура

### Основные компоненты
- **Controllers**: REST API endpoints
- **Services**: Бизнес-логика
- **Repositories**: Доступ к данным (Redis, PostgreSQL)
- **WebSocket Handlers**: Real-time коммуникация
- **Engine Handlers**: Обработка игровых событий

### Паттерны
- **Handler Pattern**: для обработки различных типов игровых событий
- **Repository Pattern**: для абстракции доступа к данным
- **DTO Pattern**: для передачи данных между слоями

