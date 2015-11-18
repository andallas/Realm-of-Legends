package fontMeshCreator;

import java.util.ArrayList;
import java.util.List;


public class Word
{
	private List<Character> _characters = new ArrayList<Character>();
	private double _width = 0;
	private double _fontSize;
	

	protected Word(double fontSize)
	{
		this._fontSize = fontSize;
	}
	
	protected void AddCharacter(Character character)
	{
		_characters.add(character);
		_width += character.GetxAdvance() * _fontSize;
	}
	
	protected List<Character> GetCharacters() { return _characters; }
	protected double GetWordWidth() { return _width; }

}
