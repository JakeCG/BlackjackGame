package models;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class generates and then shuffles a deck of 52 cards using the logic of 4 suits * 13 cards.
 * It then shuffles them, using the Math.random() system time as a seed for shuffling, this can be adjusted by using
 * random, but I thought this would be better for a simulated "true" randomness.
 */

@Slf4j
public class Deck {

    @Getter
    private List<Card> cards;

    public Deck() {
        initialiseDeck();
        shuffleDeck();
    }
    
    public void shuffleDeck() {
        Collections.shuffle(cards);
        log.info("Shuffled deck");
    }
    
    public Card dealCard() {
        if (cards.isEmpty()) {
            initialiseDeck();
            shuffleDeck();
        }
        Card dealtCard = cards.removeLast();
        log.info("Dealt card: {}", dealtCard);
        return dealtCard;
    }
    
    public void ensureMinimumCards(int minimumCards) {
        if (cards.size() < minimumCards) {
            resetDeck();
        }
    }
    
    private void resetDeck() {
        cards.clear();
        initialiseDeck();
        shuffleDeck();
    }

    private void initialiseDeck() {
        cards = Arrays.stream(Suit.values())
                .flatMap(suit -> Arrays.stream(Rank.values())
                    .map(rank -> new Card(rank, suit)))
                .collect(Collectors.toList());
        log.info("New deck created with {} cards", cards.size());
    }
}
