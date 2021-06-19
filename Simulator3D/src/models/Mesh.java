package models;


import renderEngine.Loader;
import renderEngine.OBJLoader;
import shaders.StaticShader;
import textures.Color;
import textures.ModelTexture;

public class Mesh{

	private RawModel rawModel;
	private ModelTexture texture;
	private Color color;
	private boolean hasColor;
	
	public Mesh(RawModel model, ModelTexture texture) {
		rawModel = model;
		this.texture = texture;
		color = new Color(-1, -1, -1);
		hasColor = false;
	}
	
	public Mesh(String objfile, String texturefile, Loader loader) {
		rawModel = OBJLoader.loadObjModel(objfile, loader);
		this.texture = new ModelTexture(loader.loadTexture(texturefile));
		color = new Color(-1, -1, -1);
		hasColor = false;
	}
	
	public Mesh(String objfile, Loader loader) {
		rawModel = OBJLoader.loadObjModel(objfile, loader);
		this.texture = new ModelTexture(loader.loadTexture("palette"));
		color = new Color(-1, -1, -1);
		hasColor = false;
	}
	
	public Mesh(RawModel model, Color color) {
		rawModel = model;
		this.color = color;
		hasColor = true;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public void setRawModel(RawModel rawModel) {
		this.rawModel = rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}

	public void setTexture(ModelTexture texture) {
		this.texture = texture;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean hasColor() {
		return hasColor;
	}

	public void setHasColor(boolean hasColor) {
		this.hasColor = hasColor;
	}
	
	public void loadShaderData(StaticShader shader) {
		shader.loadColor(color.getVector());
		shader.loadHasColor(hasColor);
	}
	
	
	
}
