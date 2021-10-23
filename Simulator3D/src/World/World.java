package World;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.EntityShadowList;
import entities.Light;
import models.Mesh;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.Color;
import textures.ModelTexture;
import toolbox.MousePicker;
import traffic.TrafficManager;
import vehicles.Car;


/**
 * contains, updates and renders the World, the content of the tiles and the trafic
 * @author Oriventi
 *
 */
public class World {
	
	private int size;
	private Entity worldEntity;
	
	private TileManager tileManager;
	private static TrafficManager trafficManager;
	
	private Loader loader;
	private Camera cam;
	private Light sun;
	
	/**
	 * init variables and starts trafficManager
	 * @param loader
	 * @param sun
	 * @param size
	 * @param cam
	 */
	public World(Loader loader, Light sun, int size, Camera cam) {
		this.loader = loader;
		this.size = size;
		this.sun = sun;
		this.cam = cam;
		tileManager = new TileManager(50, size, loader);
		worldEntity = generateMesh(loader);
	//	EntityManager.addEntity(worldEntity);
		trafficManager = new TrafficManager();
		trafficManager.start();
		
	}
	
	/**
	 * updates the tileManager, and the trafficManagers userInput
	 * @param timeSinceLastFrame
	 * @param mousePicker
	 */
	public void update(float tslf, MousePicker picker) {
		tileManager.update(tslf, picker.getPosition(cam).x, picker.getPosition(cam).z);
		trafficManager.updateUserInput(picker, cam);
	}
	
	/**
	 * renders the world, the content of the trafficManager, and the content of the tiles
	 * @param renderer
	 * @param mousePicker
	 */
	public void render(MasterRenderer renderer, MousePicker picker) {
		//DRAW STATIC
		tileManager.render(renderer, picker, cam);
		trafficManager.render(renderer);
		renderer.processEntity(worldEntity);
	}
	
	/**
	 * generates the world Surface entity
	 * @param loader
	 * @return worldEntity
	 */
	private Entity generateMesh(Loader loader) {

		float col = (float) (6.5f / 8.f);
		
		float[] vertices = {			
				-size/2, 0, -size/2, //V0
				-size/2, 0, size/2, //V1
				size/2, 0, size/2, //V2
				size/2, 0, -size/2 //V3
				
		};
		
		float[] textureCoords = {
				col, col,
				col, col,
				col, col,
				col, col
				
		};
		
		int[] indices = {
				0, 1, 2, 2, 3, 0

		};
		
		float[] normals = {
				0, 1, 0,
				0, 1, 0
		};
		
		Mesh mesh = new Mesh(loader.loadToVAO(vertices, textureCoords, normals, indices), new ModelTexture(loader.loadTexture("palette")));
		Entity entity = new Entity(mesh, new Vector3f(0,0,0), 0, 0, 0, 1);
		return entity;
	}
	
	/**
	 * returns static trafficManager instance
	 * @return trafficManager
	 */
	public static TrafficManager getTrafficManager() {
		return trafficManager;
	}

}
