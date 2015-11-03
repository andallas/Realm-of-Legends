package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
import gameObjects.ComponentType;
import gameObjects.GameObject;
import gameObjects.RenderComponent;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import terrains.TerrainMaster;
import utility.Vec4;

public class MasterRenderer
{
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.01f;
	private static final float FAR_PLANE = 10000f;
	
	private static final float RED		= 0.6f;
	private static final float GREEN	= 0.7f;
	private static final float BLUE		= 0.6f;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private GameObjectRenderer gameObjectRenderer;
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	private SkyboxRenderer skyboxRenderer;
	
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private Map<TexturedModel, List<GameObject>> gameObjects = new HashMap<TexturedModel, List<GameObject>>();
	
	
	public MasterRenderer()
	{
		EnableCulling();
		CreateProjectionMatrix();
		gameObjectRenderer = new GameObjectRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(projectionMatrix);
	}
	
	
	public static void EnableCulling()
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void DisableCulling()
	{
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	
	public void Prepare()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}
	
	public void RenderScene(List<GameObject> gameObjects, List<Light> lights, Camera camera, Vec4 clipPlane)
	{
		List<Terrain> terrains = TerrainMaster.GetAllTerrains();
		for (Terrain terrain:terrains)
		{
			ProcessTerrain(terrain);
		}
		
		for (GameObject gameObject:gameObjects)
		{
			ProcessGameObject(gameObject);
		}
		
		Render(lights, camera, clipPlane);
	}
	
	public void Render(List<Light> lights, Camera camera, Vec4 clipPlane)
	{
		// TODO: Only load lights if they change
		// TODO: Sort lights by distance and only
		// load the closest MAX_LIGHTS to the shader
		Prepare();
		
		shader.Start();
		shader.LoadClipPlane(clipPlane);
		shader.LoadSkyColor(RED, GREEN, BLUE);
		shader.LoadLights(lights);
		shader.LoadViewMatrix(camera);
		gameObjectRenderer.Render(gameObjects);
		shader.Stop();
		gameObjects.clear();
		
		terrainShader.Start();
		terrainShader.LoadClipPlane(clipPlane);
		terrainShader.LoadSkyColor(RED, GREEN, BLUE);
		terrainShader.LoadLights(lights);
		terrainShader.LoadViewMatrix(camera);
		terrainRenderer.Render(terrains);
		terrainShader.Stop();
		terrains.clear();
		
		skyboxRenderer.Render(camera, RED, GREEN, BLUE);
	}
	
	public void ProcessTerrain(Terrain terrain)
	{
		terrains.add(terrain);
	}
	
	public void ProcessGameObject(GameObject gameObject)
	{
		// TODO: Consider caching the render component on the game
		// object as this will be accessed a lot
		RenderComponent renderComponent = ((RenderComponent)gameObject.GetComponent(ComponentType.Render));
		TexturedModel gameObjectModel = renderComponent.GetModel();
		List<GameObject> batch = gameObjects.get(gameObjectModel);
		
		if (batch != null)
		{
			batch.add(gameObject);
		}
		else
		{
			List<GameObject> newBatch = new ArrayList<GameObject>();
			newBatch.add(gameObject);
			gameObjects.put(gameObjectModel, newBatch);
		}
	}
	
	public void CleanUp()
	{
		shader.CleanUp();
		terrainShader.CleanUp();
	}
	
	
	private void CreateProjectionMatrix()
	{
		float aspectRatio = (float)Display.getWidth() / (float)Display.getHeight();
		float yScale = (float)(1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
		float xScale = yScale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
		projectionMatrix.m33 = 0;
	}

	
	public Matrix4f GetProjectionMatrix() { return projectionMatrix; }
}