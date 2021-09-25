package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;


public class Camera {

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;

	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 20;
	private float yaw;
	private float roll;

	public Player player;
	
	public Camera(Player player) {
		this.player = player;
	}

	public void move() {
		calculateZoom();
		calculatePitch();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		player.move();
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	private void calculateCameraPosition(float horizDistance, float verticalDistance) {
		float theta = player.getRotY();
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticalDistance;
	}

	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		if(distanceFromPlayer < 35) {
			distanceFromPlayer = 35;
		}else if(distanceFromPlayer > 98) {
			distanceFromPlayer = 98;
		}
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(2)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
			if(pitch < 20) {
				pitch = 20;
			}else if(pitch > 70) {
				pitch = 70;
			}
			float angleChange = Mouse.getDX() * 0.3f;
			player.increaseRotation(0, -angleChange, 0);
		}
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}	
	
}
