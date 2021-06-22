package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import traffic.PathMarker;
import traffic.TrafficLightManager;

public class T_Junction extends Street{

	TrafficLightManager trafficLightManager;
	
	public T_Junction(int xtile, int ytile) {
		super(xtile, ytile);
		trafficLightManager = new TrafficLightManager(this);
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.t_junction;
	}

	@Override
	protected void placePathMarkers() {
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new int[] {3, -1, 1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new int[] {-1, -1, 3, -1, 5}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.35f, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.65f, new int[] {5, -1, -1, -1, 1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.65f, null));			
	}

	@Override
	protected int setRotation() {
		if(left && top && right) {
			return 0;
		}else if(top && right && bottom) {
			return -90;
		}else if(right && bottom && left) {
			return 180;
		}else {
			return 90;
		}
	}

	@Override
	protected void renderContent(MasterRenderer renderer) {
		trafficLightManager.render(renderer);
	}
	
	public void changeTrafficLights() {
		trafficLightManager.changeTrafficLights();
	}

}
