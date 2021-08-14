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
	protected float xpos, ypos;
	protected int rotation;
	
	private Mesh mesh;
	private Entity entity;
	private Foundation foundation;
	
	public Building(int xtile, int ytile) {
		super(xtile, ytile);
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
		EntityShadowList.addEntity(entity);
	}
	
	@Override
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
		foundation.render(renderer);
	}
	
	@Override
	public void destroy() {
		EntityShadowList.removeEntity(entity);
	}
		
}
