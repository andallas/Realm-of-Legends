package fontMeshCreator;


public class TextMeshData
{	
	private float[] _vertexPositions;
	private float[] _textureCoords;
	
	protected TextMeshData(float[] vertexPositions, float[] textureCoords)
	{
		this._vertexPositions = vertexPositions;
		this._textureCoords = textureCoords;
	}

	public float[] GetVertexPositions() { return _vertexPositions; }
	public float[] GetTextureCoords() { return _textureCoords; }
	public int GetVertexCount() { return _vertexPositions.length / 2; }

}
