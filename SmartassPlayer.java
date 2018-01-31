package smartasscards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class SmartassPlayer {
	/*
	 * Future addition: solodeck.  Every player brings their own white deck.
	 */
	private String name;
	private SmartassGame game;
	private ArrayList<SmartassCard> hand;
	private SmartassDeck deck;
	private String black_white = "";
	private List<String> winners = new ArrayList<>();
	
	SmartassPlayer(String name, SmartassGame game, SmartassDeck deck){
		this.game = game;
		this.name = name;
		hand = new ArrayList<>();
		this.deck = deck;
	}
	
	void draw(){
		SmartassDeck deck = game.getDeck();
		if(deck.isEmpty())
			deck.shuffle();
		hand.add( deck.drawWhiteCard() );
	}
	
	void draw(String cardString){
		hand.add( deck.drawWhiteCard(cardString) );
	}
	
	void drawTen(){
		for(int i=1; i<=10; i++)
			draw();
	}
	
	void discard(SmartassCard card){
		hand.remove(card);
		deck.discardWhiteCard(card);
	}
	
	void discardHand(){
		Iterator<SmartassCard> itr = hand.iterator();
		while( itr.hasNext() ){
			SmartassCard card = (SmartassCard) itr.next();
			itr.remove();
			deck.discardWhiteCard(card);
		}
		winners.clear();
	}
	
	void play(ArrayList<SmartassCard> whiteCards){
		for(SmartassCard card : whiteCards){
			discard(card);
			draw();
		}
		black_white = game.combine(whiteCards);
	}	
	
	void play(int[] card_indexes){
		ArrayList<SmartassCard> play_cards = new ArrayList<>();
		for( int card_index : card_indexes )
			play_cards.add( hand.get(card_index) );
		play(play_cards);
		black_white = game.combine(play_cards);
	}
	
	void playRandom(){
		Random rng = game.getRNG();
		int blank_count = game.getActiveBlackCard().getBlankIndexes().size();
		ArrayList<SmartassCard> play_cards = new ArrayList<>();
		int random_cards_selected = 0;
		while(random_cards_selected < blank_count){
			int random_card_index = rng.nextInt(hand.size());
			SmartassCard random_card = hand.get(random_card_index);
			if( !play_cards.contains(random_card) ){
				play_cards.add(random_card);
				random_cards_selected++;
			}
		}
		for(SmartassCard card : play_cards){
			discard(card);
			draw();
		}
		black_white = game.combine(play_cards);
	}
	
	String getName(){
		return name;
	}
	
	ArrayList<SmartassCard> getHand(){
		return hand;
	}
	
	String getBlackWhite(){
		return black_white;
	}
	
	void setBlackWhite(String blackWhite){
		black_white = blackWhite;
	}
	
	boolean hasCard(String card){
		for( SmartassCard c : hand )
			if( c.getText().equals(card) )
				return true;
		return false;
	}
	
}
