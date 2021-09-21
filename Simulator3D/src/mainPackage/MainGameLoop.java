package mainPackage;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import World.World;
import entities.Camera;
import entities.EntityShadowList;
import entities.Light;
import entities.LightManager;
import entities.Player;
import hud.HUDButton;
import hud.HUDRenderList;
import hud.HUDWindow;
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
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		loader = new Loader();
		gameState = GameState.GAME_MODE;
		hudManager = new HUDRenderList();		
		
		MeshContainer container = new MeshContainer(loader);
		EntityShadowList entityManager = new EntityShadowList();
		Player player = new Player(null, new Vector3f(0,0,0), 0, 180, 0, 1);
		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer(camera, loader);
		LightManager lightManager = new LightManager(new Light(new Vector3f(3000, 2000, 1000), new Color(1.f,1.f,1.f)));		
		World world = new World(loader, LightManager.getSun(), 500, camera);
		
		HUDButton windowButton = new HUDButton("close_button", 100, 100, 70, 70);

		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix());
		
		Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);
		
		while(!Display.isCloseRequested()) {
			//shadowMap
			renderer.renderShadowMap(EntityShadowList.entities);
			
			//game logic
			windowButton.update();
			if(windowButton.onMouseClicked()) {
				HUDRenderList.addWindow(new HUDWindow(300, 200, 300, 500) {
					
					@Override
					protected void updateContent() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					protected void renderContent() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					protected void moveContentWithWindowMovement(int xpos, int ypos) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					protected void destroyContent() {
						// TODO Auto-generated method stub
						
					}
				});//indow(300, 200, 300, 500));
			}
			camera.move();
			picker.update();
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

			//update
			DisplayManager.updateDisplay();
		}
		
		//cleanUp on close
		PostProcessing.cleanUp();
		multisampleFbo.cleanUp();
		outputFbo.cleanUp();
		hudManager.cleanUp();
		renderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
	}
}