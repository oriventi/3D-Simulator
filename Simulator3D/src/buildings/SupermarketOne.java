package buildings;

import models.Mesh;
import models.MeshContainer;

public class SupermarketOne extends Building{

	public SupermarketOne(int xtile, int ytile) {
		super(xtile, ytile);
	}

	@Override
	protected Mesh setMesh() {
		return MeshContainer.supermarket_1;
	}

	@Override
	protected int setRotation() {
		// TODO Auto-generated method stub
		return 0;
	}

}
