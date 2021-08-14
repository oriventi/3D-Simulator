package entities;

import java.util.ArrayList;
import java.util.List;

public class EntityShadowList {

	public static List<Entity> entities;
	
	public EntityShadowList() {
		entities = new ArrayList<Entity>();
	}
	
	public static void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public static void removeEntity(Entity entity) {
		entities.remove(entity);
	}

}
