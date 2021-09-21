package hud;

import org.lwjgl.input.Mouse;

import animations.Timer;
import mainPackage.MainGameLoop;
import renderEngine.DisplayManager;
import toolbox.EnumHolder.GameState;

public class HUDButton {
	
	private HUDTexture normalTexture;
	private HUDTexture hoveredTexture;
	
	private int xpos;
	private int ypos;
	private int xsize;
	private int ysize;
	
	private boolean mouseIsHovering;
	private boolean enabled;
		
	private Timer lastClickTimer;
	
	public HUDButton(String normalTextureName, int xpos, int ypos, int xsize, int ysize) {
		this.xpos = xpos * DisplayManager.resizeRatio;
		this.ypos = ypos * DisplayManager.resizeRatio;
		this.xsize = xsize * DisplayManager.resizeRatio;
		this.ysize = ysize * DisplayManager.resizeRatio;
		enabled = true;
		mouseIsHovering = false;
		lastClickTimer = new Timer(0.6f);
		lastClickTimer.start();
		
		normalTexture = new HUDTexture(MainGameLoop.loader.loadTexture("buttons/" + normalTextureName), xpos, ypos, xsize, ysize);
		hoveredTexture = new HUDTexture(MainGameLoop.loader.loadTexture("buttons/hovered/" + normalTextureName + "_hovered"), xpos, ypos, xsize, ysize);
		HUDRenderList.addHUD(normalTexture);
	}
	
	public void update() {		
		if(enabled) {
			if(mouseIsHovering) {
				onMouseStopsHovering();
			}else {
				onMouseStartsHovering();
			}
		}
		
		lastClickTimer.update();
	}
	
	private void onMouseStartsHovering() {
		if(Mouse.getX() < xpos + xsize && Mouse.getX() > xpos) {
			if(DisplayManager.HEIGHT - Mouse.getY() < ypos + ysize && DisplayManager.HEIGHT - Mouse.getY() > ypos) {
				MainGameLoop.gameState = GameState.UI_MODE;
				mouseIsHovering = true;
				HUDRenderList.addHUD(hoveredTexture);
				HUDRenderList.removeHUD(normalTexture);
			}
		}
	}
	
	private void onMouseStopsHovering() {
		if(Mouse.getX() > xpos + xsize || Mouse.getX() < xpos 
				|| DisplayManager.HEIGHT - Mouse.getY() > ypos + ysize || DisplayManager.HEIGHT - Mouse.getY() < ypos) {
			MainGameLoop.gameState = GameState.GAME_MODE;
			mouseIsHovering = false;
			HUDRenderList.addHUD(normalTexture);
			HUDRenderList.removeHUD(hoveredTexture);
		}
	}
	
	public boolean onMouseClicked() {
		if(MainGameLoop.gameState == GameState.UI_MODE) {
			if(mouseIsHovering && Mouse.isButtonDown(0) && lastClickTimer.timeReachedEnd()) {
				lastClickTimer.restart();
				return true;
			}
		}
		return false;
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void disable() {
		enabled = false;
	}	
}