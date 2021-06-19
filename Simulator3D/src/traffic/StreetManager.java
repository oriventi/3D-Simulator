package traffic;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import models.Mesh;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;

public class StreetManager {

	private Street[][] streetsystem;
	private List<Integer> streetlist; // contains information about where in the matrix is a street
	private Loader loader;
	private int size;
	private int wsize;
	
	private float trafficLightTimer = 0;
	
	public StreetManager(int size, int wsize,  Loader loader) {
		streetsystem = new Street[size][size];
		this.loader = loader;
		this.size = size;
		this.wsize = wsize;
		streetlist = new ArrayList<>();
	}
	
	public void renderPathMarkers(MasterRenderer renderer) {
		for(int i = 0; i < streetsystem.length; i++) {
			for(int j = 0; j < streetsystem.length; j++) {
				if(streetsystem[i][j] != null) {
					streetsystem[i][j].renderPathMarkers(renderer);
				}
			}
		}
	}
	
	public void renderTrafficLights(MasterRenderer renderer) {
		for(int i = 0; i < streetsystem.length; i++) {
			for(int j = 0; j < streetsystem.length; j++) {
				if(streetsystem[i][j] != null) {
					streetsystem[i][j].renderTrafficLights(renderer);
				}
			}
		}
	}
	
	public void updateTrafficLightTimer() {
		trafficLightTimer += DisplayManager.getFrameTimeSeconds();
		if(trafficLightTimer >= 10) {
			trafficLightTimer = 0;
			for(int i = 0; i < streetsystem.length; i++) {
				for(int j = 0; j < streetsystem.length; j++) {
					if(streetsystem[i][j] != null) {
						streetsystem[i][j].changeTrafficLights();
					}
				}
			}
		}
	}
	
	private void updateStreetsAround(int xtile, int ytile) {
		//update top street
		if(hasTop(xtile, ytile)) {
			streetsystem[xtile][ytile - 1].update(false, false, true, false);
		}
		//update right street
		if(hasRight(xtile, ytile)) {
			streetsystem[xtile + 1][ytile].update(false, false, false, true);
		}
		//update bottom street
		if(hasBottom(xtile, ytile)) {
			streetsystem[xtile][ytile + 1].update(true, false, false, false);
		}
		//update left street
		if(hasLeft(xtile, ytile)) {
			streetsystem[xtile - 1][ytile].update(false, true, false, false);
		}	
		
	}
	
	public Entity getStreetEntity(int xtile, int ytile) {
		return streetsystem[xtile][ytile].generateEntity();
	}
	
	public void addStreet(int xtile, int ytile) {
		if(xtile < 50 && ytile < 50) {
			if(streetsystem[xtile][ytile] == null) {
				boolean top = hasTop(xtile, ytile);
				boolean right = hasRight(xtile, ytile);
				boolean bottom = hasBottom(xtile, ytile);
				boolean left = hasLeft(xtile, ytile);
				streetsystem[xtile][ytile] = new Street(xtile, ytile, top, right, bottom, left, loader, size, wsize);
				updateStreetsAround(xtile, ytile);
				streetlist.add(xtile);
				streetlist.add(ytile);
			}
		}
	}
	
	public void removeStreet(int xtile, int ytile) {
		if(xtile < 50 && ytile < 50) {
			if(!streetlist.isEmpty() && streetsystem[xtile][ytile] != null) {
				streetlist.remove(streetlist.indexOf(xtile));
				streetlist.remove(streetlist.indexOf(ytile));
			}
			if(streetsystem[xtile][ytile] != null) {
				streetsystem[xtile][ytile] = null;
				updateStreetsAround(xtile, ytile);
			}
		}
	}
	
	public Street[][] getStreetSystem() {
		return streetsystem;
	}
	
	public boolean hasTop(int xtile, int ytile) {
		if(ytile - 1 >= 0) {
			if(streetsystem[xtile][ytile - 1] != null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasRight(int xtile, int ytile) {
		if(xtile + 1 < size) {
			if(streetsystem[xtile + 1][ytile] != null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasBottom(int xtile, int ytile) {
		if(ytile + 1 < size) {
			if(streetsystem[xtile][ytile + 1] != null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasLeft(int xtile, int ytile) {
		if(xtile - 1 >= 0) {
			if(streetsystem[xtile - 1][ytile] != null) {
				return true;
			}
		}
		return false;
	}
	
	
	public Entity getTopEntity(int xtile, int ytile) {
		if(hasTop(xtile, ytile)) {
			return streetsystem[xtile][ytile - 1].generateEntity();
		}
		return null;
	}
	
	public Entity getRightEntity(int xtile, int ytile) {
		if(hasRight(xtile, ytile)) {
			return streetsystem[xtile + 1][ytile].generateEntity();
		}
		return null;
	}
	
	public Entity getBottomEntity(int xtile, int ytile) {
		if(hasBottom(xtile, ytile)) {
			return streetsystem[xtile][ytile + 1].generateEntity();
		}
		return null;
	}
	
	public Entity getLeftEntity(int xtile, int ytile) {
		if(hasLeft(xtile, ytile)) {
			return streetsystem[xtile - 1][ytile].generateEntity();
		}
		return null;
	}
	
	public Street getTopStreet(int xtile, int ytile) {
		return streetsystem[xtile][ytile - 1];
	}
	
	public Street getRightStreet(int xtile, int ytile) {
		return streetsystem[xtile + 1][ytile];
	}
	
	public Street getBottomStreet(int xtile, int ytile) {
		return streetsystem[xtile][ytile + 1];
	}
	
	public Street getLeftStreet(int xtile, int ytile) {
		return streetsystem[xtile - 1][ytile];
	}
	
}
