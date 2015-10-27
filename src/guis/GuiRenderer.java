package guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import models.RawModel;
import renderEngine.ModelLoader;
import utility.MathUtil;

public class GuiRenderer
{
	private final RawModel quad;
	private GuiShader shader;
	
	public GuiRenderer()
	{
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = ModelLoader.LoadToVAO(positions);
		shader = new GuiShader();
	}
	
	public void Render(List<GuiTexture> guis)
	{
		shader.Start();
		GL30.glBindVertexArray(quad.GetVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		for (GuiTexture gui:guis)
		{
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.GetTexture());
			Matrix4f matrix = MathUtil.CreateTransformationMatrix(gui.GetPosition(), gui.GetScale());
			shader.LoadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.GetVertexCount());
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.Stop();
	}
	
	public void CleanUp()
	{
		shader.CleanUp();
	}
	
}