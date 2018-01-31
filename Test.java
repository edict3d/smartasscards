package smartasscards;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.joou.UInteger;
import org.joou.ULong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.apis.database.models.state.Discussion;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.AccountVote;
import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.base.models.VoteState;
import eu.bittrade.libs.steemj.base.models.operations.CommentOperation;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;
import my.sample.project.SteemJUsageExample;

public class Test {
	
	public static void main(String[] args){
		new Test();
	}	
	
	Test(){
		
		Map<String, String> team1 = new HashMap<String, String>();
		team1.put("United", "");
		team1.put("Unit", "4");
		team1.put("Uned", "2345");
		
		for( String s : team1.keySet() ){
			System.out.println(s);
		}
		String line = "suckit !!", c = "";
		int i = line.indexOf(" ");
		if( i > 0 )
			c = line.substring(0, i);
		System.out.println(c);
		
		
		Map<String, String> commands = new HashMap<>();
		System.out.println(commands.get("noob"));

		File file = new File("c:/users/anon/desktop/newgame.txt");
		List<String> newGame = new ArrayList<>();
		Scanner scan = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(scan.hasNextLine())
			newGame.add(scan.nextLine());
		
		SmartassGame game = new SmartassGame(newGame);
		
		printDeckCounts(game);
		game.start();
		printDeckCounts(game);
		
		/*
	    final Logger LOGGER = LoggerFactory.getLogger(SteemJUsageExample.class);
	    
		SmartassSteem testSteem = new SmartassSteem();
		SmartassGame game = testSteem.getGame();
		ArrayList<SmartassPlayer> players = game.getPlayers();
		SmartassDeck deck = game.getDeck();
		
		testGamePrint(game);
		System.out.println("\n\n");
		
		printDeckCounts(deck);
		//*/
		
		/*
		testDeckPrint(deck);
		System.out.println("\n\n");
		//*/
		
		//game.start();
		
		/*
		testDeckPrint(deck);
		System.out.println("\n\n");
		testHandsPrint(players);
		System.out.println("\n\n");
		
		testRandomRounds(5, game, players);
		
		game.end();
		printDeckCounts(deck);
		
		game.start();
		testRandomRounds(5, game, players);
		game.end();
		printDeckCounts(deck);
		
		//*/
		
		
		/*
		game.start();
		testRandomRounds(5, game, players);
		game.end();
		printDeckCounts(deck);
		
		game.start();
		testRandomRounds(5, game, players);
		game.end();
		printDeckCounts(deck);
		//*/
		
		// Let's play a game on Steemit
		//exportStart(game, steemJ);

		//testSteem.exportRound();
		//*
		//testSteem.catchUp();
		//testSteem.importPlays();
		//testSteem.exportCombine();
		//*/
		
		//testHandsPrint(game);
		//testSteem.importPlays();
		//testSteem.exportBlackWhite();
		

		//testSteem.exportRound();
		
		
		/*  test SmartassSteem.updatePlays()
		game.start();
		List<String> names = new ArrayList<>() , plays = new ArrayList<>();
		names.add("alxgraham");
		plays.add("#play 1");
		names.add("edicted");
		plays.add("#play 7");
		names.add("edicted");
		plays.add("#play 7");
		names.add("troll");
		plays.add("#play 12");
		names.add("troll2");
		plays.add("#play 5 6");
		names.add("troll3");
		plays.add("#play 0");
		names.add("Alice");
		plays.add("#play 4abc");
		names.add("Alice");
		plays.add("#play 5");
		names.add("Alice");
		plays.add("#play 6");
		names.add("garbage");
		plays.add("#play 8");
		names.add("ericwilson");
		plays.add("#play 11");
		System.out.println(game.getPlayers().get(0).getHand().get(7).getText());
		System.out.println(game.getPlayers().get(1).getHand().get(1).getText());
		System.out.println(game.getPlayers().get(2).getHand().get(5).getText());
		System.out.println(game.getActiveBlackCard().getBlankText());
		testSteem.updatePlays(names, plays);
		List<String> black_white_list = game.getBlackWhiteList();
		int z = 0;
		for( String black_white : black_white_list ){
			System.out.println(game.getPlayers().get(z).getName() + ": " + black_white + " TEST");
			z++;
		}
		System.out.println("\n" + game.getPlayers().get(3).getHand().get(1).getText());
		System.out.println(game.getPlayers().get(3).getHand().get(2).getText());
		System.out.println(game.getPlayers().get(3).getHand().get(3).getText());
		System.out.println(game.getPlayers().get(3).getHand().get(4).getText());
		System.out.println(game.getPlayers().get(3).getHand().get(5).getText());
		System.out.println(game.getPlayers().get(3).getHand().get(6).getText());
		System.out.println(game.getPlayers().get(3).getHand().get(7).getText());
		System.out.println(game.getPlayers().get(3).getHand().get(8).getText());
		System.out.println(game.getPlayers().get(3).getHand().get(9).getText());
		System.out.println(game.getPlayers().get(3).getHand().get(0).getText() + "\n");
		game.injectRandom();
		z=0;
		for( String black_white : black_white_list ){
			System.out.println(game.getPlayers().get(z).getName() + ": " + black_white + " TEST");
			z++;
		}
		//*/
	}
	
	
	
	
	
	void testSteemJ(Logger LOGGER){
		try{
		    AccountName edicted = new AccountName("edicted");
			
	        SteemJConfig myConfig = SteemJConfig.getInstance();
	        myConfig.setResponseTimeout(5000);
	        myConfig.setDefaultAccount(edicted);
	        
	        List<ImmutablePair<PrivateKeyType, String>> privateKeys = new ArrayList<>();
	        privateKeys.add(new ImmutablePair<>(
	        		PrivateKeyType.POSTING, "5KXwpQzdtSBsEyoMsaX39XukgNzMKgeyWzhcACSjH649MJh1RmU"));
	        myConfig.getPrivateKeyStorage().addAccount(myConfig.getDefaultAccount(), privateKeys);
	        
	        SteemJ steemJ = new SteemJ();
	        
	        //*
	        
	        
	        //*/	        
	        
	        /*  
            steemJ.vote(edicted), 
            		new Permlink("did-you-know-anyone-can-upvote"),
                    (short) 10);
           	//*/
            
	        
	        /*
            steemJ.createComment(edicted), 
            		new Permlink("did-you-know-anyone-can-upvote"),
                    "Upvoted myself for 10% even though I only have 37 SP. \n"
                    		+ "", 
                    new String[] { "arethesereally", "tags" });
            //*/
                    
            /*
            CommentOperation myNewPost = steemJ.createPost(
            		"Programmatically-Generated Post Using SteemJ Java Wrapper",
                    "In the end I gave up on Maven and added the jar files "
                            + "using 'build path' >> 'add external archives ' "
                    		+ "in Eclipse.  I can figure out Maven later. "
                            + "https://github.com/marvin-we/steem-java-api-wrapper/ "
                            + "![SteemJV2Logo](https://imgur.com/bIhZlYT.png).",
                    new String[] { "java", "programming", "bot", "steemit", "steemj" });
            
            LOGGER.info(
                    "SteemJ has generated some additional values for my new post. "
                    		+"One good example is the permlink {} that I may need later on.",
                    myNewPost.getPermlink().getLink());
            //*/
	        
	        
            
	        
		} 
		catch (SteemResponseException e) {
	        LOGGER.error("An error occured.", e);
	        LOGGER.error("The error code is {}", e.getCode());
	    } 
		catch (SteemCommunicationException e) {
	        LOGGER.error("A communication error occured!", e);
	    } /*
	    catch (SteemInvalidTransactionException e) {
	        LOGGER.error("There was a problem to sign a transaction.", e);
	    }//*/
	}
	
	void testRandomRounds(int rounds, SmartassGame game, ArrayList<SmartassPlayer> players){
		for(; rounds > 0; rounds--){
			System.out.println(game.getBlackText());
			for( SmartassPlayer player : players ){
				player.playRandom();
				System.out.print(player.getBlackWhite());
				System.out.println("    -" + player.getName());
			}
			game.nextRound();
			System.out.println();
		}
	}
	
	void printDeckCounts(SmartassGame game){
		SmartassDeck deck = game.getDeck();
		int blackCard = 0;
		if( game.getActiveBlackCard() != null )
			blackCard = 1;
		int playerCardCount = 0;
		for (SmartassPlayer player : game.getPlayers())
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

	void testHandsPrint(SmartassGame game){
		ArrayList<SmartassPlayer> players = game.getPlayers();
		for(SmartassPlayer player : players){
			System.out.println(player.getName() + ":");
			ArrayList<SmartassCard> cards = player.getHand();
			for(SmartassCard card : cards)
				System.out.println(card.getText());
			System.out.println();
		}
		System.out.println("\n\n" + game.getBlackText() );
	}
	
	void testGamePrint(SmartassGame game){
		ArrayList<SmartassPlayer> players = game.getPlayers();
		System.out.println("\nplayers:");
		for(SmartassPlayer player : players){
			System.out.println(player.getName());
		}
		System.out.println("\nmode:");
		System.out.println(game.getMode());
	}
	
	void testDeckPrint(SmartassDeck deck){
		System.out.println("\nBLACK DECK:");
		for(SmartassCard card : deck.getBlackDeck()){
			System.out.println(card.getBlankText());
		}
		System.out.println("\nWHITE DECK:");
		for(SmartassCard card : deck.getWhiteDeck()){
			System.out.println(card.getText());
		}
		System.out.println("\nBLACK DEAD:");
		for(SmartassCard card : deck.getBlackDead()){
			System.out.println(card.getBlankText());
		}
		System.out.println("\nWHITE DEAD:");
		for(SmartassCard card : deck.getWhiteDead()){
			System.out.println(card.getText());
		}
	}
}







/*
void importPlays(SteemJ steemJ){
		try {
			List<Discussion> replies  = importReplies(steemJ);
			Discussion reply = replies.get(replies.size()-1);//////cheat
			String name = reply.getAuthor().getName();
			String text = reply.getBody();
			System.out.println(name);
			System.out.println(text);
			// #round found, now get #play commands from each player
			Permlink link = reply.getPermlink();
			List<Discussion> names_plays  = steemJ.getContentReplies(new AccountName("edicted"), link);
			List<String> names = new ArrayList<>();
			List<String> plays = new ArrayList<>();
			for( Discussion name_play : names_plays ){
				names.add(name_play.getAuthor().getName());////////cheat
				plays.add(name_play.getBody());
			}
			
		} catch (SteemCommunicationException | SteemResponseException e) {
			e.printStackTrace();
		}
	}
	
	void exportStart(SmartassGame game, SteemJ steemJ){
		game.start();
		String output = "";
		for( SmartassPlayer player : game.getPlayers() ){
			output += "\n#dealt " + player.getName() + "\n";
			int i = 0; 
			for( SmartassCard card : player.getHand() ){
				output += "\t"+ i + ". " + card.getText() + "\n";
				i++;
			}
		}
		output += "\n";
		if( game.getMode().equals("czar") )
			output += "#czar: " + game.getCzar() + "\n";
		output += "#round: " + game.getRound() + "\n";;
		output += "#blackcard:\n";
		output += game.getBlackText();
		
		AccountName edicted = new AccountName("edicted");
        try {
			steemJ.createComment(edicted, 
			new Permlink("windows-10-users-1clipboard-is-the-program-i-use"),
					output, 
					new String[] { "arethesereally", "tags" });
		} 
        catch (SteemCommunicationException | SteemResponseException | SteemInvalidTransactionException e) {
			e.printStackTrace();
		}
	}
	
	List<String> importGame(SteemJ steemJ) throws SteemCommunicationException, SteemResponseException{
		List<Discussion> replies  = importReplies(steemJ);
        for( Discussion reply : replies){
	        String body = reply.getBody();
	        List<String> lines = new ArrayList<String>(Arrays.asList(body.split("\n")));
	        if( lines.size() > 1 && lines.get(1).equals("#newgame") ){
	        	System.out.println("worked");
	        	return lines;
	        }
        }
        return null;
	}
	
	List<String> importDeck(SteemJ steemJ) throws SteemCommunicationException, SteemResponseException{
		List<Discussion> replies  = importReplies(steemJ);
        for( Discussion reply : replies){
	        String body = reply.getBody();
	        List<String> lines = new ArrayList<String>(Arrays.asList(body.split("\n")));
	        if( lines.size() > 1 && lines.get(1).equals("#newdeck") ){
	        	System.out.println("worked");
	        	return lines;
	        }
        }
		return null;
	}
	
	List<Discussion> importReplies(SteemJ steemJ) throws SteemCommunicationException, SteemResponseException{
	    AccountName edicted = new AccountName("edicted");
        Permlink link = new Permlink("windows-10-users-1clipboard-is-the-program-i-use");
        List<Discussion> replies = steemJ.getContentReplies(edicted, link);
		return replies;
	}
	
	SteemJ getSteemJ() throws SteemCommunicationException, SteemResponseException{
		AccountName edicted = new AccountName("edicted");
	    SteemJConfig myConfig = SteemJConfig.getInstance();
	    myConfig.setResponseTimeout(5000);
	    myConfig.setDefaultAccount(edicted);
	    List<ImmutablePair<PrivateKeyType, String>> privateKeys = new ArrayList<>();
	    privateKeys.add(new ImmutablePair<>(
	    		PrivateKeyType.POSTING, "5KXwpQzdtSBsEyoMsaX39XukgNzMKgeyWzhcACSjH649MJh1RmU"));
	    myConfig.getPrivateKeyStorage().addAccount(myConfig.getDefaultAccount(), privateKeys);
	    return new SteemJ();
	}

*/






/*

		
		File gameFile = new File ("C:/users/anon/desktop/newgame.txt");
		File deckFile = new File ("C:/users/anon/desktop/newdeck.txt");
		Scanner gameScanner = null;
		Scanner deckScanner = null;
		
		try{
			gameScanner = new Scanner(gameFile);
			deckScanner = new Scanner(deckFile);
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			System.exit(1);
		}

 */









	/*
	void nextRound(SmartassGame game){
		int p = czar + 1;
		for(SmartassPlayer player : players){
			//waitForAction(players[p]);
			p++;
		}
		czar++;
	}
	*/
	

	/*
	void testBlanks(){
		int[] a  = new int[5];
		System.out.println(a.length);
		int b = a[0];
		System.out.println(b);
		System.out.println("abc_def_ghi__".indexOf("_"));
		
		ArrayList<Integer> blank_indexes = new ArrayList<>();
		String text = "_su__p __and____...._";
		StringBuilder string_builder = new StringBuilder(text);
		int i;
		i = string_builder.indexOf("_");
		int j = i + 1;
		while( i >= 0 ){
			while( string_builder.indexOf("_", j) == j )
				j++;
			string_builder.delete(i, j);
			blank_indexes.add(i);
			i = string_builder.indexOf("_");
			j = i + 1;
		}
		System.out.println("string_builder");
		System.out.println(string_builder.toString());
		for(Integer index : blank_indexes){
			System.out.print(index + " ");
		}
		System.out.println("\n" + string_builder.length() + "\n\n\n");
		System.out.println("\n" + string_builder.append("bard") + "\n\n\n");
	}
	//*/
	

/*


 */

/*


		Integer z = 0;
		Integer y = z;
		z = 1;
		System.out.println(y);
		System.out.println(z);

//*/




