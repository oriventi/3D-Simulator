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
		activatedMenu.activateMenu();
		isMenuActivated = true;
	}
	
	public static void deactivateMenu() {
		activatedMenu.deactivateMenu();
		activatedMenu.deactivated = true;
	}
	
	private void doDeactivating() {
		if(activatedMenu.deactivateMenu()) {
			MainGameLoop.gameState = GameState.GAME_MODE;
			activatedMenu = null;
			isMenuActivated = false;
		}
	}
	
	public void update() {
		if(isMenuActivated && activatedMenu != null) {
			activatedMenu.update();
			if(activatedMenu.deactivated) {
				doDeactivating();
			}
		}
	}
	
	public static boolean isMenuActivated() {
		return isMenuActivated;
	}
}