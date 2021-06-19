package traffic;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.Mesh;
import models.MeshContainer;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import worldEntities.Car;

public class Street{
	
	private boolean top, right, bottom, left;
	private int xtile, ytile;
	private float xpos, ypos;
	private int size, wsize, tsize;
	private int neighbors;
	private Mesh mesh;
	private Entity entity;
	private Loader loader;
	private int rot;
	private List<PathMarker> markers;
	private List<Car> cars;
	private TrafficLightManager trafficLightManager;
	
	
	public Street(int xtile, int ytile, boolean top, boolean right, boolean bottom, boolean left, Loader loader, int size, int wsize) {
		this.xtile = xtile;
		this.ytile = ytile;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
		this.loader = loader;
		rot = 0;
		this.size = size;
		this.wsize = wsize;
		tsize = wsize / size;
		xpos = xtile * tsize - wsize / 2 + tsize / 2;
		ypos = ytile * tsize - wsize / 2 + tsize / 2;
		entity = new Entity(null, new Vector3f(0, 0, 0), 0, 0, 0, 1);
		trafficLightManager = new TrafficLightManager(this);
		markers = new ArrayList<>();
		cars = new ArrayList<>();
		updateNeighborsInteger();
		placePathMarkers();
		updateTrafficLights();
		
	}
	
	private void updateTrafficLights() {
		trafficLightManager.update(this);
	}
	
	//changes the light
	public void changeTrafficLights() {
		trafficLightManager.changeTrafficLights();
	}
	
	//places the Path markers for the current street type
	//NORMAL TURN Y = 0.25 AND X = 0.25
	//SMALL TURN Y = 0.15 AND X = 0.15
	//BIG TURN Y = 0.55 AND X = 0.55
	private void placePathMarkers() {
		markers.clear();
		if(neighbors == 1) {
			markers.add(new PathMarker(xtile, ytile, 0.65f, 0.9f, new int[] {1, -1, -1, -1, -1}));
			markers.add(new PathMarker(xtile, ytile, 0.65f, 0.45f, new int[] {-1, -1, -1, 2, -1}));
			markers.add(new PathMarker(xtile, ytile, 0.5f, 0.3f, new int[] {-1, -1, -1, 3, -1}));
			markers.add(new PathMarker(xtile, ytile, 0.35f, 0.45f, new int[] {4, -1, -1, -1, -1}));
			markers.add(new PathMarker(xtile, ytile, 0.35f, 0.9f, null));
		}else if(neighbors == 2){
			if((left && right) || (top && bottom)) {
				markers.add(new PathMarker(xtile, ytile, 0.65f, 0.9f, new int[] {1, -1, -1, -1, -1}));
				markers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, null));
				markers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new int[] {3, -1, -1, -1, -1}));
				markers.add(new PathMarker(xtile, ytile, 0.35f, 0.9f, null));
			}else {
				markers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new int[] {-1, -1, 1, -1, -1}));
				markers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, null));
				markers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new int[] {3, -1, -1, -1, -1}));
				markers.add(new PathMarker(xtile, ytile, 0.35f, 0.4f, new int[] {-1, 4, -1, -1, -1}));
				markers.add(new PathMarker(xtile, ytile, 0.6f, 0.65f, new int[] {5, -1, -1, -1, -1}));
				markers.add(new PathMarker(xtile, ytile, 0.9f, 0.65f, null));
			}
		}else if(neighbors == 3){
			markers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new int[] {3, -1, 1, -1, -1}));
			markers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, null));
			markers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new int[] {-1, -1, 3, -1, 5}));
			markers.add(new PathMarker(xtile, ytile, 0.1f, 0.35f, null));
			markers.add(new PathMarker(xtile, ytile, 0.1f, 0.65f, new int[] {5, -1, -1, -1, 1}));
			markers.add(new PathMarker(xtile, ytile, 0.9f, 0.65f, null));	
		}else if(neighbors == 4){
			markers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new int[] {3, -1, 1, -1, 5}));
			markers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, null));
			markers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new int[] {5, -1, 3, -1, 7}));
			markers.add(new PathMarker(xtile, ytile, 0.1f, 0.35f, null));
			markers.add(new PathMarker(xtile, ytile, 0.1f, 0.65f, new int[] {7, -1, 5, -1, 1}));
			markers.add(new PathMarker(xtile, ytile, 0.35f, 0.9f, null));
			markers.add(new PathMarker(xtile, ytile, 0.65f, 0.9f, new int[] {1, -1, 7, -1, 3}));
			markers.add(new PathMarker(xtile, ytile, 0.9f, 0.65f, null));	
		}else {
			markers.clear();
		}
		
		updatePathMarkersPosition();
	}
	
	//updates for each marker the position
	private void updatePathMarkersPosition() {
		for(int i = 0; i < markers.size(); i++) {
			markers.get(i).setPositionToStreetRotation(rot);
		}
	}
	
	public void renderPathMarkers(MasterRenderer renderer) {
		for(int i = 0; i < markers.size(); i++) {
			markers.get(i).render(renderer);
		}
	}
	
	public void renderTrafficLights(MasterRenderer renderer) {
		trafficLightManager.render(renderer);
	}

	//Method to get the neighbors variable
	private void updateNeighborsInteger() {
		neighbors = 0;
		if(top)
			neighbors += 1;
		if(right)
			neighbors += 1;
		if(bottom)
			neighbors += 1;
		if(left)
			neighbors += 1;
	}
	
	//those booleans are true only if something has changed
	//e.g. top has changed: topchange = true, rightchange = false, bottomchange = false, leftchange == false
	public void update(boolean topchange, boolean rightchange, boolean bottomchange, boolean leftchange) {
		if(topchange) {
			top = !top;
		}
		if(rightchange) {
			right = !right;
		}
		if(bottomchange) {
			bottom = !bottom;
		}
		if(leftchange) {
			left = !left;
		}
		updateNeighborsInteger();
		pickCorrectMesh();
		applyRotation();
		updateTrafficLights();
	}
	
	//generates the street entity from given data
	public Entity generateEntity() {
		pickCorrectMesh();
		applyRotation();
		entity.setRotY(rot);
		entity.setPosition(xpos, 0, ypos);
		entity.setModel(mesh);
		entity.setScale((float)tsize/10.f);
		return entity;
	}
	
	
	//applies the *rot* integer from given data
	public void applyRotation() {
		if(neighbors == 1) {
			//Blind alley rotation
			if(top) {
				rot = 180;
			}else if(left) {
				rot = -90;
			}else if(right) {
				rot = 90;
			}else {
				rot = 0;
			}
		}else if(neighbors == 2) {
			//forward street rotation
			if(left && right) {
				rot = 90;
			}else if(top && bottom) {
				rot = 0;
				
			//curve rotation
			}else if(top && right) {
				rot = 0;
			}else if(right && bottom) {
				rot = -90;
			}else if(bottom && left) {
				rot = 180;
			}else if(left && top) {
				rot = 90;
			}
			//t_junction rotation;
		}else if(neighbors == 3) {
			if(left && top && right) {
				rot = 0;
			}else if(top && right && bottom) {
				rot = -90;
			}else if(right && bottom && left) {
				rot = 180;
			}else if(bottom && left && top) {
				rot = 90;
			}
		}else {
			rot = 0;
		}
		
		placePathMarkers();
		updatePathMarkersPosition();
	}
	
	
	//picks the correct street_mesh from neighbor streets
	public void pickCorrectMesh() {
		if(neighbors == 0) {
			mesh = MeshContainer.no_connection;
		}else if(neighbors == 1) {
			mesh = MeshContainer.blind_alley;
		}else if(neighbors == 2) {
			
			if(top == bottom || left == right) {
				mesh = MeshContainer.forward;
			}else {
				mesh = MeshContainer.curve;
			}
			
		}else if(neighbors == 3) {
			mesh = MeshContainer.t_junction;
		}else if(neighbors == 4) {
			mesh = MeshContainer.intersection;
		}
	}
	
	public List<PathMarker> getPathMarkers(){
		return markers;
	}
	
	public void addCar(Car car) {
		cars.add(car);
	}
	
	public void removeCar(Car car) {
		cars.remove(car);
	}
	
	public List<Car> getCars(){
		return cars;
	}
	
	public float getWorldPositionX() {
		return xpos;
	}
	
	public float getWorldPositionY() {
		return ypos;
	}
	
	public int getTileX() {
		return xtile;
	}
	
	public int getTileY() {
		return ytile;
	}
	
	public int getNeighbors() {
		return neighbors;
	}
	
	public int getRotation() {
		return rot;
	}

}
