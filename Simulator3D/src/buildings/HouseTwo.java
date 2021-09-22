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

	@Override
	protected int setBuildingID() {
		// TODO Auto-generated method stub
		return 2;
	}

}
