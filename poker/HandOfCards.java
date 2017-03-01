/*	Dara Callinan
 * 	14500717
 * 	Assignment 3 - HandOfCards class & Assignment 4 - getGameValue() method & Assignment 5 - discard prob
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
	static private final int DECK_SIZE = 52;
	static private final int CARD_TYPES = 13;
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
	
	//Determine whether the hand is a busted straight (1 card needed to make a straight)
	public boolean isBustedStraight(){
		//Return false for any hand other than high hand, one pair or flush. - All other hands either
		//contain a straight already, or would require more than 1 card to make a straight
		if(isHighHand() || isOnePair() || isFlush()){
			//Get lowest card
			int min = cards[HAND_SIZE-1].getFaceValue();
			int prev = min;
			//Amount of 'straight' cards - 1 (including initial lowest card)
			int straightCards = 1;
			//For all other cards, if card is less than lowest card + 5, increase straightCards
			//Check for cards of same value (do not include pairs as 2 cards)
			for(int i=HAND_SIZE-2; i>=0; i--){
				if(cards[i].getFaceValue()!=prev && cards[i].getFaceValue() < min+5){
					straightCards ++;
					prev = cards[i].getFaceValue();
				}
			}
			//Check if ace can be low
			if(cards[0].getFaceValue()==1 && min ==2) straightCards++;
			//Return true if 4 of 5 cards are straight cards
			if(straightCards == 4) return true;
			
			//Repeat, but with second lowest card.
			min = cards[HAND_SIZE-2].getFaceValue();
			prev = min;
			straightCards = 1;
			//For all higher cards, if card is less than second lowest card +5, increase straightCards 
			//Check for cards of same value (do not include pairs as 2 cards)
			for(int i=HAND_SIZE-3; i>=0; i--){
				if(cards[i].getFaceValue()!=prev && cards[i].getFaceValue() < min+5){
					straightCards ++;
					prev = cards[i].getFaceValue();
				}
			}
			//*No need to check ace a second time.
			//Return true if 4 of 5 cards are straight cards, otherwise return false.
			if(straightCards == 4){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	//Determine whether the hand is a busted flush (1 card needed to make a flush)
	public boolean isBustedFlush(){
		//Return false for any hand other than high hand, one pair or straight. - All other hands either
		//contain a straight already, or would require more than 1 card to make a straight
		if(isHighHand() || isOnePair() || isStraight()){
			//Get suit of first card
			char suitOne = cards[0].getSuit();
			char suitTwo = '\0';
			//Counters for cards of each suit
			int suitOneAmount = 1;
			int suitTwoAmount = 0;
			
			//For each card, if card suit is suit one or suit two, increase appropriate counter
			//If card suit does not equal suit one, and suit two counter is zero, assign this suit
			//to suit two, and increase counter. If a third suit is discovered, return false.
			for(int i=1; i<HAND_SIZE; i++){
				char cardSuit = cards[i].getSuit();
				//Card suit is the same as suit one
				if(cardSuit==suitOne){
					suitOneAmount += 1;
				//Card suit is the same as suit two
				} else if(suitTwoAmount != 0 && cardSuit==suitTwo){
					suitTwoAmount += 1;
				//Card suit is not the same as suit one, and is the second suit discovered
				} else if(suitTwoAmount == 0){
					suitTwo = cardSuit;
					suitTwoAmount += 1;
				//Card suit is neither suit one nor suit two - return false
				} else {
					return false;
				}
			}
			//If there are exactly 4 cards of suit one or two, return true, otherwise return false
			if(suitOneAmount == 4 || suitTwoAmount == 4){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	//Returns integer value representing score of a high hand
	private int getValueHighHand(){
		//Add default hand value
		int val = HIGH_HAND_DEFAULT;
		for(int i=0; i<HAND_SIZE; i++){
			//Increase hand value by card value to the power of hand size (5) - card position
			val += Math.pow(cards[i].getGameValue(), HAND_SIZE-i);
		}
		return val;
	}
	
	//Returns integer value representing score of a one pair hand
	//Pair value is to the highest power, followed by the highest to lowest single cards.
	//The pair values are multiplied by a scalar (175) to avoid situations where a high
	//single card is ranked higher than a low pair.
	private int getValueOnePair(){
		int val = ONE_PAIR_DEFAULT;
		int singleCount = 0;
		for(int i=0; i<HAND_SIZE; i++){
			//If card is part of pair; otherwise single card
			if((i<HAND_SIZE-1) && cards[i].getGameValue()==cards[i+1].getGameValue()){
				//Increase hand value by pair (value ^ 4) * 175
				val += Math.pow(cards[i].getGameValue(), HAND_SIZE-1) * 175;
				//Skip second card in pair
				i++;
			} else {
				//Increase hand value by the card value to a lower power (3, 2 or 1) 
				val += Math.pow(cards[i].getGameValue(), (HAND_SIZE-2)-singleCount);
				//Increase variable to decrease power for next single card
				singleCount += 1;
			}
		}
		return val;
	}
	
	//Returns integer value representing score of a two pair hand
	//Highest pair value is to the highest power, followed by the lower pair and single card.
	//The pair values are multiplied by a scalar (2) to avoid situations where a high
	//single card is ranked higher than a low pair.
	private int getValueTwoPair(){
		int val = TWO_PAIR_DEFAULT;
		int pairCount = 0;
		for(int i=0; i<HAND_SIZE; i++){
			//If card is part of pair; otherwise single card
			if((i<HAND_SIZE-1) && cards[i].getGameValue()==cards[i+1].getGameValue()){
				//Increase hand value by 2*(pair value to the power of 3 or 2 (first is higher))
				val += Math.pow(cards[i].getGameValue(), (HAND_SIZE-2)-pairCount) * 2;
				//Increase variable to decrease power for second pair
				pairCount += 1;
				//Skip second card in pair
				i++;
			} else {
				//Increase hand value by the card value (single card)
				val += cards[i].getGameValue();
			}
		}
		return val;
	}
	
	//Returns integer value representing score of a three of a kind hand
	//The set (3 cards) is to the highest power, followed by the highest to lowest single cards.
	//The set value is multiplied by a scalar (25) to avoid situations where a high
	//single card is ranked higher than a low set.
	private int getValueThreeOfAKind(){
		int val = THREE_OF_A_KIND_DEFAULT;
		int singleCount = 0;
		for(int i=0; i<HAND_SIZE; i++){
			//If card is part of three of a kind; otherwise single card
			if((i<HAND_SIZE-2) && cards[i].getGameValue()==cards[i+2].getGameValue()){
				//Increase hand value by pair (value ^ 3) * 25
				val += Math.pow(cards[i].getGameValue(), HAND_SIZE-2) * 25;
				//Skip next two cards
				i = i+2;
			} else {
				//Increase hand value by the card value to a lower power (2 or 1) 
				val += Math.pow(cards[i].getGameValue(), (HAND_SIZE-3)-singleCount);
				//Increase variable to decrease power for next single card
				singleCount += 1;
			}
		}
		return val;
	}
	
	//Returns integer value representing score of a straight hand
	//The value of the first (highest) card in the straight influences the hand value,
	//unless it's a low ace straight (always a value of 5 + STRAIGHT_DEFAULT)
	private int getValueStraight(){
		int val = 0;
		//If low ace straight, increase game value by 5, otherwise increase by first card's value
		if(cards[0].getFaceValue()==1 && cards[HAND_SIZE-1].getFaceValue()==2){
			val += STRAIGHT_DEFAULT + 5;
		} else {
			val += STRAIGHT_DEFAULT + cards[0].getGameValue();
		}
		return val;
	}
	
	//Returns integer value representing score of a full house hand
	//The set (3 cards) is to the highest power, followed by the pair.
	//The set value is multiplied by a scalar (4) to avoid situations where a high pair value is
	//ranked more than a low set.
	private int getValueFullHouse(){
		int val = FULL_HOUSE_DEFAULT;
		//If card is part of three of a kind; otherwise pair
		if(cards[0].getGameValue()==cards[2].getGameValue()){
			//Increase hand value by three of a kind card (value ^ 2) * 4
			val += Math.pow(cards[0].getGameValue(), 2) * 4;
			//Add pair value
			val += cards[HAND_SIZE-1].getGameValue();
		} else {
			//Increase hand value by the pair card value
			val += cards[0].getGameValue();
			//Add three of a kind (value ^ 2) * 4
			val += Math.pow(cards[HAND_SIZE-1].getGameValue(), 2) * 4;
		}
		return val;
	}
	
	//Returns integer value representing score of a four of a kind hand
	//The set (4 cards) is to the highest power, followed by the single card.
	//The set value is multiplied by a scalar (4) to avoid situations where a high
	//single card is ranked higher than a low set.
	private int getValueFourOfAKind(){
		int val = FOUR_OF_A_KIND_DEFAULT;
		//If card is part of four of a kind; otherwise single card
		if(cards[0].getGameValue()==cards[3].getGameValue()){
			//Increase hand value by (four of a kind card value ^ 2) * 4
			val += Math.pow(cards[0].getGameValue(), 2) * 4;
			//Add pair value
			val += cards[HAND_SIZE-1].getGameValue();
		} else {
			//Increase hand value by the pair card value
			val += cards[0].getGameValue();
			//Add (four of a kind value ^ 2) * 4
			val += Math.pow(cards[HAND_SIZE-1].getGameValue(), 2) * 4;
		}
		return val;
	}
	
	//Returns an integer value representing the value of the hand when compared with
	//other hand values. Higher is better. Better hand types are valued more, and cards are
	//valued according to rules of poker (single cards will not affect the value of a hand more
	//than a pair, etc. Uses helper functions for specific hand types to calculate values, by 
	//putting valuable groups/pairs of cards to high powers, with less valuable groups and single
	//cards to lower powers. Scalar multiplication is also applied to ensure that the lowest value
	//of each group type is greater than the highest value of smaller groups.
	public int getGameValue(){
		for(int i=0; i<HAND_SIZE; i++){
			if(cards[i]==null) return 0;
		}
		int gameVal = 0;
		if(isHighHand()){//HIGH HAND
			gameVal = getValueHighHand();
		} else if(isOnePair()){//ONE PAIR
			gameVal = getValueOnePair();
		} else if(isTwoPair()){//TWO PAIR
			gameVal = getValueTwoPair();
		} else if(isThreeOfAKind()){//THREE OF A KIND
			gameVal = getValueThreeOfAKind();
		} else if(isStraight()){//STRAIGHT
			gameVal = getValueStraight();
		} else if(isFlush()){//FLUSH
			//Add flush score and high hand score (subtract high_hand_default if non-zero)
			gameVal = FLUSH_DEFAULT + getValueHighHand() - HIGH_HAND_DEFAULT;
		} else if(isFullHouse()){//FULL HOUSE
			gameVal = getValueFullHouse();
		} else if(isFourOfAKind()){//FOUR OF A KIND
			gameVal = getValueFourOfAKind();
		} else if(isStraightFlush()){//STRAIGHT FLUSH
			//Add straight flush score and straight value (subtracting straight_default)
			gameVal = STRAIGHT_FLUSH_DEFAULT + getValueStraight() - STRAIGHT_DEFAULT;
		} else if(isRoyalFlush()){//ROYAL FLUSH
			gameVal = ROYAL_FLUSH_DEFAULT;
		}
		return gameVal;
	}
	
	//Get card to discard in busted straight
	private int getBustedStraightCard(){
		//If not a busted straight, cannot discard any card
		if(!isBustedStraight()){
			return -1;
		}
		
		int max, ind;
		//Get the max card in the hand - assume to be highest card in straight
		//If straight is an ace low straight, take second highest (after ace) and
		//start straight from there.
		if(cards[0].getFaceValue() == 1 && cards[HAND_SIZE-1].getFaceValue() == 2){
			max = cards[1].getFaceValue();
			ind = 2;
			//In ace low straight, ensure the highest card is within range of ace
			//Otherwise discard that card.
			if(max > cards[0].getFaceValue()+4){
				return 1;
			}
		} else {
			max = cards[0].getGameValue();
			ind = 1;
		}
		int prev = max;
		int discard = -1;
		//Check if each card is compatible with straight
		for(int i=ind; i<HAND_SIZE; i++){
			//Continue if card is compatible
			if(cards[i].getFaceValue()!=prev && cards[i].getFaceValue() > max-5){
				prev = cards[i].getFaceValue();
			} else {
				//If card does not make straight, and there is already a discard value,
				//discard max card (incompatible with at least 2 cards). Otherwise set 
				//discard value to current card.
				if(discard != -1){
					return 0;
				} else {
					discard = i;
				}
				
			}
		}
		return discard;
	}

	//Get card to discard in busted flush
	private int getBustedFlushCard(){
		//If not a busted straight, cannot discard any card
		if(!isBustedFlush()){
			return -1;
		}
		char suit = cards[0].getSuit();
		int unmatched = 0;
		int discard = -1;
		for(int i=1; i<HAND_SIZE; i++){
			if(cards[i].getSuit() != suit){
				discard = i;
				unmatched++;
			}
		}
		if(unmatched>1){
			return 0;
		} else {
			return discard;
		}
	}
	
	
	//Return probability of drawing a matching card
	private int drawMatchProbability(int amount, int matched){
		//Amount of cards presumed to be available that would result in a match with current cards:
		int base = 4 - matched;
		base += 3 * (HAND_SIZE - (amount + matched));
		//One card to be replaced:
		if(amount == 1){
			double toDraw = DECK_SIZE - 5;
			double p = 0;
			p = (toDraw-base)/toDraw;
			return (int) ((1 - p) * 100);
		//Two cards to be replaced:
		} else if(amount == 2){
			double toDraw = DECK_SIZE - 5;
			double p = 0;
			p = (toDraw-base)/toDraw;
			toDraw--;
			p *= (toDraw-(base+3))/toDraw;
			return (int) ((1 - p) * 100);
		//Three cards to be replaced:
		} else if(amount == 3){
			double toDraw = DECK_SIZE - 5;
			double p = 0;
			p = (toDraw-base)/toDraw;
			toDraw--;
			p *= (toDraw-(base+3))/toDraw;
			toDraw--;
			p *= (toDraw-(base+6))/toDraw;
			return (int) ((1 - p) * 100);
		} else {
			return 0;
		}
	}
	
	//Return the discard probability for a card in a high hand
	private int getDiscardProbHighHand(int cardPos){
		boolean potentialFlush = isBustedFlush();
		boolean potentialStraight = isBustedStraight();
		int cardGameVal = cards[cardPos].getGameValue();
		//Normal High Hand
		if(!potentialFlush && !potentialStraight){
			//Discard most cards, but keep valuable cards (like ace, king)
			return (int) 100 - ((cardGameVal * cardGameVal) / 2);
		//Busted flush
		} else if(potentialFlush){
			char suit = cards[cardPos].getSuit();
			//Check 2 cards before this card
			if(cardPos > 1){
				//If this card matches another cards suit, prioritize keeping card, otherwise discard
				if(suit == cards[0].getSuit() || suit == cards[1].getSuit()){
					return 20 - cardGameVal;
				} else {
					return 100;
				}
			//Check 2 cards after this card
			} else {
				if(suit == cards[HAND_SIZE-1].getSuit() || suit == cards[HAND_SIZE-2].getSuit()){
					return 20 - cardGameVal;
				} else {
					return 100;
				}
			}
		//Busted straight
		} else {
			if(cardPos == getBustedStraightCard()){
				return 100;
			} else {
				return 20 - cardGameVal;
			}
		}
	}
	
	
	//Return the discard probability for a card in a one pair hand
	public int getDiscardProbOnePair(int cardPos){
		boolean potentialFlush = isBustedFlush();
		boolean potentialStraight = isBustedStraight();
		int cardGameVal = cards[cardPos].getGameValue();
		//Normal hand
		if(!potentialFlush && !potentialStraight){
			//Prioritize discarding any card that is not in the pair
			if(cardPos+1 < HAND_SIZE && cards[cardPos+1].getGameValue() == cardGameVal){
				return 0;
			} else if(cardPos-1 >= 0 && cards[cardPos-1].getGameValue() == cardGameVal){
				return 0;
			} else {
				//Probability of drawing 3 new cards and getting a pair/set:
				int p = drawMatchProbability(3, 2);
				//Add base probability and weight card value
				return p + 33 + (14 - cardGameVal);
			}
		//Busted flush
		} else if(potentialFlush){
			if(cardPos == getBustedFlushCard()){
				return 100;
			} else {
				return 20 - cardGameVal;
			}
		//Busted straight
		} else {
			if(cardPos == getBustedStraightCard()){
				return 100;
			} else {
				return 20 - cardGameVal;
			}
		}
	}
	
	//Return the discard probability for a card in a two pair hand
	public int getDiscardProbTwoPair(int cardPos){
		int cardGameVal = cards[cardPos].getGameValue();
		//Prioritize discarding any card that is not in the pair
		if(cardPos+1 < HAND_SIZE && cards[cardPos+1].getGameValue() == cardGameVal){
			return 0;
		} else if(cardPos-1 >= 0 && cards[cardPos-1].getGameValue() == cardGameVal){
			return 0;
		} else {
			//probability of not drawing matching card:
			double toDraw = DECK_SIZE - 5;
			double p = 0;
			p = (toDraw-4)/toDraw;
			//probability of drawing matching card (improving hand type)
			p = (1 - p) * 100;
			//weight high cards and add base discard probability
			return (int) p + 33 + (14 - cardGameVal);
		}
	}
	
	//Return the discard probability for a card in a three of a kind hand
	public int getDiscardProbThreeOfAKind(int cardPos){
		int cardGameVal = cards[cardPos].getGameValue();
		//Keep all cards in the set
		if(cardPos+2 < HAND_SIZE && cards[cardPos+2].getGameValue() == cardGameVal){
			return 0;
		} else if(cardPos-2 >= 0 && cards[cardPos-2].getGameValue() == cardGameVal){
			return 0;
		} else if(	cardPos-1 >= 0 && cardPos+1 < HAND_SIZE &&
					cards[cardPos-1].getGameValue() == cards[cardPos+1].getGameValue()){
			return 0;
		} else {
			//probability of drawing matching card by discarding 2 (improving hand type)
			int p = drawMatchProbability(2, 3);
			//weight high cards and add base discard probability
			return p + 50 + (14 - cardGameVal);
		}
	}
	
	//Return the discard probability for a card in a straight (0 unless flush can be made)
	public int getDiscardProbStraight(int cardPos){
		if(isBustedFlush() && cardPos == getBustedFlushCard()){
			//Return probability of making the flush (about 20%)
			double p = (CARD_TYPES-4)/(DECK_SIZE-5);
			return (int) p;
		} else {
			return 0;
		}
	}
	
	//Return the discard probability for a card in a flush
	public int getDiscardProbFlush(int cardPos){
		if(isBustedStraight() && cardPos == getBustedStraightCard()){
			//Return probability of making a straight flush (very low)
			double p = 1/(DECK_SIZE-5);
			return (int) p;
		} else {
			return 0;
		}
	}
	
	//Return the discard probability for a card in a four of a kind hand
	public int getDiscardProbFourOfAKind(int cardPos){
		int cardGameVal = cards[cardPos].getGameValue();
		//Keep all cards in the four of a kind
		if(cardPos+1 < HAND_SIZE && cards[cardPos+1].getGameValue() == cardGameVal){
			return 0;
		} else if(cardPos-1 >= 0 && cards[cardPos-1].getGameValue() == cardGameVal){
			return 0;
		} else {
			//discard card based on value:
			return (int) 100 - ((cardGameVal * cardGameVal) / 2) - 2;
		}
	}
	
	//Returns integer representing whether the card should be discarded
	public int getDiscardProbability(int cardPosition){
		//Return 0 if cardPosition outside range/card is null
		if(cardPosition < 0 || cardPosition > HAND_SIZE || cards[cardPosition] == null){
			return 0;
		}
		int discard = 0;
		if(isHighHand()){
			discard = getDiscardProbHighHand(cardPosition);
		} else if(isOnePair()){
			discard = getDiscardProbOnePair(cardPosition);
		} else if(isTwoPair()){
			discard = getDiscardProbTwoPair(cardPosition);
		} else if(isThreeOfAKind()){
			discard = getDiscardProbThreeOfAKind(cardPosition);
		} else if(isStraight()){
			discard = getDiscardProbStraight(cardPosition);
		} else if(isFlush()){
			discard = getDiscardProbFlush(cardPosition);
		} else if(isFullHouse()){
			return 0;
		} else if(isFourOfAKind()){
			discard = getDiscardProbFourOfAKind(cardPosition);;
		} else if(isStraightFlush()){
			return 0;
		} else {
			return 0;
		}
		if(discard>100){
			discard = 100;
		} else if(discard<0){
			discard = 0;
		}
		return discard;
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
		str += "\ndis%:";
		for(int i=0; i<HAND_SIZE; i++){
			str += getDiscardProbability(i) + "  ";
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
		output = handB.setHand(aceS, jackH, queenH, aceH, fourC);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output);
		
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
		
		//Testing busted straight:
		output = handA.setHand(threeC, queenH, fiveC, fourC, fiveS);
		System.out.println("\nA: " + handA + "  \t" + handA.getGameValue() + "\t" + output + "\t\tBusted Straight:" + handA.isBustedStraight());
		output = handB.setHand(aceC, kingH, queenH, jackH, sixC);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output + "\t\tBusted Straight:"  + handB.isBustedStraight());
		
		//Testing busted flush:
		output = handA.setHand(aceD, twoC, threeC, fourS, sixC);
		System.out.println("\nA: " + handA + "  \t" + handA.getGameValue() + "\t" + output + "\t\tBusted Flush:" + handA.isBustedFlush());
		output = handB.setHand(aceH, kingH, queenH, jackH, sixC);
		System.out.println("B: " + handB + "  \t" + handB.getGameValue() + "\t" + output + "\t\tBusted Flush:"  + handB.isBustedFlush());
	}
}
