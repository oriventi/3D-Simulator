package traffic;

import toolbox.EnumHolder.Direction;
import toolbox.EnumHolder.DrivingMode;

public class MovingAction {
	
	private int destination;
	private DrivingMode mode;
	private Direction direction;
	
	public MovingAction(int destination, DrivingMode mode, Direction direction) {
		this.destination = destination;
		this.mode = mode;
		this.direction = direction;
	}
	
	public void updateDirectionByStreetRotation(int currentReps, int finalReps) {
		currentReps += 1;
		switch(direction) {
			case UP:
				direction = Direction.RIGHT;
				break;
			case RIGHT:
				direction = Direction.DOWN;
				break;
			case DOWN:
				direction = Direction.LEFT;
				break;
			case LEFT:
				direction = Direction.UP;
				break;
			default:
				break;
		}
		if(currentReps < finalReps) {
			updateDirectionByStreetRotation(currentReps, finalReps);
		}
	}
	
	public int getDestinationIndex() {
		return destination;
	}
	
	public DrivingMode getMode() {
		return mode;
	}
	
	public Direction getDirection() {
		return direction;
	}
}
