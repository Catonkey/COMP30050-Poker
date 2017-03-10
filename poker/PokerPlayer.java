/*	Dara Callinan
 * 	14500717
 * 	Assignment 6 - PokerPlayer class
 */

package poker;

import java.util.concurrent.ThreadLocalRandom;

//PokerPlayer class - represents a player in a poker game, with a hand of cards. Contains method for 
//discarding cards to improve the player's hand (uses a helper function randomEvent() which, combined
//with discard probabilities in HandOfCards class creates weighted random discard patterns). Also
//contains a main method for testing.
public class PokerPlayer {
	
	private static final int MAX_DISCARD = 3;
	private static final int MINIMUM_DISCARD_CHANCE = 0;
	private static final int MAXIMUM_DISCARD_CHANCE = 100;

	private HandOfCards hand;
	
	//Public constructor
	public PokerPlayer(DeckOfCards gameDeck){
		hand = new HandOfCards(gameDeck);
	}
	
	//Returns the random integer responsible for discarding cards (can be 0-99 incl.)
	private int randomEvent(){
		return ThreadLocalRandom.current().nextInt(MINIMUM_DISCARD_CHANCE, MAXIMUM_DISCARD_CHANCE);
	}
	
	//Discards cards from player hand based on a random event with less valuable cards weighted
	//towards being discarded.
	public synchronized int discard(){
		//Amount of cards to be discarded
		int discarded = 0;
		
		//randomEvent is applied to the probability of all cards (it would not make
		//sense for the same player to discard a low% and high% probability card in the
		//same discard phase.)
		int random = randomEvent();
		
		//Discard cards, but no more than MAX_DISCARD (3)
		for(int i=0; i<HandOfCards.HAND_SIZE && discarded<MAX_DISCARD; i++){
			int discardProb = hand.getDiscardProbability(i);
			//If discard probability in range, discard if random < probability
			if(discardProb > 0 && discardProb < 100){
				if(random<discardProb){
					hand.discard(i);
					discarded++;
				}
			//Else if discard probability = 100, discard. Otherwise do not discard.
			} else if(discardProb >= 100){
				hand.discard(i);
				discarded++;
			}
		}
		
		//Sort new hand
		hand.sort();
		
		//Return amount of cards discarded
		return discarded;
	}
	
	
	//main method for testing
	public static void main(String[] args){
		
		//One discard (likely to improve loose cards, attempt to make a straight etc.)
		DeckOfCards deck = new DeckOfCards();
		PokerPlayer player = new PokerPlayer(deck);
		System.out.println("Before discard:" + player.hand);
		player.discard();
		System.out.println("After discard: " + player.hand);
		
		//Multiple discards
		//*Note: this does not mean the end result will be a royal flush, as hands such as a full house
		//or straight are not worth discarding any cards; especially if other players are in the game 
		//(as indicated by poker theory). Likely ending scenarios are: full house, four of a kind, flush,
		//straight, straight flush
		deck = new DeckOfCards();
		player = new PokerPlayer(deck);
		System.out.println("\nBefore discard:" + player.hand);
		for(int i=0; i<10000; i++){
			player.discard();
		}
		System.out.println("After discard: " + player.hand);
		
	}
}
