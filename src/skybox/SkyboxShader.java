package skybox;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import shaders.ShaderProgram;
import utility.MathUtil;

public class SkyboxShader extends ShaderProgram
{
	private static final String VERTEX_FILE = "src/skybox/skybox.vert";
	private static final String FRAGMENT_FILE = "src/skybox/skybox.frag";
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	public SkyboxShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void LoadProjectionMatrix(Matrix4f matrix)
	{
		super.LoadMatrix(location_projectionMatrix, matrix);
	}
	
	public void LoadViewMatrix(Camera camera)
	{
		Matrix4f matrix = MathUtil.CreateViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		super.LoadMatrix(location_viewMatrix, matrix);
	}
	
	@Override
	protected void GetAllUniformLocations()
	{
		location_projectionMatrix = super.GetUniformLocation("projectionMatrix");
		location_viewMatrix = super.GetUniformLocation("viewMatrix");
	}
	
	@Override
	protected void BindAttributes()
	{
		super.BindAttribute(0, "position");
	}
	
}