package smartasscards;

import java.util.ArrayList;
import java.util.List;

public class SmartassCard {
	private String color; 
	private String text;
	private List<Integer> blank_indexes = new ArrayList<>();
	private ArrayList<String> options = new ArrayList<>();
	private String owner;
	private String rating;
	private int grade;
	private ArrayList<String> tags;
	private int id;
	
	SmartassCard(String color, String text){
		if( !(color.equals("black") || color.equals("white")) )
			throw new SmartassFailException("Invalid color: " + color);
		this.color = color;
		this.text = text;
		parseBlanks();
		if( color.equals("white") ){
			// delete period "." at the end of white cards
			this.text = text.replaceAll("\\.+$", "");
		}
	}
	
	private void parseBlanks(){
		/* 
		 * Remove underscores (blanks) from text and records index locations.
		 * Currently allowing white cards to have blanks.
		 */
		StringBuilder card_text = new StringBuilder(text);
		int i;
		i = card_text.indexOf("_");
		int j = i + 1;
		while( i >= 0 ){
			while( card_text.indexOf("_", j) == j )
				j++;
			card_text.delete(i, j);
			blank_indexes.add(i);
			i = card_text.indexOf("_");
			j = i + 1;
		}
		if( color.equals("black") && blank_indexes.size() == 0 ){
			card_text.append(" ");
			blank_indexes.add(card_text.length());
		}
		text = card_text.toString();
	}
	
	String getColor(){
		return color;
	}
	
	String getText(){
		return text;
	}
	
	String getBlankText(){
		// Add Blanks (6 underscores) back into card text and return the String.
		StringBuilder altered_text = new StringBuilder(text);
		String add_underscores = "______";
		int displacement = 0;
		for( Integer i : blank_indexes ){
			altered_text.insert(i + displacement, add_underscores);
			displacement += add_underscores.length();
		}
		return altered_text.toString();
	}
	
	int getLength(){
		return text.length();
	}
	
	String getOwner(){
		return owner;
	}
	
	ArrayList<String> getTags(){
		return tags;
	}
	
	List<Integer> getBlankIndexes(){
		return blank_indexes;
	}
	
	void setTags(ArrayList<String> tags){
		this.tags = tags;
	}
	
	
}
