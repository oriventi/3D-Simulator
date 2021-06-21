package streets;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import World.TileManager;

import java.util.ArrayList;

import traffic.PathMarker;
import worldEntities.Car;
import entities.Entity;
import models.Mesh;
import renderEngine.MasterRenderer;

public abstract class Street {

	protected int xtile, ytile;
	protected float xpos, ypos;
	protected int rotation;
	
	protected Mesh mesh;
	protected Entity entity;
	
	protected List<PathMarker> pathMarkers;
	public List<Car> cars;
	
	public Street(int xtile, int ytile) {
		
		pathMarkers = new ArrayList<PathMarker>();
		cars = new ArrayList<Car>();
		
		this.xtile = xtile;
		this.ytile = ytile;
		xpos = (xtile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		ypos = (ytile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		
		setRotation();
		placePathMarkers();
		applyPathMarkersToPosition();
		makeEntity();
	}
	
	protected abstract void setMesh();
	
	protected abstract void setRotation();
	
	protected abstract void placePathMarkers();
	
	private void applyPathMarkersToPosition() {
		for(int i = 0; i < pathMarkers.size(); i++) {
			pathMarkers.get(i).setPositionToStreetRotation(rotation);
		}
	}

	private void makeEntity() {
		entity = new Entity(mesh, new Vector3f(xpos, 0, ypos), 0, rotation, 0, 1);
	}
	
	protected abstract void update();
	
	public abstract void render(MasterRenderer renderer);
	
	public void addCar(Car car) {
		cars.add(car);
	}
	
	public void removeCar(Car car) {
		cars.remove(car);
	}
}
