package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

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
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;


public class MainGameLoop
{
	public static void main(String[] args)
	{
		DisplayManager.CreateDisplay();
		MasterRenderer renderer = new MasterRenderer();
		
		
		
	// ************ Terrain ************
		List<Terrain> terrains = new ArrayList<Terrain>();
		
		TerrainTexture splat1 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/grassGround001.png"));
		TerrainTexture splat2 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/dirtGround001.png"));
		TerrainTexture splat3 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/flowersGround001.png"));
		TerrainTexture splat4 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/pathGround001.png"));
		TerrainTexture splatMap = new TerrainTexture(TextureLoader.LoadTexture("Terrain/splatMap.png"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(splat1, splat2, splat3, splat4);
		
		Terrain terrain = new Terrain(0, 0, texturePack, splatMap, "Terrain/heightmap.png");
		terrains.add(terrain);
	// *********************************
		
		
		
	// ******** Terrain Objects ********
		List<Entity> entities = new ArrayList<Entity>();
		
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

		Random random = new Random();
		float terrainOffset = 800;
		
		int grassCount = 500;
		for (int i = 0; i < grassCount; i++)
		{	
			float fernX = random.nextFloat() * terrainOffset;
			float fernZ = random.nextFloat() * terrainOffset;
			float fernY = terrain.GetHeight(fernX, fernZ);
			if (fernY <= 0)
			{
				continue;
			}
			
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
			if (treeY <= 0)
			{
				continue;
			}
			
			entities.add(	new Entity(treeModel,
							new Vector3f(treeX, treeY, treeZ),
							new Vector3f(0, random.nextFloat() * 360, 0),
							12));
		}
	// *********************************
		
		
		
	// ************ Lights *************
		List<Light> lights = new ArrayList<Light>();
		
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f)));
		lights.add(new Light(new Vector3f(25, 5, 0), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(0, 5, 25), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(25, 5, 25), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));
	// *********************************
		
	
		
		
	// ************ Player *************
		TexturedModel playerModel = new TexturedModel(	OBJLoader.LoadObj("person.obj").GetRawModel(),
														new ModelTexture(TextureLoader.LoadTexture("person.png")));
		Player player = new Player(playerModel, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1);
		entities.add(player);
		Camera camera = new Camera(player);
	// *********************************
		
		
		
		
	// ************ Water **************
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(waterShader, renderer.GetProjectionMatrix());
		List<WaterTile> waterTiles = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(400, 400, 0);
		waterTiles.add(water);
		
		WaterFrameBuffers waterFBOs = new WaterFrameBuffers();
		
		GuiRenderer guiRenderer = new GuiRenderer();
		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
		GuiTexture reflection = new GuiTexture(waterFBOs.GetReflectionTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		GuiTexture refraction = new GuiTexture(waterFBOs.GetRefractionTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		guiTextures.add(reflection);
		guiTextures.add(refraction);
		
		Vector4f reflectionClipPlane = new Vector4f(0, 1, 0, -water.GetHeight());
		Vector4f refractionClipPlane = new Vector4f(0, -1, 0, water.GetHeight());
		// TODO: Wrap vector classes to add custom functionality, like static ZERO, UP, FORWARD, etc...
		Vector4f zeroClipPlane = new Vector4f(0, 0, 0, 0);
	// *********************************
		

		
		while (!Display.isCloseRequested())
		{
		// ********** Game Logic ***********
			camera.Move();
			player.Move(terrain);
		// *********************************
			
			
			
		// ************ Render *************
			// Render reflection texture
			float distance = 2 * (camera.GetPosition().y - water.GetHeight());
			camera.GetPosition().y -= distance;
			camera.InvertPitch();
			waterFBOs.BindReflectionFrameBuffer();
			renderer.RenderScene(entities, terrains, lights, camera, reflectionClipPlane);
			camera.GetPosition().y += distance;
			camera.InvertPitch();

			// Render refraction texture
			waterFBOs.BindRefractionFrameBuffer();
			renderer.RenderScene(entities, terrains, lights, camera, refractionClipPlane);
			
			// Render to screen
			waterFBOs.UnbindCurrentFrameBuffer();
			renderer.RenderScene(entities, terrains, lights, camera, zeroClipPlane);
			waterRenderer.render(waterTiles, camera);

			// Render GUIs
			guiRenderer.Render(guiTextures);
		// *********************************
			
			
			
			DisplayManager.UpdateDisplay();
		}
		
		guiRenderer.CleanUp();
		waterFBOs.CleanUp();
		waterShader.CleanUp();
		renderer.CleanUp();
		ModelLoader.CleanUp();
		TextureLoader.CleanUp();
		
		DisplayManager.CloseDisplay();
	}
}