package gameObjects;

import utility.Vec3;

public class Transform extends Component
{
	// TODO: Add transform parenting
	//public Transform parent;
	
	public Vec3 position;
	public Vec3 rotation;
	public Vec3 scale;
	
	
	public Transform()
	{
		//this.parent = null;
		this.position = new Vec3();
		this.rotation = new Vec3();
		this.scale = new Vec3(1, 1, 1);
	}
	
	
	public void Translate() { }
	
	public void Rotate() { }
	
}
