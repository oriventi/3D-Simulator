package hud;

import org.lwjgl.input.Mouse;
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
		
		this.xsize = xsize;
		this.ysize = ysize;
		xpos = (int)(1280 / 2 - xsize / 2);
		ypos = 900;
		this.questionText = questionText;
		this.headlineText = headlineText;
		
		backgroundTexture = new HUDTexture(MainGameLoop.loader.loadTexture("hudwindow_background"),
				(int)(xpos * DisplayManager.resizeRatio), (int)(ypos * DisplayManager.resizeRatio),
				(int)(xsize * DisplayManager.resizeRatio),(int)(ysize * DisplayManager.resizeRatio), isMenuDialog);
		
		taskbarTexture = new HUDTexture(MainGameLoop.loader.loadTexture("hudwindow_background"),
				(int)(xpos * DisplayManager.resizeRatio), (int)(ypos * DisplayManager.resizeRatio),
				(int)(xsize * DisplayManager.resizeRatio), (int)(35 * DisplayManager.resizeRatio), isMenuDialog);
		
		this.isMenuDialog = isMenuDialog;
		isDrawing = false;
	}
	
	/**
	 * checks whether mouse is hovering and executes the swipeAnimation if activated
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
		okButton = new HUDButton("ok_button", xpos + xsize / 4 - 40, ypos + ysize - 80,
				80, 50, isMenuDialog);
		okButton.setText("OK  ", 1.5f, 0.9f, 0.9f, 0.9f, true);

		cancelButton = new HUDButton("cancel_button", xpos + xsize * 3/4 - 40, ypos+ ysize - 80, 80 , 50, isMenuDialog);
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
	}
	
	/**
	 * sets the position of the top left corner of the dialog to xpos and ypos
	 * @param x position of the dialog
	 * @param y position of the dialog
	 */
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

	/**
	 * lets the dialog swipe from its current position to the middle of the screen
	 */
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
		if(Mouse.getX() > xpos * DisplayManager.resizeRatio && Mouse.getX() < (xpos + xsize) * DisplayManager.resizeRatio) {
			if(DisplayManager.HEIGHT - Mouse.getY() < (ypos + ysize) * DisplayManager.resizeRatio &&
					DisplayManager.HEIGHT - Mouse.getY() > ypos * DisplayManager.resizeRatio) {
				mouseIsHovering = true;
				MainGameLoop.gameState = GameState.UI_MODE;
			}
		}
	}
	
	/**
	 * checks whether mouse stops hovering and changes gameState to GAME_MODE
	 */
	private void onMouseStopsHovering() {
		if(Mouse.getX() > (xpos + xsize) * DisplayManager.resizeRatio || Mouse.getX() < xpos * DisplayManager.resizeRatio
				|| DisplayManager.HEIGHT - Mouse.getY() > (ypos + ysize) * DisplayManager.resizeRatio ||
				DisplayManager.HEIGHT - Mouse.getY() < ypos * DisplayManager.resizeRatio) {
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
		headline = new HUDText(headlineText, 1.2f, MainGameLoop.font, (int)((xpos + 20) * DisplayManager.resizeRatio),
				(int)((ypos + 6) * DisplayManager.resizeRatio),
				xsize, false, isMenuDialog);
		headline.setColour(0.9f, 0.9f, 0.9f);
		
		contentText = new HUDText(questionText, 1.2f, MainGameLoop.font, (int)((xpos + 20) * DisplayManager.resizeRatio),
				(int)((ypos + 70) * DisplayManager.resizeRatio),
				(int)((xsize - 40) * DisplayManager.resizeRatio), true, isMenuDialog);
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
