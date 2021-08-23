package hud;

import java.util.ArrayList;
import java.util.List;

public class HUDManager {

	private static List<HUDTexture> huds;
	private HUDRenderer renderer;
	
	public HUDManager() {
		huds = new ArrayList<HUDTexture>();
		renderer = new HUDRenderer();
	}
	
	public void update() {
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
