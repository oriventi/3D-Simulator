package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;
import traffic.MovingAction;
import traffic.PathMarker;

public class Forward extends Street{

	
	public Forward(int xtile, int ytile) {
		super(xtile, ytile);
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.forward;
	}

	@Override
	protected void placeVehiclePathMarkers() {
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.9f, new MovingAction[] {
				new MovingAction(1, DrivingMode.STRAIGHT, Direction.UP)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, new MovingAction[] {
						new MovingAction(-1, DrivingMode.INVALID, Direction.UP)
				}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new MovingAction[] {
				new MovingAction(3, DrivingMode.STRAIGHT, Direction.DOWN)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.9f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.DOWN)
		}));
	}

	@Override
	protected void placePeoplePathMarkers() {
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.9f, new MovingAction[] {
				new MovingAction(1, DrivingMode.STRAIGHT, Direction.UP)
		}));	
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.1f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.UP)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.1f, new MovingAction[] {
				new MovingAction(3, DrivingMode.STRAIGHT, Direction.DOWN)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.9f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.DOWN)
		}));
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
		
	}

	@Override
	public void destroyContent() {
		// TODO Auto-generated method stub
		
	}	
}
