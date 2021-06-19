package World;

import org.lwjgl.input.Keyboard;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import models.Mesh;
import models.MeshContainer;
import models.RawModel;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import textures.Color;
import textures.ModelTexture;
import toolbox.MousePicker;
import worldEntities.Car;
import traffic.StreetManager;

public class TileManager {

	private Tile[][] tiles;
	private StreetManager streetManager;
	public static int size; //how many tiles?
	public static  int tsize; //tilesize
	public static int wsize; //worldsize
	private Loader loader;
	private Mesh selectormesh;
	private Entity selector;
	private float clicktimer;
	
	int current_id;
	private boolean street_mode;
	

	public TileManager(int size, int wsize, Loader loader) {
		this.size = size;
		this.wsize = wsize;
		this.tsize = wsize / size;
		this.loader = loader;
		tiles = new Tile[size][size];
		initTiles();
		
		streetManager = new StreetManager(size, wsize, loader);
		
		selectormesh = generateSelectorMesh();
		selector = new Entity(selectormesh, new Vector3f(0, 0, 0), 0, 0, 0, 1);
		street_mode = true;
		current_id = 0;
	}
	
	//initializes tile array
	private void initTiles() {
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
		clicktimer += tslf;
		checkInput(mousex, mousey);
		streetManager.updateTrafficLightTimer();
	}
	
	private void checkInput(float mousex, float mousey) {
		if(Keyboard.isKeyDown(Keyboard.KEY_0)) {
			street_mode = true;
			current_id = 0;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_1)) {
			street_mode = false;
			current_id = 1;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_2)) {
			street_mode = false;
			current_id = 2;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_3)) {
			street_mode = false;
			current_id = 3;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_4)) {
			street_mode = false;
			current_id = 4;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_5)) {
			street_mode = false;
			current_id = 5;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_6)) {
			street_mode = false;
			current_id = 6;
		}
		
		 if(Mouse.isButtonDown(0)) {
			 setTileContent(mousex, mousey, getMeshById());			 
		 }else if(Mouse.isButtonDown(1)) {
			 removeTileContent(mousex, mousey);		 
		 }else if(Mouse.isButtonDown(2) && clicktimer >= 0.2f) {
			 tiles[(int)getTile(mousex, mousey).x][(int)getTile(mousex, mousey).y].rotateEntity();
			 clicktimer = 0;
		 }
	}
	
	public void render(MasterRenderer renderer, MousePicker picker, Camera cam) {
		drawTileContent(renderer);
		drawSelector(renderer, picker, cam);
	}
	
	//MouseX and MouseY
	public void setTileContent(float xpos, float ypos, Mesh mesh) {
		Vector2f current_tile = getTile(xpos, ypos);
		if(street_mode) {
			streetManager.removeStreet((int)current_tile.x, (int)current_tile.y);
			streetManager.addStreet((int)current_tile.x, (int)current_tile.y);
			tiles[(int)current_tile.x][(int)current_tile.y].removeContent();
			tiles[(int)current_tile.x][(int)current_tile.y].setContent(streetManager.getStreetEntity((int)current_tile.x,
					(int)current_tile.y), true);
			updateStreetEntitiesAround((int)current_tile.x, (int)current_tile.y);
			
		}else {
			
			tiles[(int)current_tile.x][(int)current_tile.y].setContent(new Entity(mesh, 
					new Vector3f(current_tile.x * tsize - wsize / 2 + tsize / 2, 0, current_tile.y * tsize - wsize / 2 + tsize / 2),
					0, 0, 0, 1), false);
			
		}
		
	}
	
	//MouseX and MouseY
	public void removeTileContent(float xpos, float ypos) {
		Vector2f current_tile = getTile(xpos, ypos);
		
		if(tiles[(int) current_tile.x][(int) current_tile.y].hasStreet()) {
			//is Street
			streetManager.removeStreet((int)current_tile.x, (int)current_tile.y);
			tiles[(int) current_tile.x][(int) current_tile.y].removeContent();
			updateStreetEntitiesAround((int)current_tile.x, (int)current_tile.y);
		}else {
			//is not Street
			tiles[(int) current_tile.x][(int) current_tile.y].removeContent();
		}
	}
	
	//updates streets entities around a specific tile
	private void updateStreetEntitiesAround(int xtile, int ytile) {
		//top
		if(streetManager.getTopEntity(xtile, ytile) != null && ytile - 1 >= 0) {
			tiles[xtile][ytile - 1].removeContent();
			tiles[xtile][ytile - 1].setContent(streetManager.getTopEntity(xtile, ytile), true);
		}
		//right
		if(streetManager.getRightEntity(xtile, ytile) != null && xtile + 1 < size) {
			tiles[xtile + 1][ytile].removeContent();
			tiles[xtile + 1][ytile].setContent(streetManager.getRightEntity(xtile, ytile), true);
		}
		//bottom
		if(streetManager.getBottomEntity(xtile, ytile) != null && ytile + 1 < size) {
			tiles[xtile][ytile + 1].removeContent();
			tiles[xtile][ytile + 1].setContent(streetManager.getBottomEntity(xtile, ytile), true);
		}
		//left
		if(streetManager.getLeftEntity(xtile, ytile)  != null && xtile - 1 >= 0) {
			tiles[xtile - 1][ytile].removeContent();
			tiles[xtile - 1][ytile].setContent(streetManager.getLeftEntity(xtile, ytile) , true);
		}
	}
	
	
	//draws the content of all tiles
	private void drawTileContent(MasterRenderer renderer) {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(tiles[i][j].getContent() != null) {
					renderer.processEntity(tiles[i][j].getContent());
				}
			}
		}
		//streetManager.renderPathMarkers(renderer);
		streetManager.renderTrafficLights(renderer);
	}
	
	//Draws selector on grid, vec contains x and y of the tile
	private void drawSelector(MasterRenderer renderer, MousePicker picker, Camera cam) {
		Vector2f current_tile = getTile(picker, cam);
		selector.setPosition(current_tile.x * tsize -wsize / 2, 0.5f, current_tile.y * tsize - wsize / 2);
		renderer.processEntity(selector);
	}
	
	
	//generates Selector
	private Mesh generateSelectorMesh() {

		float[] vertices = {
			0, 0, 0,
			tsize, 0, 0,
			0, 0, tsize,
			tsize, 0, tsize
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
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, normals, indices);
		return new Mesh(model, new Color(255, 96, 70));
		
		
	}
	
	//returns tile out of xpos and zpos
	public Vector2f getTile(float x, float z) {
		int tilex = (int)((x + wsize / 2) / tsize);
		int tiley = (int)((z + wsize / 2) / tsize);
		
		if(tilex < 0) {
			tilex = 0;
		}
		if(tiley < 0) {
			tiley = 0;
		}
		if(tilex > size - 1) {
			tilex = size - 1;
		}
		if(tiley > size - 1) {
			tiley = size -1 ;
		}
		
		return new Vector2f(tilex, tiley);
		
	}
	
	//returns tile from mousePosition
	public Vector2f getTile(MousePicker picker, Camera cam) {
		int x = (int) picker.getPosition(cam).x;
		int z = (int) picker.getPosition(cam).z;
		int tilex = (int)((x + wsize / 2) / tsize);
		int tiley = (int)((z + wsize / 2) / tsize);
		
		if(tilex < 0) {
			tilex = 0;
		}
		if(tiley < 0) {
			tiley = 0;
		}
		if(tilex > size - 1) {
			tilex = size - 1;
		}
		if(tiley > size - 1) {
			tiley = size -1 ;
		}
		
		//Output XTile and YTile from Mouse hovered
		//System.out.println(tilex + " + " + tiley);
		
		return new Vector2f(tilex, tiley);
		
	}
	
	public StreetManager getStreetManager() {
		return streetManager;
	}
	
}
