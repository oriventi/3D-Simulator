package toolbox;

import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import World.TileManager;
import entities.Camera;

public class Maths {
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()) , new Vector3f(1,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
	public static int getRandomBetween(int min, int max) {
		return new Random().nextInt((max - min) + 1) + min;
	}
	
	//rotates a vector by 90 Degrees *number* times
	public static Vector2f rotateVectorBy90Degrees(Vector2f vec, int number) {
		Vector2f vector = vec;
		for(int i = 0; i < number; i++) {
			vector = getOrthogonalVector(vector);
		}
		return vector;
	}
	
	//
	public static Vector3f rotateVectorBy90Degrees(Vector3f vec, int number) {
		Vector2f vector = new Vector2f(vec.x, vec.z);
		for(int i = 0; i < number; i++) {
			vector = getOrthogonalVector(vector);
		}
		return new Vector3f(vector.x, 0, vector.y);
	}
	
	//returns orthogonal Vector2f clockwise
	public static Vector2f getOrthogonalVector(Vector2f vec) {
		return new Vector2f(vec.y, -vec.x);
	}
	
	public static float getPositionFromTile(int tile, float delta) {
		return (tile + delta) * TileManager.tsize - TileManager.wsize / 2;
	}
	
	//returns tile from mousePosition
	public static Vector2f getTileFromMousePosition(MousePicker picker, Camera cam) {
		int x = (int) picker.getPosition(cam).x;
		int z = (int) picker.getPosition(cam).z;
		int tilex = (int)((x + TileManager.wsize / 2) / TileManager.tsize);
		int tiley = (int)((z + TileManager.wsize / 2) / TileManager.tsize);
		
		if(tilex < 0) {
			tilex = 0;
		}
		if(tiley < 0) {
			tiley = 0;
		}
		if(tilex > TileManager.size - 1) {
			tilex = TileManager.size - 1;
		}
		if(tiley > TileManager.size - 1) {
			tiley = TileManager.size -1 ;
		}
				
		return new Vector2f(tilex, tiley);
		
	}
	
	//returns tile out of xpos and zpos
	public static Vector2f getTileFromPosition(float x, float z) {
		int tilex = (int)((x + TileManager.wsize / 2) / TileManager.tsize);
		int tiley = (int)((z + TileManager.wsize / 2) / TileManager.tsize);
		
		if(tilex < 0) {
			tilex = 0;
		}
		if(tiley < 0) {
			tiley = 0;
		}
		if(tilex > TileManager.size - 1) {
			tilex = TileManager.size - 1;
		}
		if(tiley > TileManager.size - 1) {
			tiley = TileManager.size -1 ;
		}
		
		return new Vector2f(tilex, tiley);
		
	}
	
	
	

}
