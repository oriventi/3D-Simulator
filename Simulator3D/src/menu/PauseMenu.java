package menu;

import hud.HUDButton;
import mainPackage.MainGameLoop;
import renderEngine.DisplayManager;

public class PauseMenu extends Menu{

	private HUDButton resumeButton;
	private HUDButton optionsButton;
	private HUDButton saveButton;
	private HUDButton exitButton;
	
	
	public PauseMenu() {
		super();
	}

	@Override
	protected void updateMenu() {
		if(activated) {
			MainGameLoop.player.increaseRotation(0, 10 * DisplayManager.getFrameTimeSeconds(), 0);
		}
	}

	@Override
	protected boolean onActivated() {
		if(MainGameLoop.camera.getPitch() < 50) {
			MainGameLoop.camera.setPitch(MainGameLoop.camera.getPitch() + 20.f * DisplayManager.getFrameTimeSeconds());
		}else {
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean onDeactivated() {
		return true;
		
	}
	
	private void initButtons() {
		resumeButton = new HUDButton(normalTextureName, xpos, ypos, xsize, ysize, true);
		optionsButton = new HUDButton(normalTextureName, xpos, ypos, xsize, ysize, true);
		saveButton = new HUDButton(normalTextureName, xpos, ypos, xsize, ysize, true);
		exitButton = new HUDButton(normalTextureName, xpos, ypos, xsize, ysize, true);
	}
}