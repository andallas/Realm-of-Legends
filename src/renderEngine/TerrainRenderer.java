package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.TerrainTexturePack;
import utility.MathUtil;

public class TerrainRenderer
{
	private TerrainShader shader;
	
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix)
	{
		this.shader = shader;
		shader.Start();
		shader.LoadProjectionMatrix(projectionMatrix);
		shader.LoadSplatMaps();
		shader.Stop();
	}
	
	public void Render(List<Terrain> terrains)
	{
		for (Terrain terrain:terrains)
		{
			PrepareTerrain(terrain);
			LoadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.GetModel().GetVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			UnbindTerrain();
		}
	}
	
	private void PrepareTerrain(Terrain terrain)
	{
		RawModel rawModel = terrain.GetModel();
		
		GL30.glBindVertexArray(rawModel.GetVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		BindTextures(terrain);
		shader.LoadSpecularVariables(1, 0);
	}
	
	private void BindTextures(Terrain terrain)
	{
		TerrainTexturePack texturePack = terrain.GetTexturePack();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.GetSplat1().GetTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.GetSplat2().GetTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.GetSplat3().GetTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.GetSplat4().GetTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.GetSplatMap().GetTextureID());
	}
	
	private void UnbindTerrain()
	{
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void LoadModelMatrix(Terrain terrain)
	{
		Matrix4f transformationMatrix = MathUtil.CreateTransformationMatrix(new Vector3f(terrain.GetX(), 0, terrain.GetZ()),
																			new Vector3f(0, 0, 0),
																			new Vector3f(1, 1, 1));
		shader.LoadTransformationMatrix(transformationMatrix);
	}
	
}