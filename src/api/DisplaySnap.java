package api;

import java.util.ArrayList;

public class DisplaySnap
{
	private ArrayList<Snap> snaps;
	private String username;
	private String displayName;
	
	public DisplaySnap(ArrayList<Snap> snaps, String username, String displayName)
	{
		this.snaps = snaps;
		this.username = username;
		this.displayName = displayName;
	}
	
	public ArrayList<Snap> getSnaps()
	{
		return this.snaps;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public String getDisplayName()
	{
		return this.displayName;
	}
}
