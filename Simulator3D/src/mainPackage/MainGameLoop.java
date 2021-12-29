package mainPackage;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import World.World;
import animations.AnimationTimerUpdater;
import animations.Timer;
import entities.Camera;
import entities.Entity;
import entities.EntityShadowList;
import entities.Light;
import entities.LightManager;
import entities.Player;
import fileManager.WorldFileManager;
import fontMeshCreator.FontType;
import fontRendering.TextMaster;
import hud.HUDButton;
import hud.HUDDialog;
import hud.HUDUpdater;
import hud.HUDWindow;
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
	public static HUDUpdater hudManager;
	public static GameState gameState;
	public static Player player;
	public static Camera camera;
	public static FontType font;

	public static boolean isClosed;
	
	public static void main(String[] args) {
		isClosed = false;
		DisplayManager.createDisplay();
		
		loader = new Loader();
		gameState = GameState.GAME_MODE;
		hudManager = new HUDUpdater();
		TextMaster.init();
		
		font = new FontType(loader.loadTexture("candara"), new File("res/font/candara.fnt"));
		
		AnimationTimerUpdater animationUpdater = new AnimationTimerUpdater();
		MenuUpdater menuUpdater = new MenuUpdater();
		MeshContainer container = new MeshContainer(loader);
		EntityShadowList entityShadowList = new EntityShadowList();
		player = new Player(null, new Vector3f(0,0,0), 0, 180, 0, 1);
		camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer(camera, loader);
		LightManager lightManager = new LightManager(new Light(new Vector3f(2000, 1000, 1000), new Color(1.f,1.f,1.f)));		
		World world = new World(loader, LightManager.getSun(), 500, camera);

		HUDButton windowButton = new HUDButton("close_button", (int)100, (int)100, 70, 70, false, true);
		HUDDialog dialog = new HUDDialog("MOIN", "Bist du cool?", 500, 200, false);
		dialog.show();

		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix());

		Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);
		
		WorldFileManager worldFileManager = new WorldFileManager();

		//TEST
		Timer timer = new Timer(0.5f);
		timer.start();

		
		while(!Display.isCloseRequested() && !isClosed) {
			dialog.onPositiveClicked();
			dialog.onNegativeClicked();
			//shadowMap
			renderer.renderShadowMap(EntityShadowList.entities);
			//game logic
			if(MenuUpdater.isMenuActivated()) {
				menuUpdater.update();
			}else {
				picker.update();
			}
			
			//Button to load world
			if(windowButton.onMouseClicked()) {
				worldFileManager.fillWorldByInformationFromFile("world1");
				World.getTrafficManager().spawnRandomTraffic();
			}
			
			//pause menu handling
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && timer.timeReachedEnd()) {
				timer.restart();
				if(MenuUpdater.isMenuActivated()) {
					MenuUpdater.deactivateCurrentMenu();
				}else {
					MenuUpdater.activateMenu(new PauseMenu());
				}
			}
			
			//Updaters
			animationUpdater.update();
			camera.move();
			world.update(DisplayManager.getFrameTimeSeconds(), picker);
			hudManager.update();
			
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
		World.getTrafficManager().stopThread();
		TextMaster.cleanUp();
		PostProcessing.cleanUp();
		multisampleFbo.cleanUp();
		outputFbo.cleanUp();
		hudManager.cleanUp();
		renderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
	}
	
	public static void closeGame() {
		isClosed = true;
	}
}