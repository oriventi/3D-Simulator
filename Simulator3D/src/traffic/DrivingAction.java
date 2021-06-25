package traffic;

import toolbox.EnumHolder.DrivingMode;

public class DrivingAction {
	
	private int destination;
	private DrivingMode mode;
	
	public DrivingAction(int destination, DrivingMode mode) {
		this.destination = destination;
		this.mode = mode;
	}
	
	public int getDestinationIndex() {
		return destination;
	}
	
	public DrivingMode getMode() {
		return mode;
	}
}
