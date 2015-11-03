package gameObjects;

public interface IComponent
{
	public void Receive(ComponentMessage message);
	public String ToString();
	
}
