package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.PNGDecoder;
import org.newdawn.slick.opengl.Texture;

import textures.TextureData;

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
			case "png":
				format = "PNG";
				break;
			case "jpg":
				format = "JPG";
				break;
			case "tga":
				format = "TGA";
				break;
			default:
				System.err.println("WARNING: Unsupported texture format - " + extension);
				System.exit(-1);
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
	
	public static int LoadCubeMap(String[] textureFiles)
	{
		// Textures must be ordered as such:
		// Right, Left, Top, Bottom, Back, Front
		
		int length = textureFiles.length;
		if (length != 6)
		{
			System.err.println("ERROR: Wrong number of images for cube map.\nCube map must have exactly 6 images - " + length);
			System.exit(-1);
		}
		
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		
		for (int i = 0; i < length; i++)
		{
			String fileName = textureFiles[i];
			String extension = fileName.split("\\.")[1];
			
			if (!extension.equals("png"))
			{
				System.err.println("ERROR: Cubemap texture must be in PNG format - " + fileName);
				System.exit(-1);
			}
			
			TextureData data = DecodeTextureFile(fileName);
			GL11.glTexImage2D(	GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA,
								data.GetWidth(), data.GetHeight(), 0, GL11.GL_RGBA,
								GL11.GL_UNSIGNED_BYTE, data.GetBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		textures.add(texID);
		
		return texID;
	}
	
	public static void CleanUp()
	{
		for (int texture:textures)
		{
			GL11.glDeleteTextures(texture);
		}
	}
	
	private static TextureData DecodeTextureFile(String fileName)
	{
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try
		{
			FileInputStream in = new FileInputStream(RES_LOC + fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, PNGDecoder.RGBA);
			buffer.flip();
			in.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("ERROR: Failed to load texture - " + fileName);
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	
}