package smartasscards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmartassDeck {
	private ArrayList<SmartassCard> blackDeck = new ArrayList<>();
	private ArrayList<SmartassCard> whiteDeck = new ArrayList<>();
	private ArrayList<SmartassCard> blackDead = new ArrayList<>();
	private ArrayList<SmartassCard> whiteDead = new ArrayList<>();
	private Random rng = new Random();
	private int import_count = 0;
	private int fail_count = 0;
	private static final int MIN_CARD_LENGTH = 2;
	private static final int MAX_CARD_LENGTH = 200;
	private static final int MIN_BLACK_DECK_SIZE = 50;
	private static final int MIN_WHITE_DECK_SIZE = 150;
	private static final int MAX_DECK_SIZE = 500;
	
	SmartassDeck(List<String> deckLines){
		importDeck(deckLines);
	}

	private void importDeck(List<String> deckLines){
		SmartassCard card;
		String command = "";
		String color = null;
		for( String line : deckLines ){
			line = line.trim();
			if( isCommand(line) ){
				command = SmartassSteem.getCommand(line);
			}
			if( line.length() >= MIN_CARD_LENGTH && line.length() <= MAX_CARD_LENGTH 
					&& (command.equals("#blackcards") || command.equals("#whitecards")) ){
				if( command.equals("#blackcards") ){
					color = "black";
				}
				else if( command.equals("#whitecards") ){
					color = "white";
				}
				if( !isCommand(line) ){
					if(line.length() > 0 && line.substring(0, 1).equals("*") ){
						// "*" is an escape character for the # command in text files
						line = line.substring(1);
					}
					card = new SmartassCard(color, line);
					add(card);
					import_count++;
				}
			}
			else if ( line.length() != 0
					&& (command.equals("#blackcards") || command.equals("#whitecards")) 
					&& (line.length() < MIN_CARD_LENGTH || line.length() > MAX_CARD_LENGTH)){
				fail_count++;
			}
		}
		if( blackDeck.size() < MIN_BLACK_DECK_SIZE )
			throw new SmartassFailException("Black Deck too small: " + blackDeck.size());
		if( whiteDeck.size() < MIN_WHITE_DECK_SIZE )
			throw new SmartassFailException("White Deck too small: " + whiteDeck.size());
		if( whiteDeck.size() + blackDeck.size() > MAX_DECK_SIZE )
			throw new SmartassFailException("Deck too big: " + (whiteDeck.size()+blackDeck.size()) );
		
	}
	
	private boolean isCommand(String line){
		if( line.length() > 0 && line.substring(0, 1).equals("#") )
			return true;
		return false;
	}
	
	private void add(SmartassCard card){
		if( card.getColor().equals("white") )
			whiteDeck.add(card);
		else if ( card.getColor().equals("black") )
			blackDeck.add(card);	
		else throw new SmartassFailException("card color is: " + card.getColor());
	}
	
	SmartassCard drawBlackCard(){
		if(blackDeck.isEmpty())
			shuffle();
		int r = rng.nextInt( blackDeck.size() );
		return blackDeck.remove(r);
	}
	
	SmartassCard drawBlackCard(String black_card){
		black_card = black_card.trim();
		SmartassCard match = null;
		for( SmartassCard card : blackDeck ){
			if( card.getBlankText().equals(black_card) ){
				match = card;
			}
		}
		blackDeck.remove(match);
		if(match == null)
			throw new SmartassFailException("Nonexistent card force drawn: " + black_card);
		return match;
	}
	
	SmartassCard drawWhiteCard(){
		if(whiteDeck.isEmpty())
			shuffle();
		int r = rng.nextInt( whiteDeck.size() );
		return whiteDeck.remove(r);
	}
	
	SmartassCard drawWhiteCard(String white_card){
		white_card = white_card.trim();
		SmartassCard match = null;
		for( SmartassCard card : whiteDeck ){
			if( card.getText().equals(white_card) ){
				match = card;
			}
		}
		whiteDeck.remove(match);
		if(match == null)
			throw new SmartassFailException("Non-existent card force drawn: " + white_card);
		return match;
	}
	
	void discardBlackCard(SmartassCard card){
		blackDead.add(card);
	}
	
	void discardWhiteCard(SmartassCard card){
		whiteDead.add(card);
	}
	
	void shuffle(){
		while( !blackDead.isEmpty() )
			blackDeck.add( blackDead.remove(0) );
		while( !whiteDead.isEmpty() )
			whiteDeck.add( whiteDead.remove(0) );
	}

	boolean isEmpty(){
		if( blackDeck.isEmpty() || whiteDeck.isEmpty() )
			return true;
		return false;
	}
	
	ArrayList<SmartassCard> getBlackDeck(){
		return blackDeck;
	}
	
	ArrayList<SmartassCard> getWhiteDeck(){
		return whiteDeck;
	}
	
	ArrayList<SmartassCard> getBlackDead(){
		return blackDead;
	}
	
	ArrayList<SmartassCard> getWhiteDead(){
		return whiteDead;
	}
	
	int size(){
		return blackDeck.size() + whiteDeck.size() +
				blackDead.size() + whiteDead.size();
	}
	
	int getBlackCardCount(){
		return blackDeck.size() + blackDead.size();
	}
	
	int getWhiteCardCount(){
		return whiteDeck.size() + whiteDead.size();
	}
	
	int getBlackDeadCount(){
		return blackDead.size();
	}
	
	int getWhiteDeadCount(){
		return whiteDead.size();
	}
	
	int getImportCount(){
		return import_count;
	}
	
	int getFailCount(){
		return fail_count;
	}
	
	
	
	
	
	
}



/* 




	
	private void remove(SmartassCard card){
		if( card.getColor().equals("white") )
			whiteDeck.remove(card);
		else if ( card.getColor().equals("black") )
			blackDeck.remove(card);
		else throw new SmartassFailException("card color is: " + card.getColor());
	}




//*/
