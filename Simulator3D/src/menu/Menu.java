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
	
	protected abstract void activate();
	
	protected abstract void deactivate();
		
	protected abstract boolean onActivated();
	
	protected abstract boolean onDeactivated();
	
	public void activateMenu() {
		activated = true;
		deactivated = false;
		hasActivationFinished = false;
		activate();
	}
	
	public boolean deactivateMenu() {
		activated = false;
		deactivated = true;
		deactivate();
		return hasDeactivationFinished;
	}
}
