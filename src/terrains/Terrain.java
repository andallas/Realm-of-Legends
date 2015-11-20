package terrains;

import java.awt.image.BufferedImage;

import models.RawModel;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import utility.MathUtil;
import utility.Vec2;
import utility.Vec3;

public abstract class Terrain
{
	protected static final float SIZE = 800;
	protected static final float MAX_HEIGHT = 40;
	
	protected float[][] heights;
	
	protected float x;
	protected float z;
	protected RawModel model;
	
	
	public float GetHeight(float worldX, float worldZ)
	{
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float)heights.length - 1);
		
		int gridX = (int)Math.floor(terrainX / gridSquareSize);
		int gridZ = (int)Math.floor(terrainZ / gridSquareSize);
		
		if (gridX >= heights.length - 1 || gridX < 0 ||
			gridZ >= heights.length - 1 || gridZ < 0)
		{
			return 0;
		}
		
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		
		float result;
		if (xCoord <= (1 - zCoord))
		{
			result = MathUtil.BarryCentric(	new Vec3(0, heights[gridX][gridZ], 0),
											new Vec3(1, heights[gridX + 1][gridZ], 0),
											new Vec3(0, heights[gridX][gridZ + 1], 1),
											new Vec2(xCoord, zCoord));
		}
		else
		{
			result = MathUtil.BarryCentric(	new Vec3(1, heights[gridX + 1][gridZ], 0),
											new Vec3(1, heights[gridX + 1][gridZ + 1], 1),
											new Vec3(0, heights[gridX][gridZ + 1], 1),
											new Vec2(xCoord, zCoord));
		}
		
		return result;
	}

	public float GetX() { return x; }
	public float GetZ() { return z; }
	public RawModel GetModel() { return model; }
	
	
	protected Vec3 CalculateNormal(int x, int z, BufferedImage image)
	{
		float heightL = GetHeightFromArray(x - 1, z);
		float heightR = GetHeightFromArray(x + 1, z);
		float heightD = GetHeightFromArray(x, z - 1);
		float heightU = GetHeightFromArray(x, z + 1);
		Vec3 normal = new Vec3(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}
	
	protected float GetHeightFromArray(int x, int z)
	{
		int size = heights.length;
		if (x < 0 || x >= size ||
			z < 0 || z >= size)
		{
			return 0;
		}
		
		return heights[x][z];
	}

	
	public abstract TerrainTexturePack GetTexturePack();
	public abstract TerrainTexture GetSplatMap();
	
	
	protected abstract RawModel GenerateTerrain();
	
}