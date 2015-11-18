package fontMeshCreator;

import java.util.ArrayList;
import java.util.List;


public class Line
{
	private double _maxLength;
	private double _spaceSize;

	private List<Word> _words = new ArrayList<Word>();
	private double _currentLineLength = 0;

	
	protected Line(double spaceWidth, double fontSize, double maxLength)
	{
		this._spaceSize = spaceWidth * fontSize;
		this._maxLength = maxLength;
	}

	protected boolean AttemptToAddWord(Word word)
	{
		double additionalLength = word.GetWordWidth();
		additionalLength += !_words.isEmpty() ? _spaceSize : 0;
		if (_currentLineLength + additionalLength <= _maxLength)
		{
			_words.add(word);
			_currentLineLength += additionalLength;
			return true;
		}
		return false;
	}

	protected double GetMaxLength() { return _maxLength; }
	protected double GetLineLength() { return _currentLineLength; }
	protected List<Word> GetWords() { return _words; }

}
