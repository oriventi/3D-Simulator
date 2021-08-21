package animations;

import toolbox.EnumHolder.AnimationID;

public class WalkingAnimation extends Animation{

	public WalkingAnimation(float runningTime) {
		super(runningTime, true);
		
	}

	@Override
	public AnimationID setID() {
		
		return AnimationID.WALKING;
	}
	
	@Override
	public float getCurrentValue() {
		
		return (float) Math.sin((3.14f / 1.5f) * currentTime);
	}

}
