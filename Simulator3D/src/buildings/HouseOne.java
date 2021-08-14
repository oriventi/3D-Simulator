package buildings;

import models.Mesh;
import models.MeshContainer;

public class HouseOne extends Building{

	public HouseOne(int xtile, int ytile) {
		super(xtile, ytile);
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.house_1;
	}

	@Override
	protected int setRotation() {
		return 0;
	}

	@Override
	protected int setXFoundationSize() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	protected int setYFoundationSize() {
		// TODO Auto-generated method stub
		return 1;
	}

}
