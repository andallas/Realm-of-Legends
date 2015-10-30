package guis;

import utility.Vec2;

public class GuiTexture
{
	private int texture;
	private Vec2 position;
	private Vec2 scale;
	
	
	
	public GuiTexture(int texture, Vec2 position, Vec2 scale)
	{
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	
	public int GetTexture() { return this.texture; }
	public Vec2 GetPosition() { return this.position; }
	public Vec2 GetScale() { return this.scale; }
	
}