package hud;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import mainPackage.MainGameLoop;
import renderEngine.DisplayManager;
import toolbox.EnumHolder.GameState;

public class HUDButton {
	
	private HUDTexture normalTexture;
	private HUDTexture hoveredTexture;
	
	private Vector2f texturePosition;
	private Vector2f textureScale;
	
	private int xpos;
	private int ypos;
	private int xsize;
	private int ysize;
	
	private boolean mouseIsHovering;
	private boolean enabled;
	
	private float timeSinceLastClick;
	
	public HUDButton(int normalTextureID, int hoveredTextureID, int xpos, int ypos, int xsize, int ysize) {
		this.xpos = xpos * DisplayManager.resizeRatio;
		this.ypos = ypos * DisplayManager.resizeRatio;
		this.xsize = xsize * DisplayManager.resizeRatio;
		this.ysize = ysize * DisplayManager.resizeRatio;
		enabled = true;
		mouseIsHovering = false;
		texturePosition = new Vector2f();
		textureScale = new Vector2f();
		
		texturePosition.x = (2.f * (xpos + xsize / 2.f) / (float)DisplayManager.WIDTH ) - 1.f;
		texturePosition.y = -(2.f * (ypos + ysize / 2.f) / (float)DisplayManager.HEIGHT ) + 1.f;
		
		textureScale.x = (float)xsize / (float)DisplayManager.WIDTH;
		textureScale.y = (float)ysize / (float)DisplayManager.HEIGHT;
		
		normalTexture = new HUDTexture(normalTextureID, texturePosition, textureScale);
		hoveredTexture = new HUDTexture(hoveredTextureID, texturePosition, textureScale);
		HUDManager.addHUD(normalTexture);
	}
	
	public void update() {
		System.out.println(Mouse.getY() + " " + ypos);
		
		if(enabled) {
			if(mouseIsHovering) {
				onMouseStopsHovering();
			}else {
				onMouseStartsHovering();
			}
		}
		
		if(timeSinceLastClick < 3.0f) {
			timeSinceLastClick += DisplayManager.getFrameTimeSeconds();
		}
		
		onMouseClicked();
	}
	
	private void onMouseStartsHovering() {
		if(Mouse.getX() < xpos + xsize && Mouse.getX() > xpos) {
			if(DisplayManager.HEIGHT - Mouse.getY() < ypos + ysize && DisplayManager.HEIGHT - Mouse.getY() > ypos) {
				MainGameLoop.gameState = GameState.UI_MODE;
				mouseIsHovering = true;
				HUDManager.addHUD(hoveredTexture);
				HUDManager.removeHUD(normalTexture);
			}
		}
	}
	
	private void onMouseStopsHovering() {
		if(Mouse.getX() > xpos + xsize || Mouse.getX() < xpos 
				|| DisplayManager.HEIGHT - Mouse.getY() > ypos + ysize || DisplayManager.HEIGHT - Mouse.getY() < ypos) {
			MainGameLoop.gameState = GameState.GAME_MODE;
			mouseIsHovering = false;
			HUDManager.addHUD(normalTexture);
			HUDManager.removeHUD(hoveredTexture);
		}
	}
	
	public void onMouseClicked() {
		if(MainGameLoop.gameState == GameState.UI_MODE) {
			if(mouseIsHovering && Mouse.isButtonDown(0) && timeSinceLastClick > 0.5f) {
				timeSinceLastClick = 0;
				System.out.println("CLICKED ON BUTTON");
			}
		}
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void disable() {
		enabled = false;
	}
	
	
}
