package toolbox;

public class EnumHolder {

	public enum DrivingMode{
		STRAIGHT, LEFT, RIGHT, FULL_STOP, INVALID
	}
	
	public enum Direction{
		UP, DOWN, RIGHT, LEFT, INVALID
	}
	
	public enum MovingEntityID{
		TRUCK, CAR, PEDESTRIAN, INVALID
	}
	
	public enum AnimationID{
		LINEAR, INVALID
	}
	
	public enum GameState{
		UI_MODE, GAME_MODE, INVALID
	}
	
}
