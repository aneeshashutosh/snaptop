package api;
import java.util.ArrayList;
import java.util.Arrays;

import json.JSONException;
import json.JSONObject;

public class Story implements JSONBinder<Story>, Comparable<Story> {

	public static int TYPE_IMAGE = 0;
	public static int TYPE_VIDEO = 1;
	public static int TYPE_VIDEO_NOAUDIO = 2;

	private static final String STORY_KEY = "story";

	private static final String MEDIA_ID_KEY = "media_id";
	private static final String MEDIA_KEY_KEY = "media_key";
	private static final String MEDIA_IV_KEY = "media_iv";
	private static final String MEDIA_TYPE_KEY = "media_type";
	private static final String SENDER_KEY = "username";
	private static final String TIME_KEY = "time";
	private static final String TIME_LEFT_KEY = "time_left";
	private static final String CAPTION_KEY = "caption_text_display";

	private String id;
	private String media_key;
	private String media_iv;
	private String sender;
	private int type;
	private int time;
	private int time_left;

	private String caption;

	public Story() { }

	public Story bind(JSONObject obj)
	{
		try
		{
			this.id = obj.getString(MEDIA_ID_KEY);
			this.media_key = obj.getString(MEDIA_KEY_KEY);
			this.media_iv = obj.getString(MEDIA_IV_KEY);
			this.type = obj.getInt(MEDIA_TYPE_KEY);
			this.sender = obj.getString(SENDER_KEY);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return this;
		}

		try
		{
			this.caption = obj.getString(CAPTION_KEY);
		}
		catch (JSONException e)
		{
			this.caption = "";
		}

		try
		{
			this.time = obj.getInt(TIME_KEY);
			this.time_left = obj.getInt(TIME_LEFT_KEY);
		}
		catch (JSONException e)
		{
			return this;
		}

		return this;
	}

	public static Story[] filterDownloadable(Story[] input)
	{
		ArrayList<Story> result = new ArrayList<Story>();
		for (Story s : input)
		{
			result.add(s);
		}
		return result.toArray(new Story[result.size()]);
	}

	public boolean isImage()
	{
		return (type == TYPE_IMAGE);
	}

	public boolean isVideo()
	{
		return (type == TYPE_VIDEO || type == TYPE_VIDEO_NOAUDIO);
	}

	public boolean isMedia()
	{
		return (type <= TYPE_VIDEO);
	}

	public String getId()
	{
		return id;
	}

	public String getMediaKey()
	{
		return media_key;
	}

	public String getMediaIV()
	{
		return media_iv;
	}

	public String getSender()
	{
		return sender;
	}

	public int getType()
	{
		return type;
	}

	public int getTime()
	{
		return time;
	}

	public int getTimeLeft()
	{
		return time_left;
	}

	public String getCaption()
	{
		return caption;
	}

	public String toString()
	{
		String[] attrs = new String[]
				{
				id,
				sender,
				Integer.toString(type),
				Integer.toString(time),
				Integer.toString(time_left),
				caption
				};
		return Arrays.toString(attrs);
	}

	public int compareTo(Story other)
	{
		return this.sender.compareTo(other.sender);
	}
}