package buildings;

import org.lwjgl.util.vector.Vector3f;

import World.TileContent;
import World.TileManager;
import entities.Entity;
import entities.EntityShadowList;
import models.Mesh;
import renderEngine.MasterRenderer;

/**
 * abstract class of tileContent Building, which contains 
 * general information about a building
 * @author Oriventi
 *
 */
public abstract class Building extends TileContent{

	protected int xtile, ytile;
	protected float xpos, ypos = 0;
	protected int rotation;
	protected int xFoundations, yFoundations;
	
	private Mesh mesh;
	private Entity entity;
	private Foundation[] foundations;
	
	private String buildingID = "";
	
	/**
	 * fetches and sets all data needed for the building
	 * @param xtile of the building
	 * @param ytile of the buildign
	 */
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
	
	/**
	 * creates a foundation with the given size of the building
	 */
	private void generateFoundations() {
		foundations = new Foundation[xFoundations * yFoundations];
		for(int i = 0; i < yFoundations; i++) {
			for(int j = 0; j < xFoundations; j++) {
				foundations[(j) + (i*xFoundations)] = new Foundation(xtile + j, ytile + i);
			}
		}
	}
	
	/**
	 * sets the building in the center if its bigger than one tile
	 */
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
	
	/**
	 * gets the mesh of the building
	 * @return mesh of building
	 */
	protected abstract Mesh setMesh();
	
	/**
	 * gets the amount of xFoundations needed
	 * @return amount of xFoundations
	 */
	protected abstract int setXFoundationSize();
	
	/**
	 * gets the amount of yFoundations needed
	 * @return amount of y Foundations
	 */
	protected abstract int setYFoundationSize();
	
	/**
	 * gets the initial rotation of the building
	 * @return rotation in degrees of the building
	 */
	protected abstract int setRotation();
	
	/**
	 * gets the ID of the buidling
	 * @return building ID
	 */
	protected abstract int setBuildingID();
	
	/**
	 * creates the entity out of given Data and adds it to the shadowList
	 */
	private void makeEntity() {
		entity = new Entity(mesh, new Vector3f(xpos, 0.5f, ypos), 0, rotation, 0, 1);
		EntityShadowList.addEntity(entity);
	}
	
	@Override
	public void increaseRotation() {
		rotation += 90;
		entity.setRotY(rotation);
		buildingID = setBuildingID() + "r" + rotation;
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
	
	/**
	 * returns the amount of Foundation in x direction
	 * @return amount of xFoundations
	 */
	public int getXFoundations() {
		return xFoundations;
	}
	
	/**
	 * returns the amount of FOundations in y direction
	 * @return amount of yFoudnations
	 */
	public int getYFoundations() {
		return yFoundations;
	}
	
	@Override
	public String getBuildingID() {
		return buildingID;
	}
		
}