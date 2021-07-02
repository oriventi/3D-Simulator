package mainPackage;

import java.util.ArrayList;

import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import World.World;
import entities.Camera;
import entities.Entity;
import entities.EntityManager;
import entities.Light;
import entities.LightManager;
import entities.Player;
import hud.HUDManager;
import hud.HUDTexture;
import models.RawModel;
import models.Mesh;
import models.MeshContainer;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.Color;
import textures.ModelTexture;
import toolbox.MousePicker;
import vehicles.Car;

public class MainGameLoop {

	public static Loader loader;
	public static HUDManager hudManager;
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		loader = new Loader();
		MeshContainer container = new MeshContainer(loader);
		EntityManager entityManager = new EntityManager();
		Player player = new Player(null, new Vector3f(0,0,0), 0, 180, 0, 1);
		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer(camera, loader);
		LightManager lightManager = new LightManager(new Light(new Vector3f(3000, 2000, 1000), new Color(1.f,1.f,1.f)));		
		World world = new World(loader, LightManager.getSun(), 500, camera);
		
		hudManager = new HUDManager();
	//	hudManager.addHUD(new HUDTexture(loader.loadTexture("grass_1"), new Vector2f(0.5f , 0.5f), new Vector2f(0.25f, 0.25f)));
	//	hudManager.addHUD(new HUDTexture(renderer.getShadowMapTexture(), new Vector2f(0.5f , 0.5f), new Vector2f(0.5f, 0.55f)));
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix());
		
		while(!Display.isCloseRequested()) {
			System.out.println(EntityManager.entities.size());
			//shadowMap
			renderer.renderShadowMap(EntityManager.entities);
			
			//game logic
			camera.move();
			picker.update();
			world.update(DisplayManager.getFrameTimeSeconds(), picker);
			
			//render
			world.render(renderer, picker);
			lightManager.render(renderer, camera);
			hudManager.render();

			//update
			DisplayManager.updateDisplay();
		}
		
		hudManager.cleanUp();
		renderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
	}
}
