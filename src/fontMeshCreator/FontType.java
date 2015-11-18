package fontMeshCreator;

import java.io.File;


public class FontType
{
	private int _textureAtlas;
	private TextMeshCreator _loader;


	public FontType(int textureAtlas, File fontFile)
	{
		this._textureAtlas = textureAtlas;
		this._loader = new TextMeshCreator(fontFile);
	}

	public int GetTextureAtlas() { return _textureAtlas; }

	public TextMeshData LoadText(GUIText text)
	{
		return _loader.CreateTextMesh(text);
	}

}
