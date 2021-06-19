package traffic;

import org.lwjgl.util.vector.Vector3f;

import World.TileManager;
import entities.Entity;
import models.MeshContainer;
import renderEngine.MasterRenderer;

public class TrafficLight {
	
	private Entity entity;
	private boolean red;
	private int pathMarker; //index of pathmarker in street array
	
	public TrafficLight(int xtile, int ytile, float dx, float dy, int rot, int pathMarker, Street street) {
		entity = new Entity(MeshContainer.traffic_light, new Vector3f((xtile + dx) * TileManager.tsize - TileManager.wsize / 2, 0, (ytile + dy) * TileManager.tsize - TileManager.wsize / 2), 0, rot, 0, 1);
		this.pathMarker = pathMarker;
		if(pathMarker == 0 || pathMarker == 4) {
			setGreen(street);
		}else {
			setRed(street);
		}
	}
	
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
	}
	
	public void setRed(Street street) {
		red = true;
		street.getPathMarkers().get(pathMarker).setStop(red);
	}
	
	public void setGreen(Street street) {
		red = false;
		street.getPathMarkers().get(pathMarker).setStop(red);
	}
	
	public void changeLights(Street street) {
		red = !red;
		street.getPathMarkers().get(pathMarker).setStop(red);
	}

}
