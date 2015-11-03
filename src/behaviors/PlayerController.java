package behaviors;

import org.lwjgl.input.Keyboard;

import gameObjects.GameObject;
import terrains.TerrainMaster;
import utility.Time;

public class PlayerController extends Behavior
{
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	private static final float SPRINT_SPEED = 50;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private boolean isInAir = false;
	
	
	public PlayerController(GameObject go)
	{
		super(go);
	}

	private void Jump()
	{
		if (!isInAir)
		{
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	public void Update()
	{
		super.Update();
		CheckInputs();
		
		transform.Rotate(0, currentTurnSpeed * Time.Delta(), 0);
		
		float distance = currentSpeed * Time.Delta();
		float dx = (float)(distance * Math.sin(Math.toRadians(transform.rotation.y)));
		float dz = (float)(distance * Math.cos(Math.toRadians(transform.rotation.y)));
		transform.Translate(dx, 0, dz);
		
		upwardsSpeed += GRAVITY * Time.Delta();
		transform.Translate(0, upwardsSpeed * Time.Delta(), 0);
		
		float terrainHeight = TerrainMaster.GetHeight(transform.position.x, transform.position.z);
		if (transform.position.y < terrainHeight)
		{
			upwardsSpeed = 0;
			transform.position.y = terrainHeight;
			isInAir = false;
		}
	}
	
	private void CheckInputs()
	{
		float speedBoost = 0;
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			speedBoost = SPRINT_SPEED;
		}
		else
		{
			speedBoost = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			this.currentSpeed = RUN_SPEED + speedBoost;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			this.currentSpeed = -(RUN_SPEED + speedBoost);
		}
		else
		{
			this.currentSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			this.currentTurnSpeed = -TURN_SPEED;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			this.currentTurnSpeed = TURN_SPEED;
		}
		else
		{
			this.currentTurnSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			Jump();
		}
	}
	
}