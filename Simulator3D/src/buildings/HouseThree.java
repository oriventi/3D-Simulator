package buildings;

import models.Mesh;
import models.MeshContainer;

public class HouseThree extends Building{

	public HouseThree(int xtile, int ytile) {
		super(xtile, ytile);
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.house_3;
	}

	@Override
	protected int setRotation() {
		return 0;
	}

}
