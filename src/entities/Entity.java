package entities;

import models.TexturedModel;
import utility.Vec3;


public class Entity
{
	private TexturedModel model;
	private Vec3 position;
	private Vec3 rotation;
	private Vec3 scale;
	
	private int textureIndex = 0;
	
	public Entity(TexturedModel model, Vec3 position, Vec3 rotation, Vec3 scale)
	{
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Entity(TexturedModel model, int index, Vec3 position, Vec3 rotation, Vec3 scale)
	{
		this.model = model;
		this.textureIndex = index;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Entity(TexturedModel model, Vec3 position, Vec3 rotation, float scale)
	{
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = new Vec3(scale, scale, scale);
	}
	
	public Entity(TexturedModel model, int index, Vec3 position, Vec3 rotation, float scale)
	{
		this.model = model;
		this.textureIndex = index;
		this.position = position;
		this.rotation = rotation;
		this.scale = new Vec3(scale, scale, scale);
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

	public Vec3 GetPosition() { return position; }
	public void SetPosition(Vec3 position) { this.position = position; }

	public Vec3 GetRotation() { return rotation; }
	public void SetRotation(Vec3 rotation) { this.rotation = rotation; }

	public Vec3 GetScale() { return scale; }
	public void SetScale(Vec3 scale) { this.scale = scale; }
	
}