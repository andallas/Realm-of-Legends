package entities;

import org.lwjgl.input.Keyboard;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;
import utility.Vec3;

public class Player extends Entity
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
	
	
	public Player(TexturedModel model, Vec3 position, Vec3 rotation, float scale)
	{
		super(model, position, rotation, scale);
	}

	private void Jump()
	{
		if (!isInAir)
		{
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	public void Move(Terrain terrain)
	{
		CheckInputs();
		super.IncreaseRotation(0, currentTurnSpeed * DisplayManager.Delta(), 0);
		float distance = currentSpeed * DisplayManager.Delta();
		float dx = (float)(distance * Math.sin(Math.toRadians(super.GetRotation().y)));
		float dz = (float)(distance * Math.cos(Math.toRadians(super.GetRotation().y)));
		super.IncreasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManager.Delta();
		super.IncreasePosition(0, upwardsSpeed * DisplayManager.Delta(), 0);
		float terrainHeight = terrain.GetHeight(super.GetPosition().x, super.GetPosition().z);
		if (super.GetPosition().y < terrainHeight)
		{
			upwardsSpeed = 0;
			super.GetPosition().y = terrainHeight;
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










