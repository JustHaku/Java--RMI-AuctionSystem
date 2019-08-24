import java.io.*;
import java.util.*;  
import java.util.Scanner;
import java.nio.file.*;
import javax.crypto.*;
import java.security.*;
import javax.crypto.spec.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import java.security.*;
import java.security.spec.*;

/** An auction client class that contains all the information
/  needed to create a client and store their auctions.
*/


public class auctionClient implements Serializable{
	private static final AtomicInteger idCounter = new AtomicInteger(0); 
	private static int userID;
	private String name;
	private String email;
	private int counter = 0;
    static final long serialVersionUID =-6135993888317510268L;
	private int [] hold = new int[10];
	private double startBid;
	private double minReserve;
	private String desc;
	private int auctionID;
	private int bidID;
	private double itemBid;
	PrivateKey privKey;
	PublicKey pubKey;
	challenge userChallenge;
	response userResponse;
	

	/**
	* Constructor for the auctionClient class 
	* used to create a new client that can create/bid on auctions
	* @param  tName  the name of the user piloting the client
	* @param  tEmail  the email of the user piloting the client
	*/
	
	public auctionClient(String tName,String tEmail){
		name = tName;
		email = tEmail;
		
		try{
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
			keyGen.initialize(1024, random);
			KeyPair pair = keyGen.generateKeyPair();
			privKey = pair.getPrivate();
			pubKey = pair.getPublic();
			
		}
		catch(Exception h){
			System.out.println("Could not generate a secure key pair");
		}
		
		userID = idCounter.incrementAndGet();
	}

	/**
	* Gets the name of the clien 
	* @return name of the client 
	*/
	public String getName(){
		return name;
	}
	/**
	* Returns the ID of the client
	* @return userID ID of the client
	*/	
	public int getUserID(){
		return userID;
	}
	/**
	* Gets the current price of an item
	* @return getItemBid item price
	*/	
	public double getItemBid(){
		return itemBid;
	}
	/**
	* Gets the users email adress 
	* @return email users email
	*/	
	public String getEmail(){
		return email;
	}
	/**
	* Holds the set of the users auctions
	* @return hold set autions
	*/	
	public int[] getAuctions(){
		return hold;
	}
	/**
	* Gets the initial (starting) price for the item
	* @return startBid initial price
	*/	
	public double getStartPrice(){
		return startBid;
	}
	/**
	* gets the reserve price for an item
	* @return minReserve reserve prince
	*/	
	public double getReservePrice(){
		return minReserve;
	}
	/**
	* Gets the description of the item in question
	* @return desc desription of the item
	*/	
	public String getDescription(){
		return desc;
	}
	/**
	* Gets the ID for the action in question
	* @return auctionID
	*/	
	public int getAuctionID(){
		return auctionID;
	}
	/**
	* 
	* @return userResponse
	*/	
	public response getResponse(){
		return userResponse;
	}
	/**
	* 
	* @return pubKey
	*/	
	public PublicKey getPublicKey(){
		return pubKey;
	}
	/**
	* 
	* @return userChallenge
	*/	
	public challenge getChallenge(){
		return userChallenge;
	}
	/**
	* 
	* @param bid
	*/	
	public void setStartPrice(double bid){
		startBid = bid;
	}
	/**
	*
	* @param  aucBid
	*/
	public void setItemBid(double aucBid){
		itemBid = aucBid;
	}
	/**
	*
	* @param  aucID
	*/
	public void setAuctionID(int aucID){
		auctionID = aucID;
	}
	/**
	*
	* @param  reserveBid
	*/	
	public void setReservePrice(double reserveBid){
		minReserve = reserveBid;
	}
	/**
	*
	* @param  decription
	*/	
	public void setDescription(String decription){
		desc = decription;
	}
	/**
	*
	* @param  newName
	*/	
	public void setName(String newName){
		name = newName;
	}
	/**
	*
	* @param  newEmail
	*/
	public void setEmail(String newEmail){
		email = newEmail;
	}	
	/**
	*
	* @param  auctID
	*/
	public void setAuctions(int auctID){
		hold[counter] = auctID;
		counter++;
	}
	/**
	*
	* @param  path
	*/	
	public void requestChallenge(String path){
		userChallenge = new challenge(path);
	}
	
	/**
	* Generates a signature for a requested challenge
	* 
	*/	
	public void genSig(auctionInterface a) throws java.rmi.RemoteException{
		try{
			File challenge = (a.getChallenge()).getChallengeFile();
			Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
			dsa.initSign(privKey);
			FileInputStream fis = new FileInputStream(challenge);
			BufferedInputStream bufin = new BufferedInputStream(fis);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = bufin.read(buffer)) >= 0){
				dsa.update(buffer, 0, len);
			};

			bufin.close();
			byte[] realSig = dsa.sign();
			userResponse = new response(realSig);
		}
		catch(Exception e){
			System.out.println("Could not respond to the challenge");
		}	
	}
	
	/**
	* verifies the signature of a file by another object
	* @returns boolean verifies
	*/	
	public boolean verSig(auctionInterface a){
		boolean verifies = false;
		
		try{
			//Writes the server signed file onto local area of client
			byte[] serverResponse = ((a.getResponse()).getResponseFile());
			FileOutputStream responsefos = new FileOutputStream("serverResponse");
			responsefos.write(serverResponse);
			responsefos.close();

			//Writes the server public key onto local area of client
			byte[] serverPublicKey = (a.getPublicKey()).getEncoded();
			FileOutputStream keyfos = new FileOutputStream("serverPublicKey");
			keyfos.write(serverPublicKey);
			keyfos.close();

			//Reads in server public key file
			FileInputStream keyfis = new FileInputStream("serverPublicKey");
            byte[] encKey = new byte[keyfis.available()];  
            keyfis.read(encKey);
 
            keyfis.close();
 
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
 
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
 
            /* input the signature bytes */
            FileInputStream sigfis = new FileInputStream("serverResponse");
            byte[] sigToVerify = new byte[sigfis.available()]; 
            sigfis.read(sigToVerify );
	
            sigfis.close();
 
            /* create a Signature object and initialize it with the public key */
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pubKey);
 
            /* Update and verify the data */
 
            FileInputStream datafis = new FileInputStream("signMeServer.txt");
            BufferedInputStream bufin = new BufferedInputStream(datafis);
            byte[] buffer = new byte[1024];
            int len;
			
            while (bufin.available() != 0){
                len = bufin.read(buffer);
                sig.update(buffer, 0, len);
            };
 
            bufin.close();
 
            verifies = sig.verify(sigToVerify);	
		}
		catch(Exception e){
			
		}
		
		return verifies;
	}
}