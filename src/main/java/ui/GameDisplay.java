package ui;

import game.GameResult;
import models.Player;
import players.DealerNpc;

public class GameDisplay {
	private static final String SEPARATOR = "================================";
	private static final String THIN_SEPARATOR = "--------------------------------";
	
	public void showWelcome(String playerName) {
		System.out.println(SEPARATOR);
		System.out.println("Welcome to Blackjack, " + playerName + ".");
		System.out.println(SEPARATOR);
		System.out.println();
	}
	
	public void showPlayerStatus(Player player) {
		System.out.println(player.getName() + " has " + player.getChipsAmount() + " chips.");
		System.out.println();
	}
	
	public void showInitialDeal(Player player, DealerNpc DealerNpc) {
		System.out.println(THIN_SEPARATOR);
		System.out.println("INITIAL DEAL");
		System.out.println(THIN_SEPARATOR);
		showPlayerHand(player, false);
		showDealerHand(DealerNpc, true);
		System.out.println();
	}
	
	public void showPlayerHand(Player player, boolean hideValue) {
		String handDisplay = player.getName() + ": " + player.getHand().toString();
		if (!hideValue) {
			handDisplay += " (Value: " + player.getHand().getHandValue() + ")";
		}
		System.out.println(handDisplay);
	}
	
	public void showDealerHand(DealerNpc dealer, boolean hideSecondCard) {
		String handDisplay;
		if (hideSecondCard) {
			handDisplay = dealer.getName() + ": " + dealer.getVisibleHand();
			handDisplay += " (Visible Value: " + dealer.getVisibleValue() + ")";
		} else {
			handDisplay = dealer.getName() + ": " + dealer.getFullHand();
		}
		System.out.println(handDisplay);
	}
	
	public void showCardDealt(String playerName, String cardName) {
		System.out.println(playerName + " draws: " + cardName);
	}
	
	public void showDealerTurn() {
		System.out.println();
		System.out.println(THIN_SEPARATOR);
		System.out.println("DEALER'S TURN");
		System.out.println(THIN_SEPARATOR);
	}
	
	public void showResults(Player player, DealerNpc dealer, GameResult result, int originalBet) {
		showResultsHeader();
		showResultsHands(player, dealer);
		showResultsOutcome(result, player, originalBet);
	}
	
	
	public void showGameOver(Player player) {
		System.out.println(SEPARATOR);
		System.out.println("GAME OVER");
		System.out.println(SEPARATOR);
		System.out.println("Final chips: " + player.getChipsAmount());
		System.out.println("Thanks for playing!");
		System.out.println(SEPARATOR);
	}
	
	public void showMessage(String message) {
		System.out.println(message);
	}
	
	public void showBustMessage(String playerName) {
		System.out.println(playerName + " busted! (Over 21)");
	}
	
	public void showStandMessage(String playerName) {
		System.out.println(playerName + " stands.");
	}
	
	private void showResultsHeader() {
		System.out.println();
		System.out.println(SEPARATOR);
		System.out.println("ROUND RESULTS");
		System.out.println(SEPARATOR);
	}
	
	private void showResultsHands(Player player, DealerNpc dealer) {
		showPlayerHand(player, false);
		showDealerHand(dealer, false);
		System.out.println();
	}
	
	private void showResultsOutcome(GameResult result, Player player, int originalBet) {
		switch (result.winner()) {
			case PLAYER:
				if (result.payoutType() == Player.PayoutType.BLACKJACK) {
					System.out.println("BLACKJACK! You win!");
				} else {
					System.out.println("You win!");
				}
				System.out.println("You won " + (originalBet +
									player.calculatePayout(originalBet, result.payoutType())) + " " + "chips!");
				break;
			case DEALER:
				System.out.println("Dealer wins!");
				System.out.println("You lost " + originalBet + " chips.");
				break;
			case TIE:
				System.out.println("It's a tie! Your bet is returned.");
				break;
		}
		System.out.println();
	}
}
