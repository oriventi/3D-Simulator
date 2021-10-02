package hud;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import animations.LinearAnimation;
import mainPackage.MainGameLoop;
import menu.MenuUpdater;
import renderEngine.DisplayManager;
import toolbox.EnumHolder.GameState;

public class HUDDialog {
	
	private int xpos, ypos, xsize, ysize, original_xsize, original_ysize;
	private String questionText, headlineText;
	private HUDButton okButton, cancelButton;
	private HUDTexture backgroundTexture, taskbarTexture;
	private HUDText contentText, headline;
	private float scale;	
	
	private boolean isMenuDialog;
	private boolean isDrawing;
	private boolean mouseIsHovering;
	private boolean isSwiping;
	private LinearAnimation swipeAnimation;
	

	public HUDDialog(String headlineText, String questionText, int xsize, int ysize, boolean isMenuDialog) {
		
		this.xsize = xsize;
		this.ysize = ysize;
		original_xsize = xsize;
		original_ysize = ysize;
		xpos = (int)(1280 / 2 - xsize / 2);
		ypos = 900;
		this.questionText = questionText;
		this.headlineText = headlineText;
		scale = 0.1f;
		
		backgroundTexture = new HUDTexture(MainGameLoop.loader.loadTexture("hudwindow_background"),
				(int)(xpos * DisplayManager.resizeRatio), (int)(ypos * DisplayManager.resizeRatio),
				(int)(xsize * DisplayManager.resizeRatio),(int)(ysize * DisplayManager.resizeRatio), isMenuDialog);
		
		taskbarTexture = new HUDTexture(MainGameLoop.loader.loadTexture("hudwindow_background"),
				(int)(xpos * DisplayManager.resizeRatio), (int)(ypos * DisplayManager.resizeRatio),
				(int)(xsize * DisplayManager.resizeRatio), (int)(35 * DisplayManager.resizeRatio), isMenuDialog);
		
		this.isMenuDialog = isMenuDialog;
		isDrawing = false;
	}
	
	public void update() {
		if(isMenuDialog && MenuUpdater.isMenuActivated() || !isMenuDialog && !MenuUpdater.isMenuActivated()) {
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
		if(isSwiping){
			if(swipeAnimation.isFinished()) isSwiping = false;
			doSwipeUpdate();
		}
	}
	
	public void show() {
		isDrawing = true;
		backgroundTexture.startDrawing();
		taskbarTexture.startDrawing();
		okButton = new HUDButton("ok_button", xpos + xsize / 4 - 40, ypos + ysize - 80,
				80, 50, isMenuDialog);
		okButton.setText("OK  ", 1.5f, 0.9f, 0.9f, 0.9f, true);

		cancelButton = new HUDButton("cancel_button", xpos + xsize * 3/4 - 40, ypos+ ysize - 80, 80 , 50, isMenuDialog);
		setText();
		cancelButton.setText("NO  ", 1.5f, 0.9f, 0.9f, 0.9f, true);
		setPosition(xpos, ypos);
		swipe();
	}
	
	public void destroy() {
		okButton.destroy();
		backgroundTexture.stopDrawing();
	}
	
	private void setPosition(int xpos, int ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
		backgroundTexture.setPosition((int)(xpos * DisplayManager.resizeRatio), (int)(ypos * DisplayManager.resizeRatio));
		taskbarTexture.setPosition((int)(xpos * DisplayManager.resizeRatio), (int)(ypos * DisplayManager.resizeRatio));
		okButton.setPosition((int)((xpos + xsize / 4 - 40) * DisplayManager.resizeRatio), (int)((ypos + ysize - 80) * DisplayManager.resizeRatio));
		cancelButton.setPosition((int)((xpos+ xsize * 3/4 - 40) * DisplayManager.resizeRatio), (int)((ypos + ysize - 80) * DisplayManager.resizeRatio));
		removeText();
		setText();
	}

	
	private void swipe() {
		float runningTime = Math.abs((540 - ysize/2) / 600.f);
		if(ypos >= 900) {
			swipeAnimation = new LinearAnimation(runningTime, -1200, 360 - ysize / 2, 0);
		
		}else {
			swipeAnimation = new LinearAnimation(runningTime,  1200,  900, 0);
		}
		swipeAnimation.startAnimation();
		isSwiping = true;
	}
	
	private void doSwipeUpdate() {
		setPosition(xpos, (int)swipeAnimation.getCurrentValue(ypos));
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
	
	public boolean onPositiveClicked() {
		if(okButton.onMouseClicked()) {
			swipe();
			return true;
		}
		return false;
	}
	
	public boolean onNegativeClicked() {
		if(cancelButton.onMouseClicked()) {
			swipe();
			return true;
		}
		return false;
	}
	
	private void setText() {
		headline = new HUDText(headlineText, 1.2f, MainGameLoop.font, (int)((xpos + 20) * DisplayManager.resizeRatio),
				(int)((ypos + 6) * DisplayManager.resizeRatio),
				xsize, false, isMenuDialog);
		headline.setColour(0.9f, 0.9f, 0.9f);
		
		contentText = new HUDText(questionText, 1.2f, MainGameLoop.font, (int)((xpos + 20) * DisplayManager.resizeRatio),
				(int)((ypos + 70) * DisplayManager.resizeRatio),
				(int)((xsize - 40) * DisplayManager.resizeRatio), true, isMenuDialog);
		contentText.setColour(0.9f, 0.9f, 0.9f);
	}
	
	private void removeText() {
		headline.remove();
		headline = null;
		contentText.remove();
		contentText = null;
	}
	
}
