package water;

public class WaterTile
{
	public static final float TILE_SIZE = 60;
	 
    private float height;
    private float x;
    private float z;
     
    public WaterTile(float centerX, float centerZ, float height)
    {
        this.x = centerX;
        this.z = centerZ;
        this.height = height;
    }

    public float GetHeight() { return height; }
    public float GetX() { return x; }
    public float GetZ() { return z; }
}