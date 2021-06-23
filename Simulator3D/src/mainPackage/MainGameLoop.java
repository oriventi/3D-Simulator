package mainPackage;

import java.util.ArrayList;

import java.util.List;

import org.lwjgl.opengl.Display;

import org.lwjgl.util.vector.Vector3f;

import World.World;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.LightManager;
import entities.Player;
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
import worldEntities.Car;

public class MainGameLoop {

	//TEST ENTITY TODO DELETE
	public static Loader loader;
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		loader = new Loader();
		MeshContainer container = new MeshContainer(loader);
		Player player = new Player(null, new Vector3f(0,0,0), 0, 180, 0, 1);
		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer(camera, loader);
		LightManager lightManager = new LightManager(new Light(new Vector3f(3000, 2000, 1000), new Color(1.f,1.f,1.f)));		
		
		World world = new World(loader, lightManager.getSun(), 500, camera);
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix());
		
		
		
		while(!Display.isCloseRequested()) {
			//game logic
			camera.move();
			picker.update();
			world.update(DisplayManager.getFrameTimeSeconds(), picker);
			
			//render
			world.render(renderer, picker);
			lightManager.render(renderer, camera);
			
			//update
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
	}
}
