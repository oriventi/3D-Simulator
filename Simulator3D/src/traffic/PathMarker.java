package traffic;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import World.TileManager;
import entities.Entity;
import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.Maths;
import toolbox.EnumHolder.DrivingMode;

public class PathMarker{

	private float anim_rot; //animation, rotation of the cube
	private int xtile, ytile; //which tile
	private float xpos, ypos; //actual position in world grid
	private int[] actions; //straight, left, right , small_left, big_left --- -1 is no and other number gives index of markers list in street
	private List<DrivingAction> possibleActions;
	private boolean stop;
	
	private Vector2f position = new Vector2f();
	private Vector2f originalRelativePos = new Vector2f();
	private Vector2f relativePos = new Vector2f(); //which position in tile like 0.5 is in the middle
	
	private Entity cube; //in order to render the cube
	
	public PathMarker(int xtile, int ytile, float relativeX, float relativeY, int[] actions) {
		possibleActions = new ArrayList<DrivingAction>();
		
		this.relativePos.x = relativeX;
		this.relativePos.y = relativeY;
		this.originalRelativePos = relativePos;
		
		this.xtile = xtile;
		this.ytile = ytile;
		this.xpos = Maths.getPositionFromTile(xtile, relativeX);
		this.ypos = Maths.getPositionFromTile(ytile, relativeY);
		position.x = xpos;
		position.y = ypos;
		
		if(actions != null) {
			this.actions = actions;
		}else {
			this.actions = new int[] {-1, -1, -1, -1, -1};
		}
		
		for(int i = 0; i < this.actions.length; i++) {
			if(this.actions[i] >= 0) {
				switch(i) {
				case 0:
					possibleActions.add(new DrivingAction(actions[i], DrivingMode.STRAIGHT));
					break;
				case 1:
					possibleActions.add(new DrivingAction(actions[i], DrivingMode.LEFT));
					break;
				case 2:
					possibleActions.add(new DrivingAction(actions[i], DrivingMode.RIGHT));
					break;
				case 3:
					possibleActions.add(new DrivingAction(actions[i], DrivingMode.SMALL_LEFT));
					break;
				case 4:
					possibleActions.add(new DrivingAction(actions[i], DrivingMode.BIG_LEFT));
					break;
				}
			}
		}
		if(possibleActions.size() == 0) {
			possibleActions.add(new DrivingAction(-1, DrivingMode.INVALID));
		}
		
		
		cube = new Entity(MeshContainer.cube, new Vector3f(xpos, 0.8f, ypos), 0, 0, 0, 0.5f);
	}
	
	//creates Vector from the center of the tile towards the PathMarker
	private Vector2f makeCenterVec() {
		Vector2f a = new Vector2f(0.5f * TileManager.tsize, 0.5f * TileManager.tsize);
		Vector2f b = new Vector2f(relativePos.x * TileManager.tsize, relativePos.y * TileManager.tsize);
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
		relativePos = originalRelativePos;
		switch(streetRot) {
			case 90:
				Vector2f toRotateVector1 = makeCenterVec();
				toRotateVector1 = Maths.rotateVectorBy90Degrees(toRotateVector1, 1);
				relativePos = getRelativePosByVector(toRotateVector1);
				break;
			case 180:
				Vector2f toRotateVector2 = makeCenterVec();
				toRotateVector2 = Maths.rotateVectorBy90Degrees(toRotateVector2, 2);
				relativePos = getRelativePosByVector(toRotateVector2);
				break;
			case -90:
				Vector2f toRotateVector3 = makeCenterVec();
				toRotateVector3 = Maths.rotateVectorBy90Degrees(toRotateVector3, 3);
				relativePos = getRelativePosByVector(toRotateVector3);
				break;		
		}
		updateWorldPosition();
	}
	
	private void updateWorldPosition() {
		this.xpos = Maths.getPositionFromTile(xtile, relativePos.x);
		this.ypos = Maths.getPositionFromTile(ytile, relativePos.y);
		cube.setPosition(xpos, 0.8f, ypos);
	}
	
	//Cube mesh rotates all the time
	private void updateRotation() {
		if(anim_rot > 359.8) {
			anim_rot = 0;
		}else {
			anim_rot += 0.2;
		}
		cube.setRotY(anim_rot);
	}
	
	public void render(MasterRenderer renderer) {
		updateRotation();
		renderer.processEntity(cube);
	}

	public float getWorldPositionX() {
		return xpos;
	}
	
	public float getWorldPositionY() {
		return ypos;
	}
	
	public Vector3f getPosition3f() {
		return new Vector3f(xpos, 0.2f, ypos);
	}
	
	public Vector2f getPosition2f() {
		return new Vector2f(xpos, ypos);
	}
	
	public int[] getActions() {
		return actions;
	}
	
	public List<DrivingAction> getPossibleActions() {
		return possibleActions;
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
		if(stop == true) {
			cube.setModel(MeshContainer.cube_red);
		}else {
			cube.setModel(MeshContainer.cube);
		}
	}
	
	public boolean isStop() {
		return stop;
	}
	
}
