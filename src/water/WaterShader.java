package water;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import shaders.ShaderProgram;
import utility.MathUtil;

public class WaterShader extends ShaderProgram
{
	private final static String VERTEX_FILE = "src/water/water.vert";
    private final static String FRAGMENT_FILE = "src/water/water.frag";
 
    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
 
    public WaterShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
    protected void BindAttributes()
    {
        BindAttribute(0, "position");
    }
 
    @Override
    protected void GetAllUniformLocations()
    {
        location_projectionMatrix	= GetUniformLocation("projectionMatrix");
        location_viewMatrix			= GetUniformLocation("viewMatrix");
        location_modelMatrix		= GetUniformLocation("modelMatrix");
    }
 
    public void LoadProjectionMatrix(Matrix4f projection)
    {
        LoadMatrix(location_projectionMatrix, projection);
    }
     
    public void LoadViewMatrix(Camera camera)
    {
        Matrix4f viewMatrix = MathUtil.CreateViewMatrix(camera);
        LoadMatrix(location_viewMatrix, viewMatrix);
    }
 
    public void LoadModelMatrix(Matrix4f modelMatrix)
    {
        LoadMatrix(location_modelMatrix, modelMatrix);
    }

}