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
		for (int i = 0; i < 500; i++)
		{
			entities.add(new Entity(treeModel,
									new Vector3f(random.nextFloat() * 800 - 400, -0.15f, random.nextFloat() * -600),
									new Vector3f(0, 0, 0),
									new Vector3f(5, 12, 5)));
			entities.add(new Entity(grassModel,
									new Vector3f(random.nextFloat() * 800 - 400, -0.15f, random.nextFloat() * -600),
									new Vector3f(0, 0, 0),
									3));
			entities.add(new Entity(fernModel,
									new Vector3f(random.nextFloat() * 800 - 400, -0.5f, random.nextFloat() * -600),
									new Vector3f(0, 0, 0),
									0.85f));
		}
		
		// ************ Terrain ************
		
		TerrainTexture splat1 = new TerrainTexture(TextureLoader.LoadTexture("grassGround.png"));
		TerrainTexture splat2 = new TerrainTexture(TextureLoader.LoadTexture("dirtGround.png"));
		TerrainTexture splat3 = new TerrainTexture(TextureLoader.LoadTexture("flowersGround.png"));
		TerrainTexture splat4 = new TerrainTexture(TextureLoader.LoadTexture("pathGround.png"));
		TerrainTexture splatMap = new TerrainTexture(TextureLoader.LoadTexture("splatMap.png"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(splat1, splat2, splat3, splat4);
		
		Terrain terrain1 = new Terrain(-1, -1, texturePack, splatMap);
		Terrain terrain2 = new Terrain(0, -1, texturePack, splatMap);
		
		// *********************************
		
		Light light = new Light(new Vector3f(0, 100, -100), new Vector3f(1, 1, 1));
		
		TexturedModel bunnyModel = new TexturedModel(OBJLoader.LoadObj("bunny.obj").GetRawModel());
		Player player = new Player(bunnyModel, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1);
		Camera camera = new Camera(player);
		
		while (!Display.isCloseRequested())
		{
			// Game Logic
			camera.Move();
			player.Move();
			
			// Rendering
			renderer.ProcessTerrain(terrain1);
			renderer.ProcessTerrain(terrain2);
			
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