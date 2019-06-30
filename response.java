import java.rmi.Naming;	
import java.rmi.RemoteException;
import java.net.MalformedURLException;	
import java.rmi.NotBoundException;	
import java.io.*; 
import java.util.*; 
import java.security.*;
import java.nio.file.Files;

public class response implements Serializable{
	
	private String path = null;
	private byte[] responseFile = null;
	private static final long serialVersionUID = 1L;
	
	public response(byte[] response)
	{	
		try
		{
			responseFile = response;
		}

		catch(Exception e)
		{
			System.out.println("Could not create a file at the ");
		}
	}
	
	public byte[] getResponseFile()
	{
		return responseFile;
	}
		
}