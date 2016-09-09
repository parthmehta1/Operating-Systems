package utd.persistentDataStore.datastoreClient;


import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreClientImpl implements DatastoreClient
{
	private static Logger logger = Logger.getLogger(DatastoreClientImpl.class);

	private InetAddress address;
	private int port;

	public DatastoreClientImpl(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.String, byte[])
	 */

	@Override
    public void write(String name, byte data[]) throws ClientException
	{
		
		logger.debug("Executing Write Operation");
		String command = "write\n";
		
		try
		{
       
	         Socket client = new Socket(address, port);
	         
	         InputStream inStream = null;
	 		 OutputStream outStream = null;
	 		 
	 		 inStream = client.getInputStream();
			 outStream = client.getOutputStream();
			 
	         StreamUtil.writeLine(command, outStream);
	         StreamUtil.writeLine(name, outStream);
	         
	         int len = data.length;
	         String append = Integer.toString(len) + "\n";
	         
	         StreamUtil.writeLine(append, outStream);
	         StreamUtil.writeData(data, outStream);

	         String response = StreamUtil.readLine(inStream);
	         logger.debug("Response: " + response);
	         
	         StreamUtil.closeSocket(inStream);
	         client.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#read(java.lang.String)
	 */

	@Override
    public byte[] read(String name) throws ClientException
	{
		logger.debug("Executing Read Operation");
		String result = null;
		int size;
		String command = "read\n";
		byte bytes[] = null;
		
		
		
		try
		{
			 Socket client = new Socket(address, port);
			 InputStream inStream = null;
			 OutputStream outStream = null;
	 		 inStream = client.getInputStream();
			 outStream = client.getOutputStream();
			 StreamUtil.writeLine(command, outStream);
	         StreamUtil.writeLine(name, outStream);
	         
	         String response = StreamUtil.readLine(inStream);
	         logger.debug("Response: " + response);
	         
	         if(response.equalsIgnoreCase("OK"))
	         {
	        	 for(int i=0; i<2 ;i++)
	        	 {
	        		 if(i != 1)
	        		 {
	        			 result = StreamUtil.readLine(inStream);	 		

	        		 }
	        		 else if(i==1)
	        		 {
	        			 size = Integer.parseInt(result);
	        			 bytes = StreamUtil.readData(size, inStream);
	        		 }
	        		 
	        	 }
	         }
	         else
	         {
	        	 String err ="File is Missing";
	        	 logger.error("Exception while processing the request. " + err);
	        	 StreamUtil.sendError(err, outStream);
	           	 StreamUtil.closeSocket(inStream);
	        	 client.close();
	        	 throw new ClientException(err);
	        	 
	         }
	         
	         StreamUtil.closeSocket(inStream);
	         client.close();
		}
		catch(IOException e)
		{
			String errmsg = e.getMessage();
			logger.error("Exception while processing the request. " + errmsg);

		}
		return bytes;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#delete(java.lang.String)
	 */
	@Override
    public void delete(String name) throws ClientException
	{
		logger.debug("Executing Delete Operation");
		String command = "delete\n";
		
		try
		{
			Socket client = new Socket(address, port);
			InputStream inStream = null;
			OutputStream outStream = null;
			inStream = client.getInputStream();
			outStream = client.getOutputStream();
			StreamUtil.writeLine(command, outStream);
	        StreamUtil.writeLine(name, outStream);
	        
	        String response = StreamUtil.readLine(inStream);
	        logger.debug("Response: " + response);
	        
	        if(response.equalsIgnoreCase("OK"))
	        {
	        	StreamUtil.writeLine(response, outStream);
	        }
	        else
	        {
	        	String err = "File Not Found";
	        	StreamUtil.writeLine(err, outStream);
	        	StreamUtil.closeSocket(inStream);
	        	client.close();
	        	throw new ClientException(err);
	        }
	        StreamUtil.closeSocket(inStream);
	        client.close();
		}
		catch(IOException e)
		{
			String errmsg = e.getMessage();
			logger.error("Exception while processing the request. " + errmsg);
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#directory()
	 */
	@Override
    public List<String> directory() throws ClientException
	{
		logger.debug("Executing Directory Operation");
		String command = "directory\n";
		List<String> lst = null;
		
		try
		{
			Socket client = new Socket(address, port);
			InputStream inStream = null;
			OutputStream outStream = null;
			inStream = client.getInputStream();
			outStream = client.getOutputStream();
			StreamUtil.writeLine(command, outStream);
			
			String response = StreamUtil.readLine(inStream);
	        logger.debug("Response: " + response);
	        
	        String number = StreamUtil.readLine(inStream);
	        
	        int size = Integer.parseInt(number);
	        lst = new ArrayList<String>(size);
	        for(int i = 0; i<size ;i++)
	        {

	        	lst.add(StreamUtil.readLine(inStream));
	        	
	        }
	        	
	        StreamUtil.closeSocket(inStream);
	        client.close();
		}
		catch(IOException e)
		{
			String errmsg = e.getMessage();
			logger.error("Exception while processing the request. " + errmsg);
		}

		return lst;
	}
	
	

}
