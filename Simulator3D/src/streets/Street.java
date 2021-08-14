package streets;

import java.util.List;


import org.lwjgl.util.vector.Vector3f;

import World.TileManager;

import java.util.ArrayList;

import traffic.PathMarker;
import traffic.StreetManager;
import vehicles.Car;
import vehicles.Vehicle;
import entities.Entity;
import entities.EntityShadowList;
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
	private List<Vehicle> cars;
	
	public Street(int xtile, int ytile) {
		
		pathMarkers = new ArrayList<PathMarker>();
		cars = new ArrayList<Vehicle>();
		
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
	
	public void destroy() {
		destroyContent();
		//EntityManager.removeEntity(entity);
	}
	
	protected abstract void destroyContent();

	private void makeEntity() {
		entity = new Entity(mesh, new Vector3f(xpos, 0, ypos), 0, rotation, 0, 1);
		//EntityManager.addEntity(entity);
	}
	
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
		renderContent(renderer);
	}
		
	public void addCar(Vehicle car) {
		cars.add(car);
	}
	
	public void removeCar(Vehicle car) {
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
	
	public List<Vehicle> getCars(){
		return cars;
	}
	
	public List<PathMarker> getPathMarkers(){
		return pathMarkers;
	}
}
