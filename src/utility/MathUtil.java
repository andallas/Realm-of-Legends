package utility;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class MathUtil
{
	public static float BarryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos)
	{
		float det = (p2.z - p3.z) * (p1.x  - p3.x) + (p3.x - p2.x) * (p1.z  - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f CreateTransformationMatrix(Vector2f translation, Vector2f scale)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f CreateTransformationMatrix(	Vector3f translation,
														Vector3f rotation,
														Vector3f scale)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.translate(translation, matrix, matrix);
		
		Matrix4f.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
		
		Matrix4f.scale(new Vector3f(scale.x, scale.y, scale.z), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f CreateTransformationMatrix(	Vector3f translation,
														Vector3f rotation,
														float scale)
	{
		return MathUtil.CreateTransformationMatrix(translation, rotation, new Vector3f(scale, scale, scale));
	}
	
	public static Matrix4f CreateViewMatrix(Camera camera)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.rotate((float)Math.toRadians(camera.GetPitch()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(camera.GetYaw()), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(camera.GetRoll()), new Vector3f(0, 0, 1), matrix, matrix);
		
		Vector3f cameraPos = camera.GetPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, matrix, matrix);
		
		return matrix;
	}
	
}