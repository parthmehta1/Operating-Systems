package batchProcessing;

import org.w3c.dom.Element;

public class FileCommand extends Command{

	@Override
	public String describe() {
		return inFile;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parse(Element elem) throws ProcessException {
		if(elem.getAttributes() == null) throw new ProcessException("Input File is path is empty.");
		else{
			if(elem.getAttribute("path") == null) throw new ProcessException("File path is empty.");
			else {
				String file = elem.getAttribute("path");
				setFilePath(file);
				String id = elem.getAttribute("id");
				setId(id);
				}
			
		}
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	

}
