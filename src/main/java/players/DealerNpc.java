package players;

import models.Card;
import models.Player;
import models.Rank;

import java.util.List;

/**
 * Dealer logic, following standard rules of blackjack, which is if the dealer is at or above 17 then they must always
 * stand, irrespective of the players hand.
 */
public class DealerNpc  extends Player {
	private static final int DEALER_HIT_THRESHOLD = 17;
	
	public DealerNpc() {
		super("Dealer", Integer.MAX_VALUE);
	}
	
	public boolean shouldHit() {
		return getHand().getHandValue() < DEALER_HIT_THRESHOLD;
	}
	
	public String getVisibleHand() {
		List<Card> cards = getHand().getCards();
		
		if (cards.isEmpty()) {
			return "No cards";
		}
		
		return cards.getFirst() + ", [ Hidden card ]";
	}
	
	public int getVisibleValue() {
		List<Card> cards = getHand().getCards();
		if (cards.isEmpty()) {
			return 0;
		}
		
		Card firstCard = cards.getFirst();
		
		if (firstCard.rank() == Rank.ACE) {
			return 11;
		}
		return firstCard.rank().getValue();
	}
	
	public String getFullHand() {
		return getHand().toString() + " (Value: " + getHand().getHandValue() + ")";
	}
}
