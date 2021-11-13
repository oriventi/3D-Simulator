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
	
	/**
	 * walks through a function and can return value of current time
	 * @param runningTime = how long shall the actual animation be
	 * @param is the animation looped
	 * @param delayTime = time to wait before the animation starts
	 */
	public Animation(float runningTime, boolean isLooped, float delayTime) {
		this.runningTime = runningTime + delayTime;
		currentTime = 0;
		this.isLooped = isLooped;
		isPaused = true;
		id = setID();
		if(delayTime > 0.f) {
			delayTimer = new Timer(delayTime);
		}
	}
	
	/**
	 * updates the current time
	 */
	public void update() {
		if(!isPaused && currentTime < runningTime) {
			currentTime += DisplayManager.getFrameTimeSeconds();
		}else if(isLooped && !isPaused) {
			currentTime = 0;
		}
	}
	
	/**
	 * gets the id from subclass
	 * @return animation id
	 */
	protected abstract AnimationID setID();
	
	/**
	 * returns the current value
	 * @param variable which should be changed to the return value for the next step
	 * @return returns returns the new value of varToChange at the next time stamp
	 */
	public abstract float getCurrentValue(float varToChange);
	
	/**
	 * returns whether the delay before starting animation has finished
	 * @return true if delay timer has finished
	 */
	protected boolean hasDelayFinished() {
		if(delayTimer == null) {
			return true;
		}
		return delayTimer.timeReachedEnd();
	}
	
	/**
	 * starts the current animation
	 */
	public void startAnimation() {
		isPaused = false;
		AnimationTimerUpdater.addAnimation(this);
		if(delayTimer != null) {
			delayTimer.start();			
		}
	}
	
	/**
	 * stops the current animation and resets timer
	 */
	public void stopAnimation() {
		isPaused = true;
		currentTime = 0;
	}
	
	/**
	 * pauses animation
	 */
	public void pauseAnimation() {
		isPaused = true;
	}
	
	/**
	 * resumes paused animation
	 */
	public void resumeAnimation() {
		isPaused = false;
	}
	
	/**
	 * checks whether animation has finished
	 * @return true if animation has finished
	 */
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
	
	/**
	 * returns the animation id of the current animation
	 * @return this animation id
	 */
	public AnimationID getAnimationID() {
		return id;
	}
	
	/**
	 * destroys the animation
	 */
	public void destroy() {
		AnimationTimerUpdater.removeAnimation(this);
	}
}
