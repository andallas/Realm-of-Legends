package water;

import org.lwjgl.util.vector.Matrix4f;

import behaviors.Camera;
import entities.Light;
import shaders.ShaderProgram;
import utility.MathUtil;

// TODO: Flickering edges of water
// TODO: Fog not applied to water
public class WaterShader extends ShaderProgram
{
	private final static String VERTEX_FILE = "src/water/water.vert";
    private final static String FRAGMENT_FILE = "src/water/water.frag";
 
    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;
    private int location_dUdVMap;
    private int location_normalMap;
    private int location_depthMap;
    private int location_waveFactor;
    private int location_cameraPosition;
    private int location_lightColor;
    private int location_lightPosition;
 
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
        location_reflectionTexture	= GetUniformLocation("reflectionTexture");
        location_refractionTexture	= GetUniformLocation("refractionTexture");
        location_dUdVMap			= GetUniformLocation("dUdVMap");
        location_normalMap			= GetUniformLocation("normalMap");
        location_waveFactor			= GetUniformLocation("waveFactor");
        location_cameraPosition		= GetUniformLocation("cameraPosition");
        location_lightColor			= GetUniformLocation("lightColor");
        location_lightPosition		= GetUniformLocation("lightPosition");
        location_depthMap			= GetUniformLocation("depthMap");
    }
 
    public void LoadProjectionMatrix(Matrix4f projection)
    {
        LoadMatrix(location_projectionMatrix, projection);
    }
     
    public void LoadViewMatrix(Camera camera)
    {
        Matrix4f viewMatrix = MathUtil.CreateViewMatrix(camera);
        LoadMatrix(location_viewMatrix, viewMatrix);
        super.LoadVector(location_cameraPosition, camera.transform.position);
    }
 
    public void LoadModelMatrix(Matrix4f modelMatrix)
    {
        LoadMatrix(location_modelMatrix, modelMatrix);
    }
    
    public void LoadWaveFactor(float factor)
    {
    	super.LoadFloat(location_waveFactor, factor);
    }
    
    public void LoadLight(Light light)
    {
    	super.LoadVector(location_lightColor, light.GetColor());
    	super.LoadVector(location_lightPosition, light.GetPosition());
    }
    
    public void ConnectTextureUnits()
    {
    	super.LoadInt(location_reflectionTexture, 0);
    	super.LoadInt(location_refractionTexture, 1);
    	super.LoadInt(location_dUdVMap, 2);
    	super.LoadInt(location_normalMap, 3);
    	super.LoadInt(location_depthMap, 4);
    }

}