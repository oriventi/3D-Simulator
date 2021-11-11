package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * stores each entity which casts a shadow and generates a shadowMap from it
 * @author Oriventi
 *
 */
public class EntityShadowList {

	public static List<Entity> entities;
	public static List<Entity> staticEntities;
	
	/**
	 * declares lists of entities
	 */
	public EntityShadowList() {
		entities = new ArrayList<Entity>();
		staticEntities = new ArrayList<Entity>();
	}
	
	/**
	 * adds a normal entity to list
	 * @param entity to add
	 */
	public static void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	/**
	 * removes a normal entity from list 
	 * @param entity to remove
	 */
	public static void removeEntity(Entity entity) {
		entities.remove(entity);
	}
	
	/**
	 * adds a static entity to list (doesn't move during game)
	 * @param static entity to add
	 */
	public static void addStaticEntity(Entity entity) {
		staticEntities.add(entity);
	}
	
	/**
	 * removes a static entity to list (doesn't move during game)
	 * @param static entity to remove
	 */
	public static void removeStaticEntity(Entity entity) {
		staticEntities.remove(entity);
	}

}
