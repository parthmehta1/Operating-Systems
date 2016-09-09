package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class WriteCommand extends ServerCommand{
	
	int size=0;
	String[] result = new String[2];


	@Override
	public void run() throws IOException, ServerException {
		// TODO Auto-generated method stub
		
		for(int i=0; i<3 ;i++)
		{
			if(i != 2)
			{
				result[i] = StreamUtil.readLine(inputStream);
				if(i == 1)
				{
					size = Integer.parseInt(result[i]);
				}
			}
			else if(i==2)
			{
				byte bytesdt[] = StreamUtil.readData(size, inputStream);
			    FileUtil.writeData(result[0], bytesdt);
			}
		}
		sendOK();
		StreamUtil.closeSocket(inputStream);
	}
}


