package people;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.EntityShadowList;
import models.Mesh;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.ID;
import toolbox.Maths;
import traffic.PathMarker;

public abstract class Pedestrian {

	protected Vector3f position;
	protected PathMarker currentMarker;
	protected PathMarker nextMarker;
	protected int maxSpeed;
	
	protected Entity entity;
	protected Mesh mesh;
	protected float entitySize;
	
	protected ID id;
	
	public Pedestrian(PathMarker startMarker) {
		currentMarker = startMarker;
		position = startMarker.getPosition3f();
		
		id = setID();
		maxSpeed = setMaxSpeed();
		
		mesh = setMesh();
		entitySize = Maths.getRandomBetween(8, 13) / 10.f;
		entity = new Entity(mesh, position, 0, 0, 0, entitySize);
	}
	
	protected abstract ID setID();
	
	protected abstract Mesh setMesh();
	
	protected abstract int setMaxSpeed();
	
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
		EntityShadowList.addEntity(entity);
	}
	
	public void destroy() {
		EntityShadowList.removeEntity(entity);
	}
	
}
