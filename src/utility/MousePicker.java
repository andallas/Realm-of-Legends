package utility;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import terrains.Terrain;

public class MousePicker
{
	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;
	
	private Vector3f currentRay;
	private Vector3f currentTerrainPoint;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	private Terrain terrain;
	
	public MousePicker(Camera cam, Matrix4f projection)
	{
		this.camera = cam;
		this.projectionMatrix = projection;
		this.viewMatrix = MathUtil.CreateViewMatrix(camera);
	}
	
	public MousePicker(Camera cam, Matrix4f projection, Terrain terrain)
	{
		this.camera = cam;
		this.projectionMatrix = projection;
		this.viewMatrix = MathUtil.CreateViewMatrix(camera);
		this.terrain = terrain;
	}
	
	public void Update()
	{
		viewMatrix = MathUtil.CreateViewMatrix(camera);
		currentRay = CalculateRay();
		
		if (IntersectionInRange(0, RAY_RANGE, currentRay))
		{
			currentTerrainPoint = BinarySearch(0, 0, RAY_RANGE, currentRay);
		}
		else
		{
			currentTerrainPoint = null;
		}
	}
	
	private Vector3f CalculateRay()
	{
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		
		Vector2f normalizedCoords = GetNormalizedDeviceCoords(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
		Vector4f eyeCoords = ToEyeCoords(clipCoords);
		Vector3f worldCoords = ToWorldCoords(eyeCoords); 
		
		return worldCoords;
	}
	
	private Vector3f ToWorldCoords(Vector4f eyeCoords)
	{
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vector4f ToEyeCoords(Vector4f clipCoords)
	{
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
	
	private Vector2f GetNormalizedDeviceCoords(float mouseX, float mouseY)
	{
		float x = (2f * mouseX) / Display.getWidth() - 1;
		float y = (2f * mouseY) / Display.getHeight() - 1;
		return new Vector2f(x, y);
	}
	
	private Vector3f GetPointOnRay(Vector3f ray, float distance)
	{
		Vector3f camPos = camera.GetPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	
	private Vector3f BinarySearch(int count, float start, float finish, Vector3f ray)
	{
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT)
		{
			Vector3f endPoint = GetPointOnRay(ray, half);
			Terrain terrain = GetTerrain(endPoint.getX(), endPoint.getZ());
			if (terrain != null)
			{
				return endPoint;
			}
			else
			{
				return null;
			}
		}
		
		if (IntersectionInRange(start, half, ray))
		{
			return BinarySearch(count + 1, start, half, ray);
		}
		else
		{
			return BinarySearch(count + 1, half, finish, ray);
		}
	}
	
	private boolean IntersectionInRange(float start, float finish, Vector3f ray)
	{
		Vector3f startPoint = GetPointOnRay(ray, start);
		Vector3f endPoint = GetPointOnRay(ray, finish);
		
		return (!IsUnderground(startPoint) && !IsUnderground(endPoint));
	}
	
	private boolean IsUnderground(Vector3f testPoint)
	{
		Terrain terrain = GetTerrain(testPoint.getX(), testPoint.getZ());
		float height = 0;
		if (terrain != null)
		{
			height = terrain.GetHeight(testPoint.getX(), testPoint.getZ());
		}
		
		return (testPoint.y < height);
	}
	
	private Terrain GetTerrain(float worldX, float worldZ)
	{
		return terrain;
	}
	
	
	public Vector3f	GetCurrentRay() { return currentRay; }
	public Vector3f GetCurrentTerrainPoint() { return currentTerrainPoint; }
	
}