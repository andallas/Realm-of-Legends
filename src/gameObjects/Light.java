package gameObjects;

import gameObjectFramework.GameObject;
import utility.Vec3;

public class Light extends GameObject
{
	private Vec3 color;
	private Vec3 attenuation = new Vec3(1, 0, 0);
	
	public Light(Vec3 position, Vec3 color)
	{
		transform.position = position;
		this.color = color;
	}
	
	public Light(Vec3 position, Vec3 color, Vec3 attenuation)
	{
		transform.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}

	public Vec3 GetAttenuation() { return attenuation; }
	public void SetAttenuation(Vec3 attenuation) { this.attenuation = attenuation; }

	public Vec3 GetColor() { return color; }
	public void SetColor(Vec3 color) { this.color = color; }
		
}