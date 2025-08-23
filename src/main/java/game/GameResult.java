package game;

import models.Player.PayoutType;

public record GameResult(Winner winner, PayoutType payoutType) {
}
