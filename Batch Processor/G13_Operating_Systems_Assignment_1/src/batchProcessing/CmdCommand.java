package batchProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.w3c.dom.Element;

public class CmdCommand extends Command {

	@Override
	public String describe() {
		return inFile;
		// TODO Auto-generated method stub

	}

	@Override
	public void parse(Element elem) throws ProcessException {
		if(!elem.hasAttributes()) throw new ProcessException("CMD command is inavlid.");
		else {

			if(elem.hasAttribute("id"))setId(elem.getAttribute("id"));
			else throw new ProcessException("CMD id is empty.");


			if(elem.hasAttribute("path"))setFilePath(elem.getAttribute("path"));
			else throw new ProcessException("File path is empty.");

			if(elem.hasAttribute("args"))setArgs(elem.getAttribute("args"));
			//	else throw new ProcessException("Args empty.");


			if(elem.hasAttribute("in")){
				if(Batch.lookup.get(elem.getAttribute("in")) == null ) throw new ProcessException("Incorrect file name "+ elem.getAttribute("in"));
				else setInFile(Batch.lookup.get("wd").filePath + "/" + Batch.lookup.get(elem.getAttribute("in")).getFilePath());
			}

			if(elem.hasAttribute("out")){
				if(Batch.lookup.get(elem.getAttribute("out")) == null ) throw new ProcessException("Incorrect file name :- "+ elem.getAttribute("out"));
				setOutFile(Batch.lookup.get("wd").filePath + "/" + Batch.lookup.get(elem.getAttribute("out")).getFilePath());
			}
		}
	}



	@Override
	public void execute() {

		try 
		{ 
			System.out.println("Executing Command: "+ this.filePath );
			StringBuffer command = new StringBuffer();
			String inputFilePath = this.inFile ;
			String outputFilePath = this.outFile ;
			switch(this.filePath){
			case "cmd":
			{
				command.append(this.args == null ? this.filePath  : this.filePath+ " " +this.args) ;
				break;
			}
			case "sort" : 
			{
				command.append(this.filePath + " " + this.inFile + " "  + (this.args == null ? "" : this.args));
				break;
			}
			case "java.exe" :
			{
				command.append("cmd /c " + this.filePath + " " + this.args + " testApp1.AddLines < " + inputFilePath  );
				break;					
			}
			}
			System.out.println(command);
			Process p=Runtime.getRuntime().exec(command.toString()); 
			p.waitFor(); 
			BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
			
			System.out.println("Writing cmd output in the output file at the " + outputFilePath );
			File file = new File(outputFilePath);
			System.out.println(file.getAbsolutePath());
			file.getParentFile().mkdirs();
			FileWriter filer = new FileWriter(file);
			PrintWriter	out = new PrintWriter(filer);
			String line; 
			while((line = reader.readLine()) != null) 
			{ 
				out.println(line);
			}
			out.close();
			out.flush();
			filer.close();
		}
		catch(IOException e1) {System.out.println(e1.toString());} 
		catch(InterruptedException e2) {System.out.println(e2.toString());} 

		System.out.println("Done"); 

	}

}
