package World;

import org.lwjgl.input.Keyboard;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import mainPackage.MainGameLoop;
import models.Mesh;
import models.MeshContainer;
import models.RawModel;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import textures.Color;
import textures.ModelTexture;
import toolbox.Maths;
import toolbox.MousePicker;
import worldEntities.Car;
import traffic.StreetManager;

public class TileManager {

	public static int size; //how many tiles?
	public static  int tsize; //Tile size
	public static int wsize; //World size
	int current_id;
	private boolean street_mode;
	private Vector2f mouseTile = new Vector2f();
	
	private Tile[][] tiles;
	private StreetManager streetManager;
	
	private Entity selector;

	public TileManager(int size, int wsize, Loader loader) {
		this.size = size;
		this.wsize = wsize;
		this.tsize = wsize / size;
		
		initTiles();
		
		streetManager = new StreetManager();
		
		selector = new Entity(generateSelectorMesh(), new Vector3f(0, 0, 0), 0, 0, 0, 1);
		
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
	
	private Mesh getMeshById() {
		switch(current_id) {
			case 0:
				return null;
			case 1:	
				return MeshContainer.house_1;
			case 2:
				return MeshContainer.house_2;
			case 3:
				return MeshContainer.house_3;
			case 4:
				return MeshContainer.factory_1;
			case 5:
				return MeshContainer.office_1;
			case 6:
				return MeshContainer.supermarket_1;
			default:
				return null;
				
		}
	}
	
	public void update(float tslf, float mousex, float mousey) {
		checkInput(mousex, mousey);
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
		}
		
		 if(Mouse.isButtonDown(0)) {
			 setTileContent(mousex, mousey);			 
		 }else if(Mouse.isButtonDown(1)) {
			 removeTileContent(mousex, mousey);		 
		 }
	}
	
	public void render(MasterRenderer renderer, MousePicker picker, Camera cam) {
		drawTileContent(renderer);
		drawSelector(renderer, picker, cam);
		streetManager.render(renderer);

	}
	
	//MouseX and MouseY
	public void setTileContent(float xpos, float ypos) {
		Vector2f current_tile = Maths.getTileFromPosition(xpos, ypos);
		if(street_mode) {
			streetManager.addStreet((int)current_tile.x, (int)current_tile.y);
			tiles[(int)current_tile.x][(int)current_tile.y].setStreet(true);
		}else {
			
			tiles[(int)current_tile.x][(int)current_tile.y].setContent(new Entity(getMeshById(), 
					new Vector3f(current_tile.x * tsize - wsize / 2 + tsize / 2, 0, current_tile.y * tsize - wsize / 2 + tsize / 2),
					0, 0, 0, 1), false);
			
		}
		
	}
	
	//MouseX and MouseY
	public void removeTileContent(float xpos, float ypos) {
		Vector2f current_tile = Maths.getTileFromPosition(xpos, ypos);
		
		if(tiles[(int) current_tile.x][(int) current_tile.y].hasStreet()) {
			//is Street
			streetManager.removeStreet((int)current_tile.x, (int)current_tile.y);
			tiles[(int)current_tile.x][(int)current_tile.y].setStreet(false);
			
		}else {
			//is not Street
			tiles[(int) current_tile.x][(int) current_tile.y].removeContent();
		}
	}
	
	//draws the content of all tiles
	private void drawTileContent(MasterRenderer renderer) {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(tiles[i][j].getContent() != null && !tiles[i][j].hasStreet()) {
					renderer.processEntity(tiles[i][j].getContent());
				}
			}
		}
	}
	//generates Selector
	private Mesh generateSelectorMesh() {

		float[] vertices = {
			0, 0, 0,
			TileManager.tsize, 0, 0,
			0, 0, TileManager.tsize,
			TileManager.tsize, 0, TileManager.tsize
		};
		
		float colx = (float) 2.5f / 8.f;
		float coly = (float) 3.5f / 8.f;
		
		float[] textureCoords = {
			colx, coly,
			colx, coly,
			colx, coly, 
			colx, coly
		};
		
		int[] indices = {
				0, 2, 1, 1, 2, 3
		};
		
		float[] normals = {
			0, 1, 0,
			0, 1, 0
		};
		
		RawModel model = MainGameLoop.loader.loadToVAO(vertices, textureCoords, normals, indices);
		return new Mesh(model, new Color(255, 96, 70));
		
		
	}
	//Draws selector on grid, vec contains x and y of the tile
	private void drawSelector(MasterRenderer renderer, MousePicker picker, Camera cam) {
		mouseTile = Maths.getTileFromMousePosition(picker, cam);
		selector.setPosition(mouseTile.x * tsize -wsize / 2, 0.5f, mouseTile.y * tsize - wsize / 2);
		renderer.processEntity(selector);
	}
	
}
