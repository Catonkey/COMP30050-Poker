/*	Dara Callinan
 * 	14500717
 * 	Assignment 1 - PlayingCard class
 */

package poker;

//Playing Card class - represents a standard playing card with type and suit of card
//in addition to the face value (integer representation of card's base value) and 
//game value (integer representation of cards true value, based on the game being played)
public class PlayingCard {
	
	static public final char DIAMONDS = 'D';
	static public final char HEARTS = 'H';
	static public final char SPADES = 'S';
	static public final char CLUBS = 'C';

	private String type;
	private char suit;
	private int faceValue;
	private int gameValue;
	
	//Public constructor: assigns card type (string), a character representing suit
	//and 2 integers representing the face value and playable value of the card.
	public PlayingCard(String cardType, char cardSuit, int faceVal, int gameVal){
		type = cardType;
		suit = cardSuit;
		faceValue = faceVal;
		gameValue = gameVal;
	}
	
	//Return the number value or face character of the card
	public String getType(){
		return type;
	}
	
	//Return the first character of the card's suit
	public char getSuit(){
		return suit;
	}
	
	//Return the value of the card based on playing cards standard order
	public int getFaceValue(){
		return faceValue;
	}
	
	//Return the true value of the card for the game it is being used in
	public int getGameValue(){
		return gameValue;
	}
	
	//Returns a string representation of a card with its type followed by suit
	public String toString(){
		return type + suit;
	}
	
	//main method for testing and printing cards as required in assignment document
	public static void main(String[] args){
		
		//Represents whether the ace is high (valued above king) or low (below 2)
		Boolean aceHigh = true;
		
		//Array of playing cards
		PlayingCard[] cards = new PlayingCard[52];
		
		//First loop iterates through the 4 suits
		for(int i=0; i<4; i++){
			//Determine the character that will represent the suit
			char suitChar;
			switch (i) {
				case 1: suitChar = PlayingCard.HEARTS;
					break;
				case 2: suitChar = PlayingCard.SPADES;
					break;
				case 3: suitChar = PlayingCard.CLUBS;
					break;
				default: suitChar = PlayingCard.DIAMONDS;
			}
			
			//Second loop iterates through 13 card types
			for(int j=1; j<14; j++){
				//Determine the string that will represent the card type
				String typeStr = "";
				switch (j) {
					case 1: typeStr = "A";
						break;
					case 11: typeStr = "J";
						break;
					case 12: typeStr = "Q";
						break;
					case 13: typeStr = "K";
						break;
					default: typeStr = Integer.toString(j);
				}
				//gameValue (playable value) is set to equal the card's standard value
				//unless it is an ace, and the game values an ace more than a king (14).
				int gameVal = j;
				if(j == 1 && aceHigh){
					gameVal = 14;
				}
				//add a new card to the array
				cards[(i*13)+(j-1)] = new PlayingCard(typeStr, suitChar, j, gameVal);
			}
		}
		//Print cards
		for(PlayingCard card : cards){
			System.out.println(card);
		}
	}
}
