package traffic;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import World.TileManager;
import entities.Camera;
import entities.EntityShadowList;
import people.Pedestrian;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import streets.Blind_Alley;
import streets.Forward;
import toolbox.Maths;
import toolbox.MousePicker;
import vehicles.Car;
import vehicles.Truck;
import vehicles.MovingEntity;

public class TrafficManager {

	private List<MovingEntity> vehicles;
	private List<MovingEntity> people;
	
	public TrafficManager() {
		vehicles = new ArrayList<MovingEntity>();
		people = new ArrayList<MovingEntity>();
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
			if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
				spawnPedestrian(xtile, ytile);
				tslc = 0;
			}
		}

		for(int i = 0; i < vehicles.size(); i++) {
			vehicles.get(i).update();
		}
		for(int i = 0; i < people.size(); i++) {
			people.get(i).update();
		}
	}
	
	public void render(MasterRenderer renderer) {
		for(int i = 0; i < vehicles.size(); i++) {
			vehicles.get(i).render(renderer);
		}
		for(int i = 0; i < people.size(); i++) {
			people.get(i).render(renderer);
		}
	}
	
	private void spawnNormalVehicle(int xtile, int ytile) {
		int randNum = Maths.getRandomBetween(0, 10);
		if(randNum <= 8) {
			vehicles.add(new Car(StreetManager.getStreetSystem()[xtile][ytile].getPathMarkers(true).get(0)));
		}else {
			vehicles.add(new Truck(StreetManager.getStreetSystem()[xtile][ytile].getPathMarkers(true).get(0)));
		}
	}
	
	private void spawnPedestrian(int xtile, int ytile) {
		if(StreetManager.getStreetSystem()[xtile][ytile] instanceof Forward ||
				StreetManager.getStreetSystem()[xtile][ytile] instanceof Blind_Alley) {
			people.add(new Pedestrian(StreetManager.getStreetSystem()[xtile][ytile].getPathMarkers(false).get(0)));
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
		
		return new Vector2f(tilex, tiley);
		
	}
	
}
