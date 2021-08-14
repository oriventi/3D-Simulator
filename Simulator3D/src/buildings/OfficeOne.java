package buildings;

import models.Mesh;
import models.MeshContainer;

public class OfficeOne extends Building{

	public OfficeOne(int xtile, int ytile) {
		super(xtile, ytile);
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.office_1;
	}

	@Override
	protected int setRotation() {
		// TODO Auto-generated method stub
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
