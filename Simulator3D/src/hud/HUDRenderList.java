package hud;

import java.util.ArrayList;
import java.util.List;

public class HUDRenderList {

	private static List<HUDTexture> huds;
	private static List<HUDWindow> windows;
	private HUDRenderer renderer;
	
	public HUDRenderList() {
		huds = new ArrayList<HUDTexture>();
		windows = new ArrayList<HUDWindow>();
		renderer = new HUDRenderer();
	}
	
	public void update() {
		for(int i = 0; i < windows.size(); i++) {
			windows.get(i).update();
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
	
	public void render() {
		renderer.render(huds);
	}
	
	public void cleanUp() {
		renderer.cleanUP();
	}
}
