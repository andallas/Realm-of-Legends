package gameObjects;

/*import java.util.ArrayList;
import java.util.List;

import behaviors.Behavior;*/

public class GameObject
{
	// TODO: Add tags
	public Transform transform;
	public String name;
	
	/*private List<Behavior> behaviors = new ArrayList<Behavior>();*/
	
	public GameObject()
	{
		// TODO: After editor is created, keep track of all 'default' game objects created, and name them "GameObject001, GameObject002, etc..."
		this.name = "";
		transform = new Transform();
	}
	
	public GameObject(String name)
	{
		this.name = name;
		transform = new Transform();
	}
	
}
