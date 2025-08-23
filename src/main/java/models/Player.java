package models;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class Player {
	private final String name;
	private final Hand hand;
	private int chipsAmount;
	
	public Player(@NonNull String name, int startingChips) {
		this.name = name;
		this.hand  = new Hand();
		this.chipsAmount = startingChips;
	}
	
	public void addCard(@NonNull Card card) {
		hand.addCard(card);
	}
	
	public boolean canBet(int betAmount) {
		return chipsAmount > 0 && chipsAmount >= betAmount;
	}
	
	public void bet(int betAmount) {
		if (canBet(betAmount)) {
			chipsAmount -= betAmount;
		} else {
			System.out.println("You don't have enough chips to bet that amount.");
		}
	}
	
	public void winChips(int betAmount, PayoutType payoutType) {
		int payout = calculatePayout(betAmount, payoutType);
		chipsAmount += betAmount + payout;
	}
	
	public void clearHand() {
		hand.clearCards();
		
	}
	
	public enum PayoutType {
		STANDARD,
		BLACKJACK,
		TIE
	}
	
	public int calculatePayout(int betAmount, PayoutType payoutType) {
		return switch (payoutType) {
			case STANDARD -> betAmount;
			case BLACKJACK -> (betAmount * 3) / 2;
			case TIE -> 0;
			default -> throw new IllegalArgumentException("Unknown payout type");
		};
	}
}
