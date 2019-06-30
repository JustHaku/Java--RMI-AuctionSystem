import java.io.*;

/** An auction object used to identify auction house auctions
/  contains all the attributes an auction requires.
*/

public class auction
{
	private String ownerName;
	private String ownerEmail;
	private String description;
	private String highestBidderName = "none";
	private String highestBidderEmail = "none";
	private int auctionID;
	private double startPrice;
	private double minPrice;
	private boolean active;
	
	/**
	* Constructor for the auction class 
	* used to create a new auction object for the auction client
	* @param  owner  refers to the creator of each auction
	* @param  startingPrice  gives the starting price for the item listed in the auction
	* @param  itemDescription  gives the description of the item lised in the auction
	* @param  reservePrice gives the starting price for the item listed in the auction
	*/
	public auction(auctionClient owner,double startingPrice,String itemDescription,double reservePrice)
	{
		ownerName = owner.getName();
		ownerEmail = owner.getEmail();
		description = itemDescription;
		startPrice = startingPrice;
		minPrice = reservePrice;
		active = true;
	}

	
	//various variable getters and setters
	public boolean getActive()
	{
		return active;
	}
	
	/**
	* Returns the variable in which the name of the client is stored
	* @return highestBidderName
	*/	
	public String getHighestBidderName()
	{
		return highestBidderName;
	}
	/**
	* Returns the variable in which the email of the client is stored
	* @return highestBidderEmail
	*/	
	public String getHighestBidderEmail()
	{
		return highestBidderEmail;
	}
	/**
	* Sets the name of the client 
	* @param  name of the client
	*/	
	public void setHighestBidderName(String name)
	{
		highestBidderName =name;
	}
	/**
	* Sets the email of the client 
	* @param  email of the client
	*/	
	public void setHighestBidderEmail(String email)
	{
		highestBidderEmail = email;
	}
	/**
	* Returns the name of the owner of the listed item
	* @return ownerName
	*/
	public String getOwnerName()
	{
		return ownerName;
	}
	/**
	* Returns the email of the owner of the listed item
	* @return ownerEmail
	*/
	public String getOwnerEmail()
	{
		return ownerEmail;
	}
	/**
	* Returns the ID of the auction
	* @return auctionID
	*/
	public int getID()
	{
		return auctionID;
	}
	/**
	* Sets the auction active to accept incoming bids 
	*/
	public void setActive()
	{
		active = true;
	}
	/**
	* Stops the auction from accepting bids
	*/
	public void setInactive()
	{
		active = false;
	}
	/**
	* Sets the auction ID to a given value 
	* @param  ID of the action at question
	*/	
	public void setID(int ID)
	{
		auctionID = ID;
	}
	/**
	* Returns the name of the owner of the auction
	* @return ownerName
	*/
	public double getCurrentPrice()
	{
		return startPrice;
	}
	/**
	* Returns the name of the owner of the auction
	* @return ownerName
	*/
	public double getReservePrice()
	{
		return minPrice;
	}
	/**
	* Sets the starting price for the auction
	* @param  bid the starting price
	*/	
	public void addBid(double bid)
	{
		startPrice = bid;
		
	}
	/**
	* checks if the auctioned item has met the requied minimum cost
	* @return startPrice 
	*/	
	public double metStartPrice()
	{
		if(startPrice >= minPrice)
		{
			return startPrice;
		}
		else
		{
			return 0;
		}
	}
}