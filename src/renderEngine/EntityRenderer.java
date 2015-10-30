package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import utility.MathUtil;

public class EntityRenderer
{
	private StaticShader shader;
	
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix)
	{
		this.shader = shader;
		shader.Start();
		shader.LoadProjectionMatrix(projectionMatrix);
		shader.Stop();
	}
	
	public void Render(Map<TexturedModel, List<Entity>> entities)
	{
		for (TexturedModel model:entities.keySet())
		{
			PrepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			int vertexCount = model.GetRawModel().GetVertexCount();
			for (Entity entity:batch)
			{
				PrepareInstance(entity);
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
	
	private void PrepareInstance(Entity entity)
	{
		Matrix4f transformationMatrix = MathUtil.CreateTransformationMatrix(entity.GetPosition(),
																			entity.GetRotation(),
																			entity.GetScale());
		shader.LoadTransformationMatrix(transformationMatrix);
		shader.LoadOffset(entity.GetTextureXOffset(), entity.GetTextureYOffset());
	}
	
}