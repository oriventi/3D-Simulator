package animations;

import renderEngine.DisplayManager;

public class Timer {

	private float time;
	private float maxTime;
	private boolean isPaused;
	
	public Timer(float maxTime) {
		time = 0;
		this.maxTime = maxTime;
		isPaused = true;
	}
	
	public void update() {
		if(!isPaused && time < maxTime ) {
			time += DisplayManager.getFrameTimeSeconds();
		}
	}
	
	public boolean timeReachedEnd() {
		if(time >= maxTime) {
			return true;
		}else {
			return false;
		}
	}
		
	public void start() {
		isPaused = false;
	}
	
	public void stop() {
		isPaused = true;
		time = 0;
	}
	
	public void resetTime() {
		time = 0;
		isPaused = true;
	}
	
	public void restart() {
		time = 0;
		isPaused = false;
	}
	
	public void pause() {
		isPaused = true;
	}
	
	public void resume() {
		isPaused = false;
	}
}
