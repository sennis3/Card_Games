package BlackjackPackage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Blackjack {
	static List<Integer> doubleCardDeck;
	static Queue<Integer> currDeck;
	static Scanner inputScan;
	static final DecimalFormat df = new DecimalFormat("0.00");

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		inputScan = new Scanner(System.in);
		System.out.print("Enter Player Name: ");
		final String playerName = inputScan.nextLine();
		System.out.println("Hi " + playerName + ". Welcome to Blackjack");
		
		//Get player's starting money
		System.out.println("How much money are you starting with?");
		int startingMoney = getNumericalInput(1, Integer.MAX_VALUE);
		int playerMoney = startingMoney;
		/*while (playerMoney == 0) {
			System.out.println("How much money are you starting with?");
			String input = inputScan.nextLine();
			try {
				playerMoney = Integer.parseInt(input);
			}
			catch (Exception e){
				System.out.println("Invalid input - must be a number");
			}
		}*/
		System.out.println(playerName + "'s Money: " + playerMoney);
		
		//Sets up card decks
		doubleCardDeck = new ArrayList<Integer>();
		for (int i = 1; i <= 52; i++) {
			doubleCardDeck.add(i);
			doubleCardDeck.add(i);
		}
		Collections.shuffle(doubleCardDeck);
		currDeck = new LinkedList<Integer>(doubleCardDeck);
		
		/*System.out.println(getCardString(currDeck.remove()));
		System.out.println(getCardString(currDeck.remove()));
		System.out.println(getCardString(currDeck.remove()));
		System.out.println();
		for (int card: doubleCardDeck) {
			System.out.println(getCardString(card));
		}*/
		
		boolean isGameOver = false;
		while (!isGameOver) {
			int playerTotal = 0;
			int dealerTotal = 0;
			
			List<Integer> playerCards = new ArrayList<Integer>();
			List<Integer> dealerCards = new ArrayList<Integer>();
			
			//Set Round Wager
			System.out.println("\nMoney: " + playerMoney);
			System.out.print("Enter Wager: ");
			int wager = getNumericalInput(1, playerMoney);
			
			playerCards.add(drawCard());
			dealerCards.add(drawCard());
			playerCards.add(drawCard());
			dealerCards.add(drawCard());
			
			
			System.out.println("Dealer Card: " + getCardString(dealerCards.get(0)));
			//printCards(dealerCards);
			//System.out.println("Total: " + calcTotal(dealerCards));
			System.out.print(playerName + "'s Cards: ");
			printCards(playerCards);
			playerTotal = calcTotal(playerCards);
			System.out.println("Total: " + playerTotal);
			
			
			//Player's turn
			boolean playerStands = false;
			while (!playerStands) {
				if (getChoiceInput("h", "s", "Hit or Stand? (h/s) ").equals("s")) {
					playerStands = true;
					break;
				}
				/*System.out.println("Hit or Stand? (h/s)");
				if (!inputScan.nextLine().toLowerCase().equals("h")) {
					playerStands = true;
					break;
				}*/
				int newCard = drawCard();
				playerCards.add(newCard);
				System.out.print(playerName + "'s Cards: ");
				printCards(playerCards);
				playerTotal = calcTotal(playerCards);
				System.out.println("Total: " + playerTotal);
				
				if (playerTotal > 21) {
					System.out.println(playerName + " Busts");
					playerStands = true;
				}
			}
			
			//Dealer's Turn
			while (calcTotal(dealerCards) < 17) { //Dealer hits until 17
				dealerCards.add(drawCard());
			}
			System.out.print("Dealer's Cards: ");
			printCards(dealerCards);
			dealerTotal = calcTotal(dealerCards);
			System.out.println("Total: " + dealerTotal);
			
			if (playerTotal <= 21 && (playerTotal > dealerTotal || dealerTotal > 21)) {
				//Player Wins
				System.out.println(playerName + " Wins");
				System.out.println("You've won " + wager);
				playerMoney += wager;
			}
			else if (dealerTotal <= 21 && (dealerTotal > playerTotal || playerTotal > 21)) {
				//Dealer Wins
				System.out.println("Dealer Wins");
				System.out.println("You've lost " + wager);
				playerMoney -= wager;
			}
			else {
				//Push (tie)
				System.out.println("Push");
			}
			
			//Asks user if they want to keep playing
			if (getChoiceInput("y", "n", "Play Again? (y/n) ").equals("n"))
				isGameOver = true;
			/*System.out.print("Play Again? y/n ");
			if (playerMoney <= 0 || !inputScan.nextLine().toLowerCase().equals("y"))
				isGameOver = true;*/
		}
		
		System.out.println("\nGame Over");
		if (playerMoney >= startingMoney) {
			System.out.println("You've gained " + (playerMoney-startingMoney));
			double percentChange = (double) (playerMoney - startingMoney) * 100 / startingMoney;
			System.out.println("You've gained " + df.format(percentChange) + "% of your starting money");
		}
		else {
			System.out.println("You've lost " + (startingMoney - playerMoney));
			double percentChange = (double) (startingMoney - playerMoney)* 100 / startingMoney;
			System.out.println("You've lost " + df.format(percentChange) + "% of your starting money");
		}
	
	}
	
	//Gets string value of card
	private static String getCardString(int card) {
		String val;
		String suit;
		switch ((card-1) / 13) {
			case 0:
				suit = "Spades";
				break;
			case 1:
				suit = "Clubs";
				break;
			case 2:
				suit = "Hearts";
				break;
			case 3:
				suit = "Diamonds";
				break;
			default:
				suit = "Invalid suit";
		}
		
		switch (card % 13) {
			case 1:
				val = "Ace";
				break;
			case 11:
				val = "Jack";
				break;
			case 12:
				val = "Queen";
				break;
			case 0:
				val = "King";
				break;
			default:
				val = (card % 13) + "";
		}
		
		return val + " of " + suit;
	}
	
	//Draws card from currDeck and reshuffles cards if no cards left
	private static int drawCard() {
		if (currDeck.isEmpty()) {
			System.out.println("Out of cards. Shuffling deck...");
			Collections.shuffle(doubleCardDeck);
			currDeck = new LinkedList<Integer>(doubleCardDeck);
		}
		return currDeck.remove();
	}
	
	//Calculates the total of a hand of cards
	private static int calcTotal(List<Integer> cards) {
		int total = 0;
		boolean hasAce = false;
		for (int card : cards) {
			int cardVal = Math.min((card-1) % 13 + 1, 10);
			if (cardVal == 1)
				hasAce = true;
			total += cardVal;
		}
		if (hasAce && total+10 <= 21)
			return total+10;
		return total;
	}
	
	//Prints a hand of cards
	private static void printCards(List<Integer> cards) {
		for (int i = 0; i < cards.size(); i++) {
			System.out.print(getCardString(cards.get(i)));
			if (i < cards.size()-1)
				System.out.print(", ");
		}
		System.out.println();
	}
	
	//Gets numerical input from user and ensures its within range
	private static int getNumericalInput(int min, int max) {
		int result = 0;
		boolean validInput = false;
		while (!validInput) {
			String input = inputScan.nextLine();
			try {
				result =  Integer.parseInt(input);
				if (result >= min && result <= max) {
					validInput = true;
				}
				else {
					System.out.println("Invalid Input - out of range");
				}
			}
			catch (Exception e){
				System.out.println("Invalid Input - must be a number");
			}
		}
		return result;
	}
	
	private static String getChoiceInput(String choice1, String choice2, String prompt) {
		String input = "";
		boolean validInput = false;
		while (!validInput) {
			System.out.print(prompt);
			input = inputScan.nextLine().toLowerCase();
			if (input.equals(choice1) || input.equals(choice2))
				validInput = true;
			else
				System.out.println("Invalid Input");
		}
		return input;
	}
	
	

}
