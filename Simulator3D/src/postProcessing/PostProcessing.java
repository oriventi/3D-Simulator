package postProcessing;

import org.lwjgl.opengl.Display;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import gaussianBlur.HorizontalBlur;
import gaussianBlur.VerticalBlur;
import menu.MenuUpdater;
import models.RawModel;
import renderEngine.Loader;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	private static ContrastChanger contrastChanger;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;

	public static void init(Loader loader){
		quad = loader.loadToVAO(POSITIONS);
		contrastChanger = new ContrastChanger();
		hBlur = new HorizontalBlur(Display.getHeight() / 2, Display.getHeight() / 2);
		vBlur = new VerticalBlur(Display.getWidth() / 2, Display.getHeight() / 2);
	}
	
	public static void doPostProcessing(int colourTexture){
		start();
		if(MenuUpdater.isMenuActivated()) {
			hBlur.render(colourTexture);
			vBlur.render(hBlur.getOutputTexture());
			contrastChanger.render(vBlur.getOutputTexture());
		}else {
			contrastChanger.render(colourTexture);
		}
		end();
	}
	
	public static void cleanUp(){
		contrastChanger.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
