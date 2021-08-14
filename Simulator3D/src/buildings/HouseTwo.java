package buildings;

import models.Mesh;
import models.MeshContainer;

public class HouseTwo extends Building{

	public HouseTwo(int xtile, int ytile) {
		super(xtile, ytile);
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.house_2;
	}

	@Override
	protected int setRotation() {
		// TODO Auto-generated method stub
		return 0;
	}

}
