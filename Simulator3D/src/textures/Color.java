package textures;

import org.lwjgl.util.vector.Vector3f;

public class Color {

	private Vector3f color = new Vector3f(1.f, 1.f, 1.f);
	
	public Color(Vector3f color) {
		this.color = color;
	}
	
	public Color(float r, float g, float b) {
		color = new Vector3f(r, g, b);
	}
	
	public Color(int r, int g, int b) {
		float rf = (float) r / 255;
		float gf = (float) g / 255;
		float bf = (float) b / 255;
		color = new Vector3f(rf, gf, bf);
	}
	
	public float getR() {
		return color.x;
	}
	
	public float getG() {
		return color.y;
	}
	
	public float getB() {
		return color.z;
	}
	
	public Vector3f getVector() {
		return color;
	}
	
}
