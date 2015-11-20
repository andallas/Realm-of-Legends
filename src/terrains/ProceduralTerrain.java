package terrains;

import java.awt.image.BufferedImage;

import models.RawModel;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class ProceduralTerrain extends Terrain
{
	public ProceduralTerrain(int gridX, int gridZ)
	{
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = GenerateTerrain();
		
		TerrainMaster.Add(this);
	}
	
	@Override
	protected RawModel GenerateTerrain()
	{
		return model;
	}
	
	private float GenerateHeight(int x, int z)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TerrainTexturePack GetTexturePack()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TerrainTexture GetSplatMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
