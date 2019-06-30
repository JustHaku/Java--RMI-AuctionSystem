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

/** Basic interface that extends rmi remote
/  contains methods to create and ammend auctions
/  also contains method to display a user's auctions 
*/

public interface auctionInterface extends java.rmi.Remote  
{	
    public int createAuction(auctionClient seller) throws java.rmi.RemoteException;

    public void endAuction(auctionClient seller) throws java.rmi.RemoteException;
		
	public void auctionBid(auctionClient buyer) throws java.rmi.RemoteException;

	public int [] userAuctionsDisplay(auctionClient buyer) throws java.rmi.RemoteException;		
	
	public boolean userCheck(auctionClient userCheck) throws java.rmi.RemoteException;	
	
	public void genSig(auctionClient userCheck) throws java.rmi.RemoteException;	
	
	public boolean verSig(auctionClient userCheck) throws java.rmi.RemoteException;
	
	public PublicKey getPublicKey() throws java.rmi.RemoteException;

	public challenge getChallenge() throws java.rmi.RemoteException;

	public response getResponse() throws java.rmi.RemoteException;
}

