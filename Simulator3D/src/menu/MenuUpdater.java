package menu;

import mainPackage.MainGameLoop;
import toolbox.EnumHolder.GameState;

public class MenuUpdater {

	private static Menu activatedMenu;
	private static boolean isMenuActivated;
	
	public MenuUpdater() {
		activatedMenu = null;
		isMenuActivated = false;
	}
	
	public static void activateMenu(Menu menu) {
		MainGameLoop.gameState = GameState.UI_MODE;
		activatedMenu = menu;
		activatedMenu.startActivationProcess();
		isMenuActivated = true;
	}
	
	public static void deactivateCurrentMenu() {
		activatedMenu.startDeactivationProcess();
		activatedMenu.deactivated = true;
	}
	
	private void deleteMenu() {
		MainGameLoop.gameState = GameState.GAME_MODE;
		activatedMenu = null;
		isMenuActivated = false;
	}
	
	public void update() {
		if(isMenuActivated && activatedMenu != null) {
			activatedMenu.update();
			if(activatedMenu.hasDeactivationFinished()) {
				deleteMenu();
			}
		}
	}
	
	public static boolean isMenuActivated() {
		return isMenuActivated;
	}
}