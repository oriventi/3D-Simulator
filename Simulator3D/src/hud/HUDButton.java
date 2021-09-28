package hud;

import org.lwjgl.input.Mouse;

import animations.Animation;
import animations.LinearAnimation;
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
	private boolean visible;
		
	private Timer lastClickTimer;
	
	//Text
	private String textContent;
	private float fontSize;
	private float r, g, b;
	private boolean isCentered;
	
	//Swipe
	private LinearAnimation swipeXAnimation;
	private LinearAnimation swipeYAnimation;
	private boolean isSwiping;
	
	public HUDButton(String normalTextureName, int xpos, int ypos, int xsize, int ysize, boolean isMenuButton) {
		this.xpos = (int) (xpos * DisplayManager.resizeRatio);
		this.ypos = (int) (ypos * DisplayManager.resizeRatio);
		this.xsize = (int) (xsize * DisplayManager.resizeRatio);
		this.ysize = (int) (ysize * DisplayManager.resizeRatio);
		visible = true;
		isSwiping = false;
		this.isMenuButton = isMenuButton;
		enabled = true;
		mouseIsHovering = false;
		lastClickTimer = new Timer(0.6f);
		lastClickTimer.start();
		
		normalTexture = new HUDTexture(MainGameLoop.loader.loadTexture("buttons/" + normalTextureName), this.xpos, this.ypos, this.xsize, this.ysize, isMenuButton);
		hoveredTexture = new HUDTexture(MainGameLoop.loader.loadTexture("buttons/hovered/" + normalTextureName + "_hovered"), this.xpos, this.ypos, this.xsize, this.ysize, isMenuButton);
		normalTexture.startDrawing();
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
			if(isSwiping) updatePositionWhileSwiping();
			lastClickTimer.update();
		}
	}
	
	public void setText(String textContent, float fontSize, float r, float g, float b, boolean isTextCentered) {
		text = new HUDText(textContent, fontSize, MainGameLoop.font, xpos + xsize / 12, (int)(ypos + (ysize / 2 - 10* fontSize * DisplayManager.resizeRatio)), xsize, isTextCentered, isMenuButton);
		text.setColour(r, g, b);
		this.textContent = textContent;
		this.fontSize = fontSize;
		this.r = r;
		this.g = g;
		this.b = b;
		this.isCentered = isTextCentered;
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
			setText(textContent, fontSize, r, g, b, isCentered);
		}
	}
	
	public void destroy() {
		if(!isMenuButton) {
			MainGameLoop.gameState = GameState.GAME_MODE;
		}
		disable();
		hoveredTexture.stopDrawing();
		normalTexture.stopDrawing();
		removeText();
	}
	
	public boolean hasText() {
		return (text != null);
	}
	
	public void removeText() {
		if(hasText()) {
			text.remove();
			text = null;
		}
	}
	
	public int getXPos() {
		return xpos;
	}
	
	public int getYPos() {
		return ypos;
	}
	
	public void swipeTo(int xspeed, int yspeed, int xBorder, int yBorder, float delayTime) {
		yBorder *= DisplayManager.resizeRatio;
		xBorder *= DisplayManager.resizeRatio;
		if(xspeed != 0) {
			isSwiping = true;
			float runningXTime = Math.abs(xpos - xBorder) / (float)xspeed;
			swipeXAnimation = new LinearAnimation(runningXTime, xspeed, xBorder, delayTime);
			swipeXAnimation.startAnimation();
		}
	
		if(yspeed != 0) {
			isSwiping = true;
			float runningYTime = Math.abs(ypos - yBorder) / (float)yspeed;
			swipeYAnimation = new LinearAnimation(runningYTime, yspeed, yBorder, delayTime);
			swipeYAnimation.startAnimation();
		}
	}
	
	public void updatePositionWhileSwiping() {
		if(swipeXAnimation != null) {
			if(swipeXAnimation.reachedTargetValue(xpos)) {
				swipeXAnimation.destroy();
				swipeXAnimation = null;
			}else {
				setPosition((int)swipeXAnimation.getCurrentValue(xpos), ypos);
			}
		}
		if(swipeYAnimation != null) {
			if(swipeYAnimation.reachedTargetValue(ypos)) {
				swipeYAnimation.destroy();
				swipeYAnimation = null;
			}else {
				setPosition(xpos, (int)swipeYAnimation.getCurrentValue(ypos));
			}
		}
		if(swipeXAnimation == null && swipeYAnimation == null) {
			isSwiping = false;
		}
	}
	
	public boolean hasSwipingFinished() {
		return !isSwiping;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setVisibility(boolean visible) {
		this.visible = visible;
	}
}