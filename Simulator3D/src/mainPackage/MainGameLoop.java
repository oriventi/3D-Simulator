package mainPackage;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import World.World;
import animations.Timer;
import entities.Camera;
import entities.EntityShadowList;
import entities.Light;
import entities.LightManager;
import entities.Player;
import fileManager.WorldFileManager;
import fontMeshCreator.FontType;
import fontRendering.TextMaster;
import hud.HUDButton;
import hud.HUDRenderList;
import hud.HUDText;
import menu.MenuUpdater;
import menu.PauseMenu;
import models.MeshContainer;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import textures.Color;
import toolbox.EnumHolder.GameState;
import toolbox.MousePicker;

public class MainGameLoop {

	public static Loader loader;
	public static HUDRenderList hudManager;
	public static GameState gameState;
	public static Player player;
	public static Camera camera;
	public static FontType font;
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		loader = new Loader();
		gameState = GameState.GAME_MODE;
		hudManager = new HUDRenderList();
		TextMaster.init();
		
		//TEST
		font = new FontType(loader.loadTexture("calibri"), new File("res/font/calibri.fnt"));
		HUDText text = new HUDText("Das ist ein Text!", 10, font, 200, 200, 300);
		
		MenuUpdater menuUpdater = new MenuUpdater();
		MeshContainer container = new MeshContainer(loader);
		EntityShadowList entityShadowList = new EntityShadowList();
		player = new Player(null, new Vector3f(0,0,0), 0, 180, 0, 1);
		camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer(camera, loader);
		LightManager lightManager = new LightManager(new Light(new Vector3f(3000, 2000, 1000), new Color(1.f,1.f,1.f)));		
		World world = new World(loader, LightManager.getSun(), 500, camera);
		
		HUDButton windowButton = new HUDButton("close_button", 100, 100, 70, 70, false);

		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix());
		
		Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);
		
		WorldFileManager worldFileManager = new WorldFileManager();
		
		//TEST
		PauseMenu menu = new PauseMenu();
		Timer timer = new Timer(0.5f);
		timer.start();
		
		while(!Display.isCloseRequested()) {
			//shadowMap
			renderer.renderShadowMap(EntityShadowList.entities);
			timer.update();
			
			//game logic
			windowButton.update();
			if(windowButton.onMouseClicked()) {
				worldFileManager.fillWorldByInformationFromFile("world1");
				World.getTrafficManager().spawnRandomTraffic();
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && timer.timeReachedEnd()) {
				timer.restart();
				if(MenuUpdater.isMenuActivated()) {
					MenuUpdater.deactivateMenu();
				}else {
					MenuUpdater.activateMenu(menu);
				}
			}
			menuUpdater.update();

			camera.move();
			picker.update();
			world.update(DisplayManager.getFrameTimeSeconds(), picker);
			hudManager.update();
			if(MenuUpdater.isMenuActivated()) {
				menuUpdater.update();
			}
			
			//render
			multisampleFbo.bindFrameBuffer();
			world.render(renderer, picker);
			lightManager.render(renderer, camera);
			multisampleFbo.unbindFrameBuffer();
			multisampleFbo.resolveToFbo(outputFbo);
			PostProcessing.doPostProcessing(outputFbo.getColourTexture());
			
			hudManager.render();
			TextMaster.render();

			//update
			DisplayManager.updateDisplay();
		}
		
		//cleanUp on close
		TextMaster.cleanUp();
		PostProcessing.cleanUp();
		multisampleFbo.cleanUp();
		outputFbo.cleanUp();
		hudManager.cleanUp();
		renderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
	}
}