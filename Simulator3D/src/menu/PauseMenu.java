package menu;

import animations.Timer;
import hud.HUDButton;
import mainPackage.MainGameLoop;
import renderEngine.DisplayManager;

public class PauseMenu extends Menu{

	private HUDButton resumeButton;
	private HUDButton optionsButton;
	private HUDButton saveButton;
	private HUDButton exitButton;
	
	private HUDButton quitToDesktop;
	private HUDButton quitToMainMenu;
	
	private Timer timer;
	
	
	public PauseMenu() {
		super();
		initButtons();
		timer = new Timer(1.6f);
	}

	@Override
	protected void updateMenu() {
		timer.update();
		MainGameLoop.player.increaseRotation(0, 5 * DisplayManager.getFrameTimeSeconds(), 0);
		updateButtons();
		
	}

	@Override
	protected boolean onActivated() {
		if(MainGameLoop.camera.getPitch() < 50) {
			MainGameLoop.camera.setPitch(MainGameLoop.camera.getPitch() + 50.f * DisplayManager.getFrameTimeSeconds());
		}
		
		makeButtonSwipeIn(resumeButton, 0);
		makeButtonSwipeIn(optionsButton, 0.2f);
		makeButtonSwipeIn(saveButton, 0.4f);
		makeButtonSwipeIn(exitButton, 0.6f);
		
		if(timer.timeReachedEnd() && MainGameLoop.camera.getPitch() >= 50) {
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean onDeactivated() {
	if(resumeButton.getXPos() >= 100) timer.restart();
		makeButtonSwipeOut(resumeButton, 0);
		makeButtonSwipeOut(optionsButton, 0.2f);
		makeButtonSwipeOut(saveButton, 0.4f);
		makeButtonSwipeOut(exitButton, 0.6f);
		
		if(exitButton.getXPos() <= -300) {
			return true;
		}
		return false;
	}
	

	@Override
	protected void activate() {
		timer.restart();
		initButtons();
	}

	@Override
	protected void deactivate() {
	}
	
	private void initButtons() {
		resumeButton = new HUDButton("pauseMenu_button", -300, 200, 300, 60, true);
		resumeButton.setText("Resume", 1.3f, 0.9f, 0.9f, 0.9f);
	
		optionsButton = new HUDButton("pauseMenu_button", -300, 300, 300, 60, true);
		optionsButton.setText("Options", 1.3f, 0.9f, 0.9f, 0.9f);

		saveButton = new HUDButton("pauseMenu_button", -300, 400, 300, 60, true);
		saveButton.setText("Save World", 1.3f, 0.9f, 0.9f, 0.9f);

		exitButton = new HUDButton("pauseMenu_button", -300, 500, 300, 60, true);
		exitButton.setText("Quit", 1.3f, 0.9f, 0.9f, 0.9f);

	}
	
	private void updateButtons() {
		resumeButton.update();
		optionsButton.update();
		saveButton.update();
		exitButton.update();
		if(quitToDesktop != null) {
			quitToDesktop.update();
			quitToMainMenu.update();
		}
		checkForButtonClick();
	}
	
	private void checkForButtonClick() {
		if(resumeButton.onMouseClicked()) {
			MenuUpdater.deactivateMenu();
		}
		if(exitButton.onMouseClicked()) {
			if(quitToDesktop == null) {
				quitToDesktop = new HUDButton("pauseMenu_button", 450, 500, 300, 60, true);
				quitToDesktop.setText("Quit to Desktop", 1.3f, 0.9f, 0.9f, 0.9f);

				quitToMainMenu = new HUDButton("pauseMenu_button", 450, 600, 300, 60, true);
				quitToMainMenu.setText("Quit to Main Menu", 1.3f, 0.9f, 0.9f, 0.9f);
			}else {
				quitToDesktop.destroy();
				quitToDesktop = null;
				quitToMainMenu.destroy();
				quitToMainMenu = null;
			}
		}
	}
	
	private void makeButtonSwipeIn(HUDButton button, float delay) {
		if(timer.getCurrentTime() >= delay) {
			if(button.getXPos() < 100) {
				button.setPosition((int)(button.getXPos() + 1300 * DisplayManager.getFrameTimeSeconds()), button.getYPos());
			}else {
				button.setPosition(100, button.getYPos());
			}
		}
	}
	
	private void makeButtonSwipeOut(HUDButton button, float delay) {
		if(timer.getCurrentTime() >= delay) {
			//button.removeText();
			if(button.getXPos() > -300) {
				button.setPosition((int)(button.getXPos() - 1300 * DisplayManager.getFrameTimeSeconds()), button.getYPos());
			}
		}
	}
}