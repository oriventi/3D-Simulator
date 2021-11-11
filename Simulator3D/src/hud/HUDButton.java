package hud;

import org.lwjgl.input.Mouse;

import animations.LinearAnimation;
import animations.Timer;
import mainPackage.MainGameLoop;
import menu.MenuUpdater;
import renderEngine.DisplayManager;
import toolbox.EnumHolder.GameState;

/**
 * creates and handles a button in the players hud
 * @author Oriventi
 *
 */
public class HUDButton {
	
	private HUDTexture normalTexture;
	private HUDTexture hoveredTexture;
	private HUDText text;
	
	private int xpos;
	private int ypos;
	private int xsize;
	private int ysize;
	private int original_xsize, original_ysize;
	
	private boolean mouseIsHovering;
	private boolean enabled;
	private boolean isMenuButton;
		
	private Timer lastClickTimer;
	
	//Text
	private String textContent;
	private float fontSize;
	private float r, g, b;
	private boolean isTextCentered;
	
	//Swipe
	private LinearAnimation swipeXAnimation;
	private LinearAnimation swipeYAnimation;
	private boolean isSwiping;
	
	/**
	 * 
	 * @param contains name of background Texture
	 * @param x position of top left corner
	 * @param y position of top left corner
	 * @param size of button in pixels in x direction
	 * @param size of button in pixels in y direction
	 * @param whether button is part of a menu or part of the game
	 */
	public HUDButton(String normalTextureName, int xpos, int ypos, int xsize, int ysize, boolean isMenuButton) {
		
		this.xpos = xpos;
		this.ypos = ypos;
		this.xsize = (int) (xsize * DisplayManager.resizeRatio);
		this.ysize = (int) (ysize * DisplayManager.resizeRatio);
		System.out.println(xsize + " " + DisplayManager.resizeRatio + " " + (xsize * DisplayManager.resizeRatio));
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
	
	/**
	 * check whether player clicks on button or hovers over the button
	 */
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
	
	/**
	 * Sets a text as a content of the button
	 * @param string of the text to show
	 * @param size of the font 
	 * @param r value of text color
	 * @param g value of text color
	 * @param b value of text color
	 * @param whether the text is centered in the button or not
	 */
	public void setText(String textContent, float fontSize, float r, float g, float b, boolean isTextCentered) {
		text = new HUDText(textContent, fontSize, MainGameLoop.font, xpos + xsize / 12, (int)(ypos + (ysize / 2 - 10* fontSize * DisplayManager.resizeRatio)),
				xsize, isTextCentered, isMenuButton);
		text.setColour(r, g, b);
		this.textContent = textContent;
		this.fontSize = fontSize;
		this.r = r;
		this.g = g;
		this.b = b;
		this.isTextCentered = isTextCentered;
	}
	
	/**
	 * checks whether mouse starts hovering over the button
	 */
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
	
	/**
	 * checks whether mouse stops hovering over the button
	 */
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
	
	/**
	 * returns true if mouse clicks while hovering over the button
	 * @return on mouse clicked
	 */
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
	
	/**
	 * enables the button
	 */
	public void enable() {
		enabled = true;
	}
	
	/**
	 * disables the button
	 */
	public void disable() {
		enabled = false;
	}
	
	/**
	 * sets the position of the button
	 * @param x position of top left corner
	 * @param y position of top left corner
	 */
	public void setPosition(int xpos, int ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
		normalTexture.setPosition(xpos, ypos);
		hoveredTexture.setPosition(xpos, ypos);
		if(hasText()) {
			removeText();
			setText(textContent, fontSize, r, g, b, isTextCentered);
		}
	}
	
	/**
	 * destroys the button and deletes content to clear ram
	 */
	public void destroy() {
		if(!isMenuButton) {
			MainGameLoop.gameState = GameState.GAME_MODE;
		}
		disable();
		hoveredTexture.stopDrawing();
		normalTexture.stopDrawing();
		removeText();
	}
	
	/**
	 * checks whether button has a text as content
	 * @return if button contains text
	 */
	public boolean hasText() {
		return (text != null);
	}
	
	/**
	 * checks whether button has text and removes it
	 */
	public void removeText() {
		if(hasText()) {
			text.remove();
			text = null;
		}
	}
	
	/**
	 * returns the x position of the top left corner of the button
	 * @return
	 */
	public int getXPos() {
		return xpos;
	}
	
	/**
	 * returns the y position of the top left corner of the button
	 * @return
	 */
	public int getYPos() {
		return ypos;
	}
	
	/**
	 * lets the button swipe from current position to xBorder, yBorder
	 * @param speed in x direction to move
	 * @param speed in y direction to move
	 * @param x position when to stop swiping
	 * @param y position when to stop swiping
	 * @param says after how many seconds to start swiping
	 */
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
	
	/**
	 * updates the position each frame while the button is in swiping mode
	 */
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
	
	/**
	 * checks whether button isSwiping
	 * @return button is not swiping
	 */
	public boolean hasSwipingFinished() {
		return !isSwiping;
	}
	
	/**
	 * checks whether button is enabled
	 * @return button is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
}