package engineTester;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.Display;

import behaviorFramework.Behavior;
import behaviors.Camera;
import behaviors.CameraFollow;
import behaviors.PlayerController;
import gameObjectFramework.ComponentType;
import gameObjectFramework.GameObject;
import gameObjectFramework.GameObjectMaster;
import gameObjects.Light;
import gameObjects.RenderComponent;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.RenderMaster;
import renderEngine.ModelLoader;
import renderEngine.OBJLoader;
import renderEngine.TextureLoader;
import terrains.ProceduralTerrain;
import textures.ModelTexture;
import utility.Time;
import utility.Vec4;
import utility.Logger;
import utility.Logger.Level;


public class MainGameLoop
{
	public static void main(String[] args)
	{
		Logger.SetLogLevel(Level.INFO);
		
		DisplayManager.CreateDisplay();
		Time.Initialize();
		RenderMaster renderer = new RenderMaster();


		
		
	// ************** GUI **************
		/*TextMaster.Init();
		// TODO: Create a 'container' class that can hold a GUIText
		FontType font = new FontType(TextureLoader.LoadFontTexture("Verdana.png"), new File("res/Textures/Fonts/Verdana.fnt"));
		GUIText text = new GUIText("This is a test text!", 3f, font, new Vec2(0f, 0.5f), 1f, true);
		text.SetColor(0.1176470588235294f, 0.7725490196078431f, 0.0117647058823529f);*/
	// *********************************
		
		
		
		
	// ************ Terrain ************
		InitializeProceduralTerrain();
		
		/*List<WaterTile> waterTiles = new ArrayList<WaterTile>();
		WaterFrameBuffers waterFBOs = new WaterFrameBuffers();
		
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(waterShader, renderer.GetProjectionMatrix(), waterFBOs);

		WaterTile water = new WaterTile(400, 400, 0);
		waterTiles.add(water);
		
		Vec4 reflectionClipPlane = new Vec4(Vec3.UP, -water.GetHeight() + 10f);
		Vec4 refractionClipPlane = new Vec4(Vec3.LEFT, water.GetHeight() + 1f);*/
	// *********************************
		
		
		
		
	// ********** Game Objects *********
		GameObjectMaster gameObjectMaster = new GameObjectMaster();
		
		//InitializeTerrainObjects(gameObjects, terrain);
		
		// TODO: When editor is complete, allow serialized GameObjects to be created and stored as 'prefabs'
		GameObject player = new GameObject();
		player.AddBehavior(new PlayerController(player));
		
		RenderComponent renderComponent = new RenderComponent(player);
		TexturedModel playerModel = new TexturedModel(	OBJLoader.LoadObj("person.obj").GetRawModel(),
				new ModelTexture(TextureLoader.LoadTexture("person.png")));
		renderComponent.SetModel(playerModel);
		player.AddComponent(renderComponent);
		
		gameObjectMaster.Add(player);
	// *********************************
		
		
		
		
	// ************ Lights *************
		List<Light> lights = new ArrayList<Light>();
		
		//InitializeLights(lights);
	// *********************************
		
		
		
		
	// ************ Camera *************
		GameObject camera = new GameObject();
		
		Behavior cameraScript = new Camera(camera);
		camera.AddBehavior(cameraScript);
		
		Behavior cameraFollowScript = new CameraFollow(camera);
		((CameraFollow)cameraFollowScript).camera = (Camera)cameraScript;
		((CameraFollow)cameraFollowScript).player = (PlayerController)player.GetComponent(ComponentType.PlayerController);
		camera.AddBehavior(cameraFollowScript);
		
		camera.transform.SetPosition(0, 50, 50);
		gameObjectMaster.Add(camera);
	// *********************************
		
		
		
		
		while (!Display.isCloseRequested())
		{
			Time.Update();
			
		// ********** Game Logic ***********
			gameObjectMaster.Update();
		// *********************************
			
			
			
			
		// ************ Render *************
			// Render reflection texture
			/*float distance = 2 * (camera.GetPosition().y - water.GetHeight());
			camera.GetPosition().y -= distance;
			camera.InvertPitch();
			waterFBOs.BindReflectionFrameBuffer();
			renderer.RenderScene(gameObjects, terrains, lights, camera, reflectionClipPlane);
			camera.GetPosition().y += distance;
			camera.InvertPitch();

			// Render refraction texture
			waterFBOs.BindRefractionFrameBuffer();
			renderer.RenderScene(gameObjects, terrains, lights, camera, refractionClipPlane);
			waterFBOs.UnbindCurrentFrameBuffer();*/
			
			// Render to screen
			renderer.RenderScene(gameObjectMaster.GetRenderable(), lights, camera.GetComponent(ComponentType.Camera), Vec4.ZERO());
			// TODO: This renderer should be handled in the master renderer
			//waterRenderer.render(waterTiles, camera, lights.get(0));

			// Render GUIs
			//TextMaster.Render();
		// *********************************
			
			
			
			
			DisplayManager.UpdateDisplay();
		}
		
		//TextMaster.CleanUp();
		//waterFBOs.CleanUp();
		//waterShader.CleanUp();
		renderer.CleanUp();
		ModelLoader.CleanUp();
		TextureLoader.CleanUp();
		
		DisplayManager.CloseDisplay();
	}

	/*private static HeightmapTerrain InitializeHeightmapTerrain()
	{
		TerrainTexture splat1	= new TerrainTexture(TextureLoader.LoadTexture("Terrain/grassGround001.png"));
		TerrainTexture splat2	= new TerrainTexture(TextureLoader.LoadTexture("Terrain/dirtGround001.png"));
		TerrainTexture splat3	= new TerrainTexture(TextureLoader.LoadTexture("Terrain/flowersGround001.png"));
		TerrainTexture splat4	= new TerrainTexture(TextureLoader.LoadTexture("Terrain/pathGround001.png"));
		TerrainTexture splatMap	= new TerrainTexture(TextureLoader.LoadTexture("Terrain/splatMap.png"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(splat1, splat2, splat3, splat4);
		
		return new HeightmapTerrain(0, 0, texturePack, splatMap, "Terrain/heightmap.png");
	}*/
	
	private static ProceduralTerrain InitializeProceduralTerrain()
	{
		return new ProceduralTerrain(0, 0);
	}

	/*private static void InitializeTerrainObjects(List<GameObject> gameObjects, Terrain terrain)
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
			gameObjects.add(new GameObject(fernModel,
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
			
			gameObjects.add(new GameObject(treeModel,
							new Vec3(treeX, treeY, treeZ),
							new Vec3(0, random.nextFloat() * 360, 0),
							12));
		}
	}

	private static void InitializeLights(List<Light> lights)
	{
		Vec3 attenuation = new Vec3(1, 0.01f, 0.002f);
		lights.add(new Light(new Vec3(0, -1000, -7000), new Vec3(0.8f, 0.8f, 0.8f)));
		lights.add(new Light(new Vec3(25, 5, 0), new Vec3(2, 0, 0), attenuation));
		lights.add(new Light(new Vec3(0, 5, 25), new Vec3(0, 2, 2), attenuation));
		lights.add(new Light(new Vec3(25, 5, 25), new Vec3(2, 2, 0), attenuation));
	}
*/

}