package batchProcessing;

import org.w3c.dom.Element;

public class WDCommand extends Command  {

	@Override
	public String describe() {
		return inFile;
		// TODO Auto-generated method stub

	}

	@Override
	public  void parse(Element elem) throws ProcessException {
		if(!elem.hasAttributes()) throw new ProcessException("Empty Wd Attribute.");
		else{
			String path = "C:/" + elem.getAttribute("path");
			this.setFilePath(path);

			String id = elem.getAttribute("id");
			this.setId(id);
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}



}
