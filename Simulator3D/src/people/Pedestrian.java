package people;

import animations.Animation;
import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.ID;
import traffic.PathMarker;
import vehicles.MovingEntity;

public class Pedestrian extends MovingEntity{

	public Pedestrian(PathMarker startMarker) {
		super(startMarker, false);
	}

	@Override
	protected ID setID() {
		return ID.PEDESTRIAN;
	}

	@Override
	protected float setMaxSpeed() {
		return 2;
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.man_1;
	}

	@Override
	protected int setMeshRotation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void renderContent(MasterRenderer renderer) {
		// TODO Auto-generated method stub
		
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
