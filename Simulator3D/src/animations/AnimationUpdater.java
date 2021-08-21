package animations;

import java.util.ArrayList;
import java.util.List;

import toolbox.EnumHolder.AnimationID;

public class AnimationUpdater {
	
	private List<Animation> animationList;

	public AnimationUpdater() {
		animationList = new ArrayList<Animation>();
	}
	
	public void update() {
		for(Animation animation : animationList) {
			animation.update();
		}
	}
	
	public void addAnimation(Animation animation) {
		animationList.add(animation);
	}
	
	public void removeAnimation(Animation animation) {
		animationList.remove(animation);
	}
	
	public Animation getAnimationByID(AnimationID id) {
		for(Animation animation : animationList) {
			if(animation.getAnimationID() == id) {
				return animation;
			}
		}
		return null;
	}
}
