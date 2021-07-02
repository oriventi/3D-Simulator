package traffic;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import World.TileManager;
import entities.Camera;
import entities.EntityManager;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import toolbox.Maths;
import toolbox.MousePicker;
import vehicles.Car;
import vehicles.Truck;
import vehicles.Vehicle;

public class TrafficManager {

	private List<Vehicle> vehicles = new ArrayList<>();
	
	public TrafficManager() {
	}
	
	
	private int xtile, ytile;
	private float tslc;
	public void update(MousePicker picker, Camera cam) {
		tslc += DisplayManager.getFrameTimeSeconds();
		xtile = (int) getTile(picker, cam).x;
		ytile = (int) getTile(picker, cam).y;
		if(StreetManager.getStreetSystem()[xtile][ytile] != null && tslc >= 0.5f  ) {
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				spawnNormalVehicle(xtile, ytile);
				tslc = 0;
			}
		}

		for(int i = 0; i < vehicles.size(); i++) {
			vehicles.get(i).update();
		}
	}
	
	public void render(MasterRenderer renderer) {
		for(int i = 0; i < vehicles.size(); i++) {
			vehicles.get(i).render(renderer);
		}
	}
	
	private void spawnNormalVehicle(int xtile, int ytile) {
		int randNum = Maths.getRandomBetween(0, 10);
		if(randNum <= 7) {
			vehicles.add(new Car(StreetManager.getStreetSystem()[xtile][ytile].getPathMarkers().get(0)));
		}else {
			vehicles.add(new Truck(StreetManager.getStreetSystem()[xtile][ytile].getPathMarkers().get(0)));
		}
		EntityManager.addEntity(vehicles.get(vehicles.size() - 1).getEntity());
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
	
}
