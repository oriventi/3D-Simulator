package vehicles;

import animations.Animation;
import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.MovingEntityID;
import traffic.PathMarker;

public class Truck extends MovingEntity{

	public Truck(PathMarker startMarker) {
		super(startMarker, true);
	}

	@Override
	protected MovingEntityID setID() {
		return MovingEntityID.TRUCK;
	}

	@Override
	protected float setMaxSpeed() {
		return 5;
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.truck_1_white;
	}

	@Override
	protected void renderContent(MasterRenderer renderer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int setMeshRotation() {
		// TODO Auto-generated method stub
		return 90;
	}

	@Override
	protected Animation setStartingAnimation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Animation setMovingAnimation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Animation setStoppingAnimation() {
		// TODO Auto-generated method stub
		return null;
	}

}
