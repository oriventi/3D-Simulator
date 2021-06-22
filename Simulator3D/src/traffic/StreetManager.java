package traffic;

import java.util.ArrayList;

import java.util.List;

import World.TileManager;
import renderEngine.MasterRenderer;
import streets.Street;
import streets.T_Junction;
import streets.Blind_Alley;
import streets.Curve;
import streets.Intersection;
import streets.No_Connection;
import streets.Straight;

public class StreetManager {

	private static Street[][] streetsystem;
	private List<Integer> streetlist; // contains information about where in the matrix is a street
			
	public StreetManager() {
		streetsystem = new Street[TileManager.size][TileManager.size];
		streetlist = new ArrayList<>();
	}
	
	public void render(MasterRenderer renderer) {
		for(int i = 0; i < TileManager.size; i++) {
			for(int j = 0; j < TileManager.size; j++) {
				if(streetsystem[i][j] != null)
					streetsystem[i][j].render(renderer);
			}
		}
	}
	
	public void addStreet(int xtile, int ytile) {
		addStreet(xtile, ytile, true);
	}
	
	public void removeStreet(int xtile, int ytile) {
		removeStreet(xtile, ytile, true);
	}
	
	private void addStreet(int xtile, int ytile, boolean update) {
		if(streetsystem[xtile][ytile] == null) {
			placeCorrectStreet(xtile, ytile);
			streetlist.add(xtile);
			streetlist.add(ytile);
			if(update) //if other streets shall be updated only set true at new streets
				updateStreetsAround(xtile, ytile);
		}
	}
	
	private void removeStreet(int xtile, int ytile, boolean update) {
		System.out.println("HI");
		if(streetsystem[xtile][ytile] != null) {
			streetsystem[xtile][ytile] = null;
			streetlist.remove(streetlist.indexOf(xtile));
			streetlist.remove(streetlist.indexOf(ytile));
			if(update)
				updateStreetsAround(xtile, ytile);
		}
	}
	
	private void updateStreetsAround(int xtile, int ytile) {
		if(hasTop(xtile, ytile)) {
			removeStreet(xtile, ytile - 1, false);
			addStreet(xtile, ytile - 1, false);
		}
		if(hasRight(xtile, ytile)) {
			removeStreet(xtile + 1, ytile, false);
			addStreet(xtile + 1, ytile, false);
		}
		if(hasBottom(xtile, ytile)) {
			removeStreet(xtile, ytile + 1, false);
			addStreet(xtile, ytile + 1, false);
		}
		if(hasLeft(xtile, ytile)) {
			removeStreet(xtile - 1, ytile, false);
			addStreet(xtile - 1, ytile, false);
		}
	}
	
	private void placeCorrectStreet(int xtile, int ytile) {
		streetsystem[xtile][ytile] = null;
		int neighbors = getNeighbors(xtile, ytile);
		if(neighbors == 0) {
			streetsystem[xtile][ytile] = new No_Connection(xtile, ytile);
		}else if(neighbors == 1) {
			streetsystem[xtile][ytile] = new Blind_Alley(xtile, ytile);
		}else if(neighbors == 2) {
			if(hasLeft(xtile, ytile) && hasRight(xtile, ytile) || hasTop(xtile, ytile) && hasBottom(xtile, ytile)) {
				streetsystem[xtile][ytile] = new Straight(xtile, ytile);
			}else {
				streetsystem[xtile][ytile] = new Curve(xtile, ytile);
			}
		}else if(neighbors == 3) {
			streetsystem[xtile][ytile] = new T_Junction(xtile, ytile);
		}else {
			streetsystem[xtile][ytile] = new Intersection(xtile, ytile);
		}
		
	}
	
	private int getNeighbors(int xtile, int ytile) {
		int neighbors = 0;
		if(hasTop(xtile, ytile)) {
			neighbors += 1;
		}
		if(hasRight(xtile, ytile)) {
			neighbors += 1;
		}
		if(hasLeft(xtile, ytile)) {
			neighbors += 1;
		}
		if(hasBottom(xtile, ytile)) {
			neighbors += 1;
		}
		return neighbors;
	}
	
	
	public static boolean hasTop(int xtile, int ytile) {
		if(ytile - 1 >= 0) {
			if(streetsystem[xtile][ytile - 1] != null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasRight(int xtile, int ytile) {
		if(xtile + 1 < TileManager.size) {
			if(streetsystem[xtile + 1][ytile] != null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasBottom(int xtile, int ytile) {
		if(ytile + 1 < TileManager.size) {
			if(streetsystem[xtile][ytile + 1] != null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasLeft(int xtile, int ytile) {
		if(xtile - 1 >= 0) {
			if(streetsystem[xtile - 1][ytile] != null) {
				return true;
			}
		}
		return false;
	}
	
	public static Street getTopStreet(int xtile, int ytile) {
		if(hasTop(xtile, ytile))
			return streetsystem[xtile][ytile - 1];
		else
			return null;
	}
	
	public static Street getRightStreet(int xtile, int ytile) {
		if(hasRight(xtile, ytile))
			return streetsystem[xtile + 1][ytile];
		else
			return null;
	}
	
	public static Street getBottomStreet(int xtile, int ytile) {
		if(hasBottom(xtile, ytile))
			return streetsystem[xtile][ytile + 1];
		else
			return null;
	}
	
	public static Street getLeftStreet(int xtile, int ytile) {
		if(hasLeft(xtile, ytile))
			return streetsystem[xtile - 1][ytile];
		else
			return null;
	}
	
	public static Street[][] getStreetSystem() {
		return streetsystem;
	}
	
}