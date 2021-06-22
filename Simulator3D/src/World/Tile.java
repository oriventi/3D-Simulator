
package World;

import entities.Entity;
import models.Mesh;

public class Tile {
	
	private int size;
	private Entity content;
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
	
	public void rotateEntity() {
		if(!hasStreet && content != null) {
			content.increaseRotation(0, 90, 0);
		}
	}
	
	public void setContent(Entity entity, boolean isStreet) {
		this.content = entity;
		hasStreet = isStreet;
	}
	
	public void removeContent() {
		content = null;
	}
	
	public Entity getContent() {
		return content;
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

}
