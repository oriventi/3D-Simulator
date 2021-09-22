package buildings;

import org.lwjgl.util.vector.Vector3f;

import World.TileContent;
import World.TileManager;
import entities.Entity;
import entities.EntityShadowList;
import models.Mesh;
import renderEngine.MasterRenderer;

public abstract class Building extends TileContent{

	protected int xtile, ytile;
	protected float xpos, ypos = 0;
	protected int rotation;
	protected int xFoundations, yFoundations;
	
	private Mesh mesh;
	private Entity entity;
	private Foundation[] foundations;
	
	private String buildingID = "";
	
	public Building(int xtile, int ytile) {
		super(xtile, ytile);
		this.xtile = xtile;
		this.ytile = ytile;
		
		mesh = setMesh();
		rotation = setRotation();
		xFoundations = setXFoundationSize();
		yFoundations = setYFoundationSize();
		buildingID = setBuildingID() + "r" + rotation;
		generateFoundations();
		calculateBuildingsPosition();
		
		makeEntity();
	}
	
	private void generateFoundations() {
		foundations = new Foundation[xFoundations * yFoundations];
		for(int i = 0; i < yFoundations; i++) {
			for(int j = 0; j < xFoundations; j++) {
				foundations[(j) + (i*xFoundations)] = new Foundation(xtile + j, ytile + i);
			}
		}
	}
	
	private void calculateBuildingsPosition() {
		float allFoundationXPosSummed = 0;
		float allFoundationYPosSummed = 0;
		for(int i = 0; i < foundations.length; i++) {
			allFoundationXPosSummed += foundations[i].getXPos();
			allFoundationYPosSummed += foundations[i].getYPos();
		}
		xpos = allFoundationXPosSummed / foundations.length;
		ypos = allFoundationYPosSummed / foundations.length;
	}
	
	protected abstract Mesh setMesh();
	
	protected abstract int setXFoundationSize();
	
	protected abstract int setYFoundationSize();
	
	protected abstract int setRotation();
	
	protected abstract int setBuildingID();
	
	private void makeEntity() {
		entity = new Entity(mesh, new Vector3f(xpos, 0.5f, ypos), 0, rotation, 0, 1);
		EntityShadowList.addEntity(entity);
	}
	
	@Override
	public void increaseRotation() {
		rotation += 90;
		entity.setRotY(rotation);
	}
	
	@Override
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
		for(int i = 0; i < foundations.length; i++) {
			foundations[i].render(renderer);
		}
	}
	
	@Override
	public void destroy() {
		EntityShadowList.removeEntity(entity);
	}
	
	public int getXFoundations() {
		return xFoundations;
	}
	
	public int getYFoundations() {
		return yFoundations;
	}
	
	public String getBuildingID() {
		return buildingID;
	}
		
}
