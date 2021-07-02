package entities;

import java.util.ArrayList;
import java.util.List;

import renderEngine.MasterRenderer;

public class LightManager {

	private static List<Light> lights;
	
	public LightManager(Light sun) {
		lights = new ArrayList<Light>();
		lights.add(sun);
	}
	
	public static void addLight(Light light) {
		lights.add(light);
	}
	
	public static void removeLight(Light light) {
		lights.remove(light);
	}
	
	public void render(MasterRenderer renderer, Camera cam) {
		renderer.render(lights, cam);
	}
	
	public Light getLight(int index) {
		return lights.get(index);
	}
	
	public static Light getSun() {
		return lights.get(0);
	}
}
