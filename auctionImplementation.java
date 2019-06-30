import java.io.*; 
import java.util.*; 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.*;  
import java.util.Scanner;
import java.nio.file.*;
import javax.crypto.*;
import java.security.*;
import javax.crypto.spec.*;
import java.security.spec.*;

/** An implementation class for the auction interface that
/  provides methods to complete each task
*/

public class auctionImplementation extends java.rmi.server.UnicastRemoteObject implements auctionInterface 
{
	private int counter = 0;
	private List<auction> auctions = new ArrayList<auction>();
	private List<auctionClient> users = new ArrayList<auctionClient>();
	PrivateKey privKey;
	PublicKey pubKey;
	challenge serverChallenge;
	response serverResponse;
   
	
    public auctionImplementation(String challengePath) throws java.rmi.RemoteException 
	{
        super();
		
		try
		{
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
			
			keyGen.initialize(1024, random);
			
			KeyPair pair = keyGen.generateKeyPair();
			
			privKey = pair.getPrivate();
			
			pubKey = pair.getPublic();

			serverChallenge = new challenge(challengePath);
			
		}
		catch(Exception h)
		{
			System.out.println("Could not generate a secure key pair");
		}	 
    }
	
	/**
	* Implementation for the createAuction method in the auction interface
	* allows an auction to be created when provided with the correct parameters
	*
	*@params seller the client intending on creating a new auction
	*@params startingPrice the starting price intended for the new auction item
	*@params itemDescription the decription for the new auction item
	*@params reservePrice the reserve price intended for the new auction item
	*
	*@return returns the id of the new auction
	*/
    public int createAuction(auctionClient seller) throws java.rmi.RemoteException
	{
		auctions.add(new auction(seller,seller.getStartPrice(),seller.getDescription(),seller.getReservePrice()));
		
		auctions.get(counter).setActive();
		auctions.get(counter).setID(counter);
		
		System.out.println("--------------------------------------------");
		System.out.println("Auction successfully listed by " + auctions.get(counter).getOwnerName() + ", your auction ID for this transaction is " + auctions.get(counter).getID());
		System.out.println("--------------------------------------------\n");
		
		counter++;
		
		return 	auctions.get((counter - 1)).getID();
	}
	
	/**
	* Implementation for the endAuction method in the auction interface
	* allows an auction to be closed when provided with the correct parameters
	*
	* Parses the array of auctions and cross references whether the 
	* user attempting to close the auction owns the auction	
	*
	*@params seller the client intending on closing an auction
	*@params auctionID the id for the auction being closed
	*
	*/
    public void endAuction(auctionClient seller) throws java.rmi.RemoteException	
	{
		int len = auctions.size();
		String output = "none";
		
		for(int i=0; i<len; i++) 
		{
			if (auctions.get(i).getOwnerName().equals(seller.getName())) 
			{
				if(auctions.get(i).getID() == seller.getAuctionID() && auctions.get(i).getActive() == true) 
				{
					auctions.get(i).setInactive();
					
					System.out.println("--------------------------------------------");
					System.out.println("Auction " + auctions.get(i).getID() + " was successfully closed");
					System.out.println("--------------------------------------------\n");
					
					if(auctions.get(i).metStartPrice() > 0 && seller.getEmail() != auctions.get(i).getOwnerEmail())
					{
						output = ("The auction was won by " + auctions.get(i).getHighestBidderName() + " with the winning bid of " +"\u00A3"+ auctions.get(i).getCurrentPrice());
					}
					else
					{
						output = ("The reserve price of "+"\u00A3" + auctions.get(i).getReservePrice() + " was not met, the auction is now closed");
					}		
					break;
				}
			}
			else
			{
				output = "The auction could not be found using the ID provided"; 
			}		
		}	
		
		System.out.println("--------------------------------------------");
		System.out.println(output);
		System.out.println("--------------------------------------------\n");
    }
	
	/**
	* Implementation for the auctionBid method in the auction interface
	* allows clients to bid on an existing auction when provided with the correct parameters
	*
	*@params buyer the client intending on bidding on an auction
	*@params auctionID the id for the auction being bid on
	*@params bidPrice the intended bid price for the auction item
	*
	*/
	public void auctionBid(auctionClient buyer) throws java.rmi.RemoteException
	{	
		if(auctions.isEmpty() == false)
		{
			if(auctions.get(buyer.getAuctionID()).getID() == buyer.getAuctionID() && auctions.get(buyer.getAuctionID()).getActive() == true)
			{
				if(buyer.getItemBid() > auctions.get(buyer.getAuctionID()).getCurrentPrice())
				{
					System.out.println("--------------------------------------------");
					System.out.println("The highest bid on auction " + buyer.getAuctionID() + " was: " +"\u00A3" + auctions.get(buyer.getAuctionID()).getCurrentPrice());
					System.out.println("Your bid was successful on auction " + buyer.getAuctionID() + ",the current bid for the item is now: " + "\u00A3" + buyer.getItemBid() + " by " + buyer.getName());
					System.out.println("--------------------------------------------\n");
					
					auctions.get(buyer.getAuctionID()).addBid(buyer.getItemBid());
					auctions.get(buyer.getAuctionID()).setHighestBidderName(buyer.getName());
					auctions.get(buyer.getAuctionID()).setHighestBidderEmail(buyer.getEmail());
					buyer.setAuctions(buyer.getAuctionID());
				}
				
				else
				{
					System.out.println("--------------------------------------------");
					System.out.println("The current highest bid is: " +"\u00A3"+ auctions.get(buyer.getAuctionID()).getCurrentPrice());
					System.out.println("Your bid was too low, please try again and place a higher bid " + buyer.getName());
					System.out.println("--------------------------------------------\n");
				}
			}
		}	
		else
		{
			System.out.println("--------------------------------------------");
			System.out.println("\nThere are no current auctions with the ID provided, please try again later");
			System.out.println("--------------------------------------------\n");	
		}
	} 
	
	/**
	* Implementation for the userAuctionsDisplay method in the auction interface
	* displays the auction bids of a given client
	*
	*@params buyer the client whose auctions are being retrieved
	*@return an integer array consisting of the auction indexes owned by the client
	*/
	public int [] userAuctionsDisplay(auctionClient buyer) throws java.rmi.RemoteException
	{
		int [] hold = buyer.getAuctions();
		
		int counter = 0;
		int len = auctions.size();
	
		for(int i=0; i<len; i++) 
		{
			if(auctions.isEmpty() == false)
			{
				if ((auctions.get(i).getHighestBidderName()).equals(buyer.getName()) && (auctions.get(i).getHighestBidderEmail()).equals(buyer.getEmail())) 
				{
					hold[counter] = auctions.get(i).getID();
					counter++;
					
				}					
			}
			else
			{
				System.out.println("There were no auctions to retrieve");
			}
		}
		return hold;
	}
	/**
	* Looks to see if the required user exists in the database
	* @params user that is requested 
	* @return userExists returns true if the user has been found 
	*/
	public boolean userCheck(auctionClient user) throws java.rmi.RemoteException
    {
		boolean userExists = false;
		
		if(users.isEmpty() == true)
		{
			System.out.println("--------------------------------------------");
			System.out.println("The first user has been registered in the database");
			System.out.println("--------------------------------------------\n");
			users.add(user);
			userExists = true;
		}

		else
		{
			for(int i = 0;i < users.size(); i++)
			{

			   if((users.get(i)).getUserID() == user.getUserID())
			   {
					userExists = true;
					System.out.println("--------------------------------------------");
					System.out.println("User is registered in the database");
					System.out.println("--------------------------------------------\n");
			   }
			   
			   else
			   {
			   		System.out.println("--------------------------------------------");
					System.out.println("User has been added to the database");
					System.out.println("--------------------------------------------\n");
					users.add(user);
					userExists = false;
			   } 
			}
		}
		
		return userExists;
    }
	/**
	* Generates a signiture for a users challenge
	* @params userCheck user that generates the signiture
	*/	
	public void genSig(auctionClient userCheck) throws java.rmi.RemoteException
	{
		try
		{
			File challenge = (userCheck.getChallenge()).getChallengeFile();
			
			Signature dsa = Signature.getInstance("SHA1withDSA", "SUN"); 
			
			dsa.initSign(privKey);
			
			FileInputStream fis = new FileInputStream(challenge);
			BufferedInputStream bufin = new BufferedInputStream(fis);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = bufin.read(buffer)) >= 0) {
				dsa.update(buffer, 0, len);
			};
			bufin.close();
			
			byte[] realSig = dsa.sign();

			serverResponse = new response(realSig);

		}
		
		catch(Exception e)
		{
			System.out.println("Could not respond to the challenge");
		}	
	}
	
	/**
	* Atempts to authenticate a user
	* @params user that is being vetted 
	* @return verifies returns true if the signiture maches 
	*/	
	public boolean verSig(auctionClient user) throws java.rmi.RemoteException
	{
		boolean verifies = false;
		
		try
		{
			//Writes the server signed file onto local area of client
			byte[] userResponse = ((user.getResponse()).getResponseFile());
			FileOutputStream responsefos = new FileOutputStream("userResponse");
			responsefos.write(userResponse);
			responsefos.close();

			//Writes the server public key onto local area of client
			byte[] userPublicKey = (user.getPublicKey()).getEncoded();
			FileOutputStream keyfos = new FileOutputStream("userPublicKey");
			keyfos.write(userPublicKey);
			keyfos.close();

			//Reads in server public key file
			FileInputStream keyfis = new FileInputStream("userPublicKey");
            byte[] encKey = new byte[keyfis.available()];  
            keyfis.read(encKey);
 
            keyfis.close();
 
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
 
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
 
            /* input the signature bytes */
            FileInputStream sigfis = new FileInputStream("userResponse");
            byte[] sigToVerify = new byte[sigfis.available()]; 
            sigfis.read(sigToVerify );
	
            sigfis.close();
 
            /* create a Signature object and initialize it with the public key */
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pubKey);
 
            /* Update and verify the data */
 
            FileInputStream datafis = new FileInputStream("signMeUser.txt");
            BufferedInputStream bufin = new BufferedInputStream(datafis);
            byte[] buffer = new byte[1024];
            int len;
			
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                sig.update(buffer, 0, len);
                };
 
            bufin.close();
 
            verifies = sig.verify(sigToVerify);	
		}
		catch(Exception e)
		{
			
		}
		
		return verifies;
	}
	/**
	* gets the public key of the server
	* @return pubKey  
	*/
	public PublicKey getPublicKey() throws java.rmi.RemoteException
	{
		return pubKey;
	}
	/**
	* gets the servers challenge
	* @return serverChallenge
	*/
	public challenge getChallenge() throws java.rmi.RemoteException
	{
		return serverChallenge;
	}
	/**
	* gets the servers response to a challenge
	* @return serverResponse 
	*/
	public response getResponse() throws java.rmi.RemoteException
	{
		return serverResponse;
	}
}

