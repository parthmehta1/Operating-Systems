package batchProcessing;

import java.util.ArrayList;

import org.w3c.dom.Element;

public abstract class Command {

	public abstract String describe();
	
	public abstract void parse(Element elem) throws ProcessException;
	
	public abstract void execute() throws ProcessException;
	
	static String path ;
	String inFile ;
	String outFile ;
	String filePath ;
	String id;
	String args; 
	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		Command.path = path;
	}

	public String getInFile() {
		return inFile;
	}

	public void setInFile(String inFile) {
		this.inFile = inFile;
	}

	public String getOutFile() {
		return outFile;
	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
