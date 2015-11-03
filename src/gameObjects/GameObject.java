package gameObjects;

import java.util.ArrayList;
import java.util.List;

import utility.RingBuffer;

/*import behaviors.Behavior;*/

public class GameObject
{
	// TODO: Add tags
	public Transform transform;
	public String name;
	
	/*private List<Behavior> _behaviors = new ArrayList<Behavior>();*/
	private List<IComponent> _components = new ArrayList<IComponent>();
	private RingBuffer<ComponentMessage> _messageQueue = new RingBuffer<ComponentMessage>(ComponentMessage.class, 100);
	
	public GameObject()
	{
		// TODO: After editor is created, keep track of all 'default' game objects created, and name them "GameObject001, GameObject002, etc..."
		this.name = "";
		transform = new Transform(this);
		AddComponent(transform);
	}
	
	public GameObject(String name)
	{
		this.name = name;
		transform = new Transform(this);
		AddComponent(transform);
	}
	
	public void AddComponent(IComponent component)
	{
		_components.add(component);
	}
	
	public void Send(ComponentMessage message, IComponent component)
	{
		_messageQueue.Enqueue(message);
	}
	
	public void Start() { }
	
	public void Update()
	{
		ComponentMessage message;
		while ((message = _messageQueue.Dequeue()) != null)
		{	
			for (IComponent _component:_components)
			{
				if (_component != null)
				{
					_component.Receive(message);
				}
			}
		}
	}
	
}
