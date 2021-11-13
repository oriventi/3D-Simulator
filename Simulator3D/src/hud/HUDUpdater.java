package hud;

import java.util.ArrayList;
import java.util.List;

import menu.MenuUpdater;

public class HUDUpdater {

	private static List<HUDTexture> huds;
	private static List<HUDWindow> windows;
	private static List<HUDTexture> menuHuds;
	private static List<HUDButton> buttons;
	private static List<HUDDialog> dialogs;
	private HUDRenderer renderer;
	
	public HUDUpdater() {
		huds = new ArrayList<HUDTexture>();
		windows = new ArrayList<HUDWindow>();
		menuHuds = new ArrayList<HUDTexture>();
		dialogs = new ArrayList<HUDDialog>();
		buttons = new ArrayList<HUDButton>();
		renderer = new HUDRenderer();
	}
	
	public void update() {
		if(!MenuUpdater.isMenuActivated()) {
			for(HUDWindow window : windows) {
				window.update();
			}
			for(HUDDialog dialog : dialogs) {
				dialog.update();
			}
		}
		for(HUDButton button : buttons) {
			button.update();
		}
	}
	
	public static void addWindow(HUDWindow window) {
		windows.add(window);
	}
	
	public static void removeWindow(HUDWindow window) {
		windows.remove(window);
	}
	
	public static void addHUD(HUDTexture hud) {
		huds.add(hud);
	}
	
	public static void removeHUD(HUDTexture hud) {
		huds.remove(hud);
	}
	
	public static void addMenuHUD(HUDTexture hud) {
		menuHuds.add(hud);
	}
	
	public static void removeMenuHUD(HUDTexture hud) {
		menuHuds.remove(hud);
	}
	

	public static void addButton(HUDButton button) {
		buttons.add(button);
	}
	
	public static void removeButton(HUDButton button) {
		buttons.remove(button);
	}
	
	public static void addDialog(HUDDialog dialog) {
		dialogs.add(dialog);
	}
	
	public static void removeDialog(HUDDialog dialog) {
		dialogs.remove(dialog);
	}

	
	public void render() {
		if(MenuUpdater.isMenuActivated()) {
			renderer.render(menuHuds);
		}else {
			renderer.render(huds);	
		}
	}
	
	public void cleanUp() {
		renderer.cleanUP();
	}
}
