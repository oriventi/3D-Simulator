package hud;

import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;

public class HUDTexture {
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	private int xpos, ypos, xsize, ysize;
	
	public HUDTexture(int texture, int xpos, int ypos, int xsize, int ysize) {
		this.texture = texture;
		this.xpos = xpos;
		this.ypos = ypos;
		this.xsize = xsize;
		this.ysize = ysize;
		
		position = new Vector2f();
		setPosition(xpos, ypos);
		
		scale = new Vector2f();
		scale.x = (float)xsize / (float)DisplayManager.WIDTH;
		scale.y = (float)ysize / (float)DisplayManager.HEIGHT;
	}
	
	public HUDTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	
	public void startDrawing() {
		HUDRenderList.addHUD(this);
	}
	
	public void stopDrawing() {
		HUDRenderList.removeHUD(this);
	}
	
	public int getTexture() {
		return texture;
	}
	
	public void setPosition(int xpos, int ypos) {
		position.x = (2.f * (xpos + xsize / 2.f) / (float)DisplayManager.WIDTH ) - 1.f;
		position.y = -(2.f * (ypos + ysize / 2.f) / (float)DisplayManager.HEIGHT ) + 1.f;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getScale() {
		return scale;
	}
	
}
