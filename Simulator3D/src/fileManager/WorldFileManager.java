package fileManager;

import org.newdawn.slick.util.Log;

import World.TileManager;

public class WorldFileManager {
	
	private FileManager fileManager;
	
	public WorldFileManager() {
		fileManager = new FileManager();
	}
	
	public void saveCurrentWorldInFile(String name) {
		fileManager.writeToFile(name + ".wrld", getWorldInformationAsString());
	}
	
	private String getWorldInformationAsString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < TileManager.size; i++) {
			for(int j = 0; j < TileManager.size; j++) {
				sb.append(TileManager.getTileSystem()[j][i].getBuildingID() + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public void createNewWorldFile(String name) {
		fileManager.createFile(name + ".wrld");
	}
	
	public void fillWorldByInformationFromFile(String name) {

	}
	
	public void deleteWorld(String name) {
		fileManager.deleteFile(name + ".wrld");
	}
	
	
}
