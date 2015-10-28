package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;


public class DisplayManager
{
	private static final int WIDTH		= 1280;
	private static final int HEIGHT		= 720;
	private static final int FPS_LIMIT	= 120;
	
	private static long lastFrameTime;
	private static float delta;
	
	public static void CreateDisplay()
	{
		ContextAttribs attribs = new ContextAttribs(3, 2)
									.withForwardCompatible(true)
									.withProfileCore(true);
		
		Display.setTitle("Realm of Legends");
		
		try
		{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		lastFrameTime = GetCurrentTime();
	}
	
	public static void UpdateDisplay()
	{
		Display.sync(FPS_LIMIT);
		Display.update();
		long currentFrameTime = GetCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static void CloseDisplay()
	{
		Display.destroy();
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