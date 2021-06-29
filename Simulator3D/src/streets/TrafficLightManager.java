package streets;

import renderEngine.MasterRenderer;

public class TrafficLightManager {
	
	private Street street;
	private TrafficLight[] traffic_lights; //top right bottom left
	
	public TrafficLightManager(Street street) {
		traffic_lights = new TrafficLight[4];
		this.street = street;
		
		initArray();
	}
	
	private void initArray() {
		if(street.getNeighbors() == 4) {
			traffic_lights[0] = new TrafficLight(street, 0.1f, 0.1f, 90, 2);
			traffic_lights[1] = new TrafficLight(street, 0.9f, 0.1f, 0, 0);
			traffic_lights[2] = new TrafficLight(street, 0.9f, 0.9f, 270, 4);
			traffic_lights[3] = new TrafficLight(street, 0.1f, 0.9f, 180, 6);
		}else if(street.getNeighbors() == 3) {
			
			if(street.getRotation() == 0) {
				traffic_lights[0] = new TrafficLight(street, 0.1f, 0.1f, 90, 2);
				traffic_lights[1] = new TrafficLight(street, 0.9f, 0.1f, 0, 0);
				traffic_lights[2] = null;
				traffic_lights[3] = new TrafficLight(street, 0.1f, 0.9f, 180, 4);
				
			}else if(street.getRotation() == 90) {
				traffic_lights[0] = new TrafficLight(street, 0.1f, 0.1f, 90, 2);
				traffic_lights[1] = null;
				traffic_lights[2] = new TrafficLight(street, 0.9f, 0.9f, 270, 0);
				traffic_lights[3] = new TrafficLight(street, 0.1f, 0.9f, 180, 4);
				
			}else if(street.getRotation() == 180) {
				traffic_lights[0] = null;
				traffic_lights[1] = new TrafficLight(street, 0.9f, 0.1f, 0, 2);
				traffic_lights[2] = new TrafficLight(street, 0.9f, 0.9f, 270, 0);
				traffic_lights[3] = new TrafficLight(street, 0.1f, 0.9f, 180, 4);
				
			}else if(street.getRotation() == 270) {
				traffic_lights[0] = new TrafficLight(street, 0.1f, 0.1f, 90, 2);
				traffic_lights[1] = new TrafficLight(street, 0.9f, 0.1f, 0, 0);
				traffic_lights[2] = new TrafficLight(street, 0.9f, 0.9f, 270, 4);
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
	
	public void render(MasterRenderer renderer) {
		for(int i = 0; i < 4; i++) {
			if(traffic_lights[i] != null) {
				traffic_lights[i].render(renderer);
			}
		}
	}

}
