package models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class defines the ranks available for all cards - ace being 11 or 1 is handled in the logic of Hand.Java
 */
@Getter
@RequiredArgsConstructor
public enum Rank {
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10),
    ACE(11);

    private final int value;
}
