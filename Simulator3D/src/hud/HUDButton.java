package hud;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;

public class HUDButton {
	
	private HUDTexture normalTexture;
	private HUDTexture hoveredTexture;
	
	private int xpos;
	private int ypos;
	private float xscale;
	private float yscale;
	
	public HUDButton(Vector2f position, int normalTextureID, int hoveredTextureID, Vector2f scale) {
		normalTexture = new HUDTexture(normalTextureID, position, scale);
		hoveredTexture = new HUDTexture(hoveredTextureID, position, scale);
		HUDManager.addHUD(normalTexture);
		
		xpos = (int)((position.x + 1) * (DisplayManager.WIDTH / 2));
	}
	
	public void update() {
		
	}
	
	private boolean onMouseHovered() {
		
		return false;
		//TODO
	}
	
	public boolean onMouseClicked() {
		if(onMouseHovered() && Mouse.isButtonDown(0)) {
			return true;
		}else {
			return false;
		}
	}
	
	
}
