package menu;

import animations.LinearAnimation;
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
		resumeButton = new HUDButton("pauseMenu_button", -300, 200, 300, 60, true);
		resumeButton.setText("Resume", 1.3f, 0.9f, 0.9f, 0.9f);
		resumeButton.swipe(1300, 0, 100, 0, 0);
	
		optionsButton = new HUDButton("pauseMenu_button", -300, 300, 300, 60, true);
		optionsButton.setText("Options", 1.3f, 0.9f, 0.9f, 0.9f);
		optionsButton.swipe(1300, 0, 100, 0, 0.2f);

		saveButton = new HUDButton("pauseMenu_button", -300, 400, 300, 60, true);
		saveButton.setText("Save World", 1.3f, 0.9f, 0.9f, 0.9f);
		saveButton.swipe(1300, 0, 100, 0, 0.4f);

		exitButton = new HUDButton("pauseMenu_button", -300, 500, 300, 60, true);
		exitButton.setText("Quit", 1.3f, 0.9f, 0.9f, 0.9f);
		exitButton.swipe(1300, 0, 100, 0, 0.6f);
		
		timer = new Timer(1.6f);
	}

	@Override
	protected void updateContent() {
		timer.update();
		MainGameLoop.player.increaseRotation(0, 5 * DisplayManager.getFrameTimeSeconds(), 0);
		updateButtons();
		
	}

	@Override
	protected boolean updateActivationProcess() {
		if(MainGameLoop.camera.getPitch() < 50) {
			MainGameLoop.camera.setPitch(MainGameLoop.camera.getPitch() + 50.f * DisplayManager.getFrameTimeSeconds());
		}
		
		if(timer.timeReachedEnd() && MainGameLoop.camera.getPitch() >= 50) {
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean updateDeactivationProcess() {
		exitActivated = false;
		
		if(exitButton.getXPos() <= -300) {
			resumeButton.destroy();
			optionsButton.destroy();
			saveButton.destroy();
			exitButton.destroy();
			if(quitToDesktop != null) {
				quitToDesktop.destroy();
				quitToMainMenu.destroy();
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected void onDeactivated() {
		timer.restart();
		resumeButton.swipe(-1300, 0, -300, 0, 0);
		optionsButton.swipe(-1300, 0, -300, 0, 0.2f);
		saveButton.swipe(-1300, 0, -300, 0, 0.4f);
		exitButton.swipe(-1300, 0, -300, 0, 0.6f);
		
		if(quitToDesktop != null) {
			quitToDesktop.swipe(0, 1300, 0, 900, 0);
			quitToMainMenu.swipe(0, 1300, 0, 900, 0.1f);
		}
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
	
	private boolean exitActivated = false;
	private void checkForButtonClick() {
		if(resumeButton.onMouseClicked()) {
			MenuUpdater.deactivateCurrentMenu();
		}
		if(exitButton.onMouseClicked()) {
			if(quitToDesktop == null) {
				exitActivated = true;
				quitToDesktop = new HUDButton("pauseMenu_button", 450, 900, 300, 60, true);
				quitToDesktop.setText("Quit to Desktop", 1.3f, 0.9f, 0.9f, 0.9f);
				quitToDesktop.swipe(0, -1300, 0, 500, 0);

				quitToMainMenu = new HUDButton("pauseMenu_button", 450, 900, 300, 60, true);
				quitToMainMenu.setText("Quit to Main Menu", 1.3f, 0.9f, 0.9f, 0.9f);
				quitToMainMenu.swipe(0, -1300, 0, 600, 0);
			}else {
				timer.restart();
				exitActivated = false;
				quitToDesktop.swipe(0, 1300, 0, 900, 0);
				quitToMainMenu.swipe(0, 1300, 0, 900, 0.1f);
				timer.restart();
			}
		}
		if(quitToDesktop != null && !exitActivated) {
			if(quitToDesktop.getYPos() >= 900) {
				quitToDesktop.destroy();
				quitToDesktop = null;
				quitToMainMenu.destroy();
				quitToMainMenu = null;
			}
			if(quitToDesktop != null) {
				if(quitToDesktop.onMouseClicked()) {
					MainGameLoop.closeGame();
				}
			}	
		}
	}
}