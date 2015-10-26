package textures;

// TODO: This class is becoming more and more like a Material, consider changing it.
public class ModelTexture
{
	private int textureID;
	
	private float shineDampening = 1;
	private float reflectivity = 0;
	
	private boolean isTransparent = false;
	private boolean isFakeLighting = false;

	public ModelTexture(int id)
	{
		textureID = id;
	}
	
	public int GetID() { return this.textureID; }

	public float GetShineDampening() { return shineDampening; }
	public void SetShineDampening(float shineDampening) { this.shineDampening = shineDampening; }

	public float GetReflectivity() { return reflectivity; }
	public void SetReflectivity(float reflectivity) { this.reflectivity = reflectivity; }
	
	public boolean IsTransparent() { return isTransparent; }
	public void SetTransparent(boolean isTransparent) { this.isTransparent = isTransparent; }
	
	public boolean IsFakeLighting() { return isFakeLighting; }
	public void SetUseFakeLighting(boolean isFakeLighting) { this.isFakeLighting = isFakeLighting; }
	
}