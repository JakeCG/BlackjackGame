package players;

import lombok.NonNull;
import models.Player;
import ui.ConsoleUi;

public class HumanPlayer extends Player {
	
	private static final String HIT = "h";
	private static final String STAND = "s";
	private static final String YES = "y";
	private static final String NO = "n";
	
	private final ConsoleUi ui;
	
	public HumanPlayer(@NonNull String name, int startingChips, @NonNull ConsoleUi ui) {
		super(name, startingChips);
		this.ui = ui;
	}
	
	public boolean wantsToHit() {
		String choice = ui.getPlayerChoice("Hit (h) or Stand (s)?", HIT, STAND);
		return choice.equalsIgnoreCase(HIT);
	}
	
	public int getBetAmount() {
		int maxBet = getChipsAmount();
		
		if (maxBet == 0) {
			return 0;
		}
		
		return ui.getIntegerInput(
				"Enter your bet (1-" + maxBet + "): ",
				1,
				maxBet
		);
	}
	
	public boolean wantsToPlayAgain() {
		if (getChipsAmount() == 0) {
			ui.displayMessage("You're out of chips! Game over.");
			return false;
		}
		
		String choice = ui.getPlayerChoice("Play another round? (y/n)", YES, NO);
		return choice.equals(YES);
	}
	
	public boolean wantsToDoubleDown(int currentBet) {
		if (getChipsAmount() < currentBet || getHand().getCards().size() != 2) {
			return false;
		}
		
		String choice = ui.getPlayerChoice("Double down? (y/n)", YES, NO);
		return choice.equals(YES);
	}
}
