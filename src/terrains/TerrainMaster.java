package terrains;

import java.util.ArrayList;
import java.util.List;

public class TerrainMaster
{
	private static List<Terrain> _terrains = new ArrayList<Terrain>();
	
	public static float GetHeight(float x, float z)
	{
		return _terrains.get(0).GetHeight(x, z);
	}
	
	public static void Add(Terrain terrain)
	{
		_terrains.add(terrain);
	}

	public static List<Terrain> GetAllTerrains()
	{
		return _terrains;
	}

}
