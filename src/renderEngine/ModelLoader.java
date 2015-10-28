package renderEngine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import models.RawModel;

public class ModelLoader
{
	private static List<Integer> vaos		= new ArrayList<Integer>();
	private static List<Integer> vbos		= new ArrayList<Integer>();
	
	public static RawModel LoadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices)
	{
		int vaoID = CreateVAO();
		BindIndicesBuffer(indices);
		StoreDataInAttributeList(0, 3, positions);
		StoreDataInAttributeList(1, 2, textureCoords);
		StoreDataInAttributeList(2, 3, normals);
		UnbindVAO();
		
		return new RawModel(vaoID, indices.length);
	}
	
	public static RawModel LoadToVAO(float[] positions, int dimensions)
	{
		int vaoID = CreateVAO();
		StoreDataInAttributeList(0, dimensions, positions);
		UnbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	public static void CleanUp()
	{
		for (int vao:vaos)
		{
			GL30.glDeleteVertexArrays(vao);
		}
		
		for (int vbo:vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
	}
	
	private static int CreateVAO()
	{
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		
		return vaoID;
	}
	
	private static void StoreDataInAttributeList(int attributeNumber, int coordinateSize, float[] data)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = StoreDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private static void UnbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
	
	private static void BindIndicesBuffer(int[] indices)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = StoreDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private static IntBuffer StoreDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	private static FloatBuffer StoreDataInFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
}