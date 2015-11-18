package fontMeshCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TextMeshCreator
{
	protected static final double _LINE_HEIGHT = 0.03f;
	protected static final int _SPACE_ASCII = 32;

	private MetaFile _metaData;

	protected TextMeshCreator(File metaFile)
	{
		_metaData = new MetaFile(metaFile);
	}

	
	protected TextMeshData CreateTextMesh(GUIText text)
	{
		List<Line> lines = CreateStructure(text);
		TextMeshData data = CreateQuadVertices(text, lines);
		return data;
	}

	private List<Line> CreateStructure(GUIText text)
	{
		char[] chars = text.GetTextString().toCharArray();
		List<Line> lines = new ArrayList<Line>();
		Line currentLine = new Line(_metaData.GetSpaceWidth(), text.GetFontSize(), text.GetMaxLineSize());
		Word currentWord = new Word(text.GetFontSize());
		for (char c : chars)
		{
			int ascii = (int) c;
			if (ascii == _SPACE_ASCII)
			{
				boolean added = currentLine.AttemptToAddWord(currentWord);
				if (!added)
				{
					lines.add(currentLine);
					currentLine = new Line(_metaData.GetSpaceWidth(), text.GetFontSize(), text.GetMaxLineSize());
					currentLine.AttemptToAddWord(currentWord);
				}
				currentWord = new Word(text.GetFontSize());
				continue;
			}
			Character character = _metaData.GetCharacter(ascii);
			currentWord.AddCharacter(character);
		}
		CompleteStructure(lines, currentLine, currentWord, text);
		return lines;
	}

	private void CompleteStructure(List<Line> lines, Line currentLine, Word currentWord, GUIText text)
	{
		boolean added = currentLine.AttemptToAddWord(currentWord);
		if (!added)
		{
			lines.add(currentLine);
			currentLine = new Line(_metaData.GetSpaceWidth(), text.GetFontSize(), text.GetMaxLineSize());
			currentLine.AttemptToAddWord(currentWord);
		}
		lines.add(currentLine);
	}

	private TextMeshData CreateQuadVertices(GUIText text, List<Line> lines)
	{
		text.SetNumberOfLines(lines.size());
		double curserX = 0f;
		double curserY = 0f;
		List<Float> vertices = new ArrayList<Float>();
		List<Float> textureCoords = new ArrayList<Float>();
		for (Line line : lines)
		{
			if (text.IsCentered())
			{
				curserX = (line.GetMaxLength() - line.GetLineLength()) / 2;
			}
			for (Word word : line.GetWords())
			{
				for (Character letter : word.GetCharacters())
				{
					AddVerticesForCharacter(curserX, curserY, letter, text.GetFontSize(), vertices);
					AddTexCoords(	textureCoords, letter.GetxTextureCoord(), letter.GetyTextureCoord(),
									letter.GetXMaxTextureCoord(), letter.GetYMaxTextureCoord());
					curserX += letter.GetxAdvance() * text.GetFontSize();
				}
				curserX += _metaData.GetSpaceWidth() * text.GetFontSize();
			}
			curserX = 0;
			curserY += _LINE_HEIGHT * text.GetFontSize();
		}		
		return new TextMeshData(ListToArray(vertices), ListToArray(textureCoords));
	}

	private void AddVerticesForCharacter(	double curserX, double curserY,
											Character character, double fontSize,
											List<Float> vertices)
	{
		double x = curserX + (character.GetxOffset() * fontSize);
		double y = curserY + (character.GetyOffset() * fontSize);
		double maxX = x + (character.GetSizeX() * fontSize);
		double maxY = y + (character.GetSizeY() * fontSize);
		double properX = (2 * x) - 1;
		double properY = (-2 * y) + 1;
		double properMaxX = (2 * maxX) - 1;
		double properMaxY = (-2 * maxY) + 1;
		AddVertices(vertices, properX, properY, properMaxX, properMaxY);
	}

	private static void AddVertices(List<Float> vertices, double x, double y, double maxX, double maxY)
	{
		vertices.add((float) x);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) y);
	}

	private static void AddTexCoords(List<Float> texCoords, double x, double y, double maxX, double maxY)
	{
		texCoords.add((float) x);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) y);
	}

	private static float[] ListToArray(List<Float> listOfFloats)
	{
		float[] array = new float[listOfFloats.size()];
		for (int i = 0; i < array.length; i++){
			array[i] = listOfFloats.get(i);
		}
		return array;
	}

}
