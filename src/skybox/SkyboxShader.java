package skybox;

import org.lwjgl.util.vector.Matrix4f;

import behaviors.Camera;
import shaders.ShaderProgram;
import utility.MathUtil;
import utility.Time;
import utility.Vec3;

public class SkyboxShader extends ShaderProgram
{
	private static final String VERTEX_FILE = "src/skybox/skybox.vert";
	private static final String FRAGMENT_FILE = "src/skybox/skybox.frag";
	
	private static final float ROTATE_SPEED = 1f;
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;
	private int location_cubeMapBlendFactor;
	private int location_cubeMap1;
	private int location_cubeMap2;
	
	private float currentRotation = 0f;
	
	public SkyboxShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void GetAllUniformLocations()
	{
		location_projectionMatrix	= super.GetUniformLocation("projectionMatrix");
		location_viewMatrix			= super.GetUniformLocation("viewMatrix");
		location_fogColor			= super.GetUniformLocation("fogColor");
		location_cubeMapBlendFactor	= super.GetUniformLocation("cubeMapBlendFactor");
		location_cubeMap1			= super.GetUniformLocation("cubeMap1");
		location_cubeMap2			= super.GetUniformLocation("cubeMap2");
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
		currentRotation += ROTATE_SPEED * Time.Delta();
		Matrix4f.rotate((float)Math.toRadians(currentRotation), new Vec3(0, 1, 0), matrix, matrix);
		super.LoadMatrix(location_viewMatrix, matrix);
	}
	
	public void LoadFogColor(float r, float g, float b)
	{
		super.LoadVector(location_fogColor, new Vec3(r, g, b));
	}
	
	public void LoadCubeMapBlendFactor(float factor)
	{
		super.LoadFloat(location_cubeMapBlendFactor, factor);
	}

	public void ConnectTextureUnits()
	{
		super.LoadInt(location_cubeMap1, 0);
		super.LoadInt(location_cubeMap2, 1);
	}
	
	@Override
	protected void BindAttributes()
	{
		super.BindAttribute(0, "position");
	}
	
}