package utility;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import terrains.Terrain;

public class MousePicker
{
	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;
	
	private Vec3 currentRay;
	private Vec3 currentTerrainPoint;
	
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
	
	private Vec3 CalculateRay()
	{
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		
		Vec2 normalizedCoords = GetNormalizedDeviceCoords(mouseX, mouseY);
		Vec4 clipCoords = new Vec4(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
		Vec4 eyeCoords = ToEyeCoords(clipCoords);
		Vec3 worldCoords = ToWorldCoords(eyeCoords); 
		
		return worldCoords;
	}
	
	private Vec3 ToWorldCoords(Vec4 eyeCoords)
	{
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vec4 rayWorld = (Vec4)Matrix4f.transform(invertedView, eyeCoords, null);
		Vec3 mouseRay = new Vec3(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vec4 ToEyeCoords(Vec4 clipCoords)
	{
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vec4 eyeCoords = (Vec4)Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vec4(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
	
	private Vec2 GetNormalizedDeviceCoords(float mouseX, float mouseY)
	{
		float x = (2f * mouseX) / Display.getWidth() - 1;
		float y = (2f * mouseY) / Display.getHeight() - 1;
		return new Vec2(x, y);
	}
	
	private Vec3 GetPointOnRay(Vec3 ray, float distance)
	{
		Vec3 camPos = camera.transform.position;
		Vec3 start = new Vec3(camPos.x, camPos.y, camPos.z);
		Vec3 scaledRay = new Vec3(ray.x * distance, ray.y * distance, ray.z * distance);
		return (Vec3)Vec3.add(start, scaledRay, null);
	}
	
	private Vec3 BinarySearch(int count, float start, float finish, Vec3 ray)
	{
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT)
		{
			Vec3 endPoint = GetPointOnRay(ray, half);
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
	
	private boolean IntersectionInRange(float start, float finish, Vec3 ray)
	{
		Vec3 startPoint = GetPointOnRay(ray, start);
		Vec3 endPoint = GetPointOnRay(ray, finish);
		
		return (!IsUnderground(startPoint) && !IsUnderground(endPoint));
	}
	
	private boolean IsUnderground(Vec3 testPoint)
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
	
	
	public Vec3	GetCurrentRay() { return currentRay; }
	public Vec3 GetCurrentTerrainPoint() { return currentTerrainPoint; }
	
}