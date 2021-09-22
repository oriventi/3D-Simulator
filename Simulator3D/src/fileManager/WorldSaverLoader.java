package fileManager;

public class WorldSaverLoader {
	
	private FileManager fileManager;
	
	public WorldSaverLoader() {
		fileManager = new FileManager();
	}
	
	public void saveCurrentWorldInFile(String name) {
		fileManager.writeToFile(name, getWorldInformationAsString());
	}
	
	public String getWorldInformationAsString() {
		return null;
	}

	public void createNewWorldFile(String name) {
		fileManager.createFile(name);
	}
	
	public void fillWorldByInformationFromFile() {
		
	}
	
	public void deleteWorld(String name) {
		fileManager.deleteFile(name);
	}
	
	
}
