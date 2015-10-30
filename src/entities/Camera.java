package entities;

import utility.Vec3;


public class Camera
{
	private Vec3 position = new Vec3(0, 50, 50);
	private float pitch = 20;
	private float yaw;
	private float roll;
	
	
	public Camera() { }
	
	public void InvertPitch()
	{
		this.pitch = -this.pitch;
	}
	
	public Vec3 GetPosition() { return position; }
	public void SetPosition(Vec3 position) { this.position = position; }
	public float GetPitch() { return pitch; }
	public void SetPitch(float pitch) { this.pitch = pitch; }
	public float GetYaw() { return yaw; }
	public void SetYaw(float yaw) { this.yaw = yaw; }
	public float GetRoll() { return roll; }
	public void SetRoll(float roll) { this.roll = roll; }
	
}