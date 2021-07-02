package entities;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {

	public static List<Entity> entities;
	
	public EntityManager() {
		entities = new ArrayList<Entity>();
	}
	
	public static void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public static void removeEntity(Entity entity) {
		entities.remove(entity);
	}

}
