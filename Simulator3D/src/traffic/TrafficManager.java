package traffic;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import World.TileManager;
import entities.Camera;
import entities.EntityShadowList;
import mainPackage.MainGameLoop;
import people.Pedestrian;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import streets.Blind_Alley;
import streets.Forward;
import streets.No_Connection;
import streets.Street;
import toolbox.Maths;
import toolbox.MousePicker;
import vehicles.Car;
import vehicles.Truck;
import vehicles.MovingEntity;

/**
 * Updates and renders all MovingEntities and checks for new vehicles
 * @author Oriventi
 *
 */
public class TrafficManager implements Runnable{

	private Thread thread;
	private volatile boolean exit = false;
	
	private List<MovingEntity> vehicles;
	private List<MovingEntity> people;
	
	public TrafficManager() {
		vehicles = new ArrayList<MovingEntity>();
		people = new ArrayList<MovingEntity>();
	}
	
	/**
	 * Executes the function when thread is running
	 */
	public void run() {
		long lastFrameTime = Sys.getTime()* 1000 / Sys.getTimerResolution();
		double delta;
		System.out.println("THREAD 2 is running");
		while(!exit) {
			long currentFrameTime =  Sys.getTime()* 1000 / Sys.getTimerResolution();;
			delta = (currentFrameTime - lastFrameTime) / 1000.f;
			updateMovingEntities(delta);
			lastFrameTime = currentFrameTime;
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Thread.currentThread().interrupt();
		System.out.println("THREAD 2 interrupted");
	}
	
	/**
	 * Stops the thread
	 */
	public void stopThread() {
		exit = true;
		thread.interrupt();
	}
	
	/**
	 * Starts the thread
	 */
	public void start() {
		if(thread == null) {
			thread = new Thread(this, "THREAD 2");
			thread.start();
		}
	}
	
	/**
	 * checks for new user input to spawn movingEntity
	 * @param picker
	 * @param cam
	 */
	float tslc;
	public synchronized void updateUserInput(MousePicker picker, Camera cam) {
		int xtile, ytile;
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
	}
	
	/**
	 * updates each pedestrian and vehicle
	 */
	public void updateMovingEntities(double delta) {
		for(int i = 0; i < vehicles.size(); i++) {
			vehicles.get(i).update(delta);
		}
		for(int i = 0; i < people.size(); i++) {
			people.get(i).update(delta);
		}
	}
	
	/**
	 * renders each vehicle and pedestrian
	 * @param renderer
	 */
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
		if(randNum <= 10) {
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
	
	public void spawnRandomTraffic() {
		people.clear();
		vehicles.clear();
		for(int i = 0; i < TileManager.size; i++) {
			for(int j = 0; j < TileManager.size; j++) {
				if(StreetManager.getStreetSystem()[i][j] != null && !(StreetManager.getStreetSystem()[i][j] instanceof No_Connection)) {
					if(Maths.getRandomBetween(0, 10) <= 4) {
						spawnNormalVehicle(i, j);
					}
					if(Maths.getRandomBetween(0, 10) <= 6) {
						spawnPedestrian(i, j);	
					}
				}
			}
		}
	}
	
}
