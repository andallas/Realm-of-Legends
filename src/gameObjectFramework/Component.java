package gameObjectFramework;

import gameObjects.Transform;

public abstract class Component implements IComponent
{
	public GameObject gameObject;
	public Transform transform;

	protected ComponentType _type;
	
	
	public Component(GameObject gameObject)
	{
		this.gameObject = gameObject;
		this.transform = this.gameObject.transform;
	}
	
	public ComponentType GetType() { return _type; }
	
}
