package utility;

import org.lwjgl.util.vector.Vector2f;

public class Vec2 extends Vector2f
{
	private static final long serialVersionUID = -2159485372311950564L;
	
	public static Vec2 ZERO		= new Vec2(0, 0);
	
	
	public Vec2()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Vec2(Vec2 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
	}
	
	public Vec2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
}
