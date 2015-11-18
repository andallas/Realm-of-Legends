package fontRendering;

import shaders.ShaderProgram;
import utility.Vec2;
import utility.Vec3;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/fontRendering/font.vert";
	private static final String FRAGMENT_FILE = "src/fontRendering/font.frag";
	
	private int location_color;
	private int location_translation;
	
	
	public FontShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void GetAllUniformLocations()
	{
		location_color			= super.GetUniformLocation("color");
		location_translation	= super.GetUniformLocation("translation");
	}

	@Override
	protected void BindAttributes()
	{
		super.BindAttribute(0, "position");
		super.BindAttribute(1, "textureCoords");
	}
	
	protected void LoadColor(Vec3 color)
	{
		super.LoadVector(location_color, color);
	}
	
	protected void LoadTranslation(Vec2 translation)
	{
		super.LoadVector(location_translation, translation);
	}

}
