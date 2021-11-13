package menu;

import animations.Timer;
import hud.HUDButton;
import mainPackage.MainGameLoop;
import renderEngine.DisplayManager;

public class PauseMenu extends Menu{

	private HUDButton quitToDesktop;
	private HUDButton quitToMainMenu;
	
	private HUDButton[] buttons;
	private String[] buttonNames = new String[]{"Resume", "Options", "Save World", "Quit", "Quit to Desktop", "Quit to Main Menu"};
	
	
	private Timer timer;
		
	public PauseMenu() {
		super();
		buttons = new HUDButton[6];
		for(int i = 0; i < buttons.length; i++) {
			if(i < 4) {
				buttons[i] = new HUDButton("pauseMenu_button", -300, 200 + i * 100, 300, 60, true, false);
				buttons[i].setText(buttonNames[i], 1.3f, 1, 1, 1, false);
				buttons[i].swipeTo(1300, 0, 100, 0, i * 0.2f);
			}else {
				buttons[i] = new HUDButton("pauseMenu_button", 450, 900, 300, 60, true, false);
				buttons[i].setText(buttonNames[i], 1.3f, 1, 1, 1, false);
			}
		}
		
		timer = new Timer(2f);
	}

	@Override
	protected void updateContent() {
		timer.update();
		MainGameLoop.player.increaseRotation(0, 5 * DisplayManager.getFrameTimeSeconds(), 0);
		
		updateButtons();
		checkForButtonClick();
	}

	@Override
	protected boolean updateActivationProcess() {
		if(MainGameLoop.camera.getPitch() < 50) {
			MainGameLoop.camera.setPitch(MainGameLoop.camera.getPitch() + 50.f * DisplayManager.getFrameTimeSeconds());
		}
		MainGameLoop.camera.swipePitchTo(50, 50);
		
		if(timer.timeReachedEnd() && MainGameLoop.camera.getPitch() >= 50) {
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean updateDeactivationProcess() {
		if(buttons[3].hasSwipingFinished()) {
			for(int i = 0; i < buttons.length; i++) {
				if(buttons[i] != null) {
					buttons[i].destroy();
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected void onDeactivated() {
		timer.restart();
		for(int i = 0; i < buttons.length; i++) {
			if(buttons[i] != null) {
				if(i < 4) {
					buttons[i].swipeTo(-1300, 0, -300, 0, i * 0.2f);
				}else {
					buttons[i].swipeTo(0, 1300, 0, 900, (i-4) * 0.1f);
				}
			}
		}
	}
	
	private void updateButtons() {
		for(int i = 0; i < buttons.length; i++) {
			if(buttons[i] != null) {
				buttons[i].update();
			}
		}
	}
	
	private void checkForButtonClick() {
		if(buttons[0].onMouseClicked()) {
			MenuUpdater.deactivateCurrentMenu();
		}
		
		if(buttons[3].onMouseClicked()) {
			if(buttons[4].getYPos() >= 900 * DisplayManager.resizeRatio) {
				buttons[4].swipeTo(0, -1300, 0, 500, 0);
				buttons[5].swipeTo(0, -1300, 0, 600, 0);
				buttons[4].enable();
				buttons[5].enable();
			}else {
				buttons[4].swipeTo(0, 1300, 0, 900, 0);
				buttons[5].swipeTo(0, 1300, 0, 900, 0.1f * DisplayManager.resizeRatio);
				buttons[4].disable();
				buttons[5].disable();
			}
		}
			
		if(buttons[4].isEnabled()) {
			if(buttons[4].onMouseClicked()) {
				MainGameLoop.closeGame();
			}
		}
		
	}
}