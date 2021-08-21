package animations;

import renderEngine.DisplayManager;
import toolbox.EnumHolder.AnimationID;

public abstract class Animation {
	
	protected float runningTime;
	protected float currentTime;
	protected boolean isLooped;
	protected boolean isPaused;
	protected AnimationID id;
	
	public Animation(float runningTime, boolean isLooped) {
		this.runningTime = runningTime;
		currentTime = 0;
		this.isLooped = isLooped;
		isPaused = true;
		id = setID();
	}
	
	public void update() {
		if(!isPaused && currentTime < runningTime) {
			currentTime += DisplayManager.getFrameTimeSeconds();
		}else if(isLooped && !isPaused) {
			currentTime = 0;
		}
	}
	
	protected abstract AnimationID setID();
	
	public abstract float getCurrentValue();
	
	
	public void startAnimation() {
		isPaused = false;
	}
	
	public void stopAnimation() {
		isPaused = true;
		currentTime = 0;
	}
	
	public void pauseAnimation() {
		isPaused = true;
	}
	
	public void resumeAnimation() {
		isPaused = false;
	}
	
	public boolean isFinished() {
		if(isLooped) {
			return false;
		}else {
			if(currentTime >= runningTime) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	public AnimationID getAnimationID() {
		return id;
	}
	
}
