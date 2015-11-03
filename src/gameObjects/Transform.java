package gameObjects;

import utility.Vec3;

public class Transform extends Component
{
	// TODO: Add transform parenting
	//public Transform parent;
	
	public Vec3 position;
	public Vec3 rotation;
	public Vec3 scale;
	
	
	public Transform(GameObject gameObject)
	{
		super(gameObject);
		//this.parent = null;
		this.position = new Vec3();
		this.rotation = new Vec3();
		this.scale = new Vec3(1, 1, 1);
	}
	
	
	public void Translate() { }
	
	public void Rotate() { }


	@Override
	public void Receive(ComponentMessage message)
	{
		// TODO Auto-generated method stub
		System.out.println(message);
	}


	@Override
	public String ToString()
	{
		String result = "position: " + position.ToString() + " rotation: " + rotation.ToString() + " scale: " + scale.ToString();
		return result;
	}
	
}
