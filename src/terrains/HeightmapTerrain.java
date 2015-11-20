package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import models.RawModel;
import renderEngine.ModelLoader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import utility.Logger;
import utility.Vec3;

public class HeightmapTerrain extends Terrain
{
	public static final String RES_LOC = "res/Textures/";
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	
	private TerrainTexturePack texturePack;
	private TerrainTexture splatMap;
	private String heightMap;
	
	
	public HeightmapTerrain(int gridX, int gridZ, TerrainTexturePack texturePack, TerrainTexture splatMap, String heightMap)
	{
		this.texturePack = texturePack;
		this.splatMap = splatMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.heightMap = heightMap;
		this.model = GenerateTerrain();
		
		TerrainMaster.Add(this);
	}
	
	@Override
	protected RawModel GenerateTerrain()
	{
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(new File("res/Textures/" + heightMap));
		}
		catch (FileNotFoundException e)
		{
			Logger.Error("File not found at location: " + RES_LOC + heightMap);
		}
		catch (IOException e)
		{
			Logger.Error("Unable to load file at location: " + RES_LOC + heightMap);
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
				
				Vec3 normal = CalculateNormal(j, i, image);
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

	@Override
	public TerrainTexturePack GetTexturePack() { return texturePack; }
	
	@Override
	public TerrainTexture GetSplatMap() { return splatMap; }
	
}