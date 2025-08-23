# Java Blackjack Game

A console-based Blackjack game implemented in Java featuring an intelligent NPC dealer, clean architecture, and proper game flow management.

## Features

- **Interactive Console Gameplay** - Clean, formatted display with user-friendly prompts
- **Intelligent NPC Dealer** - Follows standard casino rules (hits on 16, stands on 17)
- **Proper Blackjack Rules** - Ace handling, blackjack detection, and accurate payouts
- **Betting System** - Chip-based betting with input validation
- **Game State Management** - State machine pattern for smooth game flow
- **Auto-Reshuffling Deck** - Infinite gameplay with automatic deck management
- **Comprehensive Logging** - SLF4J logging for game events and debugging

## How to Play

1. **Start Game** - Enter your name and begin with 100 chips
2. **Place Bet** - Enter your bet amount (must have sufficient chips)
3. **Receive Cards** - You and dealer each get 2 cards (dealer's second card hidden)
4. **Make Decisions** - Choose to Hit (h) or Stand (s) to get as close to 21 as possible
5. **Dealer Plays** - Dealer reveals hidden card and follows house rules
6. **Win/Lose** - Compare hands and collect winnings
7. **Continue** - Play multiple rounds until you run out of chips or quit

### Payouts
- **Standard Win**: 1:1 (win $10 on $10 bet)
- **Blackjack**: 3:2 (win $15 on $10 bet)
- **Tie**: Return original bet

## Getting Started

### Prerequisites
- **Java 21+** (uses `List.removeLast()` method)
- **Lombok** (annotation processing)
- **SLF4J** (logging)

### Running the Game
```bash
# Compile
javac -d bin src/main/java/**/*.java

# Run
java -cp bin game.BlackjackGame
```

### With Build Tool (Maven/Gradle)
```bash
# Maven
mvn compile exec:java -Dexec.mainClass="game.BlackjackGame"

# Gradle
./gradlew run
```

## Project Structure

```
src/main/java/
├── models/              # Core game entities
│   ├── Card.java        # Immutable card (record)
│   ├── Deck.java        # 52-card deck with shuffling
│   ├── Hand.java        # Card collection with value calculation
│   ├── Player.java      # Player with chips and betting
│   ├── Rank.java        # Card ranks (2-A) with values
│   └── Suit.java        # Card suits (♥♦♣♠)
│
├── players/             # Player implementations
│   ├── HumanPlayer.java # User input handling
│   └── DealerNPC.java   # AI dealer logic
│
├── game/                # Game logic and flow
│   ├── GameState.java   # Game state enumeration
│   ├── Winner.java      # Game result enumeration
│   ├── GameResult.java  # Result with payout info
│   ├── GameEngine.java  # Core game rules
│   └── BlackjackGame.java # Main controller
│
└── ui/                  # User interface
    ├── ConsoleUI.java   # Input handling
    └── GameDisplay.java # Formatted output
```

## Architecture Highlights

### **Clean Separation of Concerns**
- **Models**: Pure data classes with business logic
- **Players**: Different player types with specific behaviors  
- **Game**: Core game rules and state management
- **UI**: User interaction and display formatting

### **Design Patterns Used**
- **State Machine**: Game flow managed through `GameState` enum
- **Strategy Pattern**: Different player types (Human vs NPC)
- **Record Classes**: Immutable data structures (`Card`, `GameResult`)
- **Builder Pattern**: Fluent deck construction with streams

### **Key Design Decisions**

#### **Immutable Cards**
```java
public record Card(Rank rank, Suit suit) {
    // Cards can't change after creation
}
```

#### **Flexible Payout System**
```java
public enum PayoutType {
    STANDARD,    // 1:1 payout
    BLACKJACK,   // 3:2 payout  
    TIE          // Return bet
}
```

#### **Smart Ace Handling**
```java
// Automatically converts Aces from 11 to 1 to prevent busting
while (baseValue > 21 && aces > 0) {
    baseValue -= 10;
    aces--;
}
```

## Technologies

- **Java 21** - Modern Java features and syntax
- **Lombok** - Reduces boilerplate code
- **SLF4J** - Structured logging
- **Stream API** - Functional programming for collections
- **Records** - Immutable data carriers

## Game Rules Implemented

### **Standard Blackjack Rules**
- Dealer hits on 16 or less, stands on 17 or more
- Aces count as 11 or 1 (automatically optimized)
- Face cards worth 10 points
- Blackjack (21 with 2 cards) pays 3:2
- Player busts lose immediately
- Ties return the original bet

### **Game Flow**
1. **Betting Phase** - Player places bet
2. **Dealing Phase** - 2 cards each, dealer's second hidden
3. **Player Phase** - Hit/Stand decisions
4. **Dealer Phase** - Automatic play following house rules
5. **Resolution** - Determine winner and handle payouts

## Sample Gameplay

```
================================
    Welcome to Blackjack, John!
================================

John has 100 chips.

Enter your bet (1-100): 20

--------------------------------
INITIAL DEAL
--------------------------------
John: KING of HEARTS, SEVEN of CLUBS (Value: 17)
Dealer: ACE of SPADES, [Hidden Card] (Visible Value: 11)

Hit (h) or Stand (s)? s
John stands.

--------------------------------
DEALER'S TURN
--------------------------------
Dealer: ACE of SPADES, SIX of DIAMONDS (Value: 17)
Dealer stands with 17

================================
ROUND RESULTS
================================
John: KING of HEARTS, SEVEN of CLUBS (Value: 17)
Dealer: ACE of SPADES, SIX of DIAMONDS (Value: 17)

It's a tie! Your bet is returned.
```

## License

This project is open source and available under the [MIT License](LICENSE).

## Questions?

Feel free to open an issue or reach out if you have questions about the implementation!
