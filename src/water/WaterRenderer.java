package water;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import models.RawModel;
import renderEngine.ModelLoader;
import utility.MathUtil;

public class WaterRenderer
{
	private RawModel quad;
    private WaterShader shader;
 
    public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix)
    {
        this.shader = shader;
        shader.Start();
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
        GL30.glBindVertexArray(quad.GetVaoID());
        GL20.glEnableVertexAttribArray(0);
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