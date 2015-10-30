package water;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.ModelLoader;
import renderEngine.TextureLoader;
import utility.MathUtil;

public class WaterRenderer
{
	private static final String DUDV_MAP = "Terrain/waterdUdV.png";
	private static final float WAVE_SPEED = 0.03f;
	
	private RawModel quad;
    private WaterShader shader;
    private WaterFrameBuffers waterFBOs;
    
    private int dUdVTexture;
    private float waveFactor = 0;
    
    public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers waterFBOs)
    {
        this.shader = shader;
        this.waterFBOs = waterFBOs;
        dUdVTexture = TextureLoader.LoadTexture(DUDV_MAP);
        
        shader.Start();
        shader.ConnectTextureUnits();
        shader.LoadProjectionMatrix(projectionMatrix);
        shader.Stop();
        SetUpVAO();
    }
 
    public void render(List<WaterTile> water, Camera camera)
    {
        PrepareRender(camera);  
        for (WaterTile tile : water)
        {
            Matrix4f modelMatrix = MathUtil.CreateTransformationMatrix(	new Vector3f(tile.GetX(), tile.GetHeight(), tile.GetZ()),
            															new Vector3f(0, 0, 0),
            															WaterTile.TILE_SIZE);
            shader.LoadModelMatrix(modelMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.GetVertexCount());
        }
        Unbind();
    }
     
    private void PrepareRender(Camera camera)
    {
        shader.Start();
        shader.LoadViewMatrix(camera);
        waveFactor += WAVE_SPEED * DisplayManager.Delta();
        waveFactor %= 1;
        shader.LoadWaveFactor(waveFactor);
        
        GL30.glBindVertexArray(quad.GetVaoID());
        GL20.glEnableVertexAttribArray(0);
        
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterFBOs.GetReflectionTexture());
        
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterFBOs.GetRefractionTexture());
        
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, dUdVTexture);
    }
     
    private void Unbind()
    {
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.Stop();
    }
 
    private void SetUpVAO()
    {
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = ModelLoader.LoadToVAO(vertices, 2);
    }
}