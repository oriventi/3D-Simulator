package vehicles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import World.TileManager;
import entities.Entity;
import models.Mesh;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import streets.Street;
import toolbox.Maths;
import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;
import toolbox.EnumHolder.ID;
import traffic.DrivingAction;
import traffic.PathMarker;
import traffic.StreetManager;

public abstract class Vehicle {
	
	protected int xtile, ytile;
	protected float currentSpeed;
	protected float MAX_SPEED;
	
	protected Vector2f dir;
	protected Vector2f original_dir;
	protected Vector3f position;
	
	protected Street currentStreet;
	protected PathMarker currentMarker, nextMarker;
	protected Entity entity;
	
	protected DrivingMode currentMode;
	protected Direction currentDirection;
	protected ID id;

	public Vehicle(PathMarker startMarker) {
		currentMarker = startMarker;
		position = startMarker.getPosition3f();
		dir = new Vector2f();
		original_dir = new Vector2f();
		
		xtile = getTileX();
		ytile = getTileY();
		MAX_SPEED = setMaxSpeed();
		currentSpeed = MAX_SPEED;
		
		currentStreet = StreetManager.getStreetSystem()[xtile][ytile];
		currentStreet.addCar(this);
		entity = new Entity(setMesh(), position, 0, 0, 0, 0.5f);
		
		id = setID();
		
		updateNextMarkerAndModeAndDirection();
	}
	
	protected abstract ID setID();
	
	protected abstract float setMaxSpeed();
	
	protected abstract Mesh setMesh();
	
	protected abstract void renderContent(MasterRenderer renderer);
	
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
		renderContent(renderer);
	}
	
	public void update() {
		driveByMode();
		if(!nextMarker.isStop()) {
			adjustSpeedToFrontCar();			
		}
		applyPositionAndRotation();
	}
	
	private void driveByMode() {
		if(reachedNextMarker()) {
			position = nextMarker.getPosition3f();
			if(nextMarker.isStop()) {
				currentSpeed = 0.01f;
			}else {
				currentMarker = nextMarker;
				updateNextMarkerAndModeAndDirection();
			}
			s = 0;
			antiBugTimer = 0;
		}else {
			switch(currentMode) {
				case STRAIGHT:
					driveStraight();
					break;
				case RIGHT:
					driveRight(2.5f);
					break;
				case LEFT:
					driveLeft(2.5f);
					break;
				case SMALL_LEFT:
					driveLeft(1.5f);
					break;
				case BIG_LEFT:
					driveLeft(5.5f);
					break;
				default:
					break;
			}
		}
	}
	
	private void applyPositionAndRotation() {
		entity.setRotY((float) -(Math.atan2(dir.y, dir.x) * 180 / Math.PI) + 90);
		if(dir.length() > 0) {
			dir.normalise();
			dir.scale(currentSpeed * DisplayManager.getFrameTimeSeconds());
		}
		position.x += dir.x;
		position.z += dir.y;
		entity.setPosition(position);
	}
	
	
	private DrivingAction chooseRandomAction() {
		int size = currentMarker.getPossibleActions().length;
		return currentMarker.getPossibleActions()[Maths.getRandomBetween(0, size - 1)];
	}
	
	
	private void updateNextMarkerAndModeAndDirection() {
		DrivingAction action = chooseRandomAction();
		currentDirection = action.getDirection();
		if(action.getMode() == DrivingMode.INVALID) {
			currentMode = DrivingMode.STRAIGHT;
			Street followingStreet = getFollowingStreet();
			nextMarker = followingStreet.getPathMarkers().get(getShortestPathMarker(followingStreet));
			xtile = followingStreet.getTileX();
			ytile = followingStreet.getTileY();
			currentStreet.removeCar(this);
			currentStreet = followingStreet;
			currentStreet.addCar(this);
		}else {
			currentMode = action.getMode();
			nextMarker = currentStreet.getPathMarkers().get(action.getDestinationIndex());
		}
	}
	
	private boolean reachedNextMarker() {
		float roundabout = 0.1f;
		if(DisplayManager.getFPS() < 80) {
			roundabout = 0.35f;
		}else if(DisplayManager.getFPS() < 100) {
			roundabout = 0.25f;
		}else if(DisplayManager.getFPS() < 150) {
			roundabout = 0.2f;
		}else if(DisplayManager.getFPS() >= 150) {
			roundabout = 0.1f;
		}
		return Maths.roundabout(position, nextMarker.getPosition2f(), roundabout);
	}
	
	private Street getFollowingStreet() {
		switch(currentDirection) {
			case UP:
				return StreetManager.getTopStreet(xtile, ytile);
			case RIGHT:
				return StreetManager.getRightStreet(xtile, ytile);
			case DOWN:
				return StreetManager.getBottomStreet(xtile, ytile);
			case LEFT:
				return StreetManager.getLeftStreet(xtile, ytile);
			default:
				return null;
		}
	}
	
	//TODO make efficient
	private int getShortestPathMarker(Street street) {
		float shortestLength = 100;
		int shortestIndex = -1;
		float length;
		Vector2f vec = new Vector2f();
		for(int i = 0; i < street.getPathMarkers().size(); i++) {
			vec.x = street.getPathMarkers().get(i).getWorldPositionX() - position.x;
			vec.y = street.getPathMarkers().get(i).getWorldPositionY() - position.z;
			length = vec.length();
			if(shortestLength > length) {
				shortestLength = length;
				shortestIndex = i;
			}
		}
		return shortestIndex;
	}
	
	private float antiBugTimer = 0;
	private void driveStraight() {
		if(antiBugTimer == 0) {
			dir.x = nextMarker.getWorldPositionX() - currentMarker.getWorldPositionX();
			dir.y = nextMarker.getWorldPositionY() - currentMarker.getWorldPositionY();
			dir.normalise();
			dir.scale(currentSpeed);
		}
		
		if(currentSpeed > 0) {
			antiBugTimer += DisplayManager.getFrameTimeSeconds() * (currentSpeed / MAX_SPEED);
		}		
		if(antiBugTimer > (TileManager.tsize / currentSpeed + 1.f)) {
			position = currentMarker.getPosition3f();
			antiBugTimer = 0;
		}
	}
	
	private float s = 0;
	private void driveRight(float r) {
		switch(currentDirection) {
		case UP:
			dir.x = -(float) Math.cos( s/r);
			dir.y = -(float) Math.sin( s/r);
			break;
		case RIGHT:
			dir.x = (float) Math.sin( s/r);
			dir.y = -(float) Math.cos( s/r);
			break;
		case DOWN:
			dir.x = (float) Math.cos( s/r);
			dir.y = (float) Math.sin( s/r);
			break;
		case LEFT:
			dir.x = -(float) Math.sin( s/r);
			dir.y = (float) Math.cos( s/r);
			break;
		default:
			break;
		}
		s+= currentSpeed * DisplayManager.getFrameTimeSeconds();
		
		if(s > 4.3f) {
			s = 0;
			position = currentMarker.getPosition3f();
		}
	}
	
	private void driveLeft(float r) {
		switch(currentDirection) {
		case UP:
			dir.x = (float) Math.cos( s/r);
			dir.y = -(float) Math.sin( s/r);
			break;
		case RIGHT:
			dir.x = (float) Math.sin( s/r);
			dir.y = (float) Math.cos( s/r);
			break;
		case LEFT:
			dir.x = -(float) Math.sin( s/r);
			dir.y = -(float) Math.cos( s/r);
			break;
		case DOWN:
			dir.x = -(float) Math.cos( s/r);
			dir.y = (float) Math.sin( s/r);
			break;
		default:
			break;
		}
		s += currentSpeed * DisplayManager.getFrameTimeSeconds();
		
		if(s > (0.5f * Math.PI * r + 0.4f)) {
			s = 0;
			position = currentMarker.getPosition3f();
		}
	}
	
	private float length = 100;
	private double timer = 0;
	private void adjustSpeedToFrontCar() {
		timer += DisplayManager.getFrameTimeSeconds();
		if(timer >= 0.15f) {
			timer = 0;
			length = getLengthToFrontCar();
			if(length > 0 && length < 5) {
				if(length < 4.0f) {
					currentSpeed = 0.01f;
				}else if(length < 4.5f) {
					currentSpeed = 2;
				}else if(length < 5.5f) {
					currentSpeed = MAX_SPEED * (length / 5.5f);
				}
			}else {	
				currentSpeed = MAX_SPEED;
			}
		}
	}
	
	private float getLengthToFrontCar() {
		List<Vehicle> vehicles = new ArrayList<>();
		vehicles.clear();
		Vector2f vec = new Vector2f();
		float shortestLength = 100;
		vehicles.addAll(currentStreet.getCars());
		if(getFollowingStreet() != null) {
			vehicles.addAll(getFollowingStreet().getCars());
		}
	
		for(int i = 0; i < vehicles.size(); i++) {
			vec.x = vehicles.get(i).getWorldPositionX() - position.x;
			vec.y = vehicles.get(i).getWorldPositionY() - position.z;
			float length = vec.length();
			float angle = (float) Math.toDegrees(Math.acos((dir.x * vec.x + dir.y * vec.y)/(dir.length() * length)));
			
			if(angle < 45 && length < shortestLength && currentDirection == vehicles.get(i).getDirection()) {
				if(currentMode != DrivingMode.BIG_LEFT) {
					shortestLength = length;
				}
			}	
		}
		return shortestLength;
	}

	//GETTERS AND SETTERS
	
	private int getTileX() {
		return (int) ((position.x + TileManager.wsize / 2) / TileManager.tsize);
	}
	
	private int getTileY() {
		return (int)((position.z + TileManager.wsize / 2) / TileManager.tsize);
	}
	
	public float getWorldPositionX() {
		return position.x;
	}
	
	public float getWorldPositionY() {
		return position.z;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public Direction getDirection() {
		return currentDirection;
	}
	
	public DrivingMode getMode() {
		return currentMode;
	}
	
	public ID getId() {
		return id;
	}
}