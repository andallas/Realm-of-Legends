package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class Entity
{
	private TexturedModel model;
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	
	private int textureIndex = 0;
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale)
	{
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Entity(TexturedModel model, int index, Vector3f position, Vector3f rotation, Vector3f scale)
	{
		this.model = model;
		this.textureIndex = index;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale)
	{
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = new Vector3f(scale, scale, scale);
	}
	
	public Entity(TexturedModel model, int index, Vector3f position, Vector3f rotation, float scale)
	{
		this.model = model;
		this.textureIndex = index;
		this.position = position;
		this.rotation = rotation;
		this.scale = new Vector3f(scale, scale, scale);
	}
	
	
	public float GetTextureXOffset()
	{
		int numRows = model.GetTexture().GetNumberOfRows();
		int column = textureIndex % numRows ;
		return (float)column / (float)numRows;
	}
	
	public float GetTextureYOffset()
	{
		int numCols = model.GetTexture().GetNumberOfRows();
		int row = textureIndex / numCols ;
		return (float)row / (float)numCols;
	}
	
	public void IncreasePosition(float dx, float dy, float dz)
	{
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void IncreaseRotation(float dx, float dy, float dz)
	{
		this.rotation.x += dx;
		this.rotation.y += dy;
		this.rotation.z += dz;
	}
	
	public TexturedModel GetModel() { return model; }
	public void SetModel(TexturedModel model) { this.model = model; }

	public Vector3f GetPosition() { return position; }
	public void SetPosition(Vector3f position) { this.position = position; }

	public Vector3f GetRotation() { return rotation; }
	public void SetRotation(Vector3f rotation) { this.rotation = rotation; }

	public Vector3f GetScale() { return scale; }
	public void SetScale(Vector3f scale) { this.scale = scale; }
	
}