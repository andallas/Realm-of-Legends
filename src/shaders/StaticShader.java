package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import utility.MathUtil;

public class StaticShader extends ShaderProgram
{
	private static final int MAX_LIGHTS = 4;
	private static final String VERTEX_FILE		= "src/shaders/default.vert";
	private static final String FRAGMENT_FILE	= "src/shaders/default.frag";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_shineDampening;
	private int location_reflectivity;
	private int location_fakeLighting;
	private int location_skyColor;
	private int location_numberOfRows;
	private int location_offset;
	private int location_lightPosition[];
	private int location_lightColor[];
	private int location_attenuation[];
	
	
	public StaticShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void BindAttributes()
	{
		super.BindAttribute(0, "position");
		super.BindAttribute(1, "textureCoord");
		super.BindAttribute(2, "normal");
	}

	@Override
	protected void GetAllUniformLocations()
	{
		location_transformationMatrix	= super.GetUniformLocation("transformationMatrix");
		location_projectionMatrix		= super.GetUniformLocation("projectionMatrix");
		location_viewMatrix				= super.GetUniformLocation("viewMatrix");
		location_shineDampening			= super.GetUniformLocation("shineDampening");
		location_reflectivity			= super.GetUniformLocation("reflectivity");
		location_fakeLighting			= super.GetUniformLocation("fakeLighting");
		location_skyColor				= super.GetUniformLocation("skyColor");
		location_numberOfRows			= super.GetUniformLocation("numberOfRows");
		location_offset					= super.GetUniformLocation("offset");
		
		location_lightPosition			= new int[MAX_LIGHTS];
		location_lightColor				= new int[MAX_LIGHTS];
		location_attenuation			= new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++)
		{
			location_lightPosition[i] = super.GetUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = super.GetUniformLocation("lightColor[" + i + "]");
			location_attenuation[i] = super.GetUniformLocation("attenuation[" + i + "]");
		}
	}
	
	
	public void LoadTransformationMatrix(Matrix4f matrix)
	{
		super.LoadMatrix(location_transformationMatrix, matrix); 
	}
	
	public void LoadProjectionMatrix(Matrix4f matrix)
	{
		super.LoadMatrix(location_projectionMatrix, matrix); 
	}
	
	public void LoadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = MathUtil.CreateViewMatrix(camera);
		super.LoadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void LoadLights(List<Light> lights)
	{
		for (int i = 0; i < MAX_LIGHTS; i++)
		{
			if (i < lights.size())
			{
				super.LoadVector(location_lightPosition[i], lights.get(i).GetPosition());
				super.LoadVector(location_lightColor[i], lights.get(i).GetColor());
				super.LoadVector(location_attenuation[i], lights.get(i).GetAttenuation());
			}
			else
			{
				super.LoadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.LoadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				super.LoadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	public void LoadSpecularVariables(float dampening, float reflectivity)
	{
		super.LoadFloat(location_shineDampening, dampening);
		super.LoadFloat(location_reflectivity, reflectivity);
	}
	
	public void LoadFakeLighting(boolean useFakeLighting)
	{
		super.LoadBoolean(location_fakeLighting, useFakeLighting);
	}
	
	public void LoadSkyColor(float r, float g, float b)
	{
		super.LoadVector(location_skyColor, new Vector3f(r, g, b));
	}
	
	public void LoadNumberOfRows(int numberOfRows)
	{
		super.LoadFloat(location_numberOfRows, numberOfRows);
	}
	
	public void LoadOffset(float x, float y)
	{
		super.LoadVector(location_offset, new Vector2f(x, y));
	}
	
}