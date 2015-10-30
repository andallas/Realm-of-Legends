package utility;

import org.lwjgl.util.vector.Vector4f;

public class Vec4 extends Vector4f
{
	private static final long serialVersionUID = 6007224512974465558L;
	
	public static Vec4 ZERO		= new Vec4(0, 0,  0, 0);
	public static Vec4 UP		= new Vec4(0, 1,  0, 0);
	public static Vec4 FORWARD	= new Vec4(0, 0, -1, 0);
	public static Vec4 RIGHT	= new Vec4(1, 0,  0, 0);
	
	
	public Vec4()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 0;
	}
	
	public Vec4(Vec4 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		this.z = vec.w;
	}
	
	public Vec4(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
}
