package gameObjectFramework;

public interface IComponent
{
	public void Receive(ComponentMessage message);
	public String ToString();
	
}
