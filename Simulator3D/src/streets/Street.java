package streets;

import java.util.List;


import org.lwjgl.util.vector.Vector3f;

import World.TileManager;

import java.util.ArrayList;

import traffic.PathMarker;
import traffic.StreetManager;
import vehicles.Car;
import vehicles.MovingEntity;
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
	
	protected List<PathMarker> vehiclePathMarkers;
	protected List<PathMarker> peoplePathMarkers;
	private List<MovingEntity> allMovingEntitiesOnStreet;
	
	public Street(int xtile, int ytile) {
		
		vehiclePathMarkers = new ArrayList<PathMarker>();
		peoplePathMarkers = new ArrayList<PathMarker>();
		allMovingEntitiesOnStreet = new ArrayList<MovingEntity>();
		
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
		placeVehiclePathMarkers();
		placePeoplePathMarkers();
		applyPathMarkersToPosition();
		makeEntity();
	}
		
	protected abstract Mesh setMesh();
	
	protected abstract void placeVehiclePathMarkers();
	
	protected abstract void placePeoplePathMarkers();
	
	private void applyPathMarkersToPosition() {
		for(int i = 0; i < vehiclePathMarkers.size(); i++) {
			vehiclePathMarkers.get(i).setPositionToStreetRotation(rotation);
		}
		for(int i = 0; i < peoplePathMarkers.size(); i++) {
			peoplePathMarkers.get(i).setPositionToStreetRotation(rotation);
		}
	}

	protected abstract int setRotation();
	
	protected abstract void renderContent(MasterRenderer renderer);
	
	public void destroy() {
		destroyContent();
	}
	
	protected abstract void destroyContent();

	private void makeEntity() {
		entity = new Entity(mesh, new Vector3f(xpos, 0, ypos), 0, rotation, 0, 1);
	}
	
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
		renderContent(renderer);
	}
		
	public void addMovingEntity(MovingEntity movingEntity) {
		allMovingEntitiesOnStreet.add(movingEntity);
	}
	
	public void removeMovingEntity(MovingEntity movingEntity) {
		allMovingEntitiesOnStreet.remove(movingEntity);
	}
	
	public int getNeighboringStreetsCount() {
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
	
	public List<MovingEntity> getCars(){
		return allMovingEntitiesOnStreet;
	}
	
	public List<PathMarker> getPathMarkers(boolean returnVehiclePathMarkers){
		if(returnVehiclePathMarkers) {
			return vehiclePathMarkers;
		}else {
			return peoplePathMarkers;
		}
	}
}
