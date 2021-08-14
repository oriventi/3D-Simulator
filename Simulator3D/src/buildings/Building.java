package buildings;

import org.lwjgl.util.vector.Vector3f;

import World.TileManager;
import entities.Entity;
import models.Mesh;
import renderEngine.MasterRenderer;

public abstract class Building {

	protected int xtile, ytile;
	protected float xpos, ypos;
	protected int rotation;
	
	private Mesh mesh;
	private Entity entity;
	private Foundation foundation;
	
	public Building(int xtile, int ytile) {
		this.xtile = xtile;
		this.ytile = ytile;
		this.xpos = (xtile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		this.ypos = (ytile + 0.5f) * TileManager.tsize - TileManager.wsize / 2;
		
		mesh = setMesh();
		rotation = setRotation();
		foundation = new Foundation(xtile, ytile);
		makeEntity();
	}
	
	protected abstract Mesh setMesh();
	
	protected abstract int setRotation();
	
	private void makeEntity() {
		entity = new Entity(mesh, new Vector3f(xpos, 0.5f, ypos), 0, rotation, 0, 1);
	}
	
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
		foundation.render(renderer);
	}
		
}
