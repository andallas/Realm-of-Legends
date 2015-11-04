package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import behaviors.Camera;
import models.RawModel;
import renderEngine.ModelLoader;
import renderEngine.TextureLoader;
import utility.Time;

public class SkyboxRenderer
{
	private static final float SIZE = 5000f;
	
	private static final float[] VERTICES =
	{
		// Right
		-SIZE,  SIZE, -SIZE,
		-SIZE, -SIZE, -SIZE,
		 SIZE, -SIZE, -SIZE,
		 SIZE, -SIZE, -SIZE,
		 SIZE,  SIZE, -SIZE,
		-SIZE,  SIZE, -SIZE,
		// Left
		-SIZE, -SIZE,  SIZE,
		-SIZE, -SIZE, -SIZE,
		-SIZE,  SIZE, -SIZE,
		-SIZE,  SIZE, -SIZE,
		-SIZE,  SIZE,  SIZE,
		-SIZE, -SIZE,  SIZE,
		// Top
		 SIZE, -SIZE, -SIZE,
		 SIZE, -SIZE,  SIZE,
		 SIZE,  SIZE,  SIZE,
		 SIZE,  SIZE,  SIZE,
		 SIZE,  SIZE, -SIZE,
		 SIZE, -SIZE, -SIZE,
		// Bottom
		-SIZE, -SIZE,  SIZE,
		-SIZE,  SIZE,  SIZE,
		 SIZE,  SIZE,  SIZE,
		 SIZE,  SIZE,  SIZE,
		 SIZE, -SIZE,  SIZE,
		-SIZE, -SIZE,  SIZE,
		// Back
		-SIZE,  SIZE, -SIZE,
		 SIZE,  SIZE, -SIZE,
		 SIZE,  SIZE,  SIZE,
		 SIZE,  SIZE,  SIZE,
		-SIZE,  SIZE,  SIZE,
		-SIZE,  SIZE, -SIZE,
		// Front
		-SIZE, -SIZE, -SIZE,
		-SIZE, -SIZE,  SIZE,
		 SIZE, -SIZE, -SIZE,
		 SIZE, -SIZE, -SIZE,
		-SIZE, -SIZE,  SIZE,
		 SIZE, -SIZE,  SIZE
	};

	private static String[] DAY_TEXTURE_FILES = {"Skybox/dayRt.png", "Skybox/dayLt.png", "Skybox/dayTp.png", "Skybox/dayBt.png", "Skybox/dayBk.png", "Skybox/dayFt.png"};
	private static String[] NIGHT_TEXTURE_FILES = {"Skybox/nightRt.png", "Skybox/nightLt.png", "Skybox/nightTp.png", "Skybox/nightBt.png", "Skybox/nightBk.png", "Skybox/nightFt.png"};
	
	private RawModel cube;
	private int dayTexture;
	private int nightTexture;
	private SkyboxShader shader;
	private float currentTime = 0;
	
	public SkyboxRenderer(Matrix4f projectionMatrix)
	{
		cube = ModelLoader.LoadToVAO(VERTICES, 3);
		dayTexture = TextureLoader.LoadCubeMap(DAY_TEXTURE_FILES);
		nightTexture = TextureLoader.LoadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		
		shader.Start();
		shader.ConnectTextureUnits();
		shader.LoadProjectionMatrix(projectionMatrix);
		shader.Stop();
	}
	
	public void Render(Camera camera, float r, float g, float b)
	{
		shader.Start();
		shader.LoadViewMatrix(camera);
		shader.LoadFogColor(r, g, b);
		
		GL30.glBindVertexArray(cube.GetVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		BindTextures();
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.GetVertexCount());
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
		shader.Stop();
	}
	
	private void BindTextures()
	{
		currentTime += Time.Delta() * 1000;
		currentTime %= 24000;
		
		int texture1;
		int texture2;
		float blendFactor;
		
		if (currentTime >= 0 && currentTime < 5000)
		{
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = (currentTime - 0) / (5000 - 0);
		}
		else if (currentTime >= 5000 && currentTime < 8000)
		{
			texture1 = nightTexture;
			texture2 = dayTexture;
			blendFactor = (currentTime - 5000) / (8000 - 5000);
		}
		else if (currentTime >= 8000 && currentTime < 21000)
		{
			texture1 = dayTexture;
			texture2 = dayTexture;
			blendFactor = (currentTime - 8000) / (21000 - 8000);
		}
		else
		{
			texture1 = dayTexture;
			texture2 = nightTexture;
			blendFactor = (currentTime - 21000) / (24000 - 21000);
		}
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		
		shader.LoadCubeMapBlendFactor(blendFactor);
	}
	
}