package streets;

import renderEngine.MasterRenderer;

public abstract class Street {

	protected int xtile, ytile;
	protected float xpos, ypos;
	
	public Street(int xtile, int ytile) {
		
	}
	
	public abstract void render(MasterRenderer renderer);
}
