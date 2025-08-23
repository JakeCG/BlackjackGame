package game;

import lombok.NonNull;
import models.Card;
import models.Deck;
import players.DealerNpc;
import players.HumanPlayer;
import ui.ConsoleUi;
import ui.GameDisplay;

public class BlackjackGame {
	private static final int STARTING_CHIPS = 100;
	private static final int INITIAL_CARDS_COUNT = 2;
	private static final int MINIMUM_DECK_SIZE = 10;
	
	private final Deck deck;
	private final HumanPlayer player;
	private final DealerNpc dealer;
	private final ConsoleUi ui;
	private final GameDisplay display;
	private final GameEngine engine;
	
	private GameState gameState;
	private int currentBet;
	
	public BlackjackGame(@NonNull HumanPlayer player, @NonNull DealerNpc dealer,
							@NonNull ConsoleUi ui, @NonNull GameEngine engine) {
		this.deck = new Deck();
		this.player = player;
		this.dealer = dealer;
		this.ui = ui;
		this.display = ui.getDisplay();
		this.engine = engine;
		this.gameState = GameState.WAITING_FOR_BET;
	}
	
	public void playGame() {
		try {
			while (gameState != GameState.GAME_OVER) {
				executeCurrentState();
			}
		} finally {
			endGame();
		}
	}
	
	private void executeCurrentState() {
		switch (gameState) {
			case WAITING_FOR_BET -> handleBetting();
			case DEALING_CARDS -> dealInitialCards();
			case PLAYER_TURN -> handlePlayerTurn();
			case DEALER_TURN -> handleDealerTurn();
			case ROUND_OVER -> handleRoundOver();
			default -> throw new IllegalStateException("Unknown game state: " + gameState);
		}
	}
	
	private void handleBetting() {
		display.showPlayerStatus(player);
		
		currentBet = player.getBetAmount();
		if (currentBet == 0) {
			gameState = GameState.GAME_OVER;
			return;
		}
		
		player.bet(currentBet);
		gameState = engine.getNextState(gameState, false, false, false);
	}
	
	private void dealInitialCards() {
		ensureDeckHasEnoughCards();
		clearAllHands();
		dealCardsToPlayers();
		
		display.showInitialDeal(player, dealer);
		
		if (hasImmediateBlackjack()) {
			gameState = GameState.ROUND_OVER;
		} else {
			gameState = engine.getNextState(gameState, false, false, false);
		}
	}
	
	private void handlePlayerTurn() {
		if (engine.isBust(player.getHand())) {
			display.showBustMessage(player.getName());
			gameState = engine.getNextState(gameState, false, true, false);
			return;
		}
		
		if (player.wantsToHit()) {
			dealCardToPlayer(player);
			display.showPlayerHand(player, false);
		} else {
			display.showStandMessage(player.getName());
			gameState = engine.getNextState(gameState, true, false, false);
		}
	}
	
	private void handleDealerTurn() {
		display.showDealerTurn();
		display.showDealerHand(dealer, false);
		
		if (engine.isBust(player.getHand())) {
			gameState = engine.getNextState(gameState, false, false, true);
			return;
		}
		
		playDealerHand();
		gameState = engine.getNextState(gameState, false, false, true);
	}
	
	private void handleRoundOver() {
		GameResult result = engine.determineResult(player.getHand(), dealer.getHand());
		
		processGameResult(result);
		display.showResults(player, dealer, result, currentBet);
		
		gameState = player.wantsToPlayAgain() ? GameState.WAITING_FOR_BET : GameState.GAME_OVER;
	}
	
	private void ensureDeckHasEnoughCards() {
		if (deck.getCards().size() < MINIMUM_DECK_SIZE) {
			deck.ensureMinimumCards(MINIMUM_DECK_SIZE);
			display.showMessage("New deck in use!");
		}
	}
	
	private void clearAllHands() {
		player.clearHand();
		dealer.clearHand();
	}
	
	private void dealCardsToPlayers() {
		for (int i = 0; i < INITIAL_CARDS_COUNT; i++) {
			player.addCard(dealCard());
			dealer.addCard(dealCard());
		}
	}
	
	private boolean hasImmediateBlackjack() {
		return engine.isBlackjack(player.getHand()) || engine.isBlackjack(dealer.getHand());
	}
	
	private void dealCardToPlayer(@NonNull HumanPlayer player) {
		Card newCard = dealCard();
		player.addCard(newCard);
		display.showCardDealt(player.getName(), newCard.toString());
	}
	
	private void playDealerHand() {
		while (engine.shouldDealerHit(dealer.getHand())) {
			Card newCard = dealCard();
			dealer.addCard(newCard);
			display.showCardDealt(dealer.getName(), newCard.toString());
			display.showDealerHand(dealer, false);
			
			if (engine.isBust(dealer.getHand())) {
				display.showBustMessage(dealer.getName());
				return;
			}
		}
		
		if (!engine.isBust(dealer.getHand())) {
			display.showStandMessage(dealer.getName());
		}
	}
	
	private void processGameResult(@NonNull GameResult result) {
		switch (result.winner()) {
			case PLAYER -> {
				player.winChips(currentBet, result.payoutType());
				ui.waitForEnter("Congratulations! You won this round.");
			}
			case TIE -> {
				player.winChips(currentBet, result.payoutType());
				ui.waitForEnter("It's a tie! Your bet has been returned.");
			}
			case DEALER -> {
				ui.waitForEnter("Dealer wins this round. Better luck next time!");
			}
		}
	}
	
	private Card dealCard() {
		if (deck.getCards().isEmpty()) {
			throw new IllegalStateException("Deck is empty! Cannot deal more cards.");
		}
		return deck.dealCard();
	}
	
	private void endGame() {
		display.showGameOver(player);
		ui.close();
	}
	
	public static void main(String[] args) {
		ConsoleUi ui = new ConsoleUi();
		GameEngine engine = new GameEngine();
		DealerNpc dealer = new DealerNpc();
		
		String playerName = ui.getPlayerName();
		HumanPlayer player = new HumanPlayer(playerName, STARTING_CHIPS, ui);
		
		ui.getDisplay().showWelcome(playerName);
		
		BlackjackGame game = new BlackjackGame(player, dealer, ui, engine);
		game.playGame();
	}
}