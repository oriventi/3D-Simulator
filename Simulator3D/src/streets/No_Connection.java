package streets;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;

public class No_Connection extends Street{

	public No_Connection(int xtile, int ytile) {
		super(xtile, ytile);

	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.no_connection;
	}

	@Override
	protected int setRotation() {
		return 0;
	}

	@Override
	protected void renderContent(MasterRenderer renderer) {
		
	}

	@Override
	public void destroyContent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void placeVehiclePathMarkers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void placePeoplePathMarkers() {
		// TODO Auto-generated method stub
		
	}

}
