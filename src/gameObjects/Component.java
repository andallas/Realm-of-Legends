package gameObjects;

public class Component 
{
	public GameObject gameObject;
	public Transform transform;
	
	public Component(GameObject gameObject)
	{
		this.gameObject = gameObject;
		this.transform = this.gameObject.transform;
	}
	
}
