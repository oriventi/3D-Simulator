package menu;

public abstract class Menu {

	protected boolean hasActivationFinished;
	protected boolean hasDeactivationFinished;
	protected boolean activated;
	protected boolean deactivated;
	
	public Menu() {
		hasActivationFinished = false;
		hasDeactivationFinished = false;
	}
	
	protected abstract void updateMenu();
	
	public void update() {
		updateMenu();
		if(activated && !hasActivationFinished) {
			hasActivationFinished = onActivated();
		}
		if(deactivated && !hasDeactivationFinished) {
			hasDeactivationFinished = onDeactivated();
		}
	}
	
	protected abstract boolean onActivated();
	
	protected abstract boolean onDeactivated();
	
	public void activate() {
		activated = true;
		deactivated = false;
		hasActivationFinished = false;
	}
	
	public void reset() {
		activated = false;
		deactivated = false;
		hasActivationFinished = false;
		hasDeactivationFinished = false;
	}
	
	public boolean deactivate() {
		activated = false;
		deactivated = true;
		return hasDeactivationFinished;
	}
}
