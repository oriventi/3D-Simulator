package World;

import renderEngine.MasterRenderer;

public abstract class TileContent {
	int xtile, ytile;
	
	public TileContent(int xtile, int ytile) {
		this.xtile = xtile;
		this.ytile = ytile;
	}
	
	public abstract void destroy();
	
	public abstract void render(MasterRenderer renderer);
	
	public abstract void increaseRotation();
	
	public abstract String getBuildingID();
}
