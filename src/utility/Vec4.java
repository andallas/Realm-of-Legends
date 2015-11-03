package utility;

import org.lwjgl.util.vector.Vector4f;

public class Vec4 extends Vector4f
{
	private static final long serialVersionUID = 6007224512974465558L;
	
	public static Vec4 ZERO()		{ return new Vec4( 0,  0,  0,  0); }
	public static Vec4 UP()			{ return new Vec4( 0,  1,  0,  0); }
	public static Vec4 DOWN()		{ return new Vec4( 0, -1,  0,  0); }
	public static Vec4 FORWARD()	{ return new Vec4( 0,  0, -1,  0); }
	public static Vec4 BACK()		{ return new Vec4( 0,  0,  1,  0); }
	public static Vec4 RIGHT()		{ return new Vec4( 1,  0,  0,  0); }
	public static Vec4 LEFT()		{ return new Vec4(-1,  0,  0,  0); }
	
	
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
	
	public Vec4(Vec3 vec, float w)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		this.w = w;
	}
	
	public String ToString()
	{
		return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
	}
	
}
