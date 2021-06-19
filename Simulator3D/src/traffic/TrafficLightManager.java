package traffic;

import renderEngine.MasterRenderer;

public class TrafficLightManager {
	
	private Street street;
	private TrafficLight[] traffic_lights; //top right bottom left
	
	public TrafficLightManager(Street street) {
		traffic_lights = new TrafficLight[4];
		update(street);
	}
	
	public void update(Street street) {
		this.street = null;
		this.street = street;
		initArray();
	}
	
	private void initArray() {
		clearArray();
		if(street.getNeighbors() == 4) {
			traffic_lights[0] = new TrafficLight(street.getTileX(), street.getTileY(), 0.1f, 0.1f, 90, 2, street);
			traffic_lights[1] = new TrafficLight(street.getTileX(), street.getTileY(), 0.9f, 0.1f, 0, 0, street);
			traffic_lights[2] = new TrafficLight(street.getTileX(), street.getTileY(), 0.9f, 0.9f, 270, 4, street);
			traffic_lights[3] = new TrafficLight(street.getTileX(), street.getTileY(), 0.1f, 0.9f, 180, 6, street);
		}else if(street.getNeighbors() == 3) {
			
			if(street.getRotation() == 0) {
				traffic_lights[0] = new TrafficLight(street.getTileX(), street.getTileY(), 0.1f, 0.1f, 90, 2, street);
				traffic_lights[1] = new TrafficLight(street.getTileX(), street.getTileY(), 0.9f, 0.1f, 0, 0, street);
				traffic_lights[2] = null;
				traffic_lights[3] = new TrafficLight(street.getTileX(), street.getTileY(), 0.1f, 0.9f, 180, 4, street);
				
			}else if(street.getRotation() == 90) {
				traffic_lights[0] = new TrafficLight(street.getTileX(), street.getTileY(), 0.1f, 0.1f, 90, 2, street);
				traffic_lights[1] = null;
				traffic_lights[2] = new TrafficLight(street.getTileX(), street.getTileY(), 0.9f, 0.9f, 270, 0, street);
				traffic_lights[3] = new TrafficLight(street.getTileX(), street.getTileY(), 0.1f, 0.9f, 180, 4, street);
				
			}else if(street.getRotation() == 180) {
				traffic_lights[0] = null;
				traffic_lights[1] = new TrafficLight(street.getTileX(), street.getTileY(), 0.9f, 0.1f, 0, 2, street);
				traffic_lights[2] = new TrafficLight(street.getTileX(), street.getTileY(), 0.9f, 0.9f, 270, 0, street);
				traffic_lights[3] = new TrafficLight(street.getTileX(), street.getTileY(), 0.1f, 0.9f, 180, 4, street);
				
			}else if(street.getRotation() == -90) {
				traffic_lights[0] = new TrafficLight(street.getTileX(), street.getTileY(), 0.1f, 0.1f, 90, 2, street);
				traffic_lights[1] = new TrafficLight(street.getTileX(), street.getTileY(), 0.9f, 0.1f, 0, 0, street);
				traffic_lights[2] = new TrafficLight(street.getTileX(), street.getTileY(), 0.9f, 0.9f, 270, 4, street);
				traffic_lights[3] = null;
			}
		}
	}
	
	public void changeTrafficLights() {
		for(int i = 0; i < 4; i++) {
			if(traffic_lights[i] != null) {
				traffic_lights[i].changeLights(street);
			}
		}
	}
	
	private void clearArray() {
		for(int i = 0; i < 4; i++) {
			traffic_lights[i] = null;
		}
	}
	
	public void render(MasterRenderer renderer) {
		for(int i = 0; i < 4; i++) {
			if(traffic_lights[i] != null) {
				traffic_lights[i].render(renderer);
			}
		}
	}

}
