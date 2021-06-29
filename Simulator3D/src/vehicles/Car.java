package vehicles;

import java.util.Random;

import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.ID;
import traffic.PathMarker;

public class Car extends Vehicle{

	public Car(PathMarker startMarker) {
		super(startMarker);
	}

	@Override
	protected ID setID() {
		return ID.CAR;
	}

	@Override
	protected float setMaxSpeed() {
		return 6;
	}

	@Override
	protected Mesh setMesh() {
		int i = new Random().nextInt(10);     
		switch(i) {
			case 0:
				return MeshContainer.car_1_black;
			case 1:
				return MeshContainer.car_1_gray;
			case 2:
				return MeshContainer.car_1_green;
			case 3:
				return MeshContainer.car_1_orange;
			case 4:
				return MeshContainer.car_1_white;
			case 5:
				return MeshContainer.car_2_brown;
			case 6:
				return MeshContainer.car_2_green;
			case 7:
				return MeshContainer.car_2_pink;
			case 8:
				return MeshContainer.car_2_red;
			case 9:
				return MeshContainer.truck_1_white;
			default:
				return null;
		}
	}

	@Override
	protected void renderContent(MasterRenderer renderer) {
		// TODO Auto-generated method stub
	}

}
