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
	protected void placePathMarkers() {
		
	}

	@Override
	protected int setRotation() {
		return 0;
	}

	@Override
	protected void renderContent(MasterRenderer renderer) {
		
	}

}
