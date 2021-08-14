package entities;

import org.lwjgl.util.vector.Vector3f;

import models.Mesh;

public class Entity {

	private Mesh model;
	private Vector3f position;
	private float rotX, rotY, rotZ, scaleX, scaleY;
	
	public Entity(Mesh model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super();
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scaleX = scale;
		this.scaleY = scale;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		rotX += dx;
		rotY += dy;
		rotZ += dz;
	}

	public Mesh getModel() {
		return model;
	}

	public void setModel(Mesh model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setPosition(float xpos, float ypos, float zpos) {
		position.x = xpos;
		position.y = ypos;
		position.z = zpos;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float d) {
		this.rotX = d;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scaleX;
	}

	public void setScale(float scale) {
		this.scaleX = scale;
		this.scaleY = scale;
	}
	
	public void setScale(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	
	
}
