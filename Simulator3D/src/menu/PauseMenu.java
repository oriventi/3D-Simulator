package menu;

import mainPackage.MainGameLoop;
import renderEngine.DisplayManager;

public class PauseMenu extends Menu{

	private float initPlayerRotation;
	private float initPitch;
	
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
		if(MainGameLoop.camera.getPitch() > initPitch) {
			MainGameLoop.camera.setPitch(MainGameLoop.camera.getPitch() - 20.f * DisplayManager.getFrameTimeSeconds());
			return false;
		}
		return true;
		
	}
}