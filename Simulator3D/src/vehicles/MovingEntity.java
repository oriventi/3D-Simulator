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
import toolbox.EnumHolder.MovingEntityID;
import traffic.MovingAction;
import traffic.PathMarker;
import traffic.StreetManager;

/**
 * class which contains the functions for each moving entities, as car, or pedestrian
 * it calculates the way to move to the nextMarker
 * @author Oriventi
 *
 */
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
	protected MovingEntityID id;
	protected boolean isVehicle;
	
	protected Animation startingAnimation;
	protected Animation movingAnimation;
	protected Animation stoppingAnimation;
	
	protected double delta;

	/**
	 * initializes the entity and its vars
	 * @param pathMarker, where the entity should start moving
	 * @param isVehicle, whether instance is an vehicle
	 */
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
		
		startingAnimation = setStartingAnimation();
		movingAnimation = setMovingAnimation();
		stoppingAnimation = setStoppingAnimation();
		
		id = setID();
		
		updateNextMarkerAndModeAndDirection();
	}
	
	/**
	 * returns the ID of the MovingEntity
	 * @return MovinEntityID
	 */
	protected abstract MovingEntityID setID();
	
	/**
	 * returns the maximum speed which the vehicle can move
	 * @return maximum Speed
	 */
	protected abstract float setMaxSpeed();
	
	/**
	 * returns the Mesh, of the movingEntity
	 * @return mesh of movingEntity
	 */
	protected abstract Mesh setMesh();
	
	/**
	 * returns the additional rotation which the .obj might have
	 * @return additional rotation, to correct the .objs rotation
	 */
	protected abstract int setMeshRotation();
	
	/**
	 * renders additional things which the object might have
	 * @param renderer
	 */
	protected abstract void renderContent(MasterRenderer renderer);
	
	/**
	 * returns the starting animation
	 * @return starting animation
	 */
	protected abstract Animation setStartingAnimation();
	
	/**
	 * returns the driving animation
	 * @return driving animation
	 */
	protected abstract Animation setMovingAnimation();
	
	/**
	 * returns the stopping Animation
	 * @return stopping Animation
	 */
	protected abstract Animation setStoppingAnimation();
	
	/**
	 * is called when entity gets destroyed
	 */
	public void destroy() {
		EntityShadowList.removeEntity(entity);
	}
	
	/**
	 * renders the entity and its content
	 * @param renderer
	 */
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
		renderContent(renderer);
	}
	
	/**
	 * updates the entities position, rotation and drivingMode
	 * @param current frameTime
	 */
	public void update(double delta) {
		this.delta = delta;
		driveByMode();
		if(!nextMarker.isStop()) {
			adjustSpeedToFrontMovingEntity();			
		}
		applyPositionAndRotation();
	}
	
	/**
	 * decides which drivingMode should be applied to the entity
	 * and checks whether the vehicle reaches the next marker in 
	 * order to update the drivingMode
	 */
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
	
	/**
	 * applies the driven way to the entities position and rotates it by the droven rotation
	 */
	private void applyPositionAndRotation() {
		entity.setRotY((float) -(Math.atan2(dir.y, dir.x) * 180 / Math.PI) + setMeshRotation());
		if(dir.length() > 0) {
			dir.normalise();
			dir.scale((float) (currentSpeed * delta));
		}
		position.x += dir.x;
		position.z += dir.y;
		entity.setPosition(position);
	}
	
	/**
	 * if street has more options to move (e.g. left and right), it picks a random decision
	 * @return picked decision
	 */
	private MovingAction chooseRandomAction() {
		int size = currentMarker.getPossibleActions().length;
		return currentMarker.getPossibleActions()[Maths.getRandomBetween(0, size - 1)];
	}
	
	/**
	 * updates all important variables if entity reaches a new pathMarker 
	 */
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
	
	/**
	 * returns true if entity reaches a new marker
	 * @return reachedNextMarker
	 */
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
	
	/**
	 * anti Bug Timer checks whether the vehicle moved too long in the current drivingMode and resets the position 
	 */
	private float antiBugTimer = 0;
	private void moveStraight() {
		if(antiBugTimer == 0) {
			dir.x = nextMarker.getWorldPositionX() - currentMarker.getWorldPositionX();
			dir.y = nextMarker.getWorldPositionY() - currentMarker.getWorldPositionY();
			dir.normalise();
			dir.scale(currentSpeed);
		}
		
		if(currentSpeed > 0) {
			antiBugTimer += delta * (currentSpeed / MAX_SPEED);
		}		
		if(antiBugTimer > (TileManager.tsize / currentSpeed + 1.f)) {
			position = currentMarker.getPosition3f(isVehicle);
			antiBugTimer = 0;
		}
	}
	
	/**
	 * calculates the direction Vector for the current Frame to let the entity move Right
	 */
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
		drovenCurveDistance+= currentSpeed * delta;
		
		if(drovenCurveDistance > 4.3f) {
			drovenCurveDistance = 0;
			position = currentMarker.getPosition3f(isVehicle);
		}
	}
	
	/**
	 * calculates the direction Vector for the current Frame to let the entity move left
	 */
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
		drovenCurveDistance += currentSpeed * delta;
		
		if(drovenCurveDistance > (0.5f * Math.PI * radiusOfCurve + 0.4f)) {
			drovenCurveDistance = 0;
			position = currentMarker.getPosition3f(isVehicle);
		}
	}
	
	/**
	 * adjusts the speed to the movingEntity in front, so it doesnt collide
	 */
	private float length = 100;
	private double timer = 0;
	private void adjustSpeedToFrontMovingEntity() {
		timer += delta;
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
	
	/**
	 * calculates the shortest pathMarker to the entity of street's pathMarkers
	 * @param street
	 * @return index of pathMarker
	 */
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
	
	/**
	 * calculates the radius of the current Curve to move
	 * @return radius
	 */
	private float getRadiusOfCurve() {
		return Math.abs(nextMarker.getWorldPositionX() - currentMarker.getWorldPositionX());
	}
	
	/**
	 * calculates the length to the nearest entity in front
	 * @return length to the nearest entity in front
	 */
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
	
	/**
	 * returns the street to move next
	 * @return street to move next
	 */
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
	
	/**
	 * returns the current XTile of the entity
	 * @return xTile
	 */
	private int getTileX() {
		return (int) ((position.x + TileManager.wsize / 2) / TileManager.tsize);
	}
	
	/**
	 * returns the current YTile of the entity
	 * @return yTile
	 */
	private int getTileY() {
		return (int)((position.z + TileManager.wsize / 2) / TileManager.tsize);
	}
	
	/**
	 * returns the current x position of the entity on streetsystem
	 * @return xpos
	 */
	public float getWorldPositionX() {
		return position.x;
	}
	
	/**
	 * returns the current y position of entity on streetsystem
	 * @return ypos of streetsystem, would be zpos in world position
	 */
	public float getWorldPositionY() {
		return position.z;
	}
	
	/**
	 * returns the entity itself
	 * @return entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * returns the current Direction
	 * @return current direction
	 */
	public Direction getDirection() {
		return currentDirection;
	}
	
	/**
	 * returns the current driving mode
	 * @return current driving mode (e.g. straight, left, ...)
	 */
	public DrivingMode getMode() {
		return currentMode;
	}
	
	/**
	 * returns whether instance of movingEntity is a vehicle or not
	 * @return is vehicle
	 */
	public boolean isVehicle() {
		return isVehicle;
	}
	
	/**
	 * returns id of instance of moving entity
	 * @return id
	 */
	public MovingEntityID getId() {
		return id;
	}
}