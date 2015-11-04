package gameObjects;

import java.util.ArrayList;
import java.util.List;

public class GameObjectMaster
{
	// TODO: Have another list of activeGameObjects
	private List<GameObject> _toStart = new ArrayList<GameObject>();
	private List<GameObject> _gameObjects = new ArrayList<GameObject>();
	
	public GameObjectMaster() { }
	
	public void Add(GameObject gameObject)
	{
		_toStart.add(gameObject);
	}
	
	public void Remove(GameObject gameObject) { }
	
	public void Update()
	{
		for (GameObject toStartGOs : _toStart)
		{
			toStartGOs.Start();
			_gameObjects.add(toStartGOs);
		}
		_toStart.clear();
		
		for (GameObject gameObject : _gameObjects)
		{
			gameObject.Update();
		}
	}
	
	public List<GameObject> GetGameObjects() { return _gameObjects; }
}