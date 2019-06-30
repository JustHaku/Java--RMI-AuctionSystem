import java.rmi.Naming;			//Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException;	//Import the RemoteException class so you can catch it
import java.net.MalformedURLException;	//Import the MalformedURLException class so you can catch it
import java.rmi.NotBoundException;	//Import the NotBoundException class so you can catch it
import java.io.*; 
import java.util.*; 
import java.util.Arrays;

/** A program made for the use of auction buyers to register themselves
/  as clients and start buying items in auctions from sellers
*/

public class bidClient 
{
	Scanner scan = new Scanner(System.in);
	String name;
	String email;
	
    public static void main(String[] args) 
	{
		bidClient user = new bidClient();
	}	
	
	/**
	* Constructor for the bidClient class 
	* used to create a new client program that can bid on auctions
	* runs methods that collect intended bid settings via user input
	*/	
	
	public bidClient()
	{
		try 
		{
			System.out.println("--------------------------------------------");
			System.out.println("Welcome to the buyers auction system");
			System.out.println("--------------------------------------------\n");
			name = getName();
			email = getEmail();
			
			auctionInterface a = (auctionInterface) Naming.lookup("rmi://localhost/Auctions");
				
			auctionClient user = new auctionClient(name,email);
			
			if(a.userCheck(user) == true)
			{
				user.requestChallenge("signMeServer.txt");
				
				a.genSig(user);

				a.getChallenge();

				user.genSig(a);
				
				if(user.verSig(a) == true && a.verSig(user) == true)
				{
					System.out.println("--------------------------------------------");
					System.out.println("Successful 5 way verification!");
					System.out.println("--------------------------------------------\n");
					
					int [] bids = a.userAuctionsDisplay(user);
			
					if(checkEmpty(bids) == true)
					{
						System.out.println("--------------------------------------------");
						System.out.println("You are currently the highest bidder in no auctions");
						System.out.println("--------------------------------------------\n");
						System.out.println("Auctions " + Arrays.toString(bids));
					}
					else
					{
						System.out.println("--------------------------------------------");
						System.out.println("You are currently the highest bidder in the following auctions: ");
						System.out.println("--------------------------------------------\n");
						System.out.println("Auctions " + Arrays.toString(bids));
					}
					
					user.setAuctionID(getAuctID());
					user.setItemBid(getBid());
					
					a.auctionBid(user);
					
				}
				else
				{
					System.out.println("--------------------------------------------");
					System.out.println("Failed to authorize communication");
					System.out.println("--------------------------------------------\n");
				}
			}
		}
		catch (MalformedURLException murle) 
		{
				System.out.println();
				System.out.println("MalformedURLException");
				System.out.println(murle);
		}
			catch (RemoteException re) 
			{
				System.out.println();
				System.out.println("RemoteException");
				System.out.println(re);
			}
			catch (NotBoundException nbe) 
			{
				System.out.println();
				System.out.println("NotBoundException");
				System.out.println(nbe);
			}
			catch (java.lang.ArithmeticException ae) 
			{
				System.out.println();
				System.out.println("java.lang.ArithmeticException");
				System.out.println(ae);
			}
    }
	
	/**
	* Method for retrieving the intended auction id
	* for auction bids via user input
	*the user input is then error trapped
	*/
	public int getAuctID()
	{
		while (true) 
		{
			int auctID = 0;
			System.out.println("--------------------------------------------");
			System.out.println("Please enter the auction ID for the item you want to bid on: ");
			System.out.println("--------------------------------------------\n");
			String line = scan.nextLine();
			try
			{
				 auctID = Integer.parseInt(line);
			}
			catch(Exception e)
			{
				System.out.println("\nPlease enter a valid auction ID!");
				getAuctID();
			}
			return auctID;
		}
	}
	
	/**
	* Method for retrieving the intended bid prices
	* for auction bids via user input
	*the user input is then error trapped
	*/
	public Double getBid()
	{
		while (true) 
		{
			double bid = 0;
			System.out.println("--------------------------------------------");
			System.out.println("Please enter the amount you would like to bid: ");
			System.out.println("--------------------------------------------\n");
			String line = scan.nextLine();
			try
			{
				 bid = Double.parseDouble(line);
			}
			catch(Exception e)
			{
				System.out.println("\nPlease enter a valid number such as 5.00!");
				getBid();
			}
			return bid;
		}
	}	
	
	/**
	* Method for retrieving the users name via user input
	* the user input is then error trapped
	*/
	public String getName()
	{
		String tempName = null;
		System.out.println("--------------------------------------------");
		System.out.println("Please enter your name: ");
		System.out.println("--------------------------------------------\n");
		try
		{
			tempName = scan.nextLine();
		}
		catch(Exception e)
		{
			System.out.println("\nPlease enter a valid name");
			getName();
		}
		return tempName;
	}
	
	/**
	* Method for retrieving the users email via user input
	*the user input is then error trapped
	*/
	public String getEmail()
	{
		String tempEmail = null;
		System.out.println("--------------------------------------------");
		System.out.println("Please enter your email: ");
		System.out.println("--------------------------------------------\n");
		try
		{
			tempEmail = scan.nextLine();
		}
		catch(Exception e)
		{
			System.out.println("\nPlease enter a valid email");
			getEmail();
		}
		return tempEmail;
	}
	
	/**
	* Method that checks if a provided array is empty
	*@param array the array to be checked
	*/
	public boolean checkEmpty(int[] array)
	{
		boolean ans = true;
		for (int a : array) {
			if (a != 0) {
				ans = false;
			}
		}
		return ans;
	}
}