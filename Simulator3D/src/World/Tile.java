
package World;

import renderEngine.MasterRenderer;

public class Tile {
	
	private int size;
	private TileContent content;
	private boolean hasStreet;
	
	public Tile(int size) {
		this.size = size;
		hasStreet = false;
	}
	
	public boolean hasContent() {
		if(content == null) {
			return false;
		}
		return true;
	}
	
	
	public void setContent(TileContent content, boolean isStreet) {
		this.content = content;
		hasStreet = isStreet;
	}
	
	public void render(MasterRenderer renderer) {
		if(hasContent()) {
			content.render(renderer);
		}
	}
	
	public void removeContent() {
		if(hasContent()) {
			content.destroy();
		}
		content = null;
	}

	
	public int getSize() {
		return size;
	}
	
	public void setStreet(boolean street) {
		this.hasStreet = street;
	}
	
	public boolean hasStreet() {
		return hasStreet;
	}
	
	public TileContent getContent() {
		return content;
	}

}
