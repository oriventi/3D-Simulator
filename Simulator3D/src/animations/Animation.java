package animations;

import renderEngine.DisplayManager;
import toolbox.EnumHolder.AnimationID;

public abstract class Animation {
	
	protected float runningTime;
	protected float currentTime;
	protected boolean isLooped;
	protected boolean isPaused;
	protected AnimationID id;
	protected Timer delayTimer;
	
	public Animation(float runningTime, boolean isLooped, float delayTime) {
		this.runningTime = runningTime + delayTime;
		currentTime = 0;
		this.isLooped = isLooped;
		isPaused = true;
		id = setID();
		delayTimer = new Timer(delayTime);
	}
	
	public void update() {
		if(!isPaused && currentTime < runningTime) {
			currentTime += DisplayManager.getFrameTimeSeconds();
		}else if(isLooped && !isPaused) {
			currentTime = 0;
		}
		delayTimer.update();
	}
	
	protected abstract AnimationID setID();
	
	public abstract float getCurrentValue(float varToChange);
	
	protected boolean hasDelayFinished() {
		return delayTimer.timeReachedEnd();
	}
	
	public void startAnimation() {
		isPaused = false;
		AnimationTimerUpdater.addAnimation(this);
		delayTimer.start();
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
	
	public void destroy() {
		AnimationTimerUpdater.removeAnimation(this);
	}
	
}
