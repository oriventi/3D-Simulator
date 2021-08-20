package animations;

import renderEngine.DisplayManager;

public abstract class Animation {
	
	protected float runningTime;
	protected float currentTime;
	protected boolean isLooped;
	protected boolean isPaused;
	
	public Animation(float runningTime, boolean isLooped) {
		this.runningTime = runningTime;
		currentTime = 0;
		this.isLooped = isLooped;
		isPaused = true;
	}
	
	public void update() {
		if(!isPaused && currentTime < runningTime) {
			currentTime += DisplayManager.getFrameTimeSeconds();
		}else if(isLooped && !isPaused) {
			currentTime = 0;
		}
	}
	
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
}
