package textures;

public class TerrainTexturePack
{
	private TerrainTexture splat1;
	private TerrainTexture splat2;
	private TerrainTexture splat3;
	private TerrainTexture splat4;
	
	
	public TerrainTexturePack(TerrainTexture splat1, TerrainTexture splat2, TerrainTexture splat3, TerrainTexture splat4)
	{
		this.splat1 = splat1;
		this.splat2 = splat2;
		this.splat3 = splat3;
		this.splat4 = splat4;
	}
	
	public TerrainTexture GetSplat1() { return splat1; }
	public TerrainTexture GetSplat2() { return splat2; }
	public TerrainTexture GetSplat3() { return splat3; }
	public TerrainTexture GetSplat4() { return splat4; }
	
}