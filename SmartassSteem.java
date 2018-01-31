package smartasscards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.apis.database.models.state.Discussion;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

public class SmartassSteem {
	private SteemJ steemJ;			
	private SteemNode root_node;
    private Permlink root_link;
	private SmartassGame game;
	private AccountName GM;				
	private AccountName host;	
	AccountName cheat_account = new AccountName("edicted");
	private Permlink cheat_link = new Permlink("windows-10-users-1clipboard-is-the-program-i-use");
	private ArrayList<SmartassPlayer> players;
	 
	SmartassSteem(){
		GM = cheat_account;//////////////////////////// CHEAT!!~!!!
		steemJ = getSteemJ();
		root_node = new SteemNode(null, findRootCheat());
		game = new SmartassGame(root_node.lines);
		root_node.pruneLevel2();
		players = game.getPlayers();
		host = root_node.author;
		root_link = root_node.discussion.getPermlink();
	}
	
	public static void main(String[] args){
		SmartassSteem steem = new SmartassSteem();
		steem.game.printDeckCounts();
		steem.sync();
		steem.game.testHandsPrint();
		steem.game.printDeckCounts();
	}
	
	Discussion findRootCheat(){
		// CHEAT: only looking at cheat account/link to find games.
		List<Discussion> find_root = importReplies(cheat_account, cheat_link);
		for( Discussion root : find_root ){
			List<String> lines = new ArrayList<String>(
					Arrays.asList(root.getBody().split("\n")));
			Set<String> commands = getCommands(lines).keySet();
			if( 	   commands.contains("#newgame") 
					&& commands.contains("#players") 
					&& commands.contains("#mode") 
					&& commands.contains("#deck") ){
				List<Discussion> root_replies = importReplies(root);
				for( Discussion reply : root_replies){
					// Make sure the game hasn't ended.
					List<String> reply_lines = new ArrayList<String>(
							Arrays.asList(reply.getBody().split("\n")));
					commands = getCommands(reply_lines).keySet();
					if( commands.contains("#endgame") )
						break;
				}
				return root;
			}
		}	
		return null;
	}
	
	void exportOutput(String output){
        try {
			steemJ.createComment(host, root_link, output, new String[] { "tag" });
		} 
        catch (   SteemCommunicationException 
        		| SteemResponseException 
        		| SteemInvalidTransactionException e) {
			e.printStackTrace();
		}
	}
	
	void sync(){
		/*
		 * Synchronizes the game on startup.  
		 * Scans all level 1 nodes and makes a series of force moves.
		 * This function is used when starting from scratch like from a crashed GM bot.
		 */
		int round = 1;
		SteemNode roundNode = root_node.getNode("#round", round);
		while( roundNode != null ){
			syncRound(roundNode);
			SteemNode combineNode = root_node.getNode("#combine", round);
			if( combineNode != null )
				syncCombine(combineNode);
			SteemNode winnerNode = root_node.getNode("#winner", round);
			if( winnerNode != null )
				syncWinner(winnerNode);
			roundNode = root_node.getNode("#round", ++round);
			if( roundNode != null )
				game.nextRoundForced();
		}
	}
	
	void syncRound(SteemNode roundNode){
		/*
		 * Extension of sync().  Processes #round nodes.
		 * Force draws all the necessary black and white cards.
		 */
		SmartassPlayer player = null;
		for( String line : roundNode.lines ){
			String command = getCommand(line);
			if( command.equals("#hand") )
				player = game.getPlayer(line.substring(6));
			else if( !command.equals("") )
				player = null;
			if( line.equals(""))
				player = null;
			if( player != null && line.length() > 0 && !isCommand(line) ){
				// Force draw white cards.
				String whiteCard = line.substring(3);
				if( !player.hasCard(whiteCard) )
					player.draw(whiteCard);
			}
			if( command.equals("#activeblackcard") ){
				// Force draw black card.
				String blackCard = line.substring(19);
				game.drawBlack(blackCard);
			}
		}		
	}
	
	void syncCombine(SteemNode combineNode){
		/*
		 * Extension of sync().  Processes #combine nodes.
		 * Force discards the white cards that got used in #combine.
		 */
		SmartassCard blackCard = game.getActiveBlackCard();
		List<Integer> blankIndexes = blackCard.getBlankIndexes();
		for( String line : combineNode.lines ){
			if( !line.isEmpty() && Character.isDigit(line.charAt(0)) ){
				StringBuilder combineCard = new StringBuilder(line.substring(3));
				// Reverse engineer the combineCard back into the white cards played.
				///// this  will let me randomize combine output if i want. 
				int cardsPlayed = 0;
				for( int blankIndex : blankIndexes ){
					for( SmartassPlayer player : game.getPlayers() ){
						Iterator<SmartassCard> itr = player.getHand().iterator();
						while( itr.hasNext() ){
							SmartassCard card = (SmartassCard) itr.next();
							String c = card.getText();
							String match = null;
							if( blankIndex + c.length() <= combineCard.length() )
								match = combineCard.substring(
										blankIndex, blankIndex+c.length()).toString();
							if( c.equals(match) ){
								itr.remove();
								game.getDeck().discardWhiteCard(card);
								cardsPlayed++;
								combineCard.delete(blankIndex, blankIndex+c.length());
								break;
							}
						}
					}
				}
				if( cardsPlayed != game.getBlankCount() )
					throw new SmartassFailException("Missed one or more force draws.");
			}
		}
	}
	
	void syncWinner(SteemNode winnerNode){
		/*
		 * Extension of sync().  Processes #winner nodes.
		 * Force draws all the necessary black and white cards.
		 */
		
	}
	
	void exportCombine(){
		/*
		 * Prints each player's choice of white card/s combined with the black card.
		 * Chooses randomly for players that didn't act in time.
		 */
		game.playRandom();
		String output = "#combine " + game.getRound() + "\n";
		int vote_index = 0;
		for( SmartassPlayer player : players ){
			output += "\t" + vote_index++ + ". " + player.getBlackWhite() + "\n";
		}
		if( game.getMode().equals("vote") )
			output += "\n" + "Reply with #vote [digit] to pick a winner.";
		else if( game.getMode().equals("czar") )
			output += "\n" + game.getCzar() + ": reply with #vote [digit] to pick a winner.";
		exportOutput(output);
	}
	
	void exportRound(){
		/*
		 * Prints each player's name and what cards they were dealt, then the black card.
		 */
		game.start();/////add if hands are empty
		String output = "";
		for( SmartassPlayer player : players ){
			output += "\n#hand " + player.getName() + "\n";
			for(int i = 0; i < player.getHand().size(); i++){
				SmartassCard card = player.getHand().get(i);
				output += "\t"+ i + ". " + card.getText() + "\n";
			}
		}
		output += "\n";
		output += "#round " + game.getRound() + "\n\n";;
		output += "#activeblackcard " + game.getBlackText() + "\n";
		output += "\n" + "Reply with #playcard [digit] to play your card.";
		exportOutput(output);
	}	
		
	void importPlays(){
		/*
		 * Finds the white card/s that got played on the black card and updates classes.
		 */
		SteemNode active_round = root_node.getActiveNode();
		if(active_round == null)
			throw new SmartassFailException("No active_round in importPlays()");
		List<String> hasPlayed = new ArrayList<>();
		for( SteemNode playcard_node : active_round.children ){
			String p = playcard_node.author.getName();
			SmartassPlayer player = game.getPlayer(p);
			int[] playcard_indexes = getPlaycardIndexes(playcard_node.lines.get(0));
			if( 	player != null
					&& playcard_indexes != null
					&& playcard_indexes.length == game.getBlankCount() 
					&& !hasPlayed.contains(p)){
				// The first valid #playcard command gets executed, the rest are ignored.
				player.play(playcard_indexes);
				hasPlayed.add(p);
			}
		}
	}
	
	List<Discussion> importReplies(AccountName account, Permlink link){
		/*
		 * Return all of the first level replies of a post.
		 */
        List<Discussion> replies = null;
		try {
			replies = steemJ.getContentReplies(account, link);
		} catch (SteemCommunicationException | SteemResponseException e) {
			e.printStackTrace();
		}
		return replies;
	}
	
	List<Discussion> importReplies(Discussion d){
		/*
		 * Return all of the first level replies of a post.
		 */
        List<Discussion> replies = null;
        AccountName account = d.getAuthor();
        Permlink link = d.getPermlink();
		try {
			replies = steemJ.getContentReplies(account, link);
		} catch (SteemCommunicationException | SteemResponseException e) {
			e.printStackTrace();
		}
		return replies;
	}
	
	SteemJ getSteemJ() {
	    SteemJConfig myConfig = SteemJConfig.getInstance();
	    myConfig.setResponseTimeout(5000);
	    myConfig.setDefaultAccount(cheat_account);//////////////
	    List<ImmutablePair<PrivateKeyType, String>> privateKeys = new ArrayList<>();
	    privateKeys.add(new ImmutablePair<>(
	    		PrivateKeyType.POSTING, "5KXwpQzdtSBsEyoMsaX39XukgNzMKgeyWzhcACSjH649MJh1RmU"));
	    myConfig.getPrivateKeyStorage().addAccount(myConfig.getDefaultAccount(), privateKeys);
	    try {
			return new SteemJ();
		} catch (SteemCommunicationException | SteemResponseException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	SmartassGame getGame(){
		return game;
	}

	static boolean isCommand(String line){
		if( line.length() <= 4 )
			return false;
		if( !line.substring(0, 1).equals("#") )
			return false;
		return true;
	}

	final static List<String> valid_commands = Arrays.asList(
			"#mode", "#hand", "#deck", "#vote", "#round", "#newgame", "#players",
			"#combine", "#winner", "#playcard", "#gameover", "#roundover", "#blackcards",
			"#whitecards", "#activeblackcard" );
	static String getCommand(String line){
		/*
		 * Scan line for a valid command and return it or "".
		 */
		if( !isCommand(line) )
			return "";
		// line.length() > 4 and starts with "#"
		String command = "";
		int i = line.indexOf(" ");
		if( i > 0 )
			command = line.substring(0, i);
		if( valid_commands.contains(command) )
			return command;
		if( valid_commands.contains(line) )
			return line;
		return "";
	}
	
	static Map<String, String> getCommands(List<String> post){
		/*
		 * Returns a dictionary of all valid commands in a single post.
		 */
		Map<String, String> commands = new HashMap<>();
		for( String line : post ){
			String command = getCommand(line);
			if( !command.equals("") )
				commands.put( command, line.substring(command.length()).trim() );
		}
		return commands;
	}
	
	static int[] getPlaycardIndexes(String playcard){
		/*
		 * Takes in "#playcard 9 3" spits out [9, 3]
		 */
		List<String> playcard_list = new ArrayList<>(Arrays.asList(playcard.split(" ")));
		int[] card_indexes = new int[playcard_list.size()-1];
		for(int i = 1; i < playcard_list.size(); i++){
			int card_index;
			try{
				card_index = Integer.parseInt(playcard_list.get(i));
			} catch (NumberFormatException e) {
				card_index = -1;
			}
			if( card_index >= 0 && card_index <= 9){
				card_indexes[i-1] = card_index;
			}
			else return null;
		}
		return card_indexes;
	}
	
	private class SteemNode{
		/*
		 * Converts valid information from SteemJ Discussions and puts them into a more usable form.
		 * Essentially a three tier tree with a game request at the root, GM commands at level 1,
		 * and user commands at level 2.
		 */
		private SteemNode parent;
		private Discussion discussion;
		private AccountName author;
		private Permlink link;
		private List<String> lines;
		private Map<String, String> commands;
		private int level;
		private List<SteemNode> children = new ArrayList<>();
		
		SteemNode(SteemNode parent, Discussion discussion){
			this.parent = parent;
			this.discussion = discussion;
			author = discussion.getAuthor();
			link = discussion.getPermlink();
			lines = new ArrayList<String>(Arrays.asList(discussion.getBody().split("\n")));
			for(int i = 0; i < lines.size(); i++){
				String line = lines.get(i);
				lines.set(i, line.trim());
			}
			commands = getCommands(lines);
			if( parent == null ){
				level = 0;
				// root node,
				// children of root are any posts that are authored by the GM (game master) 
				for( Discussion reply : importReplies(discussion) )
					if( reply.getAuthor().getName().equals(GM.getName()) )
						children.add( new SteemNode(this, reply) );
			}
			else if( parent.parent == null ){
				level = 1; 
				// Grab all replies and filter them later.  player list not populated yet.
				for( Discussion reply : importReplies(discussion) )
					children.add( new SteemNode(this, reply) );
			}
			else{
				level = 2;
				// no children
			}
			System.out.println(commands);
		}
		
		void addChild(Discussion discussion){
			
		}
		
		void pruneLevel2(){
			/*
			 * Deletes all posts on level 2 not authored by registered players.
			 */
			if( parent != null )
				throw new SmartassFailException("non-root node called pruneLevel2");
			for( SteemNode GM_node : children){
				Iterator<SteemNode> itr = GM_node.children.iterator();
				while(itr.hasNext()){
					SteemNode player_node = (SteemNode) itr.next();
					if( !game.isPlayer(player_node.author.getName()) )
						itr.remove();
				}
			}
		}
		
		SteemNode getNode(String command, int round){
			/*
			 * Returns the level 1 node that contains the command and round number given.
			 * The round number is right after #round, #combine, and #winner commands
			 * and is stored in the commands dictionary.
			 */
			if( parent != null )
				throw new SmartassFailException("non-root node called getRoundNode()");
			String roundString = Integer.toString(round);
			for( SteemNode node : children ){
				String r = node.commands.get(command);
				if( r != null && r.equals(roundString) )
					return node;
			}
			return null;
		}
		
		SteemNode getActiveNode(){
			/*
			 * Returns the level 1 SteemNode that the game is currently on.
			 */
			if( parent == null ){
				// Only to be called on root node.
				if(children.size() > 0){
					SteemNode active_node = children.get(children.size()-1);
					return active_node;
				}
				/*	// if we wanted to return the most recent #blackcard active round
				List<SteemNode> children = root_node.children;
				for( int i = children.size() - 1; i >= 0; i--){
					SteemNode child = children.get(i);
					if( child.commands.contains("#round") && child.commands.contains("#blackcard"))
						active_node = child;
				}
				if(active_round == null)
					throw new SmartassFailException("No active_round in importPlays()");
				//*/
			}
			else throw new SmartassFailException("Tried to prune wrong node.");
			return null;
		}
		

		/*
		void pruneActiveNode(){
			 * Get rid of all invalid posts from the active node's children.
			 
			if( level == 1 ){
				// Only to be used on GM nodes.
				List<SteemNode> playcards = children;
				for( SteemNode play_node : children ){
					boolean isPlaycard = play_node.commands.contains("#playcard");
					
					if( isPlaycard ){
						String first_line = play_node.lines.get(0);
						if( getCommand(first_line).equals("#playcard") ){
							// Only tracks the first time a valid player used the #playcard command.
							playcards.add(play_node);
						}
					}
				}
				
			}
			else throw new SmartassFailException("Tried to prune wrong node.");
		}
		//*/
	}
}