package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.Mesh;
import renderEngine.DisplayManager;

public class Player extends Entity{

	float SPEED = 30;
	float speedX = 0;
	float speedZ = 0;
	
	public Player(Mesh model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(null, position, rotX, rotY, rotZ, scale);
	
	}
	
	public void move() {
		checkInputs();

		
	}
	
	private void checkInputs() {
		float distance = SPEED * DisplayManager.getFrameTimeSeconds();
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			speedX = (float) (distance * Math.cos(Math.toRadians(getRotY()-90)));
			speedZ = (float) -(distance * Math.sin(Math.toRadians(getRotY()-90)));
			super.increasePosition(speedX, 0, speedZ);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			speedX = (float) -(distance * Math.cos(Math.toRadians(getRotY()-90)));
			speedZ = (float) (distance * Math.sin(Math.toRadians(getRotY()-90)));
			super.increasePosition(speedX, 0, speedZ);
		}else {
			speedX = 0;
			speedZ = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			speedX = (float) (distance * Math.cos(Math.toRadians(getRotY())));
			speedZ = (float) -(distance * Math.sin(Math.toRadians(getRotY())));
			super.increasePosition(speedX, 0, speedZ);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			speedX = (float) -(distance * Math.cos(Math.toRadians(getRotY())));
			speedZ = (float) (distance * Math.sin(Math.toRadians(getRotY())));
			super.increasePosition(speedX, 0, speedZ);
		}
		
	}

}
