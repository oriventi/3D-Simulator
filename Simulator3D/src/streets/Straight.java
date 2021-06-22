package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import traffic.PathMarker;

public class Straight extends Street{

	public Straight(int xtile, int ytile) {
		super(xtile, ytile);

	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.straight;
	}

	@Override
	protected void placePathMarkers() {
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.9f, new int[] {1, -1, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new int[] {3, -1, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.9f, null));		
	}

	@Override
	protected int setRotation() {
		if(left && right) {
			return 90;
		}else{
			return 0;
		}
	}

	@Override
	protected void renderContent(MasterRenderer renderer) {
		// TODO Auto-generated method stub
		
	}


}
