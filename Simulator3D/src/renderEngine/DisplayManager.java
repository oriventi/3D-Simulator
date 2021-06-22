package renderEngine;


import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	
	private static final int FPS_CAP = 200;
	
	private static long lastFrameTime;
	public static float delta;
	private static float timer = 0;
	
	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("FPS: ?");
			Display.create(new PixelFormat().withSamples(8), attribs);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000.f;
		setFPSToTitle();
		lastFrameTime = currentFrameTime;
	}
	
	private static void setFPSToTitle() {
		timer += delta;
		if(timer >= 1) {
			timer = 0;
			Display.setTitle("FPS: " + (int)(1.f/ delta));
		}
	}
	
	public static float getFPS() {
		return (1.f / delta);
	}
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	private static long getCurrentTime() {
		return Sys.getTime()* 1000 / Sys.getTimerResolution();
	}
	
}
