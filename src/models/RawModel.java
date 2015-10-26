package models;

public class RawModel
{
	private int vaoID;
	private int vertexCount;
	
	public RawModel(int vaoID, int vertexCount)
	{
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int GetVaoID()
	{
		return this.vaoID;
	}

	public int GetVertexCount()
	{
		return this.vertexCount;
	}
}