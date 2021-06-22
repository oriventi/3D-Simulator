package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import traffic.PathMarker;

public class Blind_Alley extends Street{

	public Blind_Alley(int xtile, int ytile) {
		super(xtile, ytile);
		
	}
	
	@Override
	protected Mesh setMesh() {
		return MeshContainer.blind_alley;
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
	protected int setRotation() {
		if(top) {
			return 180;
		}else if(left) {
			return -90;
		}else if(right) {
			return 90;
		}else {
			return 0;
		}
	}

	@Override
	public void renderContent(MasterRenderer renderer) {
		
	}


}
