package behaviors;

import org.lwjgl.input.Mouse;

import entities.Camera;
import gameObjects.ComponentType;
import gameObjects.GameObject;
import utility.Vec3;

public class CameraFollow extends Behavior
{
	public CameraFollow(GameObject gameObject)
	{
		super(gameObject);
		_type = ComponentType.CameraFollow;
	}

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	public Camera camera;
	private PlayerController player;
	
	
	public void Update()
	{
		super.Update();
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
		transform.SetPosition(newPos);
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