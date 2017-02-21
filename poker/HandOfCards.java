/*	Dara Callinan
 * 	14500717
 * 	Assignment 3 - HandOfCards class & Assignment 4 - getGameValue() method
 */

package poker;

//Hand Of Cards class - represents a hand of standard playing cards. The hand size is represented
//by the constant HAND_SIZE. Has an array of playing cards and a reference to the deck (for returning
//cards). Contains an instructor which takes a deck, and deals five cards, then sorts the hand with
//the sort method. The sort method sorts the hand by GAME value (ace is valued high, and placed first).
//Also contains separate boolean methods for each hand type, with two method containsStraight and
//containsFlush which are used in several of these methods. Only the method representing the highest
//value playable hand will return true. Also contains a toString method, and a main method for testing.
//getGameValue() method returns an integer representing the value of the hand when compared with other
//hands. Better hand types are valued higher, and higher card values are valued higher and are weighted
//based on poker rules (value of pair considered before value of single cards etc.)
public class HandOfCards {
	
	static private final int HAND_SIZE = 5;
	//Hand type values - Added to hand game value based on the hand type.
	static private final int HIGH_HAND_DEFAULT = 0;
	static private final int ONE_PAIR_DEFAULT = 600000;
	static private final int TWO_PAIR_DEFAULT = 7500000;
	static private final int THREE_OF_A_KIND_DEFAULT = 7600000;
	static private final int STRAIGHT_DEFAULT = 7700000;
	static private final int FLUSH_DEFAULT = 7710000;
	static private final int FULL_HOUSE_DEFAULT = 9000000;
	static private final int FOUR_OF_A_KIND_DEFAULT = 9100000;
	static private final int STRAIGHT_FLUSH_DEFAULT = 9200000;
	static private final int ROYAL_FLUSH_DEFAULT = 9999999;
	
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
	
	//Returns an integer value representing the value of the hand when compared with
	//other hand values. Higher is better. Better hand types are valued more, and cards are
	//valued according to rules of poker (single cards will not affect the value of a hand more
	//than a pair, etc.
	//Generally values groups (pairs/three of a kind) more by getting their card value to a 
	//power - For example - in a high hand, the hand value is given by the highest card value
	//to the power of 5 + the next highest card value to the power of 4.. etc.
	//Since the hand is ordered, this will always yield ordered results. In hand with groups,
	//the same approach is taken *HOWEVER* since the groups that are valued highest may NOT be
	//of higher value than the 'kicker' cards, it is necessary to multiply some of these results
	//by a scalar: for example, in a two pair hand, if the second pair is a pair of 3's,
	//3^2 = 9 with a 2 kicker (9+2 = 11), a hand with a second pair of 2's (2^2 = 4) with an 
	//ace kick (4+14 = 18) yields a higher score. Multiplying the pair value avoids this problem.
	public int getGameValue(){
		for(int i=0; i<HAND_SIZE; i++){
			if(cards[i]==null) return 0;
		}
		int gameVal = 0;
		if(isHighHand()){//HIGH HAND
			//Add default hand value
			gameVal += HIGH_HAND_DEFAULT;
			for(int i=0; i<HAND_SIZE; i++){
				//Increase hand value by card value to the power of hand size (5) - card position
				gameVal += Math.pow(cards[i].getGameValue(), HAND_SIZE-i);
			}
		} else if(isOnePair()){//ONE PAIR
			gameVal += ONE_PAIR_DEFAULT;
			int singleCount = 0;
			for(int i=0; i<HAND_SIZE; i++){
				//If card is part of pair; otherwise single card
				if((i<HAND_SIZE-1) && cards[i].getGameValue()==cards[i+1].getGameValue()){
					//Increase hand value by pair (value ^ 4) * 175
					gameVal += Math.pow(cards[i].getGameValue(), HAND_SIZE-1) * 175;
					//Skip second card in pair
					i++;
				} else {
					//Increase hand value by the card value to a lower power (3, 2 or 1) 
					gameVal += Math.pow(cards[i].getGameValue(), (HAND_SIZE-2)-singleCount);
					//Increase variable to decrease power for next single card
					singleCount += 1;
				}
			}
		} else if(isTwoPair()){//TWO PAIR
			gameVal += TWO_PAIR_DEFAULT;
			int pairCount = 0;
			for(int i=0; i<HAND_SIZE; i++){
				//If card is part of pair; otherwise single card
				if((i<HAND_SIZE-1) && cards[i].getGameValue()==cards[i+1].getGameValue()){
					//Increase hand value by 2*(pair value to the power of 3 or 2 (first is higher))
					gameVal += Math.pow(cards[i].getGameValue(), (HAND_SIZE-2)-pairCount) * 2;
					//Increase variable to decrease power for second pair
					pairCount += 1;
					//Skip second card in pair
					i++;
				} else {
					//Increase hand value by the card value (single card)
					gameVal += cards[i].getGameValue();
				}
			}
		} else if(isThreeOfAKind()){//THREE OF A KIND
			gameVal += THREE_OF_A_KIND_DEFAULT;
			int singleCount = 0;
			for(int i=0; i<HAND_SIZE; i++){
				//If card is part of three of a kind; otherwise single card
				if((i<HAND_SIZE-2) && cards[i].getGameValue()==cards[i+2].getGameValue()){
					//Increase hand value by pair (value ^ 3) * 25
					gameVal += Math.pow(cards[i].getGameValue(), HAND_SIZE-2) * 25;
					//Skip next two cards
					i = i+2;
				} else {
					//Increase hand value by the card value to a lower power (2 or 1) 
					gameVal += Math.pow(cards[i].getGameValue(), (HAND_SIZE-3)-singleCount);
					//Increase variable to decrease power for next single card
					singleCount += 1;
				}
			}
		} else if(isStraight()){//STRAIGHT
			//If low ace straight, increase game value by 5, otherwise increase by first card's value
			if(cards[0].getFaceValue()==1 && cards[HAND_SIZE-1].getFaceValue()==2){
				gameVal += STRAIGHT_DEFAULT + 5;
			} else {
				gameVal += STRAIGHT_DEFAULT + cards[0].getGameValue();
			}
		} else if(isFlush()){
			//Add flush score and high hand score
			gameVal += FLUSH_DEFAULT;
			for(int i=0; i<HAND_SIZE; i++){
				gameVal += Math.pow(cards[i].getGameValue(), HAND_SIZE-i);
			}
		} else if(isFullHouse()){//FULL HOUSE
			gameVal += FULL_HOUSE_DEFAULT;
			//If card is part of three of a kind; otherwise pair
			if(cards[0].getGameValue()==cards[2].getGameValue()){
				//Increase hand value by three of a kind card (value ^ 2) * 10
				gameVal += Math.pow(cards[0].getGameValue(), 2) * 10;
				//Add pair value
				gameVal += cards[HAND_SIZE-1].getGameValue();
			} else {
				//Increase hand value by the pair card value
				gameVal += cards[0].getGameValue();
				//Add three of a kind (value ^ 2) * 10
				gameVal += Math.pow(cards[HAND_SIZE-1].getGameValue(), 2) * 10;
			}
		} else if(isFourOfAKind()){//FOUR OF A KIND
			gameVal += FOUR_OF_A_KIND_DEFAULT;
			//If card is part of four of a kind; otherwise single card
			if(cards[0].getGameValue()==cards[2].getGameValue()){
				//Increase hand value by (four of a kind card value ^ 2) * 4
				gameVal += Math.pow(cards[0].getGameValue(), 2) * 4;
				//Add pair value
				gameVal += cards[HAND_SIZE-1].getGameValue();
			} else {
				//Increase hand value by the pair card value
				gameVal += cards[0].getGameValue();
				//Add (four of a kind value ^ 2) * 4
				gameVal += Math.pow(cards[HAND_SIZE-1].getGameValue(), 2) * 4;
			}
		} else if(isStraightFlush()){//STRAIGHT FLUSH
			//If low ace straight, increase game value by 5, otherwise increase by first card's value
			if(cards[0].getFaceValue()==1 && cards[HAND_SIZE-1].getFaceValue()==2){
				gameVal += STRAIGHT_FLUSH_DEFAULT + 5;
			} else {
				gameVal += STRAIGHT_FLUSH_DEFAULT + cards[0].getGameValue();
			}
		} else if(isRoyalFlush()){//ROYAL FLUSH
			gameVal += ROYAL_FLUSH_DEFAULT;
		}
		return gameVal;
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
	
	//**TESTING METHOD FOR COMPARING HAND VALUES**
	//Included only so that hand values can be compared between consistent hands as
	//requested in assignment 4 document.
	//Sets cards and sorts a hand, as if it was a new hand, and returns a string of the hand type.
	private String setHand(PlayingCard a, PlayingCard b, PlayingCard c, PlayingCard d, PlayingCard e){
		//Set cards
		cards[0] = a;
		cards[1] = b;
		cards[2] = c;
		cards[3] = d;
		cards[4] = e;
		sort();
		//Hand type component
		String str = "";
		if(isHighHand()){
			str = "High Hand";
		} else if(isOnePair()){
			str = "One Pair";
		} else if(isTwoPair()){
			str = "Two Pair";
		} else if(isThreeOfAKind()){
			str = "Three of a Kind";
		} else if(isStraight()){
			str = "Straight";
		} else if(isFlush()){
			str = "Flush";
		} else if(isFullHouse()){
			str = "Full House";
		} else if(isFourOfAKind()){
			str = "Four of a Kind";
		} else if(isStraightFlush()){
			str = "Straight Flush";
		} else if(isRoyalFlush()){
			str = "Royal Flush";
		}
		return str;
	}
	
	//main method for testing
	//**Uses the setHand method for creating specific hands for testing purposes only.
	//Discerns hand types, compares different hand values and prints the results.
	public static void main(String[] args){
		//Create deck
		DeckOfCards deck = new DeckOfCards();
		
		//Cards used for testing purposes
		//Aces
		PlayingCard aceD = new PlayingCard("A", PlayingCard.DIAMONDS, 1, 14);
		PlayingCard aceH = new PlayingCard("A", PlayingCard.HEARTS, 1, 14);
		PlayingCard aceC = new PlayingCard("A", PlayingCard.CLUBS, 1, 14);
		PlayingCard aceS = new PlayingCard("A", PlayingCard.SPADES, 1, 14);
		//High cards
		PlayingCard kingH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
		PlayingCard queenH = new PlayingCard("Q", PlayingCard.HEARTS, 12, 12);
		PlayingCard jackH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);
		PlayingCard tenH = new PlayingCard("10", PlayingCard.HEARTS, 10, 10);
		PlayingCard tenC = new PlayingCard("10", PlayingCard.CLUBS, 10, 10);
		PlayingCard tenS = new PlayingCard("10", PlayingCard.SPADES, 10, 10);
		PlayingCard tenD = new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10);
		//Low cards
		PlayingCard nineH = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);
		PlayingCard sixC = new PlayingCard("6", PlayingCard.CLUBS, 6, 6);
		PlayingCard fiveS = new PlayingCard("5", PlayingCard.SPADES, 5, 5);
		PlayingCard fiveC = new PlayingCard("5", PlayingCard.CLUBS, 5, 5);
		PlayingCard fourS = new PlayingCard("4", PlayingCard.SPADES, 4, 4);
		PlayingCard fourC = new PlayingCard("4", PlayingCard.CLUBS, 4, 4);
		PlayingCard threeC = new PlayingCard("3", PlayingCard.CLUBS, 3, 3);
		PlayingCard twoC = new PlayingCard("2", PlayingCard.CLUBS, 2, 2);
		PlayingCard twoH = new PlayingCard("2", PlayingCard.HEARTS, 2, 2);
		
		//Create new hands of 5 cards
		HandOfCards handA = new HandOfCards(deck);
		HandOfCards handB = new HandOfCards(deck);
		HandOfCards handC = new HandOfCards(deck);
		HandOfCards handD = new HandOfCards(deck);
		
		//Set hands to specific hands, and compare game values
		String output = "";
		System.out.println("   HAND                   \tVALUE  \tHAND_TYPE\n");
		
		//Comparing high hands:
		output = handA.setHand(fourS, twoC, tenH, aceC, fiveS);
		System.out.println("A: " + handA + "  \t" + handA.getGameValue() + "\t" + output);
		output = handB.setHand(aceS, jackH, queenH, threeC, fourC);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output);
		
		//Comparing one pair:
		output = handA.setHand(fourS, jackH, queenH, threeC, fourC);
		System.out.println("\nA: " + handA + "  \t" + handA.getGameValue() + "\t" + output);
		output = handB.setHand(aceS, twoC, tenH, aceC, fiveS);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output);
		output = handC.setHand(aceS, jackH, queenH, aceH, fourC);
		System.out.println("C: " + handC + "  \t" + handC.getGameValue() + "\t" + output);
		
		//Comparing two pair:
		output = handA.setHand(tenH, tenC, fourC, fourS, threeC);
		System.out.println("\nA: " + handA + "  \t" + handA.getGameValue() + "\t" + output);
		output = handB.setHand(aceS, aceC, fourC, fourS, twoC);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output);
		output = handC.setHand(aceS, aceC, fourC, fourS, queenH);
		System.out.println("C: " + handC + "  \t" + handC.getGameValue() + "\t" + output);
		output = handD.setHand(aceS, aceC, tenH, tenC, fourC);
		System.out.println("D: " + handD + "  \t" + handD.getGameValue() + "\t" + output);
		
		//Comparing three of a kind:
		output = handA.setHand(tenS, tenH, tenC, fourS, twoC);
		System.out.println("\nA: " + handA + "  \t" + handA.getGameValue() + "\t" + output);
		output = handB.setHand(aceS, aceC, aceH, fourS, twoC);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output);
		output = handC.setHand(aceS, aceC, aceH, fourS, threeC);
		System.out.println("C: " + handC + "  \t" + handC.getGameValue() + "\t" + output);
		output = handD.setHand(aceS, aceC, aceH, fiveS, twoC);
		System.out.println("D: " + handD + "  \t" + handD.getGameValue() + "\t" + output);
		
		//Comparing straight:
		output = handA.setHand(aceC, twoC, threeC, fourC, fiveS);
		System.out.println("\nA: " + handA + "  \t" + handA.getGameValue() + "\t" + output);
		output = handB.setHand(aceC, kingH, queenH, jackH, tenH);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output);
		
		//Comparing flush:
		output = handA.setHand(sixC, twoC, threeC, fourC, tenC);
		System.out.println("\nA: " + handA + "  \t" + handA.getGameValue() + "\t" + output);
		output = handB.setHand(aceH, kingH, queenH, jackH, twoH);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output);
		
		//Comparing full house:
		output = handA.setHand(tenS, tenH, tenC, aceS, aceC);
		System.out.println("\nA: " + handA + "  \t" + handA.getGameValue() + "\t" + output);
		output = handB.setHand(aceS, aceC, aceH, fourS, fourC);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output);
		output = handC.setHand(aceS, aceC, aceH, tenC, tenS);
		System.out.println("C: " + handC + "  \t" + handC.getGameValue() + "\t" + output);
		output = handD.setHand(aceS, aceC, aceH, tenC, tenS);
		
		//Comparing four of a kind:
		output = handA.setHand(tenS, tenH, tenC, tenD, twoC);
		System.out.println("\nA: " + handA + "  \t" + handA.getGameValue() + "\t" + output);
		output = handB.setHand(tenS, tenH, tenC, tenD, aceC);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output);
		output = handC.setHand(aceS, aceC, aceH, aceD, twoC);
		System.out.println("C: " + handC + "  \t" + handC.getGameValue() + "\t" + output);
		output = handD.setHand(aceS, aceC, aceH, aceD, tenC);
		System.out.println("D: " + handD + "  \t" + handD.getGameValue() + "\t" + output);
		
		//Comparing straight flush:
		output = handA.setHand(aceC, twoC, threeC, fourC, fiveC);
		System.out.println("\nA: " + handA + "  \t" + handA.getGameValue() + "\t" + output);
		output = handB.setHand(kingH, queenH, jackH, tenH, nineH);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output);
		output = handC.setHand(aceH, kingH, queenH, jackH, tenH);
		System.out.println("C: " + handC + "  \t" + handC.getGameValue() + "\t" + output);
	}
}
