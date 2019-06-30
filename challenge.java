import java.rmi.Naming;	
import java.rmi.RemoteException;
import java.net.MalformedURLException;	
import java.rmi.NotBoundException;	
import java.io.*; 
import java.util.*; 
import java.security.*;
import java.nio.file.Files;

public class challenge implements Serializable{
	
	private String path = null;
	private File challengeBytes = null;
	private static final long serialVersionUID = 1L;
	
	public challenge(String fn)
	{	
		path = fn;
		
		try
		{
			challengeBytes = new File(path);
		}
		catch(Exception e)
		{
			System.out.println("Could not read the file at the given path");
		}
	}
	
	public File getChallengeFile()
	{
		return challengeBytes;
	}
		
}