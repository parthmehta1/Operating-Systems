package utd.persistentDataStore.datastoreServer.commands;



import java.io.IOException;


import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class ReadCommand extends ServerCommand{
	
	@Override
	public void run() throws IOException, ServerException {
		// TODO Auto-generated method stub
		
		String result = StreamUtil.readLine(inputStream);
		byte data[] = FileUtil.readData(result);
		sendOK();
		int size = data.length;
        String asc = Integer.toString(size) + "\n";
        StreamUtil.writeLine(asc, outputStream);
        StreamUtil.writeData(data, outputStream);
        StreamUtil.closeSocket(inputStream);
		
	}
	
}
