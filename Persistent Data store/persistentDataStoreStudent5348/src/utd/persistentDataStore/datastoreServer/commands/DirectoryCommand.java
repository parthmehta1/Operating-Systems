package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import java.util.List;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class DirectoryCommand extends ServerCommand{

	@Override
	public void run() throws IOException, ServerException {
		// TODO Auto-generated method stub
		
		List<String> dir;
		int size;
		
		dir = FileUtil.directory();
		size = dir.size();
		String asc = Integer.toString(size) + "\n";
		sendOK();
		StreamUtil.writeLine(asc, outputStream);
		
		for (int j = 0; j < dir.size(); j++) 
        {
    		StreamUtil.writeLine((dir.get(j)), outputStream);
		}
		StreamUtil.closeSocket(inputStream);
				
	}

}
