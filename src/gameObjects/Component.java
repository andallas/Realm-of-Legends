package gameObjects;

public abstract class Component implements IComponent
{
	public GameObject gameObject;
	public Transform transform;
	
	public Component(GameObject gameObject)
	{
		this.gameObject = gameObject;
		this.transform = this.gameObject.transform;
	}
	
}
