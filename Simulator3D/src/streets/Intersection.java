package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import traffic.PathMarker;
import traffic.TrafficLightManager;

public class Intersection extends Street{

	TrafficLightManager trafficLightManager;
	
	public Intersection(int xtile, int ytile) {
		super(xtile, ytile);
		trafficLightManager = new TrafficLightManager(this);
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.intersection;
	}

	@Override
	protected void placePathMarkers() {
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new int[] {3, -1, 1, -1, 5}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new int[] {5, -1, 3, -1, 7}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.35f, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.65f, new int[] {7, -1, 5, -1, 1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.9f, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.9f, new int[] {1, -1, 7, -1, 3}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.65f, null));			
	}

	@Override
	protected int setRotation() {
		return 0;
	}

	@Override
	protected void renderContent(MasterRenderer renderer) {
		// TODO Auto-generated method stub
		trafficLightManager.render(renderer);
	}
	
	public void changeTrafficLights() {
		trafficLightManager.changeTrafficLights();
	}

}
