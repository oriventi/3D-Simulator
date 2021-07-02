package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;
import traffic.DrivingAction;
import traffic.PathMarker;

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
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new DrivingAction[] {
				new DrivingAction(3, DrivingMode.STRAIGHT, Direction.LEFT),
				new DrivingAction(1, DrivingMode.RIGHT, Direction.UP)
		}));
				//, new int[] {3, -1, 1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, new DrivingAction[] {
				new DrivingAction(-1, DrivingMode.INVALID, Direction.UP)
		}));
				//, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new DrivingAction[] {
				new DrivingAction(3, DrivingMode.RIGHT, Direction.LEFT),
				new DrivingAction(5, DrivingMode.BIG_LEFT, Direction.RIGHT)
		}));
				//, new int[] {-1, -1, 3, -1, 5}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.35f, new DrivingAction[] {
				new DrivingAction(-1, DrivingMode.INVALID, Direction.LEFT)
		}));
				//, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.65f, new DrivingAction[] {
				new DrivingAction(5, DrivingMode.STRAIGHT, Direction.RIGHT),
				new DrivingAction(1, DrivingMode.BIG_LEFT, Direction.UP)
		}));
				//, new int[] {5, -1, -1, -1, 1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.65f, new DrivingAction[] {
				new DrivingAction(-1, DrivingMode.INVALID, Direction.RIGHT)
		}));
				//, null));			
	}

	@Override
	protected int setRotation() {
		if(left && top && right) {
			return 0;
		}else if(top && right && bottom) {
			return 270;
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
	
	@Override
	public void destroyContent() {
		trafficLightManager.destroy();
		
	}
	
	public void changeTrafficLights() {
		trafficLightManager.changeTrafficLights();
	}

}
