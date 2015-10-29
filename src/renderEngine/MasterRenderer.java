package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;

public class MasterRenderer
{
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	
	private static final float RED		= 0.3f;
	private static final float GREEN	= 0.3f;
	private static final float BLUE		= 0.3f;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	private SkyboxRenderer skyboxRenderer;
	
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	
	public MasterRenderer()
	{
		EnableCulling();
		CreateProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
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
	
	public void RenderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Camera camera)
	{
		for (Terrain terrain:terrains)
		{
			ProcessTerrain(terrain);
		}
		
		for (Entity entity:entities)
		{
			ProcessEntity(entity);
		}
		
		Render(lights, camera);
	}
	
	public void Render(List<Light> lights, Camera camera)
	{
		// TODO: Only load lights if they change
		// TODO: Sort lights by distance and only
		// load the closest MAX_LIGHTS to the shader
		Prepare();
		
		shader.Start();
		shader.LoadSkyColor(RED, GREEN, BLUE);
		shader.LoadLights(lights);
		shader.LoadViewMatrix(camera);
		renderer.Render(entities);
		shader.Stop();
		entities.clear();
		
		terrainShader.Start();
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
	
	public void ProcessEntity(Entity entity)
	{
		TexturedModel entityModel = entity.GetModel();
		List<Entity> batch = entities.get(entityModel);
		
		if (batch != null)
		{
			batch.add(entity);
		}
		else
		{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
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