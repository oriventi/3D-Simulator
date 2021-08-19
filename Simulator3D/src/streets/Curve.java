package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;
import traffic.MovingAction;
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
	protected void placeVehiclePathMarkers() {
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new MovingAction[] {
				new MovingAction(1, DrivingMode.RIGHT, Direction.UP)
		}));	
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.UP)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new MovingAction[] {
				new MovingAction(3, DrivingMode.STRAIGHT, Direction.DOWN)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.4f, new MovingAction[] {
				new MovingAction(4, DrivingMode.LEFT, Direction.RIGHT)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.6f, 0.65f, new MovingAction[] {
				new MovingAction(5, DrivingMode.STRAIGHT, Direction.RIGHT)
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
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.0f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.UP)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.1f, new MovingAction[] {
				new MovingAction(3, DrivingMode.STRAIGHT, Direction.DOWN)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.8f, new MovingAction[] {
				new MovingAction(4, DrivingMode.LEFT, Direction.RIGHT)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.2f, 0.9f, new MovingAction[] {
				new MovingAction(5, DrivingMode.STRAIGHT, Direction.RIGHT)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.9f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.RIGHT)
		}));
	}

	@Override
	protected int setRotation() {
		if(top && right) {
			return 0;
		}else if(right && bottom) {
			return 270;
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
	public void destroyContent() {
		// TODO Auto-generated method stub
		
	}

}
