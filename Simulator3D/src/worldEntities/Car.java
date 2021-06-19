package worldEntities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import traffic.PathMarker;
import traffic.Street;
import traffic.StreetManager;
import World.TileManager;
import entities.Entity;
import models.Mesh;
import models.MeshContainer;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import toolbox.Maths;

public class Car{
	
	private StreetManager streetManager;
	private Street currentStreet;
	private PathMarker currentMarker;
	private PathMarker nextMarker;
	private Entity entity;
	private float rotation;
	private int rot90 = 0; //shows whether the car is driving 0/90/180/270 degrees turnt or not -- -z dir = 0°
	private float speed = 6;
	private float initspeed;
	private int MODE = -1; //0 = Straight -- 1 = Left -- 2 == Right -- 3 == small_left -- 4 == big_left
	private int xtile, ytile;
	
	private Vector3f pos = new Vector3f();
	private Vector2f dir = new Vector2f();
	
	public Car(PathMarker startMarker, StreetManager streetManager) {
		
		initspeed = speed;
		this.streetManager = streetManager;
		currentMarker = startMarker;
		pos.x = startMarker.getWorldPositionX();
		pos.y = 0.2f;
		pos.z = startMarker.getWorldPositionY();
		xtile = getTileX();
		ytile = getTileY();
		currentStreet = streetManager.getStreetSystem()[xtile][ytile];
		currentStreet.addCar(this);
		updateMarker(true);
		entity = new Entity(getRandomCar(), pos, 0, 0, 0, 0.5f);
		
	}
	
	public void update() {
		driveByMode();
		applyEntitiesRotation();
		adjustSpeedToFrontCar();
		applyEntitiesPosition();
		
	}
	
	//Drives the car in the current mode and checks for reachedNextMarker
	private void driveByMode() {
		if(reachedNextMarker()) {
			pos.x = nextMarker.getWorldPositionX();
			pos.z = nextMarker.getWorldPositionY();
			if(nextMarker.isStop()) {
				speed = 0.01f;
			}else {
				updateMarker(false);
			}
		}else {
			switch(MODE) {
				case 1:
					driveLeft();
					break;
				case 2:
					driveRight();
					break;
				case 3:
					driveSmallLeft();
					break;
				case 4:
					driveBigLeft();
					break;
				default:
					driveStraight();
					break;
			}
		}
	}
	
	//updates the Markers when a new Marker is reached and asks whether its called in the constructor so it wont update the currentMarker
	private void updateMarker(boolean init) {
		if(!init) {
		currentMarker = nextMarker;
		}
		int action = chooseAction();
		intlist.clear();
		
		if(action < 0) {
			//change street and tile
			Street street = getClosestStreet();
			nextMarker = street.getPathMarkers().get(getShortestMarker(street));
			xtile = street.getTileX();
			ytile = street.getTileY();
			currentStreet.removeCar(this);
			currentStreet = street;
			currentStreet.addCar(this);
		}else {
			//get marker in currentstreet
			nextMarker = streetManager.getStreetSystem()[getTileX()][getTileY()].getPathMarkers().get(action);
		}
		
		updateMode(action);
	}
	
	
	//chooses a random drive action of the new marker
	List<Integer> intlist = new ArrayList<>();
	private int chooseAction() {
		for(int i = 0; i < currentMarker.getActions().length; i++) {
			if(currentMarker.getActions()[i] >= 0) {
				intlist.add(currentMarker.getActions()[i]);
			}
		}
		if(intlist.size() == 0) {
			return -1;
		}else {
			return intlist.get(Maths.getRandomBetween(0, intlist.size() - 1));
		}	
	}
	
	//updates the current driving mode
	private void updateMode(int action) {
				
		for(int i = 0; i < currentMarker.getActions().length; i++) {
			if(action == currentMarker.getActions()[i]) {
				MODE = i;
				break;
			}else {
				MODE = -1;
			}
		}
		initRot90(); //sets rot90 var by mode
		s = 0; //for drive left and right
		antiBugTimer = 0; //for straight drive
		
	}

	
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
	}
	
	//adds dir to position
	private void applyEntitiesPosition() {
		pos.x += dir.x * DisplayManager.getFrameTimeSeconds();
		pos.z += dir.y * DisplayManager.getFrameTimeSeconds();
	}
	
	//adjusts the cars rotation to dir
	private void applyEntitiesRotation() {
		rotation = (float) -(Math.atan2(dir.y, dir.x) * 180 / Math.PI) + 90;
		entity.setRotY(rotation);
	}
	
	
	private float antiBugTimer = 0;
	//driveStraight MODE
	private void driveStraight() {
		//makes Vector
		if(antiBugTimer == 0) {
			dir.x = nextMarker.getWorldPositionX() - currentMarker.getWorldPositionX();
			dir.y = nextMarker.getWorldPositionY() - currentMarker.getWorldPositionY();
			 
		}
		
		dir.normalise();
		dir.scale(speed);
		
		//makes antiBugTimer
		if(speed > 0) {
			antiBugTimer += DisplayManager.getFrameTimeSeconds() * (speed / initspeed);
		}
		
		//if fps is too low errors will occure and will be reset
		if(antiBugTimer > TileManager.tsize / speed + 1.f) {
			System.out.println("I HANGED UP!!");
			pos.x = currentMarker.getWorldPositionX();
			pos.z = currentMarker.getWorldPositionY();
			updateMarker(true);
		}
	}
	
	//driveRight MODE
	private double s = 0;
	private void driveRight() {
		dir.x = (float) -Math.sin(0.5 * Math.PI / 3.93 * s);
		dir.y = (float) Math.cos(0.5 * Math.PI / 3.93 * s);
		dir.normalise();
		dir.scale(speed);
		s += dir.length() * DisplayManager.getFrameTimeSeconds();
		dir = Maths.rotateVectorBy90Degrees(dir, (int)( rot90 / 90));
		//if fps is too low errors will occure and will be reset
		if(s > 4.3f) {
			s = 0;
			System.out.println("I HANGED UP!!");
			pos.x = currentMarker.getWorldPositionX();
			pos.z = currentMarker.getWorldPositionY();
			updateMarker(true);
		}
	}
	
	//driveLeft MODE
	private void driveLeft() {
		dir.x = (float) Math.sin(0.5 * Math.PI / 3.93 * s);
		dir.y = (float) Math.cos(0.5 * Math.PI / 3.93 * s);
		dir.normalise();
		dir.scale(speed);
		s += dir.length() * DisplayManager.getFrameTimeSeconds();
		dir = Maths.rotateVectorBy90Degrees(dir, (int)(rot90 / 90));
		//if fps is too low errors will occure and will be reset
		if(s > 4.3f) {
			s = 0;
			System.out.println("I HANGED UP!!");
			pos.x = currentMarker.getWorldPositionX();
			pos.z = currentMarker.getWorldPositionY();
			updateMarker(true);
		}
	}
	
	//driveSmallLeft MODE
	private void driveSmallLeft() {
		dir.x = (float) Math.sin(0.5 * Math.PI / 2.36 * s);
		dir.y = (float) Math.cos(0.5 * Math.PI / 2.36 * s);
		dir.normalise();
		dir.scale(speed);
		s+= dir.length() * DisplayManager.getFrameTimeSeconds();
		dir = Maths.rotateVectorBy90Degrees(dir,  (int)((270 + rot90) / 90));
		//if fps is too low errors will occure and will be reset
		if(s > 2.8f) {
			s = 0;
			System.out.println("I HANGED UP!!");
			pos.x = currentMarker.getWorldPositionX();
			pos.z = currentMarker.getWorldPositionY();
			updateMarker(true);
		}
	}
	
	//driveBigLeft MODE
	private void driveBigLeft() {
		dir.x = (float) Math.sin(0.5 * Math.PI / 8.64 * s);
		dir.y = (float) Math.cos(0.5 * Math.PI / 8.64 * s);
		dir.normalise();
		dir.scale(speed);
		s+= dir.length() * DisplayManager.getFrameTimeSeconds();
		dir = Maths.rotateVectorBy90Degrees(dir,  (int)(rot90 / 90));
		//if fps is too low errors will occure and will be reset
		if(s > 9.0f) {
			s = 0;
			System.out.println("I HANGED UP!!");
			pos.x = currentMarker.getWorldPositionX();
			pos.z = currentMarker.getWorldPositionY();
			updateMarker(true);
		}
	}
	
	//if car reaches the next marker
	private float roundabout = 0.4f;
	private boolean reachedNextMarker() {
		if(pos.x >= nextMarker.getWorldPositionX() - roundabout && pos.x <= nextMarker.getWorldPositionX() + roundabout) {
			if(pos.z >= nextMarker.getWorldPositionY() - roundabout && pos.z <= nextMarker.getWorldPositionY() + roundabout) {
				return true;
			}
		}
		return false;
	}
	
	//returns the nearest Marker of the next street
	private int getShortestMarker(Street street) {
		int shortestindex = -1;
		float shortestlength = 100;
		Vector2f vec = new Vector2f();
		for(int i = 0; i < street.getPathMarkers().size(); i++) {
			vec.x = pos.x - street.getPathMarkers().get(i).getWorldPositionX();
			vec.y = pos.z - street.getPathMarkers().get(i).getWorldPositionY();
			if(vec.length() < shortestlength) {
				shortestlength = vec.length();
				shortestindex = i;
			}
		}
		return shortestindex;
	}
	
	//returns the nearest street to cars position
	private Street getClosestStreet() {
		float length = 100;
		int streetnum = -1; //0 == top -- 1== right -- 2 == bottom -- 3 == left
		Vector2f vec = new Vector2f();
		Street[] street = new Street[4];
		if(streetManager.hasTop(getTileX(), getTileY())) {
			street[0] = streetManager.getTopStreet(getTileX(), getTileY());
		}
		if(streetManager.hasRight(getTileX(), getTileY())) {
			street[1] = streetManager.getRightStreet(getTileX(), getTileY());
		}
		if(streetManager.hasBottom(getTileX(), getTileY())) {
			street[2] = streetManager.getBottomStreet(getTileX(), getTileY());
		}
		if(streetManager.hasLeft(getTileX(), getTileY())) {
			street[3] = streetManager.getLeftStreet(getTileX(), getTileY());
		}
		
		for(int i = 0; i < 4; i++) {
			if(street[i] != null) {
				vec.x = street[i].getWorldPositionX() - pos.x;
				vec.y = street[i].getWorldPositionY() - pos.z;
				if(vec.length() < length) {
					length = vec.length();
					streetnum = i;
				}
			}
		}
		return street[streetnum];
	}
	
	private void initRot90() {
		rot90 = 0;
		switch(MODE) {
			case 0:
				//straight
				if(nextMarker.getWorldPositionX() == currentMarker.getWorldPositionX()) {
					if(nextMarker.getWorldPositionY() < currentMarker.getWorldPositionY()) {
						rot90 = 180;
					}
				}else{
					if(nextMarker.getWorldPositionX() < currentMarker.getWorldPositionX()) {
						rot90 = 270;
					}else {
						rot90 = 90;
					}
				}
				break;
			case 1:

				//left
				if(nextMarker.getWorldPositionX() < currentMarker.getWorldPositionX()) {
					if(nextMarker.getWorldPositionY() < currentMarker.getWorldPositionY()) {
						rot90 = 180;
					}else {
						rot90 = 270;
					}
				}else {
					if(nextMarker.getWorldPositionY() < currentMarker.getWorldPositionY()) {
						rot90 = 90;
					}else {
						rot90 = 0;
					}
				}
				break;
			case 2:
				//right
				if(nextMarker.getWorldPositionX() < currentMarker.getWorldPositionX()) {
					if(nextMarker.getWorldPositionY() < currentMarker.getWorldPositionY()) {
						rot90 = 270;
					}else {
						rot90 = 0;
					}
				}else {
					if(nextMarker.getWorldPositionY() < currentMarker.getWorldPositionY()) {
						rot90 = 180;
					}else {
						rot90 = 90;
					}
				}
				
				break;
			case 3:
				//small left
				if(nextMarker.getWorldPositionX() < currentMarker.getWorldPositionX()) {
					if(nextMarker.getWorldPositionY() < currentMarker.getWorldPositionY()) {
						rot90 = 180;
					}else {
						rot90 = 270;
					}
				}else {
					if(nextMarker.getWorldPositionY() < currentMarker.getWorldPositionY()) {
						rot90 = 90;
					}else {
						rot90 = 0;
					}
				}
				rot90 += 90;
				if(rot90 >= 360) {
					rot90 = 0;
				}
				break;
			case 4:
				//big left
				if(nextMarker.getWorldPositionX() < currentMarker.getWorldPositionX()) {
					if(nextMarker.getWorldPositionY() < currentMarker.getWorldPositionY()) {
						rot90 = 180;
					}else {
						rot90 = 270;
					}
				}else {
					if(nextMarker.getWorldPositionY() < currentMarker.getWorldPositionY()) {
						rot90 = 90;
					}else {
						rot90 = 0;
					}
				}
				break;
		}
		
	}
	
	private Mesh getRandomCar() {
		int i = new Random().nextInt(10);     
		switch(i) {
			case 0:
				return MeshContainer.car_1_black;
			case 1:
				return MeshContainer.car_1_gray;
			case 2:
				return MeshContainer.car_1_green;
			case 3:
				return MeshContainer.car_1_orange;
			case 4:
				return MeshContainer.car_1_white;
			case 5:
				return MeshContainer.car_2_brown;
			case 6:
				return MeshContainer.car_2_green;
			case 7:
				return MeshContainer.car_2_pink;
			case 8:
				return MeshContainer.car_2_red;
			case 9:
				return MeshContainer.truck_1_white;
		}
		return null;
	}
	
	private float length = 100;
	private double timer = 0;
	private void adjustSpeedToFrontCar() {
		timer += DisplayManager.getFrameTimeSeconds();
		if(timer >= 0.15f) {
			timer = 0;
		length = getLengthToFrontCar();
		if(length >= 0) {
			//System.out.println(length);
			if(length < 4.0f) {
				speed = 0.01f;
			}else if(length < 4.5f) {
				speed = 2;
			}else if(length < 5.5f) {
				speed = initspeed * (length / 5.5f);
			}
		}else {
			speed = initspeed;
		}
		}
	}
	
	//returns the nearest car in front of other car
	private Vector2f vec = new Vector2f();
	private Vector2f otherdir = new Vector2f();
	private List<Float> lengthlist = new ArrayList<>();
	private List<Car> cars = new ArrayList<>();
	private float shortestlength;
	private int shortestindex;
	private float dirangle = 0;
	private float getLengthToFrontCar() {
		lengthlist.clear();
		cars.clear();
		shortestindex = -1;
		shortestlength = 100;
		cars.addAll(currentStreet.getCars());
		if(streetManager.hasTop(getTileX(), getTileY())) {
			cars.addAll(streetManager.getTopStreet(getTileX(), getTileY()).getCars());
		}
		if(streetManager.hasRight(getTileX(), getTileY())) {
			cars.addAll(streetManager.getRightStreet(getTileX(), getTileY()).getCars());
		}
		if(streetManager.hasBottom(getTileX(), getTileY())) {
			cars.addAll(streetManager.getBottomStreet(getTileX(), getTileY()).getCars());
		}
		if(streetManager.hasLeft(getTileX(), getTileY())) {
			cars.addAll(streetManager.getLeftStreet(getTileX(), getTileY()).getCars());
		}
		
		//make vector to all cars
		for(int i = 0; i < cars.size(); i++) {
			vec.x = cars.get(i).getWorldPositionX() - pos.x;
			vec.y = cars.get(i).getWorldPositionY() - pos.z;
			if(vec.length() != 0) {
				//angle between car position and other car position
				float angle = (float) Math.toDegrees(Math.acos((dir.x * vec.x + dir.y * vec.y)/(dir.length() * vec.length())));
				//angle between car dir and other car dir
				otherdir = cars.get(i).getDirection();
				dirangle = (float) Math.abs(Math.toDegrees(Math.acos((dir.x * otherdir.x + dir.y * otherdir.y)/
						(dir.length() * otherdir.length()))));
				//check whether car is in front
				if(angle < 40 && vec.length() > 0 && dirangle < 90) {
					//adds car in front to list
					lengthlist.add(vec.length());
				}
			}
		}
		
		if(lengthlist.isEmpty()) {
			//no car in front
			return -1;
		}else {
			return Collections.min(lengthlist);
		}
	}
	
	private void interpolateSpeedTo(float toSpeed) {
		if(toSpeed == 0) {
			toSpeed = 0.01f;
		}
		if(toSpeed > speed) {
			while(speed <= toSpeed) {
				interpolate(0.0001f, true);
			}
			speed = toSpeed;
		}else {
			while(speed >= toSpeed) {
				interpolate(0.0001f, false);
			}
			speed = toSpeed;
		}
		
		
	}
	
	private void interpolate(float delta, boolean up) {
		if(up) {
			speed += delta;
		}else {
			speed -= delta;
		}
	}
	
	
	public float getWorldPositionX() {
		return pos.x;
	}
	
	public float getWorldPositionY() {
		return pos.z;
	}
	
	
	//returns current xtile of cars position
	private int getTileX() {
		return (int) ((pos.x + TileManager.wsize / 2) / TileManager.tsize);
	}
	
	//returns current ytile of cars position
	private int getTileY() {
		return (int)((pos.z + TileManager.wsize / 2) / TileManager.tsize);
	}
	
	public Vector2f getDirection() {
		return dir;
	}
	
	

}
