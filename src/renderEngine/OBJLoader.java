package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ModelData;
import models.RawModel;
import models.Vertex;
import utility.Vec2;
import utility.Vec3;

public class OBJLoader
{
	public static final String RES_LOC = "res/Models/";
	
	public static void LoadAllObjs(String fileName)
	{
		BufferedReader reader	= OpenFile(fileName);
		String line = "";
		
		Map<String, List<String>> objectLists = new HashMap<String, List<String>>();
		List<BufferedReader> materialFiles = new ArrayList<BufferedReader>();
		
		try
		{
			String currentObject = "";
			
			while (line != null)
			{
				line = reader.readLine();
				
				if (line == null)
				{
					continue;
				}
				
				String[] currentLine = line.split(" ");
				
				if (line.startsWith("mtllib "))
				{
					materialFiles.add(OpenFile(currentLine[1]));
					
				}
				else if (line.startsWith("o "))
				{
					currentObject = currentLine[1];
					objectLists.put(currentObject, new ArrayList<String>());
				}
				else if (	line.startsWith("v ") ||
							line.startsWith("vt ") ||
							line.startsWith("vn ") ||
							line.startsWith("usemtl ") ||
							line.startsWith("f "))
				{
					if (objectLists.containsKey(currentObject))
					{
						for (int i = 1; i < currentLine.length; i++)
						{
							objectLists.get(currentObject).add(currentLine[i]);	
						}
					}
					else
					{
						System.err.println("ERROR: Invalid key - " + currentObject);
					}
				}
			}
			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		CreateModels(objectLists, materialFiles);
	}
	
	// TODO: Instead of passing a list of bufferedReaders we should pre-process the material files
	// prior to sending them into this function. Then pass the list of strings instead
	private static void CreateModels(Map<String, List<String>> objectLists, List<BufferedReader> materialFiles)
	{
		List<RawModel> rawModels = new ArrayList<RawModel>();
		for (Map.Entry<String, List<String>> entry : objectLists.entrySet())
		{
			rawModels.add(CreateModel(entry.getValue()));
		}
		
		// TODO: Figure out how to handle the generated list of models
	}
	
	// TODO: This function should generate a model
	private static RawModel CreateModel(List<String> object)
	{
		return new RawModel(0, 0);
	}
	
	public static ModelData LoadObj(String objFileName)
	{
		BufferedReader reader	= OpenFile(objFileName);
		String line;
		
		List<Vertex> vertices	= new ArrayList<Vertex>();
		List<Vec2> uvCoords	= new ArrayList<Vec2>();
		List<Vec3> normals	= new ArrayList<Vec3>();
		List<Integer> indices	= new ArrayList<Integer>();
		
		try
		{
			while (true)
			{
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("v "))
				{
					Vec3 vertex = new Vec3(	(float)Float.valueOf(currentLine[1]),
													(float)Float.valueOf(currentLine[2]),
													(float)Float.valueOf(currentLine[3]));
					Vertex newVertex = new Vertex(vertices.size(), vertex);
					vertices.add(newVertex);
				}
				else if (line.startsWith("vt "))
				{
					Vec2 uvCoord = new Vec2((float)Float.valueOf(currentLine[1]),
													(float)Float.valueOf(currentLine[2]));
					uvCoords.add(uvCoord);
				}
				else if (line.startsWith("vn "))
				{
					Vec3 normal = new Vec3(	(float)Float.valueOf(currentLine[1]),
													(float)Float.valueOf(currentLine[2]),
													(float)Float.valueOf(currentLine[3]));
					normals.add(normal);
				}
				else if (line.startsWith("f "))
				{
					break;
				}
			}
			
			// Process faces
			while (line != null)
			{
				if (!line.startsWith("f "))
				{
					line = reader.readLine();
					continue;
				}
				String[] currentLine	= line.split(" ");
				
				String[] vertex1		= currentLine[1].split("/");
				ProcessVertex(vertex1, vertices, indices);
				
				String[] vertex2		= currentLine[2].split("/");
				ProcessVertex(vertex2, vertices, indices);
				
				String[] vertex3		= currentLine[3].split("/");
				ProcessVertex(vertex3, vertices, indices);
				
				line = reader.readLine();
			}
			reader.close();
		}
		catch (Exception e)
		{
			System.err.println("ERROR: Error reading file: " + RES_LOC + objFileName);
		}
		
		RemoveUnusedVertices(vertices);
		
		float[] verticesArray = new float[vertices.size() * 3];
		float[] uvCoordsArray = new float[vertices.size() * 2];
		float[] normalsArray = new float[vertices.size() * 3];
		float furthest = ConvertDataToArrays(vertices, uvCoords, normals, verticesArray, uvCoordsArray, normalsArray);
		
		int[] indicesArray = IndicesListToArray(indices);
		ModelData data = new ModelData(verticesArray, uvCoordsArray, normalsArray, indicesArray, furthest);
		
		return data;
	}
	
	private static BufferedReader OpenFile(String fileName)
	{
		FileReader fileReader = null;
		File objFile = new File(RES_LOC + fileName);
		try
		{
			fileReader = new FileReader(objFile);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("ERROR: File not found at location: " + RES_LOC + fileName);
		}
		
		BufferedReader reader = new BufferedReader(fileReader);
		return reader;
	}
	
	private static int[] IndicesListToArray(List<Integer> indices)
	{
		int[] array = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++)
		{
			array[i] = indices.get(i);
		}
		
		return array;
	}
	
	private static float ConvertDataToArrays(	List<Vertex> vertices,
												List<Vec2> uvCoords,
												List<Vec3> normals,
												float[] verticesArray,
												float[] uvCoordsArray,
												float[] normalsArray)
	{
		float furthestPoint = 0;
		for (int i = 0; i < vertices.size(); i++)
		{
			Vertex currentVertex = vertices.get(i);
			if (currentVertex.GetLength() > furthestPoint)
			{
				furthestPoint = currentVertex.GetLength();
			}
			Vec3 position = currentVertex.GetPosition();
			Vec2 uvCoord = uvCoords.get(currentVertex.GetTextureIndex());
			Vec3 normal = normals.get(currentVertex.GetNormalIndex());
			verticesArray[i * 3] = position.x;
			verticesArray[i * 3 + 1] = position.y;
			verticesArray[i * 3 + 2] = position.z;
			uvCoordsArray[i * 2] = uvCoord.x;
			uvCoordsArray[i * 2 + 1] = 1 - uvCoord.y;
			normalsArray[i * 3] = normal.x;
			normalsArray[i * 3 + 1] = normal.y;
			normalsArray[i * 3 + 2] = normal.z;
		}

		return furthestPoint;
	}
	
	private static void DealWithAlreadyProcessedVertex(	Vertex previousVertex,
														int newUVIndex,
														int newNormalIndex,
														List<Integer> indices,
														List<Vertex> vertices)
	{
		if (previousVertex.HasSameTextureAndNormal(newUVIndex, newNormalIndex))
		{
			indices.add(previousVertex.GetIndex());
		}
		else
		{
			Vertex anotherVertex = previousVertex.GetDuplicateVertex();
			if (anotherVertex != null)
			{
				DealWithAlreadyProcessedVertex(anotherVertex, newUVIndex, newNormalIndex, indices, vertices);
			}
			else
			{
				Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.GetPosition());
				duplicateVertex.SetTextureIndex(newUVIndex);
				duplicateVertex.SetNormalIndex(newNormalIndex);
				previousVertex.SetDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.GetIndex());
			}
		}
	}
	
	private static void RemoveUnusedVertices(List<Vertex> vertices)
	{
		for (Vertex vertex:vertices)
		{
			if (!vertex.IsSet())
			{
				vertex.SetTextureIndex(0);
				vertex.SetNormalIndex(0);
			}
		}
	}
	
	private static void ProcessVertex(	String[] vertexData,
										List<Vertex> vertices,
										List<Integer> indices)
	{
		int index = 0;
		Vertex currentVertex = null;
		if (vertexData[0].matches("\\d+"))
		{
			index = Integer.parseInt(vertexData[0]) - 1;
			currentVertex = vertices.get(index);
		}
		int uvCoordIndex = 0;
		if (vertexData[1].matches("\\d+"))
		{
			uvCoordIndex = Integer.parseInt(vertexData[1]) - 1;
		}
		
		int normalIndex = 0;
		if (vertexData[2].matches("\\d+"))
		{
			normalIndex = Integer.parseInt(vertexData[2]) - 1;
		}
		
		if (!currentVertex.IsSet())
		{
			currentVertex.SetTextureIndex(uvCoordIndex);
			currentVertex.SetNormalIndex(normalIndex);
			indices.add(index);
		}
		else
		{
			DealWithAlreadyProcessedVertex(currentVertex, uvCoordIndex, normalIndex, indices, vertices);
		}
	}
	
}