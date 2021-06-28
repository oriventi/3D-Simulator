package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;
import traffic.DrivingAction;
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
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.35f, new DrivingAction[] {
				new DrivingAction(1, DrivingMode.RIGHT, Direction.UP)
		}));
				//, new int[] {-1, -1, 1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f, new DrivingAction[] {
				new DrivingAction(-1, DrivingMode.INVALID, Direction.UP)
		}));
				//, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new DrivingAction[] {
				new DrivingAction(3, DrivingMode.STRAIGHT, Direction.DOWN)
		}));
				//, new int[] {3, -1, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.4f, new DrivingAction[] {
				new DrivingAction(4, DrivingMode.LEFT, Direction.RIGHT)
		}));
				//, new int[] {-1, 4, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.6f, 0.65f, new DrivingAction[] {
				new DrivingAction(5, DrivingMode.STRAIGHT, Direction.RIGHT)
		}));
				//, new int[] {5, -1, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.9f, 0.65f, new DrivingAction[] {
				new DrivingAction(-1, DrivingMode.INVALID, Direction.RIGHT)
		}));
				//, null));
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
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
