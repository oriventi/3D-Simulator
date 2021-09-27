package animations;

import renderEngine.DisplayManager;
import toolbox.EnumHolder.AnimationID;

public class LinearAnimation extends Animation{

	private int speed;
	private int breakBorder;
	
	public LinearAnimation(float runningTime, int speed, int breakBorder, float delayTime) {
		super(runningTime, false, delayTime);
		this.speed = speed;
		this.breakBorder = breakBorder;
	}

	@Override
	protected AnimationID setID() {
		return AnimationID.LINEAR;
	}

	@Override
	public float getCurrentValue(float varToChange) {
		if(hasDelayFinished()) {
			if(speed > 0) {
				if(varToChange < breakBorder) {
					return (varToChange += speed * DisplayManager.getFrameTimeSeconds());
				}else {
					return breakBorder;
				}
			}else {
				if(varToChange > breakBorder) {
					return (varToChange += speed * DisplayManager.getFrameTimeSeconds());
				}else {
					return breakBorder;
				}
			}
		}else {
			return varToChange;
		}
	}
	
	public boolean reachedBorder(int varToCheck) {
		return (varToCheck == breakBorder);
	}
	

}
