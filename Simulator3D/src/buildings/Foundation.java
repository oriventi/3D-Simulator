package buildings;

import org.lwjgl.util.vector.Vector3f;

import World.TileContent;
import World.TileManager;
import entities.Entity;
import models.MeshContainer;
import renderEngine.MasterRenderer;

/**
 * contains all data of a foundation of a building
 * @author Oriventi
 *
 */
public class Foundation extends TileContent{
	
	private int xtile, ytile;
	private float xpos, ypos;
	private Entity foundationEntity;
	private Entity[] content;

	private String buildingId;

	/**
	 * sets position of entity
	 * @param xtile
	 * @param ytile
	 */
	public Foundation(int xtile, int ytile) {
		super(xtile, ytile);
		this.xtile = xtile;
		this.ytile = ytile;
		this.xpos = (xtile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		this.ypos = (ytile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		buildingId = "-1";
		
		makeEntity();
	}
	
	/**
	 * creates foundation Entity out of given data
	 */
	private void makeEntity() {
		foundationEntity = new Entity(MeshContainer.foundation, new Vector3f(xpos, 0.f, ypos), 0, 0, 0, 1);
	}
	
	/**
	 * renders the foundation entity 
	 */
	public void render(MasterRenderer renderer) {
		renderer.processEntity(foundationEntity);
	}
	
	/**
	 * generates the content (bench, hydrant) of a foundation
	 */
	public void generateContent() {
		//TODO fill
	}
	
	/**
	 * returns the xpos on world surface
	 * @return x position on world surface
	 */
	public float getXPos() {
		return xpos;
	}
	
	/**
	 * returns ypos on world surface
	 * @return y position on world surface (z pos in openGL dimensions)
	 */
	public float getYPos() {
		return ypos;
	}
	
	@Override
	public String getBuildingID() {
		return buildingId;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void increaseRotation() {
		// TODO Auto-generated method stub
		
	}
}