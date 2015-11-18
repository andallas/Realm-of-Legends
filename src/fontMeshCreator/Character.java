package fontMeshCreator;

public class Character
{
	private int _id;
	private double _xTextureCoord;
	private double _yTextureCoord;
	private double _xMaxTextureCoord;
	private double _yMaxTextureCoord;
	private double _xOffset;
	private double _yOffset;
	private double _sizeX;
	private double _sizeY;
	private double _xAdvance;

	
	protected Character(int id,
						double xTextureCoord, double yTextureCoord,
						double xTexSize, double yTexSize,
						double xOffset, double yOffset,
						double sizeX, double sizeY,
						double xAdvance)
	{
		this._id = id;
		this._xTextureCoord = xTextureCoord;
		this._yTextureCoord = yTextureCoord;
		this._xOffset = xOffset;
		this._yOffset = yOffset;
		this._sizeX = sizeX;
		this._sizeY = sizeY;
		this._xMaxTextureCoord = xTexSize + xTextureCoord;
		this._yMaxTextureCoord = yTexSize + yTextureCoord;
		this._xAdvance = xAdvance;
	}

	protected int GetId() { return _id; }
	protected double GetxTextureCoord() { return _xTextureCoord; }
	protected double GetyTextureCoord() { return _yTextureCoord; }
	protected double GetXMaxTextureCoord() { return _xMaxTextureCoord; }
	protected double GetYMaxTextureCoord() { return _yMaxTextureCoord; }
	protected double GetxOffset() { return _xOffset; }
	protected double GetyOffset() { return _yOffset; }
	protected double GetSizeX() { return _sizeX; }
	protected double GetSizeY() { return _sizeY; }
	protected double GetxAdvance() { return _xAdvance; }

}
