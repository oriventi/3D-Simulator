package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;
import traffic.MovingAction;
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
	protected void placeVehiclePathMarkers() {
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new MovingAction[] {
				new MovingAction(3, DrivingMode.STRAIGHT, Direction.LEFT),
				new MovingAction(1, DrivingMode.RIGHT, Direction.UP)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.UP)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new MovingAction[] {
				new MovingAction(3, DrivingMode.RIGHT, Direction.LEFT),
				new MovingAction(5, DrivingMode.LEFT, Direction.RIGHT)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.35f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.LEFT)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.65f, new MovingAction[] {
				new MovingAction(5, DrivingMode.STRAIGHT, Direction.RIGHT),
				new MovingAction(1, DrivingMode.LEFT, Direction.UP)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.65f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.RIGHT)
		}));	
	}

	@Override
	protected void placePeoplePathMarkers() {
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 1.f, 0.1f, new MovingAction[] {
				new MovingAction(1, DrivingMode.RIGHT, Direction.UP)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.UP)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.f, new MovingAction[] {
				new MovingAction(3, DrivingMode.RIGHT, Direction.LEFT)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.f, 0.1f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.LEFT)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.9f, new MovingAction[] {
				new MovingAction(5 , DrivingMode.STRAIGHT, Direction.RIGHT)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.9f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.RIGHT)
		}));
		
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
