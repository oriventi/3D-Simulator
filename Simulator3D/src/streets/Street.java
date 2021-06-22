package streets;

import java.util.List;


import org.lwjgl.util.vector.Vector3f;

import World.TileManager;

import java.util.ArrayList;

import traffic.PathMarker;
import traffic.StreetManager;
import worldEntities.Car;
import entities.Entity;
import models.Mesh;
import renderEngine.MasterRenderer;

public abstract class Street {

	protected int xtile, ytile;
	protected float xpos, ypos;
	protected int rotation;
	public boolean top, right, bottom, left;
	
	private Mesh mesh;
	private Entity entity;
	
	protected List<PathMarker> pathMarkers;
	private List<Car> cars;
	
	public Street(int xtile, int ytile) {
		
		pathMarkers = new ArrayList<PathMarker>();
		cars = new ArrayList<Car>();
		
		this.xtile = xtile;
		this.ytile = ytile;
		xpos = (xtile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		ypos = (ytile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		
		init();
	}
	
	public void init() {
		top = StreetManager.hasTop(xtile, ytile);
		right = StreetManager.hasRight(xtile, ytile);
		bottom = StreetManager.hasBottom(xtile, ytile);
		left = StreetManager.hasLeft(xtile, ytile);
				
		mesh = setMesh();
		rotation = setRotation();
		placePathMarkers();
		applyPathMarkersToPosition();
		makeEntity();
	}
		
	protected abstract Mesh setMesh();
	
	protected abstract void placePathMarkers();
	
	private void applyPathMarkersToPosition() {
		for(int i = 0; i < pathMarkers.size(); i++) {
			pathMarkers.get(i).setPositionToStreetRotation(rotation);
		}
	}

	protected abstract int setRotation();
	
	protected abstract void renderContent(MasterRenderer renderer);

	private void makeEntity() {
		entity = new Entity(mesh, new Vector3f(xpos, 0, ypos), 0, rotation, 0, 1);
	}
	
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
		for(int i = 0; i < pathMarkers.size(); i++) {
			pathMarkers.get(i).render(renderer);
		}
		renderContent(renderer);
	}
		
	public void addCar(Car car) {
		cars.add(car);
	}
	
	public void removeCar(Car car) {
		cars.remove(car);
	}
	
	public int getNeighbors() {
		int neighbors = 0;
		if(top)
			neighbors += 1;
		if(right)
			neighbors += 1;
		if(bottom)
			neighbors += 1;
		if(left)
			neighbors += 1;
		return neighbors;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public int getTileX() {
		return xtile;
	}
	
	public int getTileY() {
		return ytile;
	}
	
	public float getPosX() {
		return xpos;
	}
	
	public float getPosY() {
		return ypos;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	public List<Car> getCars(){
		return cars;
	}
	
	public List<PathMarker> getPathMarkers(){
		return pathMarkers;
	}
}
