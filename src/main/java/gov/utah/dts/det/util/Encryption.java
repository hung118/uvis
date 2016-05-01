package gov.utah.dts.det.util;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;

public class Encryption {

	private File inFile;
	private File outFile;
	private String key;
	
	public Encryption(File inFile, File outFile, String key) {
		this.inFile = inFile;
		this.outFile = outFile;
		this.key = key;
	}
	
	public File getInFile() {
		return inFile;
	}

	public void setInFile(File inFile) {
		this.inFile = inFile;
	}

	public File getOutFile() {
		return outFile;
	}

	public void setOutFile(File outFile) {
		this.outFile = outFile;
	}

	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public void decrypt() throws Exception {
	
		ZipFile zipFile = new ZipFile(inFile);
		zipFile.setPassword(key);
		zipFile.extractAll(outFile.getAbsolutePath());			
	}    
    
}
