import java.rmi.Naming;	
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/** The main auction server binds the interface
/  to a local port and starts the server
*/ 

public class auctionServer 
{
   public auctionServer() 
   {
	try 
	{
		auctionInterface a = new auctionImplementation("signMeUser.txt");
		Naming.rebind("rmi://localhost/Auctions", a);
	} 
	catch (Exception e) 
	{
		System.out.println("Server Error: " + e);
	}
   }
   
   public static void main(String args[]) 
   {
		//Create the new auctionServer
		new auctionServer();
   }
   
}
