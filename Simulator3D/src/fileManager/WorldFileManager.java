package fileManager;

import World.NatureFoundation;
import World.TileManager;
import buildings.Bank;
import buildings.FactoryOne;
import buildings.HouseOne;
import buildings.HouseThree;
import buildings.HouseTwo;
import buildings.OfficeOne;
import buildings.SupermarketOne;

/**
 * loads, deletes, creates, and writes the world saved files
 * @author Oriventi
 *
 */
public class WorldFileManager {
	
	private FileManager fileManager;
	
	public WorldFileManager() {
		fileManager = new FileManager();
	}
	
	/**
	 * overwrites the current world in existing file with name
	 * @param name
	 */
	public void saveCurrentWorldInFile(String name) {
		fileManager.writeToFile(name + ".wrld", getWorldInformationAsString());
	}
	
	/**
	 * returns saved World file's data as a string
	 * @return
	 */
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
	
	/**
	 * creates new world file with name
	 * @param name
	 */
	public void createNewWorldFile(String name) {
		fileManager.createFile(name + ".wrld");
	}
	
	/**
	 * fills world tiles by information from file name
	 * @param name
	 */
	public void fillWorldByInformationFromFile(String name) {
		String[] lines = fileManager.readFile(name + ".wrld");
		for(int i = 0; i < lines.length; i++) {
			String[] tileContent = lines[i].split(" ");
			for(int j = 0; j < tileContent.length; j++) {
				fillTileByBuildingID(j ,i , tileContent[j]);
			}
		}
	}
	
	/**
	 * sets the content of tile(xtile, ytile) to the building with buildingID
	 * @param xtile of tile
	 * @param ytile of tile
	 * @param buildingID of building
	 */
	private void fillTileByBuildingID(int xtile, int ytile, String buildingID) {
		int rotation = 0;
		String[] rotatedID;
		
		if(buildingID.length() >= 3) {
			rotatedID = buildingID.split("r");
			rotation = Integer.valueOf(rotatedID[1]);
			buildingID = rotatedID[0];
		}
		
		switch(buildingID) {
			case "0":
				TileManager.getStreetManager().addStreet(xtile, ytile);
				break;
			case "1":
				TileManager.setTileContent(new HouseOne(xtile, ytile), xtile, ytile, rotation);
				break;
			case "2":
				TileManager.setTileContent(new HouseTwo(xtile, ytile), xtile, ytile, rotation);
				break;
			case "3":
				TileManager.setTileContent(new HouseThree(xtile, ytile), xtile, ytile, rotation);
				break;
			case "4":
				TileManager.setTileContent(new FactoryOne(xtile, ytile), xtile, ytile, rotation);
				break;
			case "5":
				TileManager.setTileContent(new OfficeOne(xtile, ytile), xtile, ytile, rotation);
				break;
			case "6":
				TileManager.setTileContent(new SupermarketOne(xtile, ytile), xtile, ytile, rotation);
				break;
			case "7":
				TileManager.setTileContent(new Bank(xtile, ytile), xtile, ytile, rotation);
				break;
			case "8":
				TileManager.setTileContent(new NatureFoundation(xtile, ytile, 3), xtile, ytile, rotation);
				break;
			default:
				break;
		}
	}
	
	/**
	 * deletes world file with name 
	 * @param name of file
	 */
	public void deleteWorld(String name) {
		fileManager.deleteFile(name + ".wrld");
	}
}
