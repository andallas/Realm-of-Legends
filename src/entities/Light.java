package entities;

import utility.Vec3;

public class Light
{
	private Vec3 position;
	private Vec3 color;
	private Vec3 attenuation = new Vec3(1, 0, 0);
	
	public Light(Vec3 position, Vec3 color)
	{
		this.position = position;
		this.color = color;
	}
	
	public Light(Vec3 position, Vec3 color, Vec3 attenuation)
	{
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}

	public Vec3 GetAttenuation() { return attenuation; }
	public void SetAttenuation(Vec3 attenuation) { this.attenuation = attenuation; }

	public Vec3 GetPosition() { return position; }
	public void SetPosition(Vec3 position) { this.position = position; }

	public Vec3 GetColor() { return color; }
	public void SetColor(Vec3 color) { this.color = color; }
		
}