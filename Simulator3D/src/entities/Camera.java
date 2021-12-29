package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import animations.LinearAnimation;
import menu.MenuUpdater;

/**
 * camera of the player
 * @author Oriventi
 *
 */
public class Camera {

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;

	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 20;
	private float yaw;
	private float roll;

	public Player player;
	
	private LinearAnimation swipeAnimation;
	
	/**
	 * defines player var
	 * @param player who will use the camera
	 */
	public Camera(Player player) {
		this.player = player;
	}

	/**
	 * updates the cameras position and rotation by players input
	 */
	public void move() {
		calculatePitch();
		calculateZoom();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		if(!MenuUpdater.isMenuActivated()) {
			player.move();
		}
		if(swipeAnimation != null) {
			doSwipeMovement();
		}
	}

	/**
	 * returns 3 dimensional vector of current position
	 * @return
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * returns current pitch
	 * @return pitch
	 */
	public float getPitch() {
		return pitch;
	}
	
	/**
	 * returns current yaw
	 * @return yaw
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * returns current roll
	 * @return roll
	 */
	public float getRoll() {
		return roll;
	}

	/**
	 * calculates cameras position by players rotation, so its always behind the player
	 * @param cameras horizontal distance from player
	 * @param cameras vertical distance from player
	 */
	private void calculateCameraPosition(float horizDistance, float verticalDistance) {
		float theta = player.getRotY();
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticalDistance;
	}

	/**
	 * calculates camera's horizontal distance from player
	 * @return verticalDistance
	 */
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	/**
	 * calculates camera's vertical distance from player
	 * @return horizontalDistance
	 */
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	/**
	 * calculates cameras overall distance to the player
	 */
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		if(distanceFromPlayer < 35) {
			distanceFromPlayer = 35;
		}else if(distanceFromPlayer > 98) {
			distanceFromPlayer = 98;
		}
	}
	
	/**
	 * calculates pitch of camera
	 */
	private void calculatePitch() {
		if(Mouse.isButtonDown(2)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			increasePitch(pitchChange);
			float angleChange = Mouse.getDX() * 0.3f;
			player.increaseRotation(0, -angleChange, 0);
		}
	}
	
	public void increasePitch(float pitchChange) {
		pitch += pitchChange;
		if(pitch < 20) {
			pitch = 20;
		}else if(pitch > 70) {
			pitch = 70;
		}
	}
	
	/**
	 * swipes cameras pitch until it reaches targetPitch
	 * @param pitch value which should be swiped to
	 * @param speed of swiping
	 */
	public void swipePitchTo(int targetPitch, int speed) {
		float runningTime = (this.pitch - targetPitch) / speed;
		if(this.pitch < targetPitch) {
			swipeAnimation = new LinearAnimation(runningTime, speed, targetPitch, 0);
		}else {
			swipeAnimation = new LinearAnimation(runningTime, -speed, targetPitch, 0);
		}
		swipeAnimation.startAnimation();
	}
	
	/**
	 * executes swipeMovement from animation 
	 */
	private void doSwipeMovement() {
		pitch = swipeAnimation.getCurrentValue(pitch);
		if(swipeAnimation.isFinished()) {
			swipeAnimation.destroy();
			swipeAnimation = null;
		}
	}
	
	/**
	 * sets pitch value
	 * @param pitch
	 */
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}	
	
}
