package utility;

import org.lwjgl.Sys;

public class Time
{
	private static long lastFrameTime;
	private static float delta;
	
	
	public static void Initialize()
	{
		lastFrameTime = GetCurrentTime();
	}
	
	public static void Update()
	{
		long currentFrameTime = GetCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	private static long GetCurrentTime()
	{
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	public static float Delta()
	{
		return delta;
	}
	
}