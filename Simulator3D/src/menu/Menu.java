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
	
	protected abstract void updateContent();
	
	public void update() {
		updateContent();
		if(activated && !hasActivationFinished) {
			hasActivationFinished = updateActivationProcess();
		}
		if(deactivated && !hasDeactivationFinished) {
			hasDeactivationFinished = updateDeactivationProcess();
		}
	}
		
	protected abstract void onDeactivated();
		
	protected abstract boolean updateActivationProcess();
	
	/**
	 * is called every second while the menu is deactivating
	 * @return whether deactivation has been finnished
	 */
	protected abstract boolean updateDeactivationProcess();
	
	public void startActivationProcess() {
		activated = true;
		deactivated = false;
		hasActivationFinished = false;
	}
	
	public void startDeactivationProcess() {
		activated = false;
		deactivated = true;
		onDeactivated();
	}
	
	public boolean hasDeactivationFinished() {
		return hasDeactivationFinished;
	}
}
