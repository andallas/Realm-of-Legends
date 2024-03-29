package gameObjects;

import gameObjectFramework.Component;
import gameObjectFramework.ComponentMessage;
import gameObjectFramework.ComponentType;
import gameObjectFramework.GameObject;
import utility.Logger;
import utility.Vec3;

public class Transform extends Component
{
	// TODO: Add transform parenting
	
	public Vec3 position;
	public Vec3 rotation;
	public Vec3 scale;
	
	
	public Transform(GameObject gameObject)
	{
		super(gameObject);
		_type = ComponentType.Transform;
		this.position = new Vec3();
		this.rotation = new Vec3();
		this.scale = new Vec3(1, 1, 1);
	}

	
	public void Translate(float dx, float dy, float dz)
	{
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void Rotate(float dx, float dy, float dz)
	{
		this.rotation.x += dx;
		this.rotation.y += dy;
		this.rotation.z += dz;
	}

	public void SetPosition(float x, float y, float z)
	{
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public void SetPosition(Vec3 pos)
	{
		SetPosition(pos.x, pos.y, pos.z);
	}
	
	
	@Override
	public void Receive(ComponentMessage message)
	{
		// TODO: Complete 'Receive' method for Transform
		Logger.Info(message.toString());
	}

	@Override
	public String ToString()
	{
		String result = "position: " + this.position.ToString() + " rotation: " + this.rotation.ToString() + " scale: " + this.scale.ToString();
		return result;
	}
	
}
