package behaviors;

import org.lwjgl.input.Mouse;

import entities.Camera;
import gameObjects.Player;
import utility.Vec3;

public class CameraFollow implements Behavior
{
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Camera camera;
	private Player player;
	
	
	@Override
	public void Start() { }

	@Override
	public void Update()
	{
		if (this.player != null)
		{
			CalculatZoom();
			CalculatePitch();
			CalculateAngleAroundPlayer();
			float horizontalDistance = CalculateHorizontalDistance();
			float verticalDistance = CalculateVerticalDistance();
			CalculateCameraPosition(horizontalDistance, verticalDistance);
			camera.SetYaw(180 - (player.transform.rotation.y + angleAroundPlayer));
		}
	}
	
	
	private void CalculateCameraPosition(float horizontal, float vertical)
	{
		float theta = player.transform.rotation.y + angleAroundPlayer;
		float offsetX = (float)(horizontal * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float)(horizontal * Math.cos(Math.toRadians(theta)));
		Vec3 newPos = new Vec3(	player.transform.position.x - offsetX,
								player.transform.position.y + vertical,
								player.transform.position.z - offsetZ);
		camera.SetPosition(newPos);
	}
	
	private float CalculateHorizontalDistance()
	{
		return (float)(distanceFromPlayer * Math.cos(Math.toRadians(camera.GetPitch())));
	}
	
	private float CalculateVerticalDistance()
	{
		return (float)(distanceFromPlayer * Math.sin(Math.toRadians(camera.GetPitch())));
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
			camera.SetPitch(camera.GetPitch() - pitchChange);
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

}