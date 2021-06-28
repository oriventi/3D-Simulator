package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;
import traffic.DrivingAction;
import traffic.PathMarker;

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
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new DrivingAction[] {
				new DrivingAction(3, DrivingMode.STRAIGHT, Direction.LEFT),
				new DrivingAction(1, DrivingMode.RIGHT, Direction.UP),
				new DrivingAction(5, DrivingMode.BIG_LEFT, Direction.DOWN)
		}));
				//, new int[] {3, -1, 1, -1, 5}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, new DrivingAction[] {
				new DrivingAction(-1, DrivingMode.INVALID, Direction.UP)
		}));
				//, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new DrivingAction[] {
				new DrivingAction(5, DrivingMode.STRAIGHT, Direction.DOWN),
				new DrivingAction(3, DrivingMode.RIGHT, Direction.LEFT),
				new DrivingAction(7, DrivingMode.BIG_LEFT, Direction.RIGHT)
		}));
				//, new int[] {5, -1, 3, -1, 7}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.35f, new DrivingAction[] {
				new DrivingAction(-1, DrivingMode.INVALID, Direction.LEFT)
		}));
				//, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.65f, new DrivingAction[] {
				new DrivingAction(7, DrivingMode.STRAIGHT, Direction.RIGHT),
				new DrivingAction(5, DrivingMode.RIGHT, Direction.DOWN),
				new DrivingAction(1, DrivingMode.BIG_LEFT, Direction.UP)
		}));
				//, new int[] {7, -1, 5, -1, 1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.9f, new DrivingAction[] {
				new DrivingAction(-1, DrivingMode.INVALID, Direction.DOWN)
		}));
				//, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.9f, new DrivingAction[] {
				new DrivingAction(1, DrivingMode.STRAIGHT, Direction.UP),
				new DrivingAction(7, DrivingMode.RIGHT, Direction.RIGHT),
				new DrivingAction(3, DrivingMode.BIG_LEFT, Direction.LEFT)
		}));
				//, new int[] {1, -1, 7, -1, 3}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.65f, new DrivingAction[] {
				new DrivingAction(-1, DrivingMode.INVALID, Direction.RIGHT)
		}));
				//, null));			
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
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public void changeTrafficLights() {
		trafficLightManager.changeTrafficLights();
	}

}
