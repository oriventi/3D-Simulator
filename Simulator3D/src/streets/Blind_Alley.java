package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;
import traffic.MovingAction;
import traffic.PathMarker;

public class Blind_Alley extends Street{
	
	public Blind_Alley(int xtile, int ytile) {
		super(xtile, ytile);
	}
	
	@Override
	protected Mesh setMesh() {
		return MeshContainer.blind_alley;
	}

	@Override
	protected int setRotation() {
		if(top) {
			return 180;
		}else if(left) {
			return 270;
		}else if(right) {
			return 90;
		}else {
			return 0;
		}
	}

	@Override
	public void renderContent(MasterRenderer renderer) {
		
	}

	@Override
	public void destroyContent() {
		
	}

	@Override
	protected void placeVehiclePathMarkers() {
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.9f, new MovingAction[] {
				new MovingAction(1, DrivingMode.STRAIGHT, Direction.UP)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.45f, new MovingAction[] {
				new MovingAction(2, DrivingMode.LEFT, Direction.LEFT)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.5f, 0.3f, new MovingAction[] {
				new MovingAction(3, DrivingMode.LEFT, Direction.DOWN)
		}));
		vehiclePathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.45f, new MovingAction[] {
				new MovingAction(4, DrivingMode.STRAIGHT, Direction.DOWN)
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
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.2f, new MovingAction[] {
				new MovingAction(2, DrivingMode.LEFT, Direction.LEFT)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.8f, 0.1f, new MovingAction[] {
				new MovingAction(3, DrivingMode.STRAIGHT, Direction.LEFT)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.2f, 0.1f, new MovingAction[] {
				new MovingAction(4, DrivingMode.LEFT, Direction.DOWN)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.2f, new MovingAction[] {
				new MovingAction(5, DrivingMode.STRAIGHT, Direction.DOWN)
		}));
		peoplePathMarkers.add(new PathMarker(xtile, ytile, 0.1f, 0.9f, new MovingAction[] {
				new MovingAction(-1, DrivingMode.INVALID, Direction.DOWN)
		}));
		
	}


}
