package hud;

import org.lwjgl.input.Mouse;

import animations.Timer;
import mainPackage.MainGameLoop;
import menu.MenuUpdater;
import renderEngine.DisplayManager;
import toolbox.EnumHolder.GameState;

public class HUDButton {
	
	private HUDTexture normalTexture;
	private HUDTexture hoveredTexture;
	private HUDText text;
	
	private int xpos;
	private int ypos;
	private int xsize;
	private int ysize;
	
	private boolean mouseIsHovering;
	private boolean enabled;
	private boolean isMenuButton;
		
	private Timer lastClickTimer;
	
	private String textContent;
	private float fontSize;
	private float r, g, b;
	
	public HUDButton(String normalTextureName, int xpos, int ypos, int xsize, int ysize, boolean isMenuButton) {
		this.xpos = xpos * DisplayManager.resizeRatio;
		this.ypos = ypos * DisplayManager.resizeRatio;
		this.xsize = xsize * DisplayManager.resizeRatio;
		this.ysize = ysize * DisplayManager.resizeRatio;
		this.isMenuButton = isMenuButton;
		enabled = true;
		mouseIsHovering = false;
		lastClickTimer = new Timer(0.6f);
		lastClickTimer.start();
		
		normalTexture = new HUDTexture(MainGameLoop.loader.loadTexture("buttons/" + normalTextureName), this.xpos, this.ypos, this.xsize, this.ysize, isMenuButton);
		hoveredTexture = new HUDTexture(MainGameLoop.loader.loadTexture("buttons/hovered/" + normalTextureName + "_hovered"), this.xpos, this.ypos, this.xsize, this.ysize, isMenuButton);
		normalTexture.startDrawing();
	}
	
	public void setText(String textContent, float fontSize, float r, float g, float b) {
		text = new HUDText(textContent, fontSize, MainGameLoop.font, xpos + xsize / 12, (int)(ypos + (ysize / 2 - 10* fontSize)), xsize, false, isMenuButton);
		text.setColour(r, g, b);
		this.textContent = textContent;
		this.fontSize = fontSize;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void update() {		
		if((isMenuButton && MenuUpdater.isMenuActivated()) || (!isMenuButton && !MenuUpdater.isMenuActivated())) {
			if(enabled) {
				if(mouseIsHovering) {
					onMouseStopsHovering();
				}else {
					onMouseStartsHovering();
				}
			}
		}
		
		lastClickTimer.update();
	}
	
	private void onMouseStartsHovering() {
		if(Mouse.getX() < xpos + xsize && Mouse.getX() > xpos) {
			if(DisplayManager.HEIGHT - Mouse.getY() < ypos + ysize && DisplayManager.HEIGHT - Mouse.getY() > ypos) {
				if(!isMenuButton) {
					MainGameLoop.gameState = GameState.UI_MODE;
				}
				mouseIsHovering = true;
				hoveredTexture.startDrawing();
				normalTexture.stopDrawing();
			}
		}
	}
	
	private void onMouseStopsHovering() {
		if(Mouse.getX() > xpos + xsize || Mouse.getX() < xpos 
				|| DisplayManager.HEIGHT - Mouse.getY() > ypos + ysize || DisplayManager.HEIGHT - Mouse.getY() < ypos) {
			if(!isMenuButton) {
				MainGameLoop.gameState = GameState.GAME_MODE;
			}
			mouseIsHovering = false;
			normalTexture.startDrawing();
			hoveredTexture.stopDrawing();
		}
	}
	
	public boolean onMouseClicked() {
		if((isMenuButton && MenuUpdater.isMenuActivated()) || (!isMenuButton && !MenuUpdater.isMenuActivated())) {
			if(MainGameLoop.gameState == GameState.UI_MODE) {
				if(mouseIsHovering && Mouse.isButtonDown(0) && lastClickTimer.timeReachedEnd()) {
					lastClickTimer.restart();
					return true;
				}
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
	
	public void setPosition(int xpos, int ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
		normalTexture.setPosition(xpos, ypos);
		hoveredTexture.setPosition(xpos, ypos);
		if(hasText()) {
			removeText();
			setText(textContent, fontSize, r, g, b);
		}
	}
	
	public void destroy() {
		if(!isMenuButton) {
			MainGameLoop.gameState = GameState.GAME_MODE;
		}
		hoveredTexture.stopDrawing();
		normalTexture.stopDrawing();
		
		removeText();
	}
	
	public boolean hasText() {
		return (text != null);
	}
	
	public void removeText() {
		if(hasText()) {
			//text = null;
			text.remove();
		}
	}
	
	public int getXPos() {
		return xpos;
	}
	
	public int getYPos() {
		return ypos;
	}
}