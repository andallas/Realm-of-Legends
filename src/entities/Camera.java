package entities;

import behaviors.Behavior;
import gameObjects.GameObject;


public class Camera extends Behavior
{
	private float pitch = 20;
	private float yaw;
	private float roll;
	
	
	public Camera(GameObject gameObject)
	{
		super(gameObject);
	}
	
	public void InvertPitch()
	{
		this.pitch = -this.pitch;
	}
	
	public float GetPitch() { return pitch; }
	public void SetPitch(float pitch) { this.pitch = pitch; }
	public float GetYaw() { return yaw; }
	public void SetYaw(float yaw) { this.yaw = yaw; }
	public float GetRoll() { return roll; }
	public void SetRoll(float roll) { this.roll = roll; }
	
}