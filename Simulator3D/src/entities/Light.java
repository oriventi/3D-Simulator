package entities;

import org.lwjgl.util.vector.Vector3f;

import textures.Color;

public class Light {

	private Vector3f position;
	private Color color;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	
	public Light(Vector3f position, Color color) {
		this.position = position;
		this.color = color;
	}
	
	public Light(Vector3f position, Color color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}
	
	public Vector3f getAttenuation() {
		return attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
