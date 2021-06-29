package vehicles;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.ID;
import traffic.PathMarker;

public class Truck extends Vehicle{

	public Truck(PathMarker startMarker) {
		super(startMarker);
	}

	@Override
	protected ID setID() {
		return ID.TRUCK;
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

}
