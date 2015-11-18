package fontMeshCreator;

import fontRendering.TextMaster;
import utility.Vec2;
import utility.Vec3;


public class GUIText
{
	private String _textString;
	private float _fontSize;

	private int _textMeshVao;
	private int _vertexCount;
	private Vec3 _color = new Vec3(0f, 0f, 0f);

	private Vec2 _position;
	private float _lineMaxSize;
	private int _numberOfLines;

	private FontType _font;

	// TODO: Add alignment enum
	private boolean _centerText = false;

	
	// TODO: Added border and glow effect variables
	public GUIText(String text, float fontSize, FontType font, Vec2 position, float maxLineLength, boolean centered)
	{
		this._textString = text;
		this._fontSize = fontSize;
		this._font = font;
		this._position = position;
		this._lineMaxSize = maxLineLength;
		this._centerText = centered;
		TextMaster.LoadText(this);
	}
	
	public void Remove()
	{
		TextMaster.RemoveText(this);
	}

	public FontType GetFont() { return _font; }
	public void SetColor(float r, float g, float b) { _color.set(r, g, b); }
	public Vec3 GetColor() { return _color; }
	public int GetNumberOfLines() { return _numberOfLines; }
	public Vec2 GetPosition() { return _position; }
	public int GetMesh() { return _textMeshVao; }

	public void SetMeshInfo(int vao, int verticesCount) {
		this._textMeshVao = vao;
		this._vertexCount = verticesCount;
	}
	
	public int GetVertexCount() { return this._vertexCount; }
	protected float GetFontSize() { return _fontSize; }
	protected void SetNumberOfLines(int number) { this._numberOfLines = number; }
	protected boolean IsCentered() { return _centerText; }
	protected float GetMaxLineSize() { return _lineMaxSize; }
	protected String GetTextString() { return _textString; }

}
