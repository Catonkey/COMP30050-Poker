/*	Dara Callinan
 * 	14500717
 * 	Assignment 2 - DeckOfCards class
 */

package poker;

import java.util.Random;

//Deck Of Cards class - represents a deck of standard playing cards. Has two arrays,
//one holds the cards in the deck, and the other holds cards returned by players to
//the discard pile (these cards cannot be dealt until the deck is reshuffled). The 
//integers 'next' and 'amountDiscarded' keep track of the positions of the next card
//to deal and the next empty space in the discard pile array. The dealNext and returnCard
//methods allow cards to be dealt and discarded. The reset method automatically shuffles a 
//reconstructed deck for a new game. Also contains a toString method and a main method for
//testing. When creating cards it is assumed that ace is high.
public class DeckOfCards {

	static private final int DECK_SIZE = 5;
	
	private PlayingCard[] cards;
	private int next;
	private PlayingCard[] discards;
	private int amountDiscarded;
	
	//Public constructor: creates the deck and initializes the integer indexes. Shuffles deck.
	public DeckOfCards(){
		//52 PlayingCard instances
		initializeDeck(true);
		//Initialize next card position and amount of discarded cards
		next = 51;
		amountDiscarded = 0;
		//Shuffle deck for new game
		shuffle();
	}
	
	//Creates 52 instances of PlayingCard and puts them into the cards array
	private void initializeDeck(Boolean aceHigh){
		//Array of playing cards
		cards = new PlayingCard[52];
		discards = new PlayingCard[52];
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
	}
	
	//Reconstructs the deck, and initializes the next, amountDiscarded variables, and shuffles
	//the deck.
	public void reset(){
		//52 PlayingCard instances
		initializeDeck(true);
		//Initialize next card position and amount of discarded cards
		next = 51;
		amountDiscarded = 0;
		//Shuffle deck for new game
		shuffle();
	}
	
	//Adds all cards in the discard pile back into the main deck, and then shuffles the deck.
	//Note: the discarded cards are placed on the top of the deck, but this does not matter
	//as the deck is immediately shuffled. The deck is shuffled by swapping cards.
	public void shuffle(){
		//Put cards from discard pile back into deck (doesn't matter where due to shuffle after)
		int index = 0;
		while(discards[index]!=null){
			//Add discarded card to deck
			cards[next+1+index] = discards[index];
			//Remove card from discard pile
			discards[index] = null;
			index++;
			//Decrease amount of discarded cards
			amountDiscarded--;
		}
		//Increase next by amount of cards added
		next += index;
		
		//Shuffle deck (swap cards the current size of the deck squared times)
		//Note: If the two random numbers are the same, there is no swap. This is
		//necessary as patterns would emerge otherwise.
		for(int i=0; i<((next+1)*(next+1)); i++){
			//Generate two random integers in deck range
			Random rand = new Random();
			int randA = rand.nextInt((next - 0) + 1) + 0;
			int randB = rand.nextInt((next - 0) + 1) + 0;
			//Swap cards at those indexes
			PlayingCard temp = cards[randA];
			cards[randA] = cards[randB];
			cards[randB] = temp;
		}
		
	}
	
	//Deals the next playing card based on the index indicated by the 'next' variable.
	//After the card is dealt, it's replaced by null in the array, and 'next' is updated.
	public PlayingCard dealNext(){
		//Return null if next card index is < zero (no cards left in deck)
		if(next<0) return null;
		//Get card to be dealt
		PlayingCard card = cards[next];
		//Set this card to null in deck array
		cards[next] = null;
		//Update the next card to be dealt
		next--;
		return card;
	}
	
	//Adds a card into the discard pile array, at the index indicated by 'amountDiscarded',
	//before updating this variable.
	public void returnCard(PlayingCard discarded){
		//Add discarded card to discards array, at index = total number of discards
		discards[amountDiscarded] = discarded;
		//Increase amount of discards
		amountDiscarded++;
	}
	
	//Returns string representation of all cards in deck (not discards) in order from front
	//(Bottom of deck/last card to be dealt is first)
	public String toString(){
		String str = "(";
		//Add first card to string if deck is not empty
		if(cards[0]!=null) str += cards[0];
		//Add remaining cards to string
		int index = 1;
		while(index != 52 && cards[index]!=null){
			str += ", " + cards[index];
			index++;
		}
		return str + ")";
	}
	
	//main method for testing - Completes the following tasks: Creates a deck of cards,
	//deals some cards, returns some cards, deals the remaining cards (discarded cards 
	//should not be dealt), shuffles the deck (adds discarded cards to deck), deals
	//remaining cards. Finally resets (including shuffling) the deck and prints the deck.
	public static void main(String[] args){
		
		//Create deck
		DeckOfCards deck = new DeckOfCards();
		//Deal several cards (first 3 are named for testing purposes)
		System.out.println("Dealing cards:");
		PlayingCard a = deck.dealNext();
		PlayingCard b = deck.dealNext();
		PlayingCard c = deck.dealNext();
		System.out.println("a:" + a + "  b:" + b + "  c:" + c);
		for(int i=0; i<20; i++){
			System.out.print(deck.dealNext() + "  ");
		}
		
		//Discard b and c
		System.out.println("\n\nDiscard/Return card b (" + b + ") and c (" + c + ")");
		deck.returnCard(b);
		deck.returnCard(c);
		//Deal remaining cards (cards b and c should not be dealt as they are in discard pile).
		System.out.println("Dealing remaining cards:");
		PlayingCard d = deck.dealNext();
		while(d!=null){
			System.out.print(d + "  ");
			d = deck.dealNext();
		}
		
		//Shuffle deck
		System.out.println("\n\nShuffling deck.");
		deck.shuffle();
		//Deal remaining cards (cards b and c should now be dealt)
		System.out.println(deck+ "Dealing remaining cards:");
		d = deck.dealNext();
		while(d!=null){
			System.out.print(d + "  ");
			d = deck.dealNext();
		}
		
		//Reset deck
		System.out.println("\n\nResetting deck.");
		deck.reset();
		//Print deck
		System.out.println("Printing deck:\n" + deck);
	}
}
