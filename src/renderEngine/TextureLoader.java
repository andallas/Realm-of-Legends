package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;

public class TextureLoader
{
	public static final String RES_LOC = "res/Textures/";
	
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
			texture = org.newdawn.slick.opengl.TextureLoader.getTexture(format, new FileInputStream(RES_LOC + fileName));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("ERROR: File not found at location: " + RES_LOC + fileName);
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Unable to load file at location: " + RES_LOC + fileName);
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