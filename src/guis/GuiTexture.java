package guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture
{
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	
	
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale)
	{
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	
	public int GetTexture() { return this.texture; }
	public Vector2f GetPosition() { return this.position; }
	public Vector2f GetScale() { return this.scale; }
	
}