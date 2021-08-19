package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;
import traffic.MovingAction;
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
	protected void placeVehiclePathMarkers() {
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new MovingAction[] {
				new MovingAction(3, DrivingMode.STRAIGHT, Direction.LEFT),
				new MovingAction(1, DrivingMode.RIGHT, Direction.UP),
				new MovingAction(5, DrivingMode.LEFT, Direction.DOWN)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.UP)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new MovingAction[] {
				new MovingAction(5, DrivingMode.STRAIGHT, Direction.DOWN),
				new MovingAction(3, DrivingMode.RIGHT, Direction.LEFT),
				new MovingAction(7, DrivingMode.LEFT, Direction.RIGHT)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.35f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.LEFT)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.65f, new MovingAction[] {
				new MovingAction(7, DrivingMode.STRAIGHT, Direction.RIGHT),
				new MovingAction(5, DrivingMode.RIGHT, Direction.DOWN),
				new MovingAction(1, DrivingMode.LEFT, Direction.UP)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.9f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.DOWN)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.9f, new MovingAction[] {
				new MovingAction(1, DrivingMode.STRAIGHT, Direction.UP),
				new MovingAction(7, DrivingMode.RIGHT, Direction.RIGHT),
				new MovingAction(3, DrivingMode.LEFT, Direction.LEFT)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.65f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.RIGHT)
		}));	
	}

	@Override
	protected void placePeoplePathMarkers() {
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 1.f, 0.1f, new MovingAction[]{
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
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.f, 0.9f, new MovingAction[] {
				new MovingAction(5, DrivingMode.RIGHT, Direction.DOWN)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 1.f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.DOWN)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 1.f, new MovingAction[] {
				new MovingAction(7, DrivingMode.RIGHT, Direction.RIGHT)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 1.f, 0.9f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.RIGHT)
		}));
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
	public void destroyContent() {
		// TODO Auto-generated method stub
		trafficLightManager.destroy();
		
	}
	
	public void changeTrafficLights() {
		trafficLightManager.changeTrafficLights();
	}
}
