package guis;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

public class GuiShader extends ShaderProgram
{
	private static final String VERTEX_FILE		= "src/guis/gui.vert";
	private static final String FRAGMENT_FILE	= "src/guis/gui.frag";
	
	private int location_transformationMatrix;
	
	public GuiShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void LoadTransformation(Matrix4f matrix)
	{
		super.LoadMatrix(location_transformationMatrix, matrix);
	}
	
	@Override
	protected void GetAllUniformLocations()
	{
		location_transformationMatrix = super.GetUniformLocation("transformationMatrix");
	}

	@Override
	protected void BindAttributes()
	{
		super.BindAttribute(0, "position");
	}

}
