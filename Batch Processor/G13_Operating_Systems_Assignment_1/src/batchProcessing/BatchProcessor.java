package batchProcessing;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BatchProcessor {

	/** 
	 * An example of parsing a CMD element 
	 * THIS LOGIC BELONGS IN INDIVIDUAL Command subclasses
	 */
	public static void parseCmd(String key) throws ProcessException
	{
		System.out.println("Parsing Command : -----");
		CmdCommand elem = (CmdCommand) Batch.lookup.get(key);
		String id = elem.getId();
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in CMD Command");
		}
		System.out.println("ID: " + id);

		String path = elem.getFilePath();
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in CMD Command");
		}
		System.out.println("Path: " + path);

		// Arguments must be passed to ProcessBuilder as a list of
		// individual strings. 
		List<String> cmdArgs = new ArrayList<String>();
		String arg = elem.getArgs();
		if (!(arg == null || arg.isEmpty())) {
			StringTokenizer st = new StringTokenizer(arg);
			while (st.hasMoreTokens()) {
				String tok = st.nextToken();
				cmdArgs.add(tok);
			}
		}
		System.out.print("Args are: ");
		for(String argi: cmdArgs) {
			System.out.print(argi);
			System.out.print(" ");
		}

		String inID = elem.getInFile();
		if (!(inID == null || inID.isEmpty())) {
			System.out.println("inID: " + inID);
		}

		String outID = elem.getOutFile();
		if (!(outID == null || outID.isEmpty())) {
			System.out.println("outID: " + outID);
		}

		System.out.println("Executing CMD .");

		elem.execute();
	}


	private static  void parseCommand(Element elem) throws ProcessException, IOException
	{
		String cmdName = elem.getNodeName();

		if (cmdName == null) {
			throw new ProcessException("unable to parse command from " + elem.getTextContent());
		}
		else if ("wd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing wd");
			Command cmd = new WDCommand();
			cmd.parse(elem);
			Batch.lookup.put("wd", cmd);

		}
		else if ("file".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing file");
			Command cmd = new FileCommand();
			cmd.parse(elem);
			Batch.lookup.put(cmd.getId(),cmd);
		}
		else if ("cmd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing cmd");
			Command cmd = new CmdCommand();
			cmd.parse(elem); // Example of parsing a cmd element
			Batch.lookup.put(cmd.getId(), cmd);
			parseCmd(cmd.getId());
		}
		else if ("pipe".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing pipe");

			NodeList nodes = elem.getChildNodes();
			Command cmd = new PipeCommand();
			for (int idx = 0; idx < nodes.getLength(); idx++) {
				Node node = nodes.item(idx);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element elem1 = (Element) node;
					cmd.parse(elem1);	
					Batch.lookup.put(cmd.getId(), cmd);
					cmd.execute();
				}
			}

		}
		else {
			throw new ProcessException("Unknown command " + cmdName + " from: " + elem.getBaseURI());
		}
	}


	public static void main(String[] args) {
		
		try {
			
				String filename = null;
				if(args.length > 0) {
					filename = args[0];
			}
			else {
			filename ="C:/work/batch4.xml" ;
			
			}
				System.out.println("Opening " + filename);
				File f = new File(filename);

				FileInputStream fis = new FileInputStream(f);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fis);

				Element pnode = doc.getDocumentElement();
				NodeList nodes = pnode.getChildNodes();
				for (int idx = 0; idx < nodes.getLength(); idx++) {
					Node node = nodes.item(idx);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element elem = (Element) node;
						parseCommand(elem);
					}
				}
			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

}
