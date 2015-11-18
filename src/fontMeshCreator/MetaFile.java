package fontMeshCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.Display;


public class MetaFile
{
	private static final int _PAD_TOP = 0;
	private static final int _PAD_LEFT = 1;
	private static final int _PAD_BOTTOM = 2;
	private static final int _PAD_RIGHT = 3;

	private static final int _DESIRED_PADDING = 8;

	private static final String _SPLITTER = " ";
	private static final String _NUMBER_SEPARATOR = ",";

	private double _aspectRatio;

	private double _verticalPerPixelSize;
	private double _horizontalPerPixelSize;
	private double _spaceWidth;
	private int[] _padding;
	private int _paddingWidth;
	private int _paddingHeight;

	private Map<Integer, Character> _metaData = new HashMap<Integer, Character>();

	private BufferedReader _reader;
	private Map<String, String> _values = new HashMap<String, String>();


	protected MetaFile(File file)
	{
		this._aspectRatio = (double) Display.getWidth() / (double) Display.getHeight();
		OpenFile(file);
		LoadPaddingData();
		LoadLineSizes();
		int imageWidth = GetValueOfVariable("scaleW");
		LoadCharacterData(imageWidth);
		Close();
	}

	protected double GetSpaceWidth() { return _spaceWidth; }
	protected Character GetCharacter(int ascii) { return _metaData.get(ascii); }

	private boolean ProcessNextLine()
	{
		_values.clear();
		String line = null;
		try
		{
			line = _reader.readLine();
		}
		catch (IOException e1)
		{
			// TODO: Handle this exception
		}
		if (line == null)
		{
			return false;
		}
		for (String part : line.split(_SPLITTER))
		{
			String[] valuePairs = part.split("=");
			if (valuePairs.length == 2)
			{
				_values.put(valuePairs[0], valuePairs[1]);
			}
		}
		return true;
	}

	private int GetValueOfVariable(String variable) { return Integer.parseInt(_values.get(variable)); }

	private int[] GetValuesOfVariable(String variable)
	{
		String[] numbers = _values.get(variable).split(_NUMBER_SEPARATOR);
		int[] actualValues = new int[numbers.length];
		for (int i = 0; i < actualValues.length; i++)
		{
			actualValues[i] = Integer.parseInt(numbers[i]);
		}
		return actualValues;
	}

	private void Close()
	{
		try
		{
			_reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void OpenFile(File file)
	{
		try
		{
			_reader = new BufferedReader(new FileReader(file));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Couldn't read font meta file!");
		}
	}

	private void LoadPaddingData()
	{
		ProcessNextLine();
		this._padding = GetValuesOfVariable("padding");
		this._paddingWidth = _padding[_PAD_LEFT] + _padding[_PAD_RIGHT];
		this._paddingHeight = _padding[_PAD_TOP] + _padding[_PAD_BOTTOM];
	}

	private void LoadLineSizes()
	{
		ProcessNextLine();
		int lineHeightPixels = GetValueOfVariable("lineHeight") - _paddingHeight;
		_verticalPerPixelSize = TextMeshCreator._LINE_HEIGHT / (double) lineHeightPixels;
		_horizontalPerPixelSize = _verticalPerPixelSize / _aspectRatio;
	}

	private void LoadCharacterData(int imageWidth)
	{
		ProcessNextLine();
		ProcessNextLine();
		while (ProcessNextLine())
		{
			Character c = LoadCharacter(imageWidth);
			if (c != null)
			{
				_metaData.put(c.GetId(), c);
			}
		}
	}

	private Character LoadCharacter(int imageSize)
	{
		int id = GetValueOfVariable("id");
		if (id == TextMeshCreator._SPACE_ASCII)
		{
			this._spaceWidth = (GetValueOfVariable("xadvance") - _paddingWidth) * _horizontalPerPixelSize;
			return null;
		}
		double xTex = ((double) GetValueOfVariable("x") + (_padding[_PAD_LEFT] - _DESIRED_PADDING)) / imageSize;
		double yTex = ((double) GetValueOfVariable("y") + (_padding[_PAD_TOP] - _DESIRED_PADDING)) / imageSize;
		
		int width = GetValueOfVariable("width") - (_paddingWidth - (2 * _DESIRED_PADDING));
		int height = GetValueOfVariable("height") - ((_paddingHeight) - (2 * _DESIRED_PADDING));
		
		double quadWidth = width * _horizontalPerPixelSize;
		double quadHeight = height * _verticalPerPixelSize;
		double xTexSize = (double) width / imageSize;
		double yTexSize = (double) height / imageSize;
		double xOff = (GetValueOfVariable("xoffset") + _padding[_PAD_LEFT] - _DESIRED_PADDING) * _horizontalPerPixelSize;
		double yOff = (GetValueOfVariable("yoffset") + (_padding[_PAD_TOP] - _DESIRED_PADDING)) * _verticalPerPixelSize;
		double xAdvance = (GetValueOfVariable("xadvance") - _paddingWidth) * _horizontalPerPixelSize;
		
		return new Character(id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, xAdvance);
	}
	
}
