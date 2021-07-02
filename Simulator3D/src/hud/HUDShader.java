package hud;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

public class HUDShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/hud/hudVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/hud/hudFragmentShader.txt";
	
	private int location_transformationMatrix;
	
	public HUDShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	

}
