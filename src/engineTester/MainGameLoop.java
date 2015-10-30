package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;

import entities.Camera;
import entities.Entity;
import entities.Light;
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
import utility.Vec3;
import utility.Vec4;
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
		//Terrain terrain = InitializeTerrain(terrains);
		//terrains.add(terrain);
	// *********************************
		
		
		
	// ************ Entities ***********
		List<Entity> entities = new ArrayList<Entity>();
		
		//InitializeTerrainObjects(entities, terrain);
	// *********************************
		
		
		
	// ************ Lights *************
		List<Light> lights = new ArrayList<Light>();
		
		//InitializeLights(lights);
	// *********************************
		
	
		
		
	// ************ Player *************
		/*TexturedModel playerModel = new TexturedModel(	OBJLoader.LoadObj("person.obj").GetRawModel(),
														new ModelTexture(TextureLoader.LoadTexture("person.png")));
		Player player = new Player(playerModel, new Vec3(0, 0, 0), new Vec3(0, 0, 0), 1);
		entities.add(player);*/
		//Camera camera = new Camera(player);
		
		Camera camera = new Camera();
	// *********************************
		
		
		
		
	// ************ Water **************
		WaterFrameBuffers waterFBOs = new WaterFrameBuffers();
		
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(waterShader, renderer.GetProjectionMatrix(), waterFBOs);
		List<WaterTile> waterTiles = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(400, 400, 0);
		waterTiles.add(water);
		
		Vec4 reflectionClipPlane = new Vec4(0, 1, 0, -water.GetHeight() + 10f);
		Vec4 refractionClipPlane = new Vec4(0, -1, 0, water.GetHeight() + 1f);

		
		Vec4 zeroClipPlane = new Vec4(0, 0, 0, 0);
	// *********************************
		

		
		while (!Display.isCloseRequested())
		{
		// ********** Game Logic ***********
			//camera.Move();
			//player.Move(terrain);
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
			// TODO: This renderer should be handled in the master renderer
			//waterRenderer.render(waterTiles, camera, lights.get(0));

			// Render GUIs
		// *********************************
			
			
			
			DisplayManager.UpdateDisplay();
		}
		
		waterFBOs.CleanUp();
		waterShader.CleanUp();
		renderer.CleanUp();
		ModelLoader.CleanUp();
		TextureLoader.CleanUp();
		
		DisplayManager.CloseDisplay();
	}

	private static Terrain InitializeTerrain(List<Terrain> terrains)
	{
		TerrainTexture splat1 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/grassGround001.png"));
		TerrainTexture splat2 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/dirtGround001.png"));
		TerrainTexture splat3 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/flowersGround001.png"));
		TerrainTexture splat4 = new TerrainTexture(TextureLoader.LoadTexture("Terrain/pathGround001.png"));
		TerrainTexture splatMap = new TerrainTexture(TextureLoader.LoadTexture("Terrain/splatMap.png"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(splat1, splat2, splat3, splat4);
		
		return new Terrain(0, 0, texturePack, splatMap, "Terrain/heightmap.png");
	}

	private static void InitializeTerrainObjects(List<Entity> entities, Terrain terrain)
	{
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
							new Vec3(fernX, fernY, fernZ),
							new Vec3(0, random.nextFloat() * 360, 0),
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
							new Vec3(treeX, treeY, treeZ),
							new Vec3(0, random.nextFloat() * 360, 0),
							12));
		}
	}

	private static void InitializeLights(List<Light> lights)
	{
		lights.add(new Light(new Vec3(0, -1000, -7000), new Vec3(0.8f, 0.8f, 0.8f)));
		lights.add(new Light(new Vec3(25, 5, 0), new Vec3(2, 0, 0), new Vec3(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vec3(0, 5, 25), new Vec3(0, 2, 2), new Vec3(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vec3(25, 5, 25), new Vec3(2, 2, 0), new Vec3(1, 0.01f, 0.002f)));
	}






}