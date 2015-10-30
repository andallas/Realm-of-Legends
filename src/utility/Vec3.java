package utility;

import org.lwjgl.util.vector.Vector3f;

public class Vec3 extends Vector3f
{
	private static final long serialVersionUID = 3274719008202078666L;

	public static Vec3 ZERO		= new Vec3(0, 0, 0);
	public static Vec3 UP		= new Vec3(0, 1, 0);
	public static Vec3 FORWARD	= new Vec3(0, 0, -1);
	public static Vec3 RIGHT	= new Vec3(1, 0, 0);
	
	
	public Vec3()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Vec3(Vec3 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public Vec3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
}