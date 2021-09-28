package hud;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import mainPackage.MainGameLoop;
import renderEngine.DisplayManager;
import toolbox.EnumHolder.GameState;

public class HUDDialog {
	
	private int xpos, ypos, xsize, ysize;
	private String question;
	private HUDButton okButton, cancelButton;
	private HUDTexture backgroundTexture, taskbarTexture;
	
	
	private boolean isMenuDialog;
	private boolean isDrawing;
	private boolean mouseIsHovering;
	

	public HUDDialog(String question, int xsize, int ysize, boolean isMenuDialog) {
		
		this.xsize = xsize;
		this.ysize = ysize;
		xpos = (int)(1280 / 2 - xsize / 2);
		ypos = (int)(720 / 2 - ysize / 2);
		this.question = question;
		
		backgroundTexture = new HUDTexture(MainGameLoop.loader.loadTexture("hudwindow_background"),
				(int)(xpos * DisplayManager.resizeRatio), (int)(ypos * DisplayManager.resizeRatio),
				(int)(xsize * DisplayManager.resizeRatio),(int)(ysize * DisplayManager.resizeRatio), isMenuDialog);
		
		taskbarTexture = new HUDTexture(MainGameLoop.loader.loadTexture("hudwindow_background"),
				(int)(xpos * DisplayManager.resizeRatio), (int)(ypos * DisplayManager.resizeRatio),
				(int)(xsize * DisplayManager.resizeRatio), (int)(ysize * DisplayManager.resizeRatio / 12), isMenuDialog);
		this.isMenuDialog = isMenuDialog;
		isDrawing = false;
	}
	
	public void update() {

		if(mouseIsHovering && isDrawing) {
			if(MainGameLoop.gameState == GameState.GAME_MODE) {
				MainGameLoop.gameState = GameState.UI_MODE;
			}
			onMouseStopsHovering();
			okButton.update();
			cancelButton.update();
		}else {
			onMouseStartsHovering();
		}
	}
	
	public void show() {
		isDrawing = true;
		backgroundTexture.startDrawing();
		taskbarTexture.startDrawing();
		okButton = new HUDButton("ok_button", (int)(xpos + xsize / 4 - 40 * DisplayManager.resizeRatio), (int)(ypos + ysize - 70 * DisplayManager.resizeRatio),
				80, 50, isMenuDialog);
		okButton.setText("OK  ", 1.5f, 0.9f, 0.9f, 0.9f, true);

		cancelButton = new HUDButton("cancel_button",(int)(xpos + xsize * 3/4 - 40 * DisplayManager.resizeRatio), (int)(ypos+ ysize - 70 * DisplayManager.resizeRatio), 80 , 50, isMenuDialog);
		cancelButton.setText("NO  ", 1.5f, 0.9f, 0.9f, 0.9f, true);
	}
	
	public void destroy() {
		okButton.destroy();
		backgroundTexture.stopDrawing();
	}
	
	private void setPosition(int xpos, int ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
		backgroundTexture.setPosition(xpos, ypos);
		taskbarTexture.setPosition(xpos, ypos);
		okButton.setPosition(xpos, ypos);
		cancelButton.setPosition(xpos, ypos);
		
	}
	
	private void resizeTo() {
		
	}
	
	private void onMouseStartsHovering() {
		if(Mouse.getX() > xpos * DisplayManager.resizeRatio && Mouse.getX() < (xpos + xsize) * DisplayManager.resizeRatio) {
			if(DisplayManager.HEIGHT - Mouse.getY() < (ypos + ysize) * DisplayManager.resizeRatio &&
					DisplayManager.HEIGHT - Mouse.getY() > ypos * DisplayManager.resizeRatio) {
				mouseIsHovering = true;
				MainGameLoop.gameState = GameState.UI_MODE;
			}
		}

	}
	
	private void onMouseStopsHovering() {
		if(Mouse.getX() > (xpos + xsize) * DisplayManager.resizeRatio || Mouse.getX() < xpos * DisplayManager.resizeRatio
				|| DisplayManager.HEIGHT - Mouse.getY() > (ypos + ysize) * DisplayManager.resizeRatio ||
				DisplayManager.HEIGHT - Mouse.getY() < ypos * DisplayManager.resizeRatio) {
			mouseIsHovering = false;
			MainGameLoop.gameState = GameState.GAME_MODE;
		}
	}
	
}
