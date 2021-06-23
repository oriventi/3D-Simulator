
package models;

import java.util.Random;

import World.TileManager;
import mainPackage.MainGameLoop;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.Color;
import textures.ModelTexture;

public class MeshContainer {

	//assets
	public static Mesh cube;
	public static Mesh cube_red;
	public static Mesh traffic_light;
	public static Mesh street_lamp;
	
	//buildings
	public static Mesh factory_1;
	public static Mesh house_1;
	public static Mesh house_2;
	public static Mesh house_3;
	public static Mesh office_1;
	public static Mesh supermarket_1;
	
	//cars
	public static Mesh car_1_black;
	public static Mesh car_1_gray;
	public static Mesh car_1_green;
	public static Mesh car_1_orange;
	public static Mesh car_1_white;
	public static Mesh car_2_brown;
	public static Mesh car_2_green;
	public static Mesh car_2_pink;
	public static Mesh car_2_red;
	public static Mesh truck_1_white;
	
	//Streets
	public static Mesh no_connection;
	public static Mesh blind_alley;
	public static Mesh straight;
	public static Mesh curve;
	public static Mesh t_junction;
	public static Mesh intersection;
		
	private Loader loader = new Loader();

	
	public MeshContainer(Loader loader) {
		
		//assets
		cube = new Mesh("assets/cube", loader);
		cube_red = new Mesh("assets/cube_red", loader);
		traffic_light = new Mesh("assets/traffic_light", loader);
		street_lamp = new Mesh("assets/street_lamp", loader);
		
		//buildings
		factory_1 = new Mesh("houses/factory_1", loader);
		house_1 = new Mesh("houses/house_1", loader);
		house_2 = new Mesh("houses/house_2", loader);
		house_3 = new Mesh("houses/house_3", loader);
		office_1 = new Mesh("houses/office_1", loader);
		supermarket_1 = new Mesh("houses/supermarket_1", loader);
		
		//cars
		car_1_black = new Mesh("cars/standard_car_black", loader);
		car_1_gray = new Mesh("cars/standard_car_gray", loader);
		car_1_green = new Mesh("cars/standard_car_green", loader);
		car_1_orange = new Mesh("cars/standard_car_orange", loader);
		car_1_white = new Mesh("cars/standard_car_white", loader);
		car_2_brown = new Mesh("cars/car_2_brown", loader);
		car_2_green = new Mesh("cars/car_2_green", loader);
		car_2_pink = new Mesh("cars/car_2_pink", loader);
		car_2_red = new Mesh("cars/car_2_red", loader);
		truck_1_white = new Mesh("cars/truck_1_white", loader);
		
		//Streets
		no_connection = new Mesh("streets/no_connection", loader);
		blind_alley = new Mesh("streets/blind_alley", loader);
		straight = new Mesh("streets/straight", loader);
		curve = new Mesh("streets/curve", loader);
		t_junction = new Mesh("streets/t_junction", loader);
		intersection = new Mesh("streets/intersection", loader);

	}
	
	
	
	public static Mesh getMeshById(int current_ID) {
		switch(current_ID) {
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
	
	//generates Selector
	public static Mesh generateSelectorMesh() {

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
}
