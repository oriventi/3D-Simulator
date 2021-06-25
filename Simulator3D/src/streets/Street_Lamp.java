package streets;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import World.TileManager;
import entities.Entity;
import entities.Light;
import entities.LightManager;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import textures.Color;
import toolbox.Maths;

public class Street_Lamp {
	

	private float xpos, ypos;
	private float dx, dy;
	private int xtile, ytile;
	private boolean light_on;
	private int rotation;
	
	private Entity entity;
	private Light light;
	
	public Street_Lamp(int xtile, int ytile, float dx, float dy, int rotation) {
		xpos = Maths.getPositionFromTile(xtile, dx);
		ypos = Maths.getPositionFromTile(ytile, dy);
		this.xtile = xtile;
		this.ytile = ytile;
		this.dx = dx;
		this.dy = dy;
		this.rotation = rotation;
		this.light_on = false;
		
		entity = new Entity(MeshContainer.street_lamp, new Vector3f(xpos, 0, ypos), 0, rotation, 0, 1);
		
		light = new Light(new Vector3f(xpos, 3, ypos), new Color(1f, 1f, 1f), new Vector3f(0.3f, 0.2f, 0.1f));
	}
	
	public void render(MasterRenderer renderer) {
		renderer.processEntity(entity);
	}
	
	//creates Vector from the center of the tile towards the PathMarker
	private Vector2f makeCenterVec() {
		Vector2f a = new Vector2f(0.5f * TileManager.tsize, 0.5f * TileManager.tsize);
		Vector2f b = new Vector2f(dx * TileManager.tsize, dy * TileManager.tsize);
		return new Vector2f(b.x - a.x, b.y - a.y);
	}
	
	//returns the relatives of a point from a centerVector
	private Vector2f getRelativePosByVector(Vector2f vec) {
		float relativeX = 0.5f + (vec.x / TileManager.tsize);
		float relativeY = 0.5f + (vec.y / TileManager.tsize);
		return new Vector2f(relativeX, relativeY);
	}
	
	public void setPositionToStreetRotation(int streetRot) {
		//sets currentStreetRot to 0
		//TODO make efficient
		switch(streetRot) {
			case 0:
			case 180:
				Vector2f toRotateVector1 = makeCenterVec();
				toRotateVector1 = Maths.rotateVectorBy90Degrees(toRotateVector1, 1);
				dx = getRelativePosByVector(toRotateVector1).x;
				dy = getRelativePosByVector(toRotateVector1).y;
				rotation +=90;
				entity.setRotY(rotation);
				break;
		}
		updateWorldPosition();
	}
	
	private void updateWorldPosition() {
		this.xpos = Maths.getPositionFromTile(xtile, dx);
		this.ypos = Maths.getPositionFromTile(ytile, dy);
		light.setPosition(xpos, 1.5f, ypos);
		entity.setPosition(xpos, 0, ypos);
	}
	
	public void turnOffLight() {
		LightManager.removeLight(light);
		light_on = false;
	}
	
	public void turnOnLight() {
		LightManager.addLight(light);
		light_on = true;
	}
	
	public void switchLightsOnOff() {
		if(light_on) {
			turnOffLight();
		}else {
			turnOnLight();
		}
	}
}
