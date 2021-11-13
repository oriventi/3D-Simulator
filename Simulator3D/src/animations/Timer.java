package animations;

import renderEngine.DisplayManager;

/**
 * a timer calculates the seconds gone for each frame since the timer has been started until it reaches the given end time
 * @author oriventi
 *
 */
public class Timer {

	private float time;
	private float maxTime;
	private boolean isPaused;
	
	/**
	 * initializes the timer with the end time
	 * @param maxTime = when the timer has to end
	 */
	public Timer(float maxTime) {
		time = 0;
		this.maxTime = maxTime;
		isPaused = true;
		AnimationTimerUpdater.addTimer(this);
	}
	
	/**
	 * doesnt need to be called, works automatically
	 * timer updates its time for each frae
	 */
	public void update() {
		if(!isPaused && time < maxTime ) {
			time += DisplayManager.getFrameTimeSeconds();
		}
	}
	
	/**
	 * checks whether the current time reached the given end time
	 * @return true if timer reached end
	 */
	public boolean timeReachedEnd() {
		if(time >= maxTime) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * destroys the timer, needs to be called when not needed anymore
	 */
	public void destroy() {
		AnimationTimerUpdater.removeTimer(this);
	}
	
	/**
	 * returns the current time since timer has started
	 * @return current time since timer has started
	 */
	public float getCurrentTime() {
		return time;
	}
		
	/**
	 * starts the timer, needs to be called
	 */
	public void start() {
		isPaused = false;
	}
	
	/**
	 * stops and resets the timer
	 */
	public void stop() {
		isPaused = true;
		time = 0;
	}
	
	/**
	 * resets the timer
	 */
	public void resetTime() {
		time = 0;
		isPaused = true;
	}
	
	/**
	 * restarts the timer and sets its current time to 0
	 */
	public void restart() {
		time = 0;
		isPaused = false;
	}
	
	/**
	 * pauses the timers calculation
	 */
	public void pause() {
		isPaused = true;
	}
	
	/**
	 * resumes the paused timer
	 * no affect if timer is still running
	 */
	public void resume() {
		isPaused = false;
	}
}
