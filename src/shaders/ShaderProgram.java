package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import utility.Logger;
import utility.Vec2;
import utility.Vec3;
import utility.Vec4;

public abstract class ShaderProgram
{
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	
	public ShaderProgram(String vertexFile, String fragmentFile)
	{
		vertexShaderID = LoadShader(vertexFile);
		fragmentShaderID = LoadShader(fragmentFile);
		CreateProgram();
		GetAllUniformLocations();
	}
	
	public void Start()
	{
		GL20.glUseProgram(programID);
	}
	
	public void Stop()
	{
		GL20.glUseProgram(0);
	}
	
	public void CleanUp()
	{
		Stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	
	protected abstract void GetAllUniformLocations();
	
	protected abstract void BindAttributes();
	
	
	protected int GetUniformLocation(String uniformName)
	{
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	protected void BindAttribute(int attribute, String variableName)
	{
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected void LoadFloat(int location, float value)
	{
		GL20.glUniform1f(location, value);
	}
	
	protected void LoadInt(int location, int value)
	{
		GL20.glUniform1i(location, value);
	}
	
	protected void LoadVector(int location, Vec2 vector)
	{
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	protected void LoadVector(int location, Vec3 vector)
	{
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	public void LoadVector(int location, Vec4 vector)
	{
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}
	
	protected void LoadBoolean(int location, boolean value)
	{
		float toLoad = 0;
		if (value)
		{
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	protected void LoadMatrix(int location, Matrix4f matrix)
	{
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	
	private void CreateProgram()
	{
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
		BindAttributes();
		
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
	}
	
	
	private static int LoadShader(String file)
	{
		int type = GetShaderType(file);
		StringBuilder shaderSource = GetShaderSource(file);
		
		return CompileShader(type, shaderSource);
	}
	
	private static int GetShaderType(String fileName)
	{
		int type = -1;
		
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0)
		{
			extension = fileName.substring(i + 1);
		}
		
		switch(extension)
		{
		case "vert":
			type = GL20.GL_VERTEX_SHADER;
			break;
		case "frag":
			type = GL20.GL_FRAGMENT_SHADER;
			break;
		default:
			Logger.Error("Invalid shader type!");
			System.exit(-1);
			break;
		}
		
		return type;
	}
	
	private static StringBuilder GetShaderSource(String fileName)
	{
		StringBuilder shaderSource = new StringBuilder();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			
			while ((line = reader.readLine()) != null)
			{
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}
		catch(IOException e)
		{
			Logger.Error("Could not read shader file!");
			System.exit(-1);
		}
		
		return shaderSource;
	}
	
	private static int CompileShader(int type, StringBuilder shaderSource)
	{
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if (GL20.glGetShaderi(shaderID,  GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			Logger.Error("Could not compile shader! " + GL20.glGetShaderInfoLog(shaderID, 500));
			System.exit(-1);
		}
		
		return shaderID;
	}

}