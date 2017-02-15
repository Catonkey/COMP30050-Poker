/*	Dara Callinan
 * 	14500717
 * 	Assignment 3 - HandOfCards class
 */

package poker;

//Hand Of Cards class - represents a hand of standard playing cards. The hand size is represented
//by the constant HAND_SIZE. Has an array of playing cards and a reference to the deck (for returning
//cards). Contains an instructor which takes a deck, and deals five cards, then sorts the hand with
//the sort method. The sort method sorts the hand by GAME value (ace is valued high, and placed first).
//Also contains separate boolean methods for each hand type, with two method containsStraight and
//containsFlush which are used in several of these methods. Only the method representing the highest
//value playable hand will return true. Also contains a toString method, and a main method for testing.
public class HandOfCards {
	
	static private final int HAND_SIZE = 5;
	
	private PlayingCard[] cards;
	private DeckOfCards deck;
	
	//Public constructor - takes a deck of cards, and deals cards from this deck to create the hand,
	//then sorts the hand.
	public HandOfCards(DeckOfCards cardDeck){
		deck = cardDeck;
		cards = new PlayingCard[HAND_SIZE];
		//Deal HAND_SIZE amount of cards and store in cards array
		for(int i=0; i<HAND_SIZE; i++){
			cards[i] = deck.dealNext();
		}
		//Sort hand
		sort();
	}

	//Returns the deck for discarding cards to.
	private DeckOfCards getDeck(){
		return deck;
	}
	
	//Sorts a hand - orders a hand by the GAME value (ranks aces highest in poker)
	private void sort(){
		//For each card position
		for(int i=0; i<HAND_SIZE; i++){
			int j = i+1;
			//For each card after that position,
			while(j<HAND_SIZE){
				//if the card is a higher value, swap with initial card
				if(cards[j].getGameValue()>=cards[i].getGameValue()){
					PlayingCard temp = cards[i];
					cards[i] = cards[j];
					cards[j] = temp;
				}
				j++;
			}
		}
	}
	
	//Determines whether a hand contains a straight (but does not check if also a flush)
	private boolean containsStraight(){
		int val = cards[0].getFaceValue();
		//If ace followed by king, set value to 14 instead of 1 (ace can be high or low)
		//Otherwise, if first card is an ace (ace is placed first due to high value), set val
		//to 5 (for low straight).
		if(val == 1 && cards[1].getFaceValue()==13){
			val = 14;
		} else if(val == 1){
			val = 6;
		}
		//Check that every card is one value lower than previous card
		for(int i=1; i<HAND_SIZE; i++){
			if(cards[i].getFaceValue()!=val-1 || cards[i].getGameValue()!=val-1){
				return false;
			}
			val = cards[i].getFaceValue();
		}
		return true;
	}
	
	//Determines whether a hand contains a flush (but does not check if also a straight)
	private boolean containsFlush(){
		//Get suit of first card
		char suit = cards[0].getSuit();
		//Return false if any card is not that suit, otherwise return true
		for(int i=1; i<HAND_SIZE; i++){
			if(cards[i].getSuit()!=suit){
				return false;
			}
		}
		return true;
	}
	
	//Determines whether hand is a royal flush (straight flush with ace, king...)
	public boolean isRoyalFlush(){
		boolean hasAceKing = cards[0].getFaceValue()==1 && cards[1].getFaceValue()==13;
		//Return true if hand has an ace, a straight, and a flush
		return containsStraight() && containsFlush() && hasAceKing;
	}
	
	//Determines whether hand is a straight flush (straight flush without ace, king...)
	public boolean isStraightFlush(){
		boolean hasAceKing = cards[0].getFaceValue()==1 && cards[1].getFaceValue()==13;
		//Return true if hand has a straight and a flush but no ace
		return containsStraight() && containsFlush() && !hasAceKing;
	}
	
	//Determines whether hand is a four of a kind (4 matching cards)
	public boolean isFourOfAKind(){
		//Return true if hand has at least 4 matching cards
		if(	cards[0].getFaceValue() == cards[3].getFaceValue() || 
			cards[1].getFaceValue() == cards[4].getFaceValue()){
			return true;
		} else {
			//Otherwise return false
			return false;
		}
	}
	
	//Determines whether hand is a full house (3 of a kind and a pair)
	public boolean isFullHouse(){
		//Return true if first 3 cards and last 2 match, or first 2 and last 3 match.
		if(	(cards[0].getFaceValue() == cards[2].getFaceValue() && 
			cards[3].getFaceValue() == cards [4].getFaceValue()) ||
			(cards[0].getFaceValue() == cards[1].getFaceValue() && 
			cards[2].getFaceValue() == cards [4].getFaceValue())){
			return true;
		} else {
			//Otherwise return false.
			return false;
		}
	}
	
	//Determines whether hand is a flush (not straight flush)
	public boolean isFlush(){
		//Return true if hand contains flush but does not contain a straight
		return containsFlush() && !containsStraight();
	}
	
	//Determines whether hand is a straight (not straight flush)
	public boolean isStraight(){
		//Return true if hand contains straight but does not contain a flush
		return containsStraight() && !containsFlush();
	}
	
	//Determines whether hand is a three of a kind (not full house/four of a kind)
	public boolean isThreeOfAKind(){
		//If hand is a four of a kind or full house, return false
		if(isFourOfAKind() || isFullHouse()){
			return false;
		}
		//If hand has at least 3 matching cards, return true
		if(	(cards[0].getFaceValue() == cards[2].getFaceValue()) || 
			(cards[1].getFaceValue() == cards[3].getFaceValue()) ||
			(cards[2].getFaceValue() == cards[4].getFaceValue())){
			return true && !isFourOfAKind() && !isFullHouse();
		}
		//Otherwise, return false
		return false;
	}
	
	//Determines whether hand is a two pair (and not a more valuable hand)
	public boolean isTwoPair(){
		//If cards make a higher value hand containing matching cards, return false
		if(isFourOfAKind() || isFullHouse()){
			return false;
		} else if(isThreeOfAKind()){
			return false;
		}
		//Ensure hand contains at least 2 pairs - if so, return true
		if(	((cards[0].getFaceValue() == cards[1].getFaceValue()) && 
			((cards[2].getFaceValue() == cards[3].getFaceValue()) ||
			(cards[3].getFaceValue() == cards[4].getFaceValue()))) ||
			((cards[1].getFaceValue() == cards[2].getFaceValue()) && 
			((cards[3].getFaceValue() == cards[4].getFaceValue())))){
			
			return true;
		}
		//Otherwise, return false
		return false;
	}
	
	//Returns boolean determining if the hand has One pair (and is not a more valuable hand)
	public boolean isOnePair(){
		//If cards make a higher value hand containing matching cards, return false
		if(isFourOfAKind() || isFullHouse()){
			return false;
		} else if(isThreeOfAKind()){
			return false;
		} else if(isTwoPair()){
			return false;
		}
		//Ensure hand contains at least 2 matching cards - if so, return true
		if(	(cards[0].getFaceValue() == cards[1].getFaceValue()) || 
			(cards[1].getFaceValue() == cards[2].getFaceValue()) ||
			(cards[2].getFaceValue() == cards[3].getFaceValue()) ||
			(cards[3].getFaceValue() == cards[4].getFaceValue())){
			return true;
		}
		//Otherwise return false
		return false;
	}
	
	//Determines if the hand is only a high hand (by ensuring that it is not any
	//of the other hands.
	public boolean isHighHand(){
		//If all other hand methods return false, then the hand is a high hand
		if(	!isOnePair() && !isTwoPair() && !isThreeOfAKind() && !isFourOfAKind() && 
			!isFullHouse() && !containsStraight() && !containsFlush()){
			return true;
		} else {
			return false;
		}
	}
	
	//Returns string representation of cards in the hand
	public String toString(){
		String str = "(";
		if(cards[0]!=null) str += cards[0];
		for(int i=1; i<HAND_SIZE; i++){
			str += ", " + cards[i];
		}
		str += ")";
		return str;
	}
	
	//main method for testing
	public static void main(String[] args){
		//Create deck
		DeckOfCards deck = new DeckOfCards();
		//Create a hand of 5 cards
		HandOfCards hand = new HandOfCards(deck);
		//Results:
		System.out.println(hand + "\n");
		System.out.println("Royal Flush: \t\t" + hand.isRoyalFlush());
		System.out.println("Straight Flush: \t" + hand.isStraightFlush());
		System.out.println("Four of a Kind: \t" + hand.isFourOfAKind());
		System.out.println("Full House: \t\t" + hand.isFullHouse());
		System.out.println("Flush: \t\t\t" + hand.isFlush());
		System.out.println("Straight: \t\t" + hand.isStraight());
		System.out.println("Three of a Kind: \t" + hand.isThreeOfAKind());
		System.out.println("Two Pair: \t\t" + hand.isTwoPair());
		System.out.println("One Pair: \t\t" + hand.isOnePair());
		System.out.println("High Hand: \t\t" + hand.isHighHand());
	}
}
