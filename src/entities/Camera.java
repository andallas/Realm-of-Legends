package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera
{
	private Vector3f position = new Vector3f(0, 7, 0);
	private float pitch = 15;
	private float yaw;
	private float roll;
	private float speed = 0.3f;
	
	public Camera() {}

	public void Move()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			this.speed = 2f;
		}
		else
		{
			this.speed = 0.3f;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			position.z -= speed;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			position.z += speed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			position.x += speed;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			position.x -= speed;
		}
	}
	
	public Vector3f GetPosition() { return position; }

	public float GetPitch() { return pitch; }

	public float GetYaw() { return yaw; }

	public float GetRoll() { return roll; }
	
	
}