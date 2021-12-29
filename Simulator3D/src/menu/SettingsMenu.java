package menu;

import hud.HUDButton;

public class SettingsMenu extends Menu {

	HUDButton button[] = new HUDButton[1];
	
	public SettingsMenu() {
		super();
		button[0] = new HUDButton("menu_button", -500, 50, 50, 50, true, false);
		button[0].setFrontTexture("menu_back_button");
		button[0].swipeTo(1200, 0, 50, 0, 0);
	}
	
	@Override
	protected void updateContent() {
		checkForButtonClick();
	}

	@Override
	protected void onDeactivated() {
		button[0].swipeTo(-1200, 0, -500, 0, 0);
		for(HUDButton btn : button) {
			btn.disable();
		}
	}

	@Override
	protected boolean updateActivationProcess() {
		if(button[0].hasSwipingFinished()) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean updateDeactivationProcess() {
		if(button[0].hasSwipingFinished()) {
			button[0].destroy();
			return true;
		}
		return false;
	}
	
	private void checkForButtonClick() {
		if(button[0].onMouseClicked()) {
			MenuUpdater.switchToMenu(new PauseMenu());
		}
	}

}
