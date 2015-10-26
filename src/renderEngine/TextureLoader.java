package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class TextureLoader
{
	private static List<Integer> textures	= new ArrayList<Integer>();
	
	public static int LoadTexture(String fileName)
	{
		Texture texture = null;
		
		try
		{
			String extension = fileName.split("\\.")[1];
			String format = "";
			
			switch (extension)
			{
			case ".png":
				format = "PNG";
				break;
			case ".jpg":
				format = "JPG";
				break;
			case ".tga":
				format = "TGA";
				break;
			default:
				format = "PNG";
				break;
			}
			texture = org.newdawn.slick.opengl.TextureLoader.getTexture(format, new FileInputStream("res/Textures/" + fileName));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		
		return textureID;
	}
	
	public static void CleanUp()
	{
		for (int texture:textures)
		{
			GL11.glDeleteTextures(texture);
		}
	}
	
}