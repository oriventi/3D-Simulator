package streets;

import java.util.List;
import java.util.ArrayList;

import traffic.PathMarker;
import entities.Entity;
import renderEngine.MasterRenderer;

public abstract class Street {

	protected int xtile, ytile;
	protected float xpos, ypos;
	protected int rotation;
	protected Entity entity;
	protected List<PathMarker> pathMarkers;
	
	public Street(int xtile, int ytile) {
		pathMarkers = new ArrayList<PathMarker>();
		this.xtile = xtile;
		this.ytile = ytile;
		
		setRotation();
		placePathMarkers();
		applyPathMarkersToPosition();
		makeEntity();
	}
	
	protected abstract void setRotation();
	
	protected abstract void placePathMarkers();
	
	private void applyPathMarkersToPosition() {
		for(int i = 0; i < pathMarkers.size(); i++) {
			pathMarkers.get(i).setPositionToStreetRotation(rotation);
		}
	}
	
	protected abstract void makeEntity();
	
	protected abstract void update();
	
	public abstract void render(MasterRenderer renderer);
}
