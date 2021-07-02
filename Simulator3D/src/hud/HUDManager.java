package hud;

import java.util.ArrayList;
import java.util.List;

public class HUDManager {

	private List<HUDTexture> huds;
	private HUDRenderer renderer;
	
	public HUDManager() {
		huds = new ArrayList<HUDTexture>();
		renderer = new HUDRenderer();
	}
	
	public void addHUD(HUDTexture hud) {
		huds.add(hud);
	}
	
	public void removeHUD(HUDTexture hud) {
		huds.remove(hud);
	}
	
	public void render() {
		renderer.render(huds);
	}
	
	public void cleanUp() {
		renderer.cleanUP();
	}
}
