package batchProcessing;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import org.w3c.dom.Element;

public class PipeCommand extends Command {
	public String describe() {
		return inFile;
	}

	InputStream data = null;
	StringBuffer str = new StringBuffer();
	PipedOutputStream pout;
	PipedInputStream pin;

	public PipeCommand() throws IOException {
		pout = new PipedOutputStream();
		pin  = new PipedInputStream(pout);
	}
	
	@Override
	public void parse(Element elem) throws ProcessException {

		if (!elem.hasAttributes())
			throw new ProcessException("PIPE command is inavlid.");
		else {

			if (elem.hasAttribute("id"))
				setId(elem.getAttribute("id"));
			else
				throw new ProcessException("PIPE id is empty.");

			if (elem.hasAttribute("path"))
				setFilePath(elem.getAttribute("path"));
			else
				throw new ProcessException("File path is empty.");

			if (elem.hasAttribute("args"))
				setArgs(elem.getAttribute("args"));
			// else throw new ProcessException("Args empty.");

			if (elem.hasAttribute("in"))
				setInFile(Batch.lookup.get("wd").filePath
						+ "/"
						+ Batch.lookup.get(elem.getAttribute("in"))
								.getFilePath());

			if (elem.hasAttribute("out"))
				setOutFile(Batch.lookup.get("wd").filePath
						+ "/"
						+ Batch.lookup.get(elem.getAttribute("out"))
								.getFilePath());
		}
	}

	@Override
	public void execute() throws ProcessException {
		try {
			// this is input command.
			if (this.outFile == null) {
				System.out.println("Executing pipe" + this.args);
				StringBuffer com = new StringBuffer();
				if (this.getId() == null)
					throw new ProcessException("Invalid CMD command.");
				else {
					System.out.println(this.filePath);
					
					com.append("cmd /c " + this.filePath + " " + this.args + " testApp1.AddLines < " + this.inFile);
					System.out.println(com.toString());
					Process p = Runtime.getRuntime().exec(com.toString());
					p.waitFor();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String Line = null;
					
					while ((Line = reader.readLine()) != null) {
						//System.out.print(Line + " ");
						pout.write(Line.getBytes());
					}
					pout.close();
				}
			}
			else {
				System.out
						.println("Writing cmd output in the output file at the "
								+ this.outFile);
				StringBuffer com = new StringBuffer();
				if (this.getId() == null)
					throw new ProcessException("Invalid CMD command.");
				else {
					System.out.println(this.filePath);
					StringBuffer pipedInputStringBuffer = new StringBuffer();
					
					com.append("cmd /c " + this.filePath + " " + this.args + " testApp1.AddLines ");
					
					Process p = Runtime.getRuntime().exec(com.toString());
					OutputStream rsyncStdIn = p.getOutputStream ();
					
					int value = 0;
					int index = -1;
					StringBuffer sb = new StringBuffer();
					while ((value = pin.read()) != -1) {
						if (index++ % 2 == 0) {
							rsyncStdIn.write(sb.append((char) value).toString().getBytes());
							rsyncStdIn.write("\r".getBytes());
						//	System.out.println(sb.toString());
							sb.setLength(0);
						} else {
							sb.append((char) value);
						}
					}
					rsyncStdIn.close();
					
					p.waitFor();
					pin.close();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					FileWriter filer = new FileWriter(this.outFile);
					PrintWriter out = new PrintWriter(filer);
					
					String line;
					while ((line = reader.readLine()) != null) {
						//System.out.println(line);
						out.println(line);
					}
					
					out.close();
					out.flush();
					filer.close();
				}
			}
		}

		catch (IOException e1) {
			System.out.println(e1.toString());
		} catch (InterruptedException e2) {
			System.out.println(e2.toString());
		}

		System.out.println("Done");

	}
}
