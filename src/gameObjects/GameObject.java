package gameObjects;

import java.util.ArrayList;
import java.util.List;

import utility.RingBuffer;


public class GameObject
{
	// TODO: Add tags to GameObjects
	public Transform transform;
	public String name;
	
	
	// TODO: Implement Behaviors to control custom scripting of GameObjects
	/*private List<Behavior> _behaviors = new ArrayList<Behavior>();*/
	private List<Component> _components = new ArrayList<Component>();
	private RingBuffer<ComponentMessage> _messageQueue = new RingBuffer<ComponentMessage>(ComponentMessage.class, 100);
	
	public GameObject()
	{
		// TODO: After editor is created, keep track of all 'default' game
		// objects created, and name them "GameObject001, GameObject002, etc..."
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
	
	
	public void AddComponent(Component component)
	{
		_components.add(component);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T GetComponent(ComponentType type)
	{
		for (Component _component:_components)
		{
			if (_component.GetType() == type)
			{
				return (T)_component;
			}
		}
		
		return null;
	}
	
	public void Send(ComponentMessage message, IComponent component)
	{
		_messageQueue.Enqueue(message);
	}
	
	public void Start()
	{
		// TODO: Implement start method
	}
	
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
