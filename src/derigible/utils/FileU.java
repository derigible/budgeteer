/**
 * 
 */
package derigible.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author marphill
 * Helper class to make file handling far easier.
 */
public final class FileU {
	
	private FileU(){
		//Here to prevent instantiation
	}
	
	/**
	 * Helper method to get the BufferedReader to read a file.
	 * 
	 * @param file - the file to read
	 * @return the BufferedReader
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getFileReader(File file) throws FileNotFoundException  {
		return new BufferedReader(new FileReader(file));
	}
	
	/**
	 * Helper method to get the BufferedReader to read a file.
	 * 
	 * @param file - the file to read's path
	 * @return the BufferedReader
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getFileReader(String file) throws FileNotFoundException  {
		return new BufferedReader(new FileReader(file));
	}
	
	/**
	 * Gets a new PrintWriter to write a file.
	 * 
	 * @param file - the file to write to
	 * @return the printWriter
	 * @throws IOException
	 */
	public static PrintWriter getFileWriter(File file) throws IOException{
		return new PrintWriter(new FileWriter(file));
	}
	
	/**
	 * Gets the default location to store the file ($HOME/{username}/Budgeteer)
	 * 
	 * @return the default file writer location
	 * @throws IOException
	 */
	public static PrintWriter getFileWriterToDefaultLocation(String filename) throws IOException{
		File file =  new File(System.getProperty("user.home") + "Budgeteer");
		if(file.exists()){
			file = new File(file.getAbsolutePath() + filename);
		} else {
			boolean result = file.mkdir();
			if(result){
				file = new File(file.getAbsolutePath() + filename);
			} else {
				throw new IOException("Directory not created");
			}
		}
		return getFileWriter(file);
	}
	
	public static BufferedReader getFileReaderAtDefaultLocation(String filename) throws FileNotFoundException{
		File file =  new File(System.getProperty("user.home") + "Budgeteer");
		if(file.exists()){
			file = new File(file.getAbsolutePath() + filename);
		} else {
			throw new FileNotFoundException();
		}
		return getFileReader(file);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
