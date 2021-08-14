package buildings;

import org.lwjgl.util.vector.Vector3f;

import World.TileManager;
import entities.Entity;
import models.MeshContainer;
import renderEngine.MasterRenderer;

public class Foundation {
	
	private int xtile, ytile;
	private float xpos, ypos;
	private Entity entity;

	public Foundation(int xtile, int ytile) {
		this.xtile = xtile;
		this.ytile = ytile;
		this.xpos = (xtile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		this.ypos = (ytile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		
	}
	
	private void makeEntity() {
		entity = new Entity(MeshContainer.foundation, new Vector3f(xpos, 0.f, ypos), 0, 0, 0, 1);
	}
	
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
	}
}
