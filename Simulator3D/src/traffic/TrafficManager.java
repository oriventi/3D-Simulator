package traffic;

import java.util.ArrayList;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import World.TileManager;
import entities.Camera;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import toolbox.MousePicker;
import worldEntities.Car;

public class TrafficManager {

	private List<Car> cars = new ArrayList<>();
	private StreetManager streetManager;
	
	public TrafficManager(StreetManager streetManager) {
		this.streetManager = streetManager;
	}
	
	
	private int xtile, ytile;
	private float tslc;
	public void update(MousePicker picker, Camera cam) {
		tslc += DisplayManager.getFrameTimeSeconds();
		xtile = (int) getTile(picker, cam).x;
		ytile = (int) getTile(picker, cam).y;
		if(streetManager.getStreetSystem()[xtile][ytile] != null && tslc >= 0.5f  ) {
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				cars.add(new Car(getNearestPathMarker(xtile, ytile, picker, cam), streetManager));
				tslc = 0;
			}
		}
	//	System.out.println(cars.size());
		for(int i = 0; i < cars.size(); i++) {
			cars.get(i).update();
		}
	}
	
	public void render(MasterRenderer renderer) {
		for(int i = 0; i < cars.size(); i++) {
			cars.get(i).render(renderer);
		}
	}
	
	//returns tile from mousePosition
	private Vector2f getTile(MousePicker picker, Camera cam) {
		int x = (int) picker.getPosition(cam).x;
		int z = (int) picker.getPosition(cam).z;
		int tilex = (int)((x + TileManager.wsize / 2) / TileManager.tsize);
		int tiley = (int)((z + TileManager.wsize / 2) / TileManager.tsize);
		
		if(tilex < 0) {
			tilex = 0;
		}
		if(tiley < 0) {
			tiley = 0;
		}
		if(tilex > TileManager.size - 1) {
			tilex = TileManager.size - 1;
		}
		if(tiley > TileManager.size - 1) {
			tiley = TileManager.size -1 ;
		}
		
		//Output XTile and YTile from Mouse hovered
		//System.out.println(tilex + " + " + tiley);
		
		return new Vector2f(tilex, tiley);
		
	}
	
	private PathMarker getNearestPathMarker(int xtile, int ytile, MousePicker picker, Camera cam) {
		Vector2f vec = new Vector2f();
		float shortestLength = 100;
		int shortestindex = -1;
		for(int i = 0; i < streetManager.getStreetSystem()[xtile][ytile].getPathMarkers().size(); i++) {
			vec.x = picker.getPosition(cam).x - streetManager.getStreetSystem()[xtile][ytile].getPathMarkers().get(i).getWorldPositionX();
			vec.y = picker.getPosition(cam).z - streetManager.getStreetSystem()[xtile][ytile].getPathMarkers().get(i).getWorldPositionY();
			if(vec.length() < shortestLength) {
				shortestLength = vec.length();
				shortestindex = i;
			}
		}
		
		return streetManager.getStreetSystem()[xtile][ytile].getPathMarkers().get(shortestindex);
	}
	
}
