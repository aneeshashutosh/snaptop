package api;

import json.JSONException;
import json.JSONObject;

public class Friend implements JSONBinder<Friend>, Comparable<Friend>
{
	private static final String USERNAME_KEY = "name";
	private static final String DISPLAY_NAME_KEY = "display";
	private static final String TYPE_KEY = "type";

	private String username;
	private String displayName;
	private boolean bestFriend = false;
	private int type;

	public Friend()
	{

	}

	public Friend bind(JSONObject obj)
	{
		try
		{
			this.username = obj.getString(USERNAME_KEY);
			this.displayName = obj.getString(DISPLAY_NAME_KEY);
			this.type = obj.getInt(TYPE_KEY);
		}
		catch (JSONException e)
		{
			return this;
		}
		return this;
	}

	public String getUsername()
	{
		return username;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public int getType()
	{
		return type;
	}
	
	public void setUsername(String name)
	{
		this.username = name;
	}
	
	public String toString()
	{
		return username + " ~>  " + displayName;
	}
	
	public boolean isBestFriend()
	{
		return this.bestFriend;
	}

	public void setBestFriend(boolean bestFriend)
	{
		this.bestFriend = bestFriend;
	}
	
	public int compareTo(Friend f)
	{
		return this.displayName.compareTo(f.displayName);
	}
}