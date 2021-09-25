package hud;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import mainPackage.MainGameLoop;
import renderEngine.DisplayManager;
import toolbox.EnumHolder.GameState;

public abstract class HUDWindow {

	private int xpos, ypos, xsize, ysize;
	private HUDButton closeButton;
	private HUDTexture backgroundTexture, taskbar;
	
	private boolean mouseIsMoving;
	private Vector2f mouseStartPosition;
	protected Vector2f windowOffset;
	
	private boolean mouseIsHovering;
	
	public HUDWindow(int xpos, int ypos, int xsize, int ysize) {
		this.xpos = xpos * DisplayManager.resizeRatio;
		this.ypos = ypos * DisplayManager.resizeRatio;
		this.xsize = xsize * DisplayManager.resizeRatio;
		this.ysize = ysize * DisplayManager.resizeRatio;
		mouseStartPosition = new Vector2f();
		windowOffset = new Vector2f();
		
		backgroundTexture = new HUDTexture(MainGameLoop.loader.loadTexture("hudwindow_background"), this.xpos, this.ypos, this.xsize, this.ysize);
		taskbar = new HUDTexture(MainGameLoop.loader.loadTexture("hudwindow_background"), this.xpos, this.ypos, this.xsize, 35 * DisplayManager.resizeRatio);
		backgroundTexture.startDrawing();
		taskbar.startDrawing();
		
		closeButton = new HUDButton("close_button", this.xpos + this.xsize - 50 * DisplayManager.resizeRatio, this.ypos,
				50 * DisplayManager.resizeRatio, 35 * DisplayManager.resizeRatio, false);
		
		mouseIsHovering = false;
		mouseIsMoving = false;

	}
	
	protected abstract void updateContent();
	
	protected abstract void renderContent();
	
	protected abstract void moveContentWithWindowMovement(int xpos, int ypos);
	
	protected abstract void destroyContent();	
	
	public void update() {
		if(mouseIsHovering) {
			if(!mouseIsMoving) {
				checkForMouseStopsHovering();
				onWindowClose();	
			}
			onWindowMove();
		}else {
			checkForMouseStartsHovering();
		}
		
		updateContent();
		renderContent();
	}
		
	private void checkForMouseStartsHovering() {
		if(Mouse.getX() < xpos + xsize && Mouse.getX() > xpos) {
			if(DisplayManager.HEIGHT - Mouse.getY() < ypos + ysize && DisplayManager.HEIGHT - Mouse.getY() > ypos) {
				mouseIsHovering = true;
				MainGameLoop.gameState = GameState.UI_MODE;
			}
		}
	}
	
	private void checkForMouseStopsHovering() {
		if(Mouse.getX() > xpos + xsize || Mouse.getX() < xpos ||
			DisplayManager.HEIGHT - Mouse.getY() > ypos + ysize || DisplayManager.HEIGHT - Mouse.getY() < ypos) {
				mouseIsHovering = false;
				MainGameLoop.gameState = GameState.GAME_MODE;
		}
	}
	
	private void onWindowMove() {
		if(Mouse.isButtonDown(0) && !mouseIsMoving) {
			if(Mouse.getX() < xpos + xsize - 50 * DisplayManager.resizeRatio) {
				if(DisplayManager.HEIGHT - Mouse.getY() < ypos + 50 * DisplayManager.resizeRatio) {
					MainGameLoop.gameState = GameState.UI_MODE;
					mouseIsMoving = true;
					mouseStartPosition.x = Mouse.getX();
					mouseStartPosition.y = Mouse.getY();
				}
			}
		}else if(Mouse.isButtonDown(0) && mouseIsMoving) {
			windowOffset.x = Mouse.getX() - mouseStartPosition.x;
			windowOffset.y = -Mouse.getY() + mouseStartPosition.y;
			updateWindowPosition(xpos + (int)windowOffset.x, ypos + (int)windowOffset.y);
		}else if(mouseIsMoving && !Mouse.isButtonDown(0)) {
			xpos += windowOffset.x;
			ypos += windowOffset.y;
			mouseIsMoving = false;
			
		}
	}
	
	private void updateWindowPosition(int xpos, int ypos) {
		closeButton.setPosition(xpos + xsize - 50 * DisplayManager.resizeRatio, ypos);
		backgroundTexture.setPosition(xpos, ypos);
		taskbar.setPosition(xpos, ypos);
		moveContentWithWindowMovement(xpos, ypos);
		
	}

	
	private void onWindowClose() {
		closeButton.update();
		if(closeButton.onMouseClicked()) {
			backgroundTexture.stopDrawing();
			taskbar.stopDrawing();
			closeButton.destroy();
			destroyContent();
			HUDRenderList.removeWindow(this);
		}
	}
}