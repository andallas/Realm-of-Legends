package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import gameObjects.ComponentType;
import gameObjects.GameObject;
import gameObjects.RenderComponent;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import utility.MathUtil;

public class GameObjectRenderer
{
	private StaticShader shader;
	
	
	public GameObjectRenderer(StaticShader shader, Matrix4f projectionMatrix)
	{
		this.shader = shader;
		shader.Start();
		shader.LoadProjectionMatrix(projectionMatrix);
		shader.Stop();
	}
	
	public void Render(Map<TexturedModel, List<GameObject>> gameObjects)
	{
		for (TexturedModel model:gameObjects.keySet())
		{
			PrepareTexturedModel(model);
			List<GameObject> batch = gameObjects.get(model);
			int vertexCount = model.GetRawModel().GetVertexCount();
			for (GameObject gameObject:batch)
			{
				PrepareInstance(gameObject);
				GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
			}
			UnbindTexturedModel();
		}
	}
	
	private void PrepareTexturedModel(TexturedModel texturedModel)
	{
		RawModel rawModel = texturedModel.GetRawModel();
		
		GL30.glBindVertexArray(rawModel.GetVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		ModelTexture texture = texturedModel.GetTexture();
		shader.LoadNumberOfRows(texture.GetNumberOfRows());
		if (texture.IsTransparent())
		{
			MasterRenderer.DisableCulling();
		}
		shader.LoadFakeLighting(texture.IsFakeLighting());
		shader.LoadSpecularVariables(texture.GetShineDampening(), texture.GetReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.GetID());
	}
	
	private void UnbindTexturedModel()
	{
		MasterRenderer.EnableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void PrepareInstance(GameObject gameObject)
	{
		shader.LoadTransformationMatrix(MathUtil.CreateTransformationMatrix(gameObject.transform));
		// TODO: Consider caching the render component on the game
		// object as this will be accessed a lot
		RenderComponent renderComponent = ((RenderComponent)gameObject.GetComponent(ComponentType.Render));
		shader.LoadOffset(renderComponent.GetTextureXOffset(), renderComponent.GetTextureYOffset());
	}
	
}