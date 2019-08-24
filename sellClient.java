import java.rmi.Naming;			//Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException;	//Import the RemoteException class so you can catch it
import java.net.MalformedURLException;	//Import the MalformedURLException class so you can catch it
import java.rmi.NotBoundException;	//Import the NotBoundException class so you can catch it
import java.io.*; 
import java.util.*; 
import java.security.*;

/** A program made for the use of auction sellers to register themselves
/  as clients and start listing/ammending auctions
*/

public class sellClient {
	Scanner scan = new Scanner(System.in);
	String name;
	String email;
	
	public static void main(String[] args){
		sellClient bob = new sellClient();
	}	

	/**
	* Constructor for the sellClient class 
	* used to create a new client program that can create/ammend auctions
	* runs the actionGui method which allows users to utilize thhe program gui
	*/	
	public sellClient() {
		auctionGui();
	}
	
	/**
	* Method for creating new auctions if called via input
	* takes parameters in order to create an auction for a specified user
	* @param  l  the auction interface being used for the auction house
	* @param  b  the client requesting the creation of a new auction
	*/
	public void addAuction(auctionInterface l,auctionClient b){
		String desc = null;
		b.setStartPrice(getStartPrice());
		b.setReservePrice(getMinPrice());

		try{
			System.out.println("--------------------------------------------");
			System.out.println("\nPlease enter a description for the item: ");
			System.out.println("--------------------------------------------\n");
			desc = scan.nextLine();
			l.createAuction(b);
		}
		catch (RemoteException re){
			System.out.println();
			System.out.println("RemoteException");
			System.out.println(re);
		}
	}
		
	/**
	* Method for closing auctions if called via input
	* takes parameters in order to close an auction for a specified user
	* @param  l  the auction interface being used for the auction house
	* @param  b  the client requesting the creation of a new auction
	*/
	public void closeAuction(auctionInterface l,auctionClient b){
		try{
			b.setAuctionID(getCloseID());
			l.endAuction(b);
		}
		catch (RemoteException re){
			System.out.println();
			System.out.println("RemoteException");
			System.out.println(re);
		}
	}
		
	/**
	* Method for retrieving the start price for an auction via input
	*/
	public Double getStartPrice(){
		while (true){
			double startPrice = 0;
			System.out.println("--------------------------------------------");
			System.out.println("Please enter the starting price for the item: ");
			System.out.println("--------------------------------------------\n");
			String line = scan.nextLine();
			try{
				 startPrice = Double.parseDouble(line);
			}
			catch(Exception e){
				System.out.println("\nPlease enter a valid number such as 5.00!");
				getStartPrice();
			}

			return startPrice;
		}
	}
	
	/**
	* Method for retrieving the auction ID
	* for an auction a user would like to close via input
	*/
	public int getCloseID(){
		while (true){
			int auctID = 0;
			System.out.println("--------------------------------------------");
			System.out.println("Please enter the ID of the auction you would like to remove: ");
			System.out.println("--------------------------------------------\n");
			String line = scan.nextLine();
			try{
				 auctID = Integer.parseInt(line);
			}
			catch(Exception e){
				System.out.println("\nPlease enter a valid number such as 5");
				getCloseID();
			}

			return auctID;
		}
	}
	
	/**
	* Method for retrieving the reserve price for an auction  via input
	*/
	public Double getMinPrice(){
		while (true){
			double minPrice = 0;
			System.out.println("--------------------------------------------");
			System.out.println("Please enter the reserve price for the item: ");
			System.out.println("--------------------------------------------\n");
			String line = scan.nextLine();
			try{
				 minPrice = Double.parseDouble(line);
			}
			catch(Exception e){
				System.out.println("\nPlease enter a valid number such as 5.00!");
				getMinPrice();
			}

			return minPrice;
		}
	}
	
	/**
	* Method for retrieving the users name via input
	*/
	public String getName(){
		String tempName = null;
		System.out.println("--------------------------------------------");
		System.out.println("Please enter your name: ");
		System.out.println("--------------------------------------------\n");
		try{
			 tempName = scan.nextLine();
		}
		catch(Exception e){
			System.out.println("\nPlease enter a valid name");
			getName();
		}

		return tempName;
	}
	/**
	* Method for retrieving the users email via input
	*/
	public String getEmail(){
		String tempEmail = null;
		System.out.println("--------------------------------------------");
		System.out.println("Please enter your email: ");
		System.out.println("--------------------------------------------\n");
		try{
			 tempEmail = scan.nextLine();
		}
		catch(Exception e){
			System.out.println("\nPlease enter a valid email");
			getEmail();
		}

		return tempEmail;
	}
	
	/**
	* Method for retrieving the state the user would like to enter via input
	*/
	public int getState(){
		while (true){
			int state = 0;
			System.out.println("--------------------------------------------");
			System.out.println("Please enter 1 if you want to create an auction and 2 if you want to close your auction");
			System.out.println("--------------------------------------------\n");
			String line = scan.nextLine();
			try{
				 state = Integer.parseInt(line);
				 
				 if(state == 1 || state == 2){
					 return state;
				 }
			}
			catch(Exception e){
				System.out.println("\nYou're really trying to wind me up... i don't like you");
				System.out.println("\nPlease enter 1 or 2");
			}
		}
	}
	
	/**
	* Welcomes users to the auction house and allows 
	* them to choose whether to add or close an auction
	*/
	public void auctionGui(){
		try{
			System.out.println("--------------------------------------------");
			System.out.println("\nWelcome to the seller auction system");
			System.out.println("--------------------------------------------\n");
			
			name = getName();
			email = getEmail();
			
			auctionInterface a = (auctionInterface)Naming.lookup("rmi://localhost/Auctions");
			auctionClient user = new auctionClient(name,email);
			
			if(a.userCheck(user) == true){
				user.requestChallenge("signMeServer.txt");
				a.genSig(user);
				a.getChallenge();
				user.genSig(a);
				
				if(user.verSig(a) == true && a.verSig(user) == true){
					System.out.println("--------------------------------------------");
					System.out.println("Successful 5 way verification!");
					System.out.println("--------------------------------------------\n");
					
					int state = getState();
			
					if(state == 1){
						addAuction(a,user);
					}
					else if (state == 2){
						closeAuction(a,user);
					}	
				}
				else{
					System.out.println("--------------------------------------------");
					System.out.println("Failed to authorize communication");
					System.out.println("--------------------------------------------\n");
				}
			}
		}
		catch (MalformedURLException murle){
				System.out.println();
				System.out.println("MalformedURLException");
				System.out.println(murle);
		}
		catch (RemoteException re){
			System.out.println();
			System.out.println("RemoteException");
			System.out.println(re);
		}
		catch (NotBoundException nbe){
			System.out.println();
			System.out.println("NotBoundException");
			System.out.println(nbe);
		}
		catch (java.lang.ArithmeticException ae){
			System.out.println();
			System.out.println("java.lang.ArithmeticException");
			System.out.println(ae);
		}
	}
}
