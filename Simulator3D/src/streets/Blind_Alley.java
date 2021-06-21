package streets;

import models.MeshContainer;
import renderEngine.MasterRenderer;
import traffic.PathMarker;

public class Blind_Alley extends Street{

	public Blind_Alley(int xtile, int ytile) {
		super(xtile, ytile);
		
	}
	
	@Override
	protected void setMesh() {
		mesh = MeshContainer.blind_alley;
	}

	@Override
	protected void placePathMarkers() {
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.9f, new int[] {1, -1, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.45f, new int[] {-1, -1, -1, 2, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.5f, 0.3f, new int[] {-1, -1, -1, 3, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.45f, new int[] {4, -1, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.9f, null));
	}

	@Override
	protected void setRotation() {
		//TODO CHANGE
		if(top) {
			rotation = 180;
		}else if(left) {
			rotation = -90;
		}else if(right) {
			rotation = 90;
		}else {
			rotation = 0;
		}
	}

	@Override
	protected void update() {
		
	}

	@Override
	public void render(MasterRenderer renderer) {
		
	}


}
