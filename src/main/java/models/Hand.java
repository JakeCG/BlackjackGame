package models;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static models.Rank.ACE;

@Data
public class Hand {
	private List<Card> cards = new ArrayList<>();
	
	public void addCard(@NonNull Card card) {
		cards.add(card);
	}
	
	public int getHandValue() {
		int baseValue = cards.stream()
								.mapToInt(card -> card.rank().getValue())
								.sum();
		
		long aces = cards.stream()
							.filter(card -> card.rank() == ACE)
							.count();
		
		while (baseValue > 21 && aces > 0) {
			baseValue -= 10;
			aces--;
		}
		
		return baseValue;
	}
	
	public boolean isBusted() {
		return getHandValue() > 21;
	}
	
	public boolean isBlackjack() {
		return cards.size() == 2 && getHandValue() == 21;
	}
	
	public List<Card> getCards() {
		return new ArrayList<>(cards);
	}
	
	public void clearCards() {
		cards.clear();
	}
	
	@Override
	public String toString() {
		if (cards.isEmpty()) {
			return "Empty hand";
		}
		
		return cards.stream()
						.map(Card::toString)
						.collect(Collectors.joining(", "));
	}
}
