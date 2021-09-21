package World;

import org.lwjgl.input.Keyboard;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import buildings.Bank;
import buildings.Building;
import buildings.FactoryOne;
import buildings.HouseOne;
import buildings.HouseThree;
import buildings.HouseTwo;
import buildings.OfficeOne;
import buildings.SupermarketOne;
import entities.Camera;
import entities.Entity;
import mainPackage.MainGameLoop;
import models.Mesh;
import models.MeshContainer;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import textures.Color;
import textures.ModelTexture;
import toolbox.EnumHolder.GameState;
import toolbox.Maths;
import toolbox.MousePicker;
import traffic.StreetManager;
import vehicles.Car;

public class TileManager {

	public static int size; //how many tiles?
	public static  int tsize; //Tile size
	public static int wsize; //World size
	int current_id;
	private boolean street_mode;
	private Vector2f mouseTile = new Vector2f();
	private float tslr = 0;
	
	private static Tile[][] tiles;
	private StreetManager streetManager;
	
	private Entity selector;

	public TileManager(int size, int wsize, Loader loader) {
		this.size = size;
		this.wsize = wsize;
		this.tsize = wsize / size;
		
		initTiles();
		
		streetManager = new StreetManager();
		
		selector = new Entity(MeshContainer.generateSelectorMesh(), new Vector3f(0, 0, 0), 0, 0, 0, 1);
		
		street_mode = true;
		current_id = 0;
	}
	
	//initializes tile array
	private void initTiles() {
		tiles = new Tile[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				tiles[i][j] = new Tile(tsize);
			}
		}
	}
	
	public void update(float tslf, float mousex, float mousey) {
		checkInput(mousex, mousey);
		tslr+=DisplayManager.getFrameTimeSeconds();
	}
	
	private void checkInput(float mousex, float mousey) {
		if(Keyboard.isKeyDown(Keyboard.KEY_0)) {
			street_mode = true;
			current_id = 0;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_1)) {
			current_id = 1;
			street_mode = false;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_2)) {
			current_id = 2;
			street_mode = false;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_3)) {
			current_id = 3;
			street_mode = false;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_4)) {
			current_id = 4;
			street_mode = false;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_5)) {
			current_id = 5;
			street_mode = false;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_6)) {
			current_id = 6;
			street_mode = false;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_7)) {
			current_id = 7;
			street_mode = false;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_8)) {
			current_id = 8;
			street_mode = false;
		}
		
		if(MainGameLoop.gameState == GameState.GAME_MODE) {
			if(Mouse.isButtonDown(0)) {
			 	tslr = 0;
			 	setTileContent(mousex, mousey);			 
		 	}else if(Mouse.isButtonDown(1)) {
			 	removeTileContent(mousex, mousey);		 
		 	}
		}
		 
		 if(Keyboard.isKeyDown(Keyboard.KEY_R) && tslr >= 0.3f) {
			 tslr = 0;
			 Vector2f tile = getTileFromPosition(mousex, mousey);
			 if(tiles[(int) tile.x][(int) tile.y].hasContent()) {
				 tiles[(int) tile.x][(int) tile.y].getContent().increaseRotation();
			 }
		 }
	}
	
	public void render(MasterRenderer renderer, MousePicker picker, Camera cam) {
		renderTileContent(renderer);
		drawSelector(renderer, picker, cam);
		streetManager.render(renderer);

	}
	
	
	//draws the content of all tiles
	private void renderTileContent(MasterRenderer renderer) {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				tiles[i][j].render(renderer);
			}
		}
	}

	//Draws selector on grid, vec contains x and y of the tile
	private void drawSelector(MasterRenderer renderer, MousePicker picker, Camera cam) {
		if(MainGameLoop.gameState == GameState.GAME_MODE) {
			mouseTile = getTileFromPosition(picker.getPosition(cam).x, picker.getPosition(cam).z);
			selector.setPosition(mouseTile.x * tsize -wsize / 2, 0.5f, mouseTile.y * tsize - wsize / 2);
			renderer.processEntity(selector);
		}
	}
	
	//MouseX and MouseY
	public void setTileContent(float xpos, float ypos) {
		Vector2f tile = getTileFromPosition(xpos, ypos);
		if(street_mode) {
			
			streetManager.addStreet((int)tile.x, (int)tile.y);
			
		}else {
			tiles[(int)tile.x][(int)tile.y].removeContent();
			tiles[(int)tile.x][(int)tile.y].setContent(getTileContentById(current_id, (int)tile.x, (int)tile.y), false);
			
		}
		
	}
	
	//MouseX and MouseY
	public void removeTileContent(float xpos, float ypos) {
		Vector2f tile = getTileFromPosition(xpos, ypos);
		
		if(tiles[(int) tile.x][(int) tile.y].hasStreet()) {
			//is Street
			streetManager.removeStreet((int)tile.x, (int)tile.y);			
		}else {
			//is not Street
			tiles[(int) tile.x][(int) tile.y].removeContent();
		}
	}
	
	public static TileContent getTileContentById(int current_ID, int xtile, int ytile) {
		switch(current_ID) {
			case 0:
				return null;
			case 1:	
				return new HouseOne(xtile, ytile);
			case 2:
				return new HouseTwo(xtile, ytile);
			case 3:
				return new HouseThree(xtile, ytile);
			case 4:
				return new FactoryOne(xtile, ytile);
			case 5:
				return new OfficeOne(xtile, ytile);
			case 6:
				return new SupermarketOne(xtile, ytile);
			case 7:
				return new Bank(xtile, ytile);
			case 8:
				return new NatureFoundation(xtile, ytile, 3);
			default:
				return null;
				
		}
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
	
	public static Tile[][] getTileSystem(){
		return tiles;
	}
	
}
