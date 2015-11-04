package gameObjects;

import java.util.ArrayList;
import java.util.List;

public class GameObjectMaster
{
	// TODO: Have another list of activeGameObjects
	private List<GameObject> _toStart = new ArrayList<GameObject>();
	private List<GameObject> _updatable = new ArrayList<GameObject>();
	private List<GameObject> _renderable = new ArrayList<GameObject>();
	
	public GameObjectMaster() { }
	
	public void Add(GameObject gameObject)
	{
		_toStart.add(gameObject);
	}
	
	public void Remove(GameObject gameObject) { }
	
	public void Update()
	{
		ProcessNewGameObjects();
		
		for (GameObject gameObject : _updatable)
		{
			gameObject.Update();
		}
	}
	
	public List<GameObject> GetGameObjects() { return _updatable; }
	
	public List<GameObject> GetRenderable() { return _renderable; }
	
	
	private void ProcessNewGameObjects()
	{
		for (GameObject toStartGameObject : _toStart)
		{
			toStartGameObject.Start();
			_updatable.add(toStartGameObject);
			
			if (toStartGameObject.renderer != null)
			{
				_renderable.add(toStartGameObject);
			}
		}
		_toStart.clear();
	}
	
}