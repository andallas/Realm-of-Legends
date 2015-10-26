package models;

import renderEngine.ModelLoader;

public class ModelData
{
	private float[] vertices;
	private float[] uvCoords;
	private float[] normals;
	private int[] indices;
	private float furthestPoint;
	
	public ModelData(float[] vertices, float[] uvCoords, float[] normals, int[] indices, float furthestPoint)
	{
		this.vertices = vertices;
		this.uvCoords = uvCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	public RawModel GetRawModel()
	{
		return ModelLoader.LoadToVAO(vertices, uvCoords, normals, indices);
	}
	
	public float[] GetVertices() { return vertices; }
	public float[] GetUvCoords() { return uvCoords; }
	public float[] GetNormals() { return normals; }
	public int[] GetIndices() { return indices; }
	public float GetFurthestPoint() { return furthestPoint; }
	
}