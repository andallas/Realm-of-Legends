package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
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
		
		
		
	// ************ Terrain ************
		TerrainTexture splat1 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/grassGround001.png"));
		TerrainTexture splat2 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/dirtGround001.png"));
		TerrainTexture splat3 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/flowersGround001.png"));
		TerrainTexture splat4 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/pathGround001.png"));
		TerrainTexture splatMap = new TerrainTexture(TextureLoader.LoadTexture("Terrain/splatMap.png"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(splat1, splat2, splat3, splat4);
		
		Terrain terrain = new Terrain(0, 0, texturePack, splatMap, "Terrain/heightmap.png");
	// *********************************
		
		
		
	// ******** Terrain Objects ********
		TexturedModel treeModel = new TexturedModel(	OBJLoader.LoadObj("tree.obj").GetRawModel(),
														new ModelTexture(TextureLoader.LoadTexture("TerrainObjects/tree001.png")));
		TexturedModel grassModel = new TexturedModel(	OBJLoader.LoadObj("grass.obj").GetRawModel(),
														new ModelTexture(TextureLoader.LoadTexture("TerrainObjects/grasses.png")));
		grassModel.GetTexture().SetTransparent(true);
		grassModel.GetTexture().SetUseFakeLighting(true);
		grassModel.GetTexture().SetNumberOfRows(4);
		TexturedModel fernModel = new TexturedModel(	OBJLoader.LoadObj("fern.obj").GetRawModel(),
														new ModelTexture(TextureLoader.LoadTexture("TerrainObjects/ferns.png")));
		fernModel.GetTexture().SetTransparent(true);
		fernModel.GetTexture().SetUseFakeLighting(true);
		fernModel.GetTexture().SetNumberOfRows(2);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		float terrainOffset = 800;
		
		int grassCount = 500;
		for (int i = 0; i < grassCount; i++)
		{
			/*float grassX = random.nextFloat() * terrainOffset;
			float grassZ = random.nextFloat() * terrainOffset;
			float grassY = terrain.GetHeight(grassX, grassZ);
			entities.add(	new Entity(grassModel,
							random.nextInt(grassModel.GetTexture().GetNumberOfRows()),
							new Vector3f(grassX, grassY, grassZ),
							new Vector3f(0, random.nextFloat() * 360, 0),
							5));*/
			
			float fernX = random.nextFloat() * terrainOffset;
			float fernZ = random.nextFloat() * terrainOffset;
			float fernY = terrain.GetHeight(fernX, fernZ);
			entities.add(	new Entity(fernModel,
							random.nextInt(fernModel.GetTexture().GetNumberOfRows()),
							new Vector3f(fernX, fernY, fernZ),
							new Vector3f(0, random.nextFloat() * 360, 0),
							0.85f));
		}
		
		int treeCount = 250;
		for (int j = 0; j < treeCount; j++)
		{
			float treeX = random.nextFloat() * terrainOffset;
			float treeZ = random.nextFloat() * terrainOffset;
			float treeY = terrain.GetHeight(treeX, treeZ);
			entities.add(	new Entity(treeModel,
							new Vector3f(treeX, treeY, treeZ),
							new Vector3f(0, random.nextFloat() * 360, 0),
							12));
		}
	// *********************************
		
		
		
	// ************ Lights *************
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0, 20000, 20000), new Vector3f(1, 1, 1)));
		lights.add(new Light(new Vector3f(-200, 50, -200), new Vector3f(10, 0, 0)));
		lights.add(new Light(new Vector3f( 200, 50,  200), new Vector3f(0, 10, 0)));
		lights.add(new Light(new Vector3f( 200, 50, -200), new Vector3f(0, 0, 10)));
		
	// *********************************
		
	
		
		
	// ************ Player *************
		TexturedModel playerModel = new TexturedModel(	OBJLoader.LoadObj("person.obj").GetRawModel(),
														new ModelTexture(TextureLoader.LoadTexture("person.png")));
		Player player = new Player(playerModel, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1);
		Camera camera = new Camera(player);
	// *********************************
		
		
		
	// ************** GUI **************
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui1 = new GuiTexture(TextureLoader.LoadTexture("socuwan.png"), new Vector2f(0.3f, 0.58f), new Vector2f(0.4f, 0.4f));
		GuiTexture gui2 = new GuiTexture(TextureLoader.LoadTexture("socuwan.png"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		guis.add(gui1);
		guis.add(gui2);
		
		GuiRenderer guiRenderer = new GuiRenderer();
	// *********************************
		
		
		
		while (!Display.isCloseRequested())
		{
			// Game Logic
			camera.Move();
			player.Move(terrain);
			
			// Rendering
			renderer.ProcessTerrain(terrain);
			
			renderer.ProcessEntity(player);
			for (Entity entity:entities)
			{
				renderer.ProcessEntity(entity);
			}
			
			renderer.Render(lights, camera);
			
			guiRenderer.Render(guis);
			
			DisplayManager.UpdateDisplay();
		}
		
		guiRenderer.CleanUp();
		renderer.CleanUp();
		ModelLoader.CleanUp();
		TextureLoader.CleanUp();
		DisplayManager.CloseDisplay();
	}
}