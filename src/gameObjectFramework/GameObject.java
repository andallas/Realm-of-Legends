package gameObjectFramework;

import java.util.ArrayList;
import java.util.List;

import behaviorFramework.Behavior;
import behaviorFramework.IBehavior;
import gameObjects.RenderComponent;
import gameObjects.Transform;
import utility.Logger;
import utility.RingBuffer;


public class GameObject
{
	// TODO: Add tags to GameObjects
	public Transform transform;
	public RenderComponent renderer;
	public String name;
	
	
	private List<IBehavior> _behaviors = new ArrayList<IBehavior>();
	private List<Component> _components = new ArrayList<Component>();
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
	
	
	public void AddComponent(Component component)
	{
		if (component.GetType() == ComponentType.Render)
		{
			if (renderer != null)
			{
				Logger.Error("Game object already has a RenderComponent, cannot add another one.");
				return;
			}
			else
			{
				renderer = (RenderComponent)component;
			}
		}
		_components.add(component);
	}
	
	public void AddBehavior(Behavior behavior)
	{
		_behaviors.add(behavior);
		AddComponent(behavior);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T GetComponent(ComponentType type)
	{
		for (Component component : _components)
		{
			if (component.GetType() == type)
			{
				return (T)component;
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
		for (IBehavior behavior:_behaviors)
		{
			behavior.Start();
		}
	}
	
	public void Update()
	{
		ComponentMessage message;
		while ((message = _messageQueue.Dequeue()) != null)
		{	
			for (IComponent component : _components)
			{
				if (component != null)
				{
					component.Receive(message);
				}
			}
		}
		
		for (IBehavior behavior : _behaviors)
		{
			behavior.Update();
		}
	}
	
}
