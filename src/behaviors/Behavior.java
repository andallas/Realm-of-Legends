package behaviors;

import gameObjects.Component;
import gameObjects.ComponentMessage;
import gameObjects.GameObject;

public class Behavior extends Component implements IBehavior
{
	public Behavior(GameObject gameObject)
	{
		super(gameObject);
	}

	@Override
	public void Start() { }

	@Override
	public void Update() { }

	@Override
	public void Receive(ComponentMessage message) { }

	@Override
	public String ToString() { return null; }

}