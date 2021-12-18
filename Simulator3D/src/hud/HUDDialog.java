package hud;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import animations.LinearAnimation;
import animations.Timer;
import mainPackage.MainGameLoop;
import menu.MenuUpdater;
import renderEngine.DisplayManager;
import toolbox.EnumHolder.GameState;

/**
 * creates a dialog which shows a text and has two options to click: yes or no
 * to let the dialog work properly you have to call update() each frame
 * to show the dialog call show()
 * @author Oriventi
 *
 */
public class HUDDialog {
	
	private int xpos, ypos, xsize, ysize;
	private String questionText, headlineText;
	private HUDButton okButton, cancelButton;
	private HUDTexture backgroundTexture, taskbarTexture;
	private HUDText contentText, headline;
	
	private boolean isMenuDialog;
	private boolean isDrawing;
	private boolean mouseIsHovering;
	private boolean isSwiping;
	private LinearAnimation swipeAnimation;
	private boolean hasClicked = false;
	private Timer switchGameModeAfterClickTimer = new Timer(0.3f);
	
	/**
	 * creates a new HUDDialog
	 * @param text of the headline
	 * @param text of the content / question
	 * @param size of the dialog in x direction
	 * @param size of the dialog in y direction
	 * @param whether dialog is a part of the menu
	 */
	public HUDDialog(String headlineText, String questionText, int xsize, int ysize, boolean isMenuDialog) {
		
		this.xsize = (int)(xsize * DisplayManager.resizeRatio);
		this.ysize = (int)(ysize * DisplayManager.resizeRatio);
		xpos = (int)(Display.getWidth()/2 - this.xsize/2);
		ypos = (int)(900 * DisplayManager.resizeRatio);
		this.questionText = questionText;
		this.headlineText = headlineText;
		
		backgroundTexture = new HUDTexture(MainGameLoop.loader.loadTexture("hudwindow_background"), xpos, ypos,
				this.xsize, this.ysize, isMenuDialog);
		
		taskbarTexture = new HUDTexture(MainGameLoop.loader.loadTexture("hudwindow_background"), xpos, ypos,
				this.xsize, (int)(35 * DisplayManager.resizeRatio), isMenuDialog);
		
		this.isMenuDialog = isMenuDialog;
		isDrawing = false;
		HUDUpdater.addDialog(this);
	}
	
	/**
	 * doesnt need to be called, works automatically
	 * checks whether mouse is hovering and executes the swipeAnimation if activated and handles game mode
	 */
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
		
		if(hasClicked){
			if(switchGameModeAfterClickTimer.timeReachedEnd()) {
				switchGameModeAfterClickTimer.destroy();
				hasClicked = false;
				MainGameLoop.gameState = GameState.GAME_MODE;
			}else {
				MainGameLoop.gameState = GameState.UI_MODE;
			}
		}
	}
	
	/**
	 * activates all textures of the dialog and lets it swipe to the middle of the screen
	 */
	public void show() {
		isDrawing = true;
		backgroundTexture.startDrawing();
		taskbarTexture.startDrawing();
		okButton = new HUDButton("ok_button", xpos + xsize /4 - 40, ypos + ysize - 80,
				80, 50, isMenuDialog, false);
		okButton.setText("OK  ", 1.5f, 0.9f, 0.9f, 0.9f, true);

		cancelButton = new HUDButton("cancel_button", (int)(xpos + xsize)*3/4 - 40, ypos+ ysize - 80, 80 , 50, isMenuDialog, false);
		setText();
		cancelButton.setText("NO  ", 1.5f, 0.9f, 0.9f, 0.9f, true);
		setPosition(xpos, ypos);
		swipe();
	}
	
	/**
	 * destroys the all dialog components
	 */
	public void destroy() {
		okButton.destroy();
		cancelButton.destroy();
		backgroundTexture.stopDrawing();
		removeText();
		HUDUpdater.removeDialog(this);
	}
	
	/**
	 * sets the position of the top left corner of the dialog to xpos and ypos
	 * @param x position of the dialog
	 * @param y position of the dialog
	 */
	private void setPosition(int xpos, int ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
		backgroundTexture.setPosition(xpos, ypos);
		taskbarTexture.setPosition(xpos, ypos);
		okButton.setPosition(xpos + xsize/4 - (int)(40 * DisplayManager.resizeRatio), ypos + ysize - (int)(80 * DisplayManager.resizeRatio));
		cancelButton.setPosition(xpos+ xsize * 3/4 - (int)(40 * DisplayManager.resizeRatio), ypos + ysize - (int)(80 * DisplayManager.resizeRatio));
		removeText();
		setText();
	}

	/**
	 * lets the dialog swipe from its current position to the middle of the screen
	 */
	private void swipe() {
		float runningTime =Math.abs((540 - ysize/2) / 600.f) + 1.f;
		if(ypos >= 900) {
			swipeAnimation = new LinearAnimation(runningTime, -1200, (int)(360 * DisplayManager.resizeRatio) - ysize / 2, 0);
		
		}else {
			swipeAnimation = new LinearAnimation(runningTime,  1200,  (int)(900 * DisplayManager.resizeRatio), 0);
		}
		swipeAnimation.startAnimation();
		isSwiping = true;
	}
	
	/**
	 * updates the position of the dialog during the swipe animation
	 */
	private void doSwipeUpdate() {
		setPosition(xpos, (int)swipeAnimation.getCurrentValue(ypos));
	}
	
	/**
	 * checks whether mouse starts hovering and changes gameState to UI_MODE
	 */
	private void onMouseStartsHovering() {
		if(Mouse.getX() > xpos && Mouse.getX() < xpos + xsize) {
			if(DisplayManager.HEIGHT - Mouse.getY() < ypos + ysize &&
					DisplayManager.HEIGHT - Mouse.getY() > ypos) {
				mouseIsHovering = true;
				MainGameLoop.gameState = GameState.UI_MODE;
			}
		}
	}
	
	/**
	 * checks whether mouse stops hovering and changes gameState to GAME_MODE
	 */
	private void onMouseStopsHovering() {
		if(Mouse.getX() > xpos + xsize || Mouse.getX() < xpos
				|| DisplayManager.HEIGHT - Mouse.getY() > ypos + ysize ||
				DisplayManager.HEIGHT - Mouse.getY() < ypos) {
			mouseIsHovering = false;
			MainGameLoop.gameState = GameState.GAME_MODE;
		}
	}
	
	/**
	 * checks whether player clicked on yes button
	 * @return true if player clicked on the positive button
	 */
	public boolean onPositiveClicked() {
		if(okButton.onMouseClicked()) {
			swipe();
			switchGameModeAfterClickTimer.start();
			hasClicked = true;
			return true;
		}
		return false;
	}
	
	/**
	 * checks whether player clicked on no button
	 * @return true if player clicked on the negative button
	 */
	public boolean onNegativeClicked() {
		if(cancelButton.onMouseClicked()) {
			swipe();
			switchGameModeAfterClickTimer.start();
			hasClicked = true;
			return true;
		}
		return false;
	}
	
	/**
	 * draws the headline text and the content text at the specific position
	 */
	private void setText() {
		headline = new HUDText(headlineText, 1.2f, MainGameLoop.font, xpos + (int)(20 * DisplayManager.resizeRatio),
				ypos + (int)(6 * DisplayManager.resizeRatio),
				xsize, false, isMenuDialog);
		headline.setColour(0.9f, 0.9f, 0.9f);
		
		contentText = new HUDText(questionText, 1.2f, MainGameLoop.font, xpos + (int)(20 * DisplayManager.resizeRatio),
				ypos + (int)(70 * DisplayManager.resizeRatio),
				xsize - (int)(40 * DisplayManager.resizeRatio), true, isMenuDialog);
		contentText.setColour(0.9f, 0.9f, 0.9f);
	}
	
	/**
	 * removes all text
	 */
	private void removeText() {
		headline.remove();
		headline = null;
		contentText.remove();
		contentText = null;
	}
	
}
