package smartasscards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SmartassGame {
	private SmartassDeck deck;
	private ArrayList<SmartassPlayer> players;
	private String mode = null;	
	private Random rng;
	private int czar;		// 'dealer' position
	private SmartassCard activeBlackCard = null;
	private int round = 1;
	private int id = -1;
	private String GM = "edicted";////////////////
	private int blank_count;
	
	SmartassGame(List<String> gameLines){
		players = new ArrayList<>();
		deck = new SmartassDeck(gameLines);
		importGame(gameLines);
		rng = new Random();
		czar = rng.nextInt(players.size());
	}
	
	private void importGame(List<String> gameLines){
		/*
		 * Imports the game information from a list of lines.
		 * Hashtags (#) signify a command or provide information about the game.
		 * Valid hashtags: #newgame, #players, #mode, 
		 * Mode options: "czar" || "vote"
		 * czar option: traditional Cards Against Humanity.  The "dealer" chooses the winner.
		 * vote option: the winner is chosen by popular vote.
		 */
		for(String line : gameLines){
			line = line.trim();
			String command = SmartassSteem.getCommand(line);
			if( command.equals("#players") ){
				List<String> names = Arrays.asList(line.split(" "));
				for(int i = 1; i < names.size(); i++){
					String name = names.get(i);
					SmartassPlayer player = new SmartassPlayer(name, this, deck);
					players.add(player);
				}
			}
			else if( command.equals("#mode") ){
				String m = line.substring(6);
				if( m.equals("czar") || m.equals("vote") )
					mode = m;
			}
			else if( command.equals("") ){}
			else if( command.equals("#newgame") ){
				List<String> game_id = new ArrayList<>(Arrays.asList(line.split(" ")));
				try{
					id = Integer.parseInt(game_id.get(1));
				} catch (NumberFormatException e) {
					id = -1;
				} catch (IndexOutOfBoundsException e){
					id = -1;
				}
			}
			else if( id != -1 && players.size() > 0 && mode != null )
				break;
		}
		if( players.size() < 2 || players.size() > 10)
			throw new InvalidSmartassGameException("Invalid player count: " + players.size());
		if( mode == null )
			throw new InvalidSmartassGameException("Game mode not selected.");
		if( id == -1 )
			throw new InvalidSmartassGameException("Invalid game ID");
	}
	
	private void drawBlack(){
		if(deck.isEmpty())
			deck.shuffle();	
		activeBlackCard = deck.drawBlackCard();	
		blank_count = activeBlackCard.getBlankIndexes().size();
	}
	
	void drawBlack(String cardString){
		activeBlackCard = deck.drawBlackCard(cardString);	
		blank_count = activeBlackCard.getBlankIndexes().size();
	}
	
	private void discardBlack(){
		if( !(activeBlackCard == null) ){
			deck.discardBlackCard(activeBlackCard);
			activeBlackCard = null;
		}
		else throw new SmartassFailException("Tried to discard a null black card.");
	}
	
	String combine(ArrayList<SmartassCard> whiteCards){
		/*
		 * Combine active black card with all corresponding white cards.
		 * Returns a String of all combined cards.
		 */
		StringBuilder card_text = new StringBuilder(activeBlackCard.getText());
		List<Integer> blank_indexes = activeBlackCard.getBlankIndexes();
		if( blank_indexes.size() != whiteCards.size() )
			throw new SmartassFailException("\nNeed more/less white cards in combineBlackWhite()\n"
					+ "Black blanks = " + blank_indexes.size() + "\n"
					+ "White cards = " + whiteCards.size() + "\n");
		int displacement = 0;
		for(int i = 0; i < blank_indexes.size(); i++){
			SmartassCard white_card = whiteCards.get(i);
			String white_text = white_card.getText();
			int blank_index = blank_indexes.get(i);
			card_text.insert( blank_index + displacement, white_text );
			displacement += white_text.length();
		}
		return card_text.toString();
	}
	
	SmartassDeck getDeck(){
		return deck;
	}
	
	ArrayList<SmartassPlayer> getPlayers(){
		return players;
	}
	
	SmartassPlayer getPlayer(String player){
		for( SmartassPlayer p : players )
			if( p.getName().equals(player) )
				return p;
		return null;
	}
	
	String getMode(){
		return mode;
	}
	
	SmartassCard getActiveBlackCard(){
		return activeBlackCard;
	}
	
	String getBlackText(){
		return activeBlackCard.getBlankText();
	}
	
	Random getRNG(){
		return rng;
	}
	
	String getCzar(){
		return players.get(czar).getName();
	}
	
	int getCzarIndex(){
		return czar;
	}
	
	int getRound(){
		return round;
	}
	
	int getBlankCount(){
		return blank_count;
	}
	
	String getGM(){
		return GM;
	}
	
	void start(){
		for( SmartassPlayer player : players)
			player.drawTen();
		drawBlack();
	}
	
	void nextRound(){
		round++;
		discardBlack();
		drawBlack();
		czar++;
		for( SmartassPlayer player : players)
			player.setBlackWhite("");
	}
	
	void nextRoundForced(){
		/*
		 * Used by SmartassSteem when the details of a game are already known
		 * Skips drawBlack() because we are forcing black draws. 
		 */
		round++;
		discardBlack();
		czar++;
		for( SmartassPlayer player : players)
			player.setBlackWhite("");
	}
	
	void end(){
		round = 1;
		czar = rng.nextInt(players.size());
		for( SmartassPlayer player : players)
			player.discardHand();
		discardBlack();
		deck.shuffle();
	}
	
	void addPlayer(SmartassPlayer player){
		players.add(player);
	}
	
	void removePlayer(SmartassPlayer player){
		players.remove(player);
	}
	
	void playRandom(){
		// Force players who haven't chosen white cards yet to choose randomly.
		for( SmartassPlayer player : players )
			if( player.getBlackWhite().equals("") )
				player.playRandom();
	}
	
	boolean isPlayer(String player){
		for( SmartassPlayer p : players)
			if( p.getName().equals(player) )
				return true;
		return false;
	}

	void testHandsPrint(){
		ArrayList<SmartassPlayer> players = getPlayers();
		for(SmartassPlayer player : players){
			System.out.println(player.getName() + ":");
			ArrayList<SmartassCard> cards = player.getHand();
			for(SmartassCard card : cards)
				System.out.println(card.getText());
			System.out.println();
		}
		System.out.println("\n\n" + getBlackText() );
	}
	
	void printDeckCounts(){
		int blackCard = 0;
		if( activeBlackCard != null )
			blackCard = 1;
		int playerCardCount = 0;
		for (SmartassPlayer player : players)
			playerCardCount += player.getHand().size() ;
		System.out.println("Deck count: " + deck.size());
		System.out.println("Active black card: " + blackCard);
		System.out.println("Import count: " + deck.getImportCount());
		System.out.println("Fail count: " + deck.getFailCount());		
		System.out.println("Black card count: " + deck.getBlackCardCount());	
		System.out.println("White card count: " + deck.getWhiteCardCount());	
		System.out.println("White dead count: " + deck.getWhiteDeadCount());	
		System.out.println("Black dead count: " + deck.getBlackDeadCount());
		System.out.println("Player card count: " + playerCardCount);
		System.out.println();
	}

}
