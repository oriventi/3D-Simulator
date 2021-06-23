package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import traffic.PathMarker;

public class Curve extends Street{

	public Curve(int xtile, int ytile) {
		super(xtile, ytile);
		
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.curve;
	}

	@Override
	protected void placePathMarkers() {
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new int[] {-1, -1, 1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new int[] {3, -1, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.4f, new int[] {-1, 4, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.6f, 0.65f, new int[] {5, -1, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.65f, null));
	}

	@Override
	protected int setRotation() {
		if(top && right) {
			return 0;
		}else if(right && bottom) {
			return -90;
		}else if(bottom && left) {
			return 180;
		}else {
			return 90;
		}
	}

	@Override
	protected void renderContent(MasterRenderer renderer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
