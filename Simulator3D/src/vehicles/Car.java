package vehicles;

import java.util.Random;

import animations.Animation;
import models.Mesh;
import models.MeshContainer;
import renderEngine.MasterRenderer;
import toolbox.EnumHolder.MovingEntityID;
import traffic.PathMarker;

public class Car extends MovingEntity{

	public Car(PathMarker startMarker) {
		super(startMarker, true);
	}

	@Override
	protected MovingEntityID setID() {
		return MovingEntityID.CAR;
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

	@Override
	protected int setMeshRotation() {
		// TODO Auto-generated method stub
		return 90;
	}

	@Override
	protected Animation setStartingAnimation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Animation setMovingAnimation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Animation setStoppingAnimation() {
		// TODO Auto-generated method stub
		return null;
	}

}
