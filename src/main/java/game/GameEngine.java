package game;

import lombok.NonNull;
import models.Hand;
import models.Player;

public class GameEngine {
	
	public boolean isBlackjack(@NonNull Hand hand) {
		return hand.isBlackjack();
	}
	
	public boolean isBust(@NonNull Hand hand) {
		return hand.isBusted();
	}
	
	public GameResult determineResult(@NonNull Hand playerHand, @NonNull Hand dealerHand) {
		if (isBust(playerHand)) {
			return new GameResult(Winner.DEALER, Player.PayoutType.STANDARD);
		}
		
		if (isBust(dealerHand)) {
			Player.PayoutType payoutType = isBlackjack(playerHand) ?
												   Player.PayoutType.BLACKJACK : Player.PayoutType.STANDARD;
			return new GameResult(Winner.PLAYER, payoutType);
		}
		
		return determineNonBustResult(playerHand, dealerHand);
	}
	
	private GameResult determineNonBustResult(Hand playerHand, Hand dealerHand) {
		boolean playerBlackjack = isBlackjack(playerHand);
		boolean dealerBlackjack = isBlackjack(dealerHand);
		
		// Both have blackjack - tie
		if (playerBlackjack && dealerBlackjack) {
			return createTieResult();
		}
		
		// Only player has blackjack - player wins with blackjack payout
		if (playerBlackjack) {
			return new GameResult(Winner.PLAYER, Player.PayoutType.BLACKJACK);
		}
		
		// Only dealer has blackjack - dealer wins
		if (dealerBlackjack) {
			return new GameResult(Winner.DEALER, Player.PayoutType.STANDARD);
		}
		
		// No blackjacks - compare hand values
		return compareHandValues(playerHand, dealerHand);
	}
	
	private GameResult compareHandValues(Hand playerHand, Hand dealerHand) {
		int playerValue = playerHand.getHandValue();
		int dealerValue = dealerHand.getHandValue();
		
		if (playerValue > dealerValue) {
			return new GameResult(Winner.PLAYER, Player.PayoutType.STANDARD);
		} else if (dealerValue > playerValue) {
			return new GameResult(Winner.DEALER, Player.PayoutType.STANDARD);
		} else {
			return createTieResult();
		}
	}
	
	private GameResult createTieResult() {
		return new GameResult(Winner.TIE, Player.PayoutType.TIE);
	}
	
	public GameState getNextState(@NonNull GameState currentState,
									boolean playerStands,
									boolean playerBusts,
									boolean dealerFinished) {
		return switch (currentState) {
			case WAITING_FOR_BET -> GameState.DEALING_CARDS;
			case DEALING_CARDS -> GameState.PLAYER_TURN;
			case PLAYER_TURN -> determinePlayerTurnNextState(playerBusts, playerStands);
			case DEALER_TURN -> dealerFinished ? GameState.ROUND_OVER : GameState.DEALER_TURN;
			case ROUND_OVER -> GameState.WAITING_FOR_BET;
			default -> currentState;
		};
	}
	
	private GameState determinePlayerTurnNextState(boolean playerBusts, boolean playerStands) {
		return (playerBusts || playerStands) ? GameState.DEALER_TURN : GameState.PLAYER_TURN;
	}
	
	public boolean isValidHit(@NonNull Hand hand) {
		return !isBust(hand) && !isBlackjack(hand);
	}
	
	public boolean shouldDealerHit(@NonNull Hand dealerHand) {
		return dealerHand.getHandValue() < 17;
	}
}