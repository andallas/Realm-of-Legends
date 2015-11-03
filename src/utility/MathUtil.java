package utility;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import gameObjects.Transform;

public class MathUtil
{
	public static float BarryCentric(Vec3 p1, Vec3 p2, Vec3 p3, Vec2 pos)
	{
		float det = (p2.z - p3.z) * (p1.x  - p3.x) + (p3.x - p2.x) * (p1.z  - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f CreateTransformationMatrix(Vec2 translation, Vec2 scale)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vec3(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f CreateTransformationMatrix(Transform transform)
	{
		return CreateTransformationMatrix(transform.position, transform.rotation, transform.scale);
	}
	
	public static Matrix4f CreateTransformationMatrix(Vec3 position, Vec3 rotation, float scale)
	{
		return CreateTransformationMatrix(position, rotation, new Vec3(scale, scale, scale));
	}
	
	public static Matrix4f CreateTransformationMatrix(Vec3 position, Vec3 rotation, Vec3 scale)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.translate(position, matrix, matrix);
		
		Matrix4f.rotate((float)Math.toRadians(rotation.x), new Vec3(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rotation.y), new Vec3(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rotation.z), new Vec3(0, 0, 1), matrix, matrix);
		
		Matrix4f.scale(new Vec3(scale.x, scale.y, scale.z), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f CreateViewMatrix(Camera camera)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.rotate((float)Math.toRadians(camera.GetPitch()), new Vec3(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(camera.GetYaw()), new Vec3(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(camera.GetRoll()), new Vec3(0, 0, 1), matrix, matrix);
		
		Vec3 cameraPos = camera.GetPosition();
		Vec3 negativeCameraPos = new Vec3(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, matrix, matrix);
		
		return matrix;
	}
	
}