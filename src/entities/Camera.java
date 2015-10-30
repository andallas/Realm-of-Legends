package entities;

import org.lwjgl.input.Mouse;

import utility.Vec3;

// TODO: Decouple the camera from the player
public class Camera
{
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vec3 position = new Vec3(0, 50, 50);
	private float pitch = 20;
	private float yaw;
	private float roll;
	
	private Player player;
	
	public Camera() { }
	
	public Camera(Player player)
	{
		this.player = player;
	}

	public void Move()
	{
		if (this.player != null)
		{
			CalculatZoom();
			CalculatePitch();
			CalculateAngleAroundPlayer();
			float horizontalDistance = CalculateHorizontalDistance();
			float verticalDistance = CalculateVerticalDistance();
			CalculateCameraPosition(horizontalDistance, verticalDistance);
			this.yaw = 180 - (player.GetRotation().y + angleAroundPlayer);
		}
	}
	
	public void InvertPitch()
	{
		this.pitch = -this.pitch;
	}
	
	
	private void CalculateCameraPosition(float horizontal, float vertical)
	{
		float theta = player.GetRotation().y + angleAroundPlayer;
		float offsetX = (float)(horizontal * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float)(horizontal * Math.cos(Math.toRadians(theta)));
		position.x = player.GetPosition().x - offsetX;
		position.y = player.GetPosition().y + vertical;
		position.z = player.GetPosition().z - offsetZ;
	}
	
	private float CalculateHorizontalDistance()
	{
		return (float)(distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float CalculateVerticalDistance()
	{
		return (float)(distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void CalculatZoom()
	{
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	private void CalculatePitch()
	{
		if (Mouse.isButtonDown(1))
		{
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	private void CalculateAngleAroundPlayer()
	{
		if (Mouse.isButtonDown(0))
		{
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
	
	public Vec3 GetPosition() { return position; }
	public float GetPitch() { return pitch; }
	public float GetYaw() { return yaw; }
	public float GetRoll() { return roll; }
	
}