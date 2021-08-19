package vehicles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import World.TileManager;
import animations.Animation;
import entities.Entity;
import entities.EntityShadowList;
import models.Mesh;
import people.Pedestrian;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import streets.Street;
import toolbox.Maths;
import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;
import toolbox.EnumHolder.ID;
import traffic.MovingAction;
import traffic.PathMarker;
import traffic.StreetManager;

public abstract class MovingEntity {
	
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
	protected boolean isVehicle;

	public MovingEntity(PathMarker startMarker, boolean isVehicle) {
		currentMarker = startMarker;
		position = startMarker.getPosition3f(isVehicle);
		dir = new Vector2f();
		original_dir = new Vector2f();
		
		this.isVehicle = isVehicle;
		xtile = getTileX();
		ytile = getTileY();
		MAX_SPEED = setMaxSpeed();
		currentSpeed = MAX_SPEED;
		
		currentStreet = StreetManager.getStreetSystem()[xtile][ytile];
		currentStreet.addMovingEntity(this);
		entity = new Entity(setMesh(), position, 0, 0, 0, 0.7f);
		EntityShadowList.addEntity(entity);
		
		id = setID();
		
		updateNextMarkerAndModeAndDirection();
	}
	
	protected abstract ID setID();
	
	protected abstract float setMaxSpeed();
	
	protected abstract Mesh setMesh();
	
	protected abstract int setMeshRotation();
	
	protected abstract void renderContent(MasterRenderer renderer);
	
	protected abstract Animation setStartingAnimation();
	
	protected abstract Animation setMovingAnimation();
	
	protected abstract Animation setStoppingAnimation();
	
	public void destroy() {
		EntityShadowList.removeEntity(entity);
	}
	
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
		renderContent(renderer);
	}
	
	public void update() {
		driveByMode();
		if(!nextMarker.isStop()) {
			adjustSpeedToFrontMovingEntity();			
		}
		applyPositionAndRotation();
	}
	
	private void driveByMode() {
		if(reachedNextMarker()) {
			position = nextMarker.getPosition3f(isVehicle);
			if(nextMarker.isStop()) {
				currentSpeed = 0.01f;
			}else {
				currentMarker = nextMarker;
				updateNextMarkerAndModeAndDirection();
			}
			drovenCurveDistance = 0;
			antiBugTimer = 0;
		}else {
			switch(currentMode) {
				case STRAIGHT:
					moveStraight();
					break;
				case RIGHT:
					moveRight();
					break;
				case LEFT:
					moveLeft();
					break;
				default:
					break;
			}
		}
	}
	
	private void applyPositionAndRotation() {
		entity.setRotY((float) -(Math.atan2(dir.y, dir.x) * 180 / Math.PI) + setMeshRotation());
		if(dir.length() > 0) {
			dir.normalise();
			dir.scale(currentSpeed * DisplayManager.getFrameTimeSeconds());
		}
		position.x += dir.x;
		position.z += dir.y;
		entity.setPosition(position);
	}
	
	private MovingAction chooseRandomAction() {
		int size = currentMarker.getPossibleActions().length;
		return currentMarker.getPossibleActions()[Maths.getRandomBetween(0, size - 1)];
	}
	
	
	private void updateNextMarkerAndModeAndDirection() {
		MovingAction action = chooseRandomAction();
		currentDirection = action.getDirection();
		if(action.getMode() == DrivingMode.INVALID) {
			currentMode = DrivingMode.STRAIGHT;
			Street followingStreet = getFollowingStreet();
			nextMarker = getShortestPathMarker(followingStreet);
			xtile = followingStreet.getTileX();
			ytile = followingStreet.getTileY();
			currentStreet.removeMovingEntity(this);
			currentStreet = followingStreet;
			currentStreet.addMovingEntity(this);
		}else {
			currentMode = action.getMode();
			nextMarker = currentStreet.getPathMarkers(isVehicle).get(action.getDestinationIndex());
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
	
	private float antiBugTimer = 0;
	private void moveStraight() {
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
			position = currentMarker.getPosition3f(isVehicle);
			antiBugTimer = 0;
		}
	}
	
	private float drovenCurveDistance = 0;
	private float radiusOfCurve = 0;
	private void moveRight() {
		if(drovenCurveDistance == 0) {
			radiusOfCurve = getRadiusOfCurve();

		}
		switch(currentDirection) {
		case UP:
			dir.x = -(float) Math.cos( drovenCurveDistance/radiusOfCurve);
			dir.y = -(float) Math.sin( drovenCurveDistance/radiusOfCurve);
			break;
		case RIGHT:
			dir.x = (float) Math.sin( drovenCurveDistance/radiusOfCurve);
			dir.y = -(float) Math.cos( drovenCurveDistance/radiusOfCurve);
			break;
		case DOWN:
			dir.x = (float) Math.cos( drovenCurveDistance/radiusOfCurve);
			dir.y = (float) Math.sin( drovenCurveDistance/radiusOfCurve);
			break;
		case LEFT:
			dir.x = -(float) Math.sin( drovenCurveDistance/radiusOfCurve);
			dir.y = (float) Math.cos( drovenCurveDistance/radiusOfCurve);
			break;
		default:
			break;
		}
		drovenCurveDistance+= currentSpeed * DisplayManager.getFrameTimeSeconds();
		
		if(drovenCurveDistance > 4.3f) {
			drovenCurveDistance = 0;
			position = currentMarker.getPosition3f(isVehicle);
		}
	}
	
	private void moveLeft() {
		if(drovenCurveDistance == 0) {
			radiusOfCurve = getRadiusOfCurve();
		}
		switch(currentDirection) {
		case UP:
			dir.x = (float) Math.cos( drovenCurveDistance/radiusOfCurve);
			dir.y = -(float) Math.sin( drovenCurveDistance/radiusOfCurve);
			break;
		case RIGHT:
			dir.x = (float) Math.sin( drovenCurveDistance/radiusOfCurve);
			dir.y = (float) Math.cos( drovenCurveDistance/radiusOfCurve);
			break;
		case LEFT:
			dir.x = -(float) Math.sin( drovenCurveDistance/radiusOfCurve);
			dir.y = -(float) Math.cos( drovenCurveDistance/radiusOfCurve);
			break;
		case DOWN:
			dir.x = -(float) Math.cos( drovenCurveDistance/radiusOfCurve);
			dir.y = (float) Math.sin( drovenCurveDistance/radiusOfCurve);
			break;
		default:
			break;
		}
		drovenCurveDistance += currentSpeed * DisplayManager.getFrameTimeSeconds();
		
		if(drovenCurveDistance > (0.5f * Math.PI * radiusOfCurve + 0.4f)) {
			drovenCurveDistance = 0;
			position = currentMarker.getPosition3f(isVehicle);
		}
	}
	
	private float length = 100;
	private double timer = 0;
	private void adjustSpeedToFrontMovingEntity() {
		timer += DisplayManager.getFrameTimeSeconds();
		if(timer >= 0.15f) {
			timer = 0;
			length = getLengthToFrontMovingEntity();
			if(length > 0 && length < 5) {
				if(length < 5.0f) {
					currentSpeed = 0.01f;
				}else if(length < 5.5f) {
					currentSpeed = 2;
				}else if(length < 6.5f) {
					currentSpeed = MAX_SPEED * (length / 5.5f);
				}
			}else {	
				currentSpeed = MAX_SPEED;
			}
		}
	}

	//GETTERS AND SETTERS
	
	
	private PathMarker getShortestPathMarker(Street street) {
		//TODO make efficient
		float shortestLength = 100;
		int shortestIndex = -1;
		float length;
		Vector2f vec = new Vector2f();
		for(int i = 0; i < street.getPathMarkers(isVehicle).size(); i++) {
			vec.x = street.getPathMarkers(isVehicle).get(i).getWorldPositionX() - position.x;
			vec.y = street.getPathMarkers(isVehicle).get(i).getWorldPositionY() - position.z;
			length = vec.length();
			if(shortestLength > length) {
				shortestLength = length;
				shortestIndex = i;
			}
		}
		return street.getPathMarkers(isVehicle).get(shortestIndex);
	}
	
	private float getRadiusOfCurve() {
		return Math.abs(nextMarker.getWorldPositionX() - currentMarker.getWorldPositionX());
	}
	
	private float getLengthToFrontMovingEntity() {
		List<MovingEntity> movingEntitiesNearby = new ArrayList<>();
		movingEntitiesNearby.clear();
		Vector2f vec = new Vector2f();
		float shortestLength = 100;
		movingEntitiesNearby.addAll(currentStreet.getCars());
		if(getFollowingStreet() != null) {
			movingEntitiesNearby.addAll(getFollowingStreet().getCars());
		}
	
		for(int i = 0; i < movingEntitiesNearby.size(); i++) {
			vec.x = movingEntitiesNearby.get(i).getWorldPositionX() - position.x;
			vec.y = movingEntitiesNearby.get(i).getWorldPositionY() - position.z;
			float length = vec.length();
			float angle = (float) Math.toDegrees(Math.acos((dir.x * vec.x + dir.y * vec.y)/(dir.length() * length)));
			
			if(angle < 45 && length < shortestLength && currentDirection == movingEntitiesNearby.get(i).getDirection()
					&& movingEntitiesNearby.get(i).isVehicle == isVehicle) {
				if(currentMode != DrivingMode.LEFT && radiusOfCurve <= 5) {
					shortestLength = length;
				}
			}	
		}
		return shortestLength;
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
	
	public boolean isVehicle() {
		return isVehicle;
	}
	
	public ID getId() {
		return id;
	}
}