package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;
import traffic.DrivingAction;
import traffic.PathMarker;

public class Straight extends Street{

	
	public Straight(int xtile, int ytile) {
		super(xtile, ytile);
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.straight;
	}

	@Override
	protected void placePathMarkers() {
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.9f, new DrivingAction[] {
				new DrivingAction(1, DrivingMode.STRAIGHT, Direction.UP)
		}));
				//, new int[] {1, -1, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.65f, 0.1f
				, new DrivingAction[] {
						new DrivingAction(-1, DrivingMode.INVALID, Direction.UP)
				}));
				//, null));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.1f, new DrivingAction[] {
				new DrivingAction(3, DrivingMode.STRAIGHT, Direction.DOWN)
		}));
				//, new int[] {3, -1, -1, -1, -1}));
		pathMarkers.add(new PathMarker(xtile, ytile, 0.35f, 0.9f, new DrivingAction[] {
				new DrivingAction(-1, DrivingMode.INVALID, Direction.DOWN)
		}));
				//, null));		
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
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
