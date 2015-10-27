package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.ModelLoader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import utility.MathUtil;

public class Terrain
{
	public static final String RES_LOC = "res/Textures/";
	
	private static final float SIZE = 800;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	
	private float[][] heights;
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture splatMap;
	
	
	public Terrain(int gridX, int gridZ, TerrainTexturePack texturePack, TerrainTexture splatMap, String heightMap)
	{
		this.texturePack = texturePack;
		this.splatMap = splatMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = GenerateTerrain(heightMap);
	}
	
	private RawModel GenerateTerrain(String heightMap)
	{
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(new File("res/Textures/" + heightMap));
		}
		catch (FileNotFoundException e)
		{
			System.err.println("ERROR: File not found at location: " + RES_LOC + heightMap);
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Unable to load file at location: " + RES_LOC + heightMap);
		}
		
		int vertexCount = image.getHeight(); 
		heights = new float[vertexCount][vertexCount];
		
		
		int count = vertexCount * vertexCount;
		float[] vertices	= new float[count * 3];
		float[] normals		= new float[count * 3];
		float[] uvCoords	= new float[count * 2];
		int[] indices		= new int[6 * (vertexCount - 1) * (vertexCount - 1)];
		
		int vertexPointer = 0;
		for(int i = 0; i < vertexCount; i++)
		{
			for(int j = 0; j < vertexCount; j++)
			{
				float height = GenerateHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer * 3]		= (float)j / ((float)vertexCount - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float)i / ((float)vertexCount - 1) * SIZE;
				
				Vector3f normal = CalculateNormal(j, i, image);
				normals[vertexPointer * 3]		= normal.x;
				normals[vertexPointer * 3 + 1]	= normal.y;
				normals[vertexPointer * 3 + 2]	= normal.z;
				
				uvCoords[vertexPointer * 2]		= (float)j / ((float)vertexCount - 1);
				uvCoords[vertexPointer * 2 + 1]	= (float)i / ((float)vertexCount - 1);
				vertexPointer++;
			}
		}
		
		int pointer = 0;
		for(int gz = 0; gz < vertexCount - 1; gz++)
		{
			for(int gx = 0; gx < vertexCount - 1; gx++)
			{
				int topLeft = (gz * vertexCount)+ gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexCount)+ gx;
				int bottomRight = bottomLeft + 1;
				
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return ModelLoader.LoadToVAO(vertices, uvCoords, normals, indices);
	}

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
			result = MathUtil.BarryCentric(	new Vector3f(0, heights[gridX][gridZ], 0),
											new Vector3f(1, heights[gridX + 1][gridZ], 0),
											new Vector3f(0, heights[gridX][gridZ + 1], 1),
											new Vector2f(xCoord, zCoord));
		}
		else
		{
			result = MathUtil.BarryCentric(	new Vector3f(1, heights[gridX + 1][gridZ], 0),
											new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
											new Vector3f(0, heights[gridX][gridZ + 1], 1),
											new Vector2f(xCoord, zCoord));
		}
		
		return result;
	}
	
	private Vector3f CalculateNormal(int x, int z, BufferedImage image)
	{
		float heightL = GetHeightFromArray(x - 1, z);
		float heightR = GetHeightFromArray(x + 1, z);
		float heightD = GetHeightFromArray(x, z - 1);
		float heightU = GetHeightFromArray(x, z + 1);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}
	
	private float GetHeightFromArray(int x, int z)
	{
		int size = heights.length;
		if (x < 0 || x >= size ||
			z < 0 || z >= size)
		{
			return 0;
		}
		
		return heights[x][z];
	}
	
	private float GenerateHeight(int x, int z, BufferedImage image)
	{
		float imageHeight = image.getHeight();
		if (x < 0 || x >= imageHeight ||
			z < 0 || z >= imageHeight)
		{
			return 0;
		}
		
		float terrainHeight = image.getRGB(x, z);
		terrainHeight += MAX_PIXEL_COLOR * 0.5f;
		terrainHeight /= MAX_PIXEL_COLOR * 0.5f;
		terrainHeight *= MAX_HEIGHT;
		
		return terrainHeight;
	}

	
	public float GetX() { return x; }
	public float GetZ() { return z; }
	public RawModel GetModel() { return model; }
	public TerrainTexturePack GetTexturePack() { return texturePack; }
	public TerrainTexture GetSplatMap() { return splatMap; }
	
}