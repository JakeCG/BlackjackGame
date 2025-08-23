package ui;

import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleUi {
	
	private final Scanner scanner;
	@Getter
	private final GameDisplay display;
	
	public ConsoleUi() {
		this.scanner = new Scanner(System.in);
		this.display = new GameDisplay();
	}
	
	@NonNull
	public String getPlayerName() {
		System.out.print("Enter your name: ");
		String name = scanner.nextLine().trim();
		
		while (name.isEmpty()) {
			System.out.print("Name cannot be empty. Enter your name: ");
			name = scanner.nextLine().trim();
		}
		
		return name;
	}
	
	@NonNull
	public String getPlayerChoice(@NonNull String prompt, @NonNull String... validChoices) {
		while (true) {
			System.out.print(prompt + " ");
			String input = scanner.nextLine().toLowerCase().trim();
			
			String matchedChoice = Arrays.stream(validChoices)
									.filter(choice -> matchesChoice(input, choice))
									.findFirst()
									.orElse(null);
			
			if (matchedChoice != null) {
				return matchedChoice.toLowerCase().substring(0, 1);
			}
			
			String choicesList = Arrays.stream(validChoices)
									.map(choice -> choice + "(" + choice.substring(0, 1).toUpperCase() + ")")
									.collect(Collectors.joining(", "));
			
			System.out.println("Invalid choice. Please enter: " + choicesList);
		}
	}
	
	public int getIntegerInput(@NonNull String prompt, int min, int max) {
		while (true) {
			System.out.print(prompt);
			try {
				String input = scanner.nextLine().trim();
				int value = Integer.parseInt(input);
				
				if (value >= min && value <= max) {
					return value;
				} else {
					System.out.println("Please enter a number between " + min + " and " + max);
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number.");
			}
		}
	}
	
	public void displayMessage(@NonNull String message) {
		display.showMessage(message);
	}
	
	public void waitForEnter(@NonNull String message) {
		System.out.print(message + " (Press Enter to continue...)");
		scanner.nextLine();
	}
	
	public void close() {
		scanner.close();
	}
	
	private boolean matchesChoice(String input, String choice) {
		String lowerChoice = choice.toLowerCase();
		return input.equals(lowerChoice) || input.equals(lowerChoice.substring(0, 1));
	}
}
