package fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * reads and writes to a savedFile
 * @author Oriventi
 *
 */
public class FileManager {

	private String directory = "res/savedFiles/";
	
	public FileManager() {
		
	}
	
	/**
	 * creates a new file with name (INCLUDE .wrld, .plyr, ...)
	 * @param name + .wrld, ...
	 */
	public void createFile(String name) {
		try {
			File file = new File(directory + name);
			if(file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			}else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occured.");
			e.printStackTrace();
		}
	}
	
	/**
	 * writes text to the file with name 
	 * @param name (INCLUDE .txt, .wrld, ...)
	 * @param text
	 */
	public void writeToFile(String name, String text) {
		try {
			FileWriter fileWriter = new FileWriter(directory + name);
			fileWriter.append(text);
			fileWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * reads from file with name 
	 * @param name
	 * @return array of the file's lines
	 */
	public String[] readFile(String name) {
		try {
			ArrayList<String> lines = new ArrayList<String>();
			File file = new File(directory + name);
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				lines.add(scanner.nextLine());
			}
			scanner.close();
			
			String[] lineArray = new String[lines.size()];
			lineArray = lines.toArray(lineArray);
			
			return lineArray;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * deletes the file with name
	 * @param name of file (INCLUDE .txt, .wrld, ...)
	 */
	public void deleteFile(String name) {
		File file = new File(directory + name);
		if(file.delete()) {
			System.out.println("Deleted the file: " + file.getName());
		}else {
			System.out.println("Failed to delete the file.");
		}
	}
	
	/**
	 * returns all world files in savedFiles directory
	 * @return string array of names of saved Files
	 */
	public String[] getAllWorldsNames() {
		File worldDirectory = new File(directory);
		return worldDirectory.list();
	}
}
