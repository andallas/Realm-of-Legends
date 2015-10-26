package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import renderEngine.ModelLoader;
import renderEngine.OBJLoader;
import renderEngine.TextureLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;


public class MainGameLoop
{
	public static void main(String[] args)
	{
		DisplayManager.CreateDisplay();
		MasterRenderer renderer = new MasterRenderer();
		
		TexturedModel treeModel = new TexturedModel(	OBJLoader.LoadObj("tree.obj").GetRawModel(),
														new ModelTexture(TextureLoader.LoadTexture("tree.png")));
		TexturedModel grassModel = new TexturedModel(	OBJLoader.LoadObj("grass.obj").GetRawModel(),
														new ModelTexture(TextureLoader.LoadTexture("grass.png")));
		grassModel.GetTexture().SetTransparent(true);
		grassModel.GetTexture().SetUseFakeLighting(true);
		TexturedModel fernModel = new TexturedModel(	OBJLoader.LoadObj("fern.obj").GetRawModel(),
														new ModelTexture(TextureLoader.LoadTexture("fern.png")));
		fernModel.GetTexture().SetTransparent(true);
		fernModel.GetTexture().SetUseFakeLighting(true);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		float terrainOffset = 800;
		
		int grassCount = 500;
		for (int i = 0; i < grassCount; i++)
		{
			entities.add(new Entity(grassModel,
									new Vector3f(random.nextFloat() * terrainOffset, 0, random.nextFloat() * terrainOffset),
									new Vector3f(0, 0, 0),
									5));
			entities.add(new Entity(fernModel,
									new Vector3f(random.nextFloat() * terrainOffset, 0, random.nextFloat() * terrainOffset),
									new Vector3f(0, 0, 0),
									0.85f));
		}
		
		int treeCount = 250;
		for (int j = 0; j < treeCount; j++)
		{
			entities.add(new Entity(treeModel,
					new Vector3f(random.nextFloat() * terrainOffset, 0, random.nextFloat() * terrainOffset),
					new Vector3f(0, 0, 0),
					12));
		}
		
		// ************ Terrain ************
		
		TerrainTexture splat1 = new TerrainTexture(TextureLoader.LoadTexture("grassGround.png"));
		TerrainTexture splat2 = new TerrainTexture(TextureLoader.LoadTexture("dirtGround.png"));
		TerrainTexture splat3 = new TerrainTexture(TextureLoader.LoadTexture("flowersGround.png"));
		TerrainTexture splat4 = new TerrainTexture(TextureLoader.LoadTexture("pathGround.png"));
		TerrainTexture splatMap = new TerrainTexture(TextureLoader.LoadTexture("splatMap.png"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(splat1, splat2, splat3, splat4);
		
		Terrain terrain1 = new Terrain(0, 0, texturePack, splatMap, "heightmap.png");
		
		// *********************************
		
		Light light = new Light(new Vector3f(0, 20000, 20000), new Vector3f(1, 1, 1));
		
		TexturedModel playerModel = new TexturedModel(	OBJLoader.LoadObj("person.obj").GetRawModel(),
														new ModelTexture(TextureLoader.LoadTexture("person.png")));
		Player player = new Player(playerModel, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1);
		Camera camera = new Camera(player);
		
		while (!Display.isCloseRequested())
		{
			// Game Logic
			camera.Move();
			player.Move();
			
			// Rendering
			renderer.ProcessTerrain(terrain1);
			
			renderer.ProcessEntity(player);
			for (Entity entity:entities)
			{
				renderer.ProcessEntity(entity);
			}
			
			renderer.Render(light, camera);
			
			DisplayManager.UpdateDisplay();
		}
		
		renderer.CleanUp();
		ModelLoader.CleanUp();
		TextureLoader.CleanUp();
		DisplayManager.CloseDisplay();
	}
}