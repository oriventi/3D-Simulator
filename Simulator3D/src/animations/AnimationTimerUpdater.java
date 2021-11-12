package animations;

import java.util.ArrayList;
import java.util.List;

import toolbox.EnumHolder.AnimationID;

/**
 * updates each animation instance and each timer instance automatically
 * @author Oriventi
 *
 */
public class AnimationTimerUpdater {
	
	private static List<Animation> animationList;
	private static List<Timer> timerList;

	/**
	 * declares a list of animations and a list of timers
	 */
	public AnimationTimerUpdater() {
		animationList = new ArrayList<Animation>();
		timerList = new ArrayList<Timer>();
	}
	
	/**
	 * updates each instance of the animation list and each instance of the timer list
	 */
	public void update() {
		for(Animation animation : animationList) {
			animation.update();
		}
		for(Timer timer : timerList) {
			timer.update();
		}
	}
	
	/**
	 * adds a animation to the animation list
	 * @param animation to add
	 */
	public static void addAnimation(Animation animation) {
		animationList.add(animation);
	}
	
	/**
	 * removes an animation from the animation list
	 * @param animation to remove
	 */
	public static void removeAnimation(Animation animation) {
		animationList.remove(animation);
	}
	
	/**
	 * adds a timer to the timer list
	 * @param timer to add
	 */
	public static void addTimer(Timer timer) {
		timerList.add(timer);
	}
	
	/**
	 * removes a timer from the timer list
	 * @param timer to remove
	 */
	public static void removeTimer(Timer timer) {
		timerList.remove(timer);
	}
	
	/**
	 * returns the next instance of an animation with a specific animation id
	 * @param id of the animation to search
	 * @return animation if found an null if not found
	 */
	public Animation getAnimationByID(AnimationID id) {
		for(Animation animation : animationList) {
			if(animation.getAnimationID() == id) {
				return animation;
			}
		}
		return null;
	}
}
