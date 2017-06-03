import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;

import org.apache.commons.io.IOUtils;

import api.Encryption;
import api.Friend;
import api.JSONBinder;
import api.Snap;
import api.Story;
import api.StoryEncryption;
import api.TokenLib;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;

/**
 * Author: samstern
 * Date: 12/29/13
 */
public class SnapchatAPI {

	/**
	 * Last response received.  Used for error reporting.
	 */
	public static String lastRequestPath;
	public static HttpResponse lastResponse;
	public static Class lastResponseBodyClass;

	/**
	 * POST parameter keys for sending requests to Snapchat.
	 */
	public static final String USERNAME_KEY = "username";
	public static final String PASSWORD_KEY = "password";
	public static final String TIMESTAMP_KEY = "timestamp";
	public static final String REQ_TOKEN_KEY = "req_token";
	public static final String AUTH_TOKEN_KEY = "auth_token";
	public static final String ID_KEY = "id";
	public static final String SNAPS_KEY = "snaps";
	public static final String MY_STORIES_KEY = "my_stories";
	public static final String FRIENDS_STORIES_KEY = "friend_stories";
	public static final String FRIENDS_KEY = "friends";
	public static final String MEDIA_ID_KEY = "media_id";
	public static final String BEST_KEY = "bests";
	public static final String CLIENT_ID_KEY = "client_id";
	public static final String CAPTION_TEXT_DISPLAY_KEY = "caption_text_display";
	public static final String TYPE_KEY = "type";
	public static final String DATA_KEY = "data";
	public static final String ZIPPED_KEY = "zipped";
	public static final String TIME_KEY = "time";
	public static final String RECIPIENT_KEY = "recipient";
	public static final String ADDED_FRIENDS_TIMESTAMP_KEY = "added_friends_timestamp";
	public static final String JSON_KEY = "json";
	public static final String EVENTS_KEY = "events";
	public static final String ACTION_KEY = "action";

	/**
	 * Paths for various Snapchat actions, relative to BASE_URL.
	 */
	public static final String LOGIN_PATH = "bq/login";
	public static final String UPLOAD_PATH = "bq/upload";
	public static final String SEND_PATH = "ph/send";
	public static final String STORY_PATH = "bq/post_story";
	public static final String DOUBLE_PATH = "bq/double_post";
	public static final String BLOB_PATH = "ph/blob";
	public static final String FRIEND_PATH = "ph/friend";
	public static final String CHAT_PATH = "loq/conversations";
	public static final String CLEAR_SNAPS_PATH = "/ph/clear";
	public static final String CLEAR_CHAT_PATH = "/loq/clear_feed";
	//EXTRAS by LIAM COTTLE
	public static final String FRIEND_STORIES_PATH = "bq/stories";
	public static final String STORY_BLOB_PATH = "bq/story_blob";
	public static final String UPDATE_SNAPS_PATH = "bq/update_snaps";

	/**
	 * Static action members for forming friend requests.
	 */

	public static final String ADD = "add";
	public static final String DELETE = "delete";
	public static final String BLOCK = "block";
	public static final String UNBLOCK = "unblock";
	public static final String DISPLAY = "display";

	//Parameter
	public static final String FRIEND = "friend";
	public static final String DISPLAY_NAME = "display";


	/**
	 * Static members for forming HTTP requests.
	 */
	public static final String BASE_URL = "https://feelinsonice-hrd.appspot.com/";
	private static final String JSON_TYPE_KEY = "accept";
	private static final String JSON_TYPE = "application/json";
	private static final String USER_AGENT_KEY = "User-Agent";
	private static final String USER_AGENT = "Snapchat/3.0.2 (Nexus 4; Android 18; gzip)";

	/**
	 * Log in to Snapchat.
	 *
	 * @param username the Snapchat username.
	 * @param password the Snapchat password.
	 * @return the entire JSON login response.
	 */
	public static JSONObject login(String username, String password)
	{
		Map<String, Object> params = new HashMap<String, Object>();

		// Add username and password
		params.put(USERNAME_KEY, username);
		params.put(PASSWORD_KEY, password);

		// Add timestamp and requestJson token made using static auth token
		Long timestamp = getTimestamp();
		String reqToken = TokenLib.staticRequestToken(timestamp);

		params.put(TIMESTAMP_KEY, timestamp.toString());
		params.put(REQ_TOKEN_KEY, reqToken);

		try
		{
			HttpResponse<JsonNode> resp = requestJson(LOGIN_PATH, params, null);
			JSONObject obj = resp.getBody().getObject();

			Globals.loginObj = obj;

			Globals.username = username;
			Globals.setFriends(SnapchatAPI.getFriends());
			Globals.initializeViewingFrame();
			Globals.snaps = SnapchatAPI.getSnaps();
			Globals.downloadableSnaps = Snap.filterDownloadable(SnapchatAPI.getSnaps());
			Globals.token = Globals.loginObj.getString(SnapchatAPI.AUTH_TOKEN_KEY);
			Globals.setStories(SnapchatAPI.getStories());
			Globals.setBestFriends(getBests());

			return obj;
		}
		catch (UnirestException e)
		{
			return null;
		}
	}

	/**
	 * Get Friends Stories from Snapchat. Added by Liam Cottle.
	 *
	 * @param username the Snapchat username.
	 * @param authToken the authToken from Loggin in.
	 * @return a Story[].
	 *
	 */

	public static Story[] getStories()
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(USERNAME_KEY, Globals.username);
		Long timestamp = getTimestamp();
		params.put(TIMESTAMP_KEY, timestamp);
		params.put(REQ_TOKEN_KEY, TokenLib.requestToken(Globals.token, timestamp));

		try {
			HttpResponse<JsonNode> resp = requestJson(FRIEND_STORIES_PATH, params, null);
			JSONObject obj = resp.getBody().getObject();
			JSONArray storyArr = obj.getJSONArray(FRIENDS_STORIES_KEY);

			JSONArray storiesArray = new JSONArray();

			for (int i=0; i<storyArr.length(); i++) {
				JSONArray items = storyArr.getJSONObject(i).getJSONArray("stories");
				for (int j=0; j<items.length(); j++) {
					JSONObject _story = items.getJSONObject(j).getJSONObject("story");
					storiesArray.put(_story);
				}
			}

			List<Story> resultList = bindArray(storiesArray, Story.class);
			return resultList.toArray(new Story[resultList.size()]);
		} catch (Exception e) {
			return new Story[0];
		}
	}

	/**
	 * Get an array of your Snap objects.
	 *
	 * @param loginObject the JSONObject received after login().
	 * @return a Snap[].
	 */
	public static Snap[] getSnaps()
	{
		try
		{
			JSONArray snapArr = Globals.loginObj.getJSONArray(SNAPS_KEY);
			List<Snap> resultList = bindArray(snapArr, Snap.class);
			return resultList.toArray(new Snap[resultList.size()]);
		}
		catch (JSONException e)
		{
			return new Snap[0];
		}
	}

	/**
	 * Get an array of your Friend objects.
	 *
	 * @param loginObject the JSONObject received after login().
	 * @return a Friend[].
	 */
	public static Friend[] getFriends()
	{
		try
		{
			JSONArray friendsArr = Globals.loginObj.getJSONArray(FRIENDS_KEY);
			List<Friend> resultList = bindArray(friendsArr, Friend.class);
			Friend[] friends = resultList.toArray(new Friend[resultList.size()]);
			return friends;
		}
		catch (JSONException e)
		{
			return new Friend[0];
		}
	}
	
	public static ArrayList<Friend> getBests()
	{
		try
		{
			ArrayList<String> bests = new ArrayList<String>();
			JSONArray friendsArr = Globals.loginObj.getJSONArray(BEST_KEY);
			for (int i = 0; i < friendsArr.length(); i++)
			{
				bests.add(friendsArr.getString(i));
			}
			
			ArrayList<Friend> bestFriends = new ArrayList<Friend>();
			for (int i = 0; i < bests.size(); i++)
			{
				for (int j = 0; j < Globals.getFriends().length; j++)
				{
					if (bests.get(i).equals(Globals.getFriends()[j].getUsername()))
					{
						Globals.getFriends()[j].setBestFriend(true);
						bestFriends.add(Globals.getFriends()[j]);
					}
				}
			}
			
			return bestFriends;
		}
		catch (JSONException e)
		{
			return new ArrayList<Friend>(0);
		}
	}

	private static <T> List<T> bindArray(JSONArray arr, Class<? extends JSONBinder<T>> clazz)
	{
		try
		{
			int length = arr.length();
			List<T> result = new ArrayList<T>();
			for (int i = 0; i < length; i++)
			{
				JSONObject obj = arr.getJSONObject(i);
				T bound = clazz.newInstance().bind(obj);
				result.add(bound);
			}
			return result;
		}
		catch (JSONException e)
		{
			return new ArrayList<T>();
		}
		catch (InstantiationException e)
		{
			return new ArrayList<T>();
		}
		catch (IllegalAccessException e)
		{
			return new ArrayList<T>();
		}
	}

	/**
	 * Download and un-encrypt a Snap from the server.
	 *
	 * @param snap the Snap to download.
	 * @param username your Snapchat username.
	 * @param authToken the auth_token you got from logging in.
	 * @return a byte[] containing decrypted image or video data.
	 */
	public static byte[] getSnap(Snap snap)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(USERNAME_KEY, Globals.username);
		Long timestamp = getTimestamp();
		params.put(TIMESTAMP_KEY, timestamp);
		params.put(REQ_TOKEN_KEY, TokenLib.requestToken(Globals.token, timestamp));
		params.put(ID_KEY, snap.getId());

		try
		{
			HttpResponse<InputStream> resp = requestBinary(BLOB_PATH, params, null);
			InputStream is = resp.getBody();
			byte[] encryptedBytes = IOUtils.toByteArray(is);
			byte[] decryptedBytes = Encryption.decrypt(encryptedBytes);
			return decryptedBytes;
		}
		catch (UnirestException e)
		{
			return new byte[0];
		}
		catch (IOException e)
		{
			return new byte[0];
		}
		catch (Encryption.EncryptionException e)
		{
			return new byte[0];
		}
	}

	/**
	 * Download and un-encrypt a Story from the server. Added by Liam Cottle
	 *
	 * @param story the Story to download.
	 * @param username your Snapchat username.
	 * @param authToken the auth_token you got from logging in.
	 * @return a byte[] containing decrypted image or video data.
	 */
	public static byte[] getStory(Story story)
	{
		try
		{
			HttpResponse<InputStream> resp = requestStoryBinary(STORY_BLOB_PATH + "?story_id=" + story.getId());
			InputStream is = resp.getBody();
			byte[] encryptedBytes = IOUtils.toByteArray(is);
			byte[] decryptedBytes = StoryEncryption.decrypt(encryptedBytes,story.getMediaKey(),story.getMediaIV());
			return decryptedBytes;
		}
		catch (UnirestException e)
		{
			return new byte[0];
		}
		catch (IOException e)
		{
			return new byte[0];
		}
	}

	/**
	 * Upload a file and return the media_id for sending.
	 *
	 * @param image the image file to upload.
	 * @param username your Snapchat username.
	 * @param authToken Snapchat auth token from logging in.
	 * @return the new upload's media_id.  Returns null if there is an error.
	 */
	public static String upload(File image, boolean video) throws FileNotFoundException
	{
		try
		{
			// Open file and ecnrypt it
			byte[] fileBytes = IOUtils.toByteArray(new FileInputStream(image));
			byte[] encryptedBytes = Encryption.encrypt(fileBytes);

			// Write to a temporary file
			File encryptedFile = File.createTempFile("encr", "snap");

			FileOutputStream fos = new FileOutputStream(encryptedFile);
			fos.write(encryptedBytes);
			fos.close();

			// Create other params
			Long timestamp = getTimestamp();
			String requestToken = TokenLib.requestToken(Globals.token, timestamp);
			String mediaId = SnapchatAPI.getNewMediaId(Globals.username);

			// Make parameter map
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(USERNAME_KEY, Globals.username);
			params.put(TIMESTAMP_KEY, timestamp);
			params.put(REQ_TOKEN_KEY, requestToken);
			params.put(MEDIA_ID_KEY, mediaId);
			if (video)
			{
				params.put(TYPE_KEY, 1);
			}
			else
			{
				params.put(TYPE_KEY, 0);
			}

			// Perform request and check for 200
			HttpResponse<String> resp = requestString(UPLOAD_PATH, params, encryptedFile);
			if (resp.getCode() == 200)
			{
				return mediaId;
			}
			else
			{
				System.out.println("Upload failed, Response Code: " + resp.getCode());
				return null;
			}
		}
		catch (IOException e)
		{
			System.out.println(e);
			return null;
		}
		catch (Encryption.EncryptionException e)
		{
			System.out.println(e);
			return null;
		}
		catch (UnirestException e)
		{
			System.out.println(e);
			return null;
		}
		catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
		catch(OutOfMemoryError e)
		{
			System.out.println(e);
			return null;
		}
	}

	public static String upload(BufferedImage image, boolean video) throws FileNotFoundException
	{
		try
		{
			// Open file and ecnrypt it
			byte[] fileBytes = new byte[0];
			try
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, "jpg", baos );
				baos.flush();
				fileBytes = baos.toByteArray();
				baos.close();

			}
			catch(IOException e)
			{
				System.out.println(e.getMessage());
			}
			byte[] encryptedBytes = Encryption.encrypt(fileBytes);

			// Write to a temporary file
			File encryptedFile = File.createTempFile("encr", "snap");

			FileOutputStream fos = new FileOutputStream(encryptedFile);
			fos.write(encryptedBytes);
			fos.close();

			// Create other params
			Long timestamp = getTimestamp();
			String requestToken = TokenLib.requestToken(Globals.token, timestamp);
			String mediaId = SnapchatAPI.getNewMediaId(Globals.username);

			// Make parameter map
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(USERNAME_KEY, Globals.username);
			params.put(TIMESTAMP_KEY, timestamp);
			params.put(REQ_TOKEN_KEY, requestToken);
			params.put(MEDIA_ID_KEY, mediaId);
			if(video)
			{
				params.put(TYPE_KEY, 1);
			}
			else
			{
				params.put(TYPE_KEY, 0);
			}

			// Perform request and check for 200
			HttpResponse<String> resp = requestString(UPLOAD_PATH, params, encryptedFile);
			if (resp.getCode() == 200)
			{
				return mediaId;
			}
			else
			{
				System.out.println("Upload failed, Response Code: " + resp.getCode());
				return null;
			}
		}
		catch (IOException e)
		{
			System.out.println(e);
			return null;
		}
		catch (Encryption.EncryptionException e)
		{
			System.out.println(e);
			return null;
		}
		catch (UnirestException e)
		{
			System.out.println(e);
			return null;
		}
		catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
		catch(OutOfMemoryError e)
		{
			System.out.println(e);
			return null;
		}
	}

	/**
	 * Send a snap that has already been uploaded.
	 *
	 * @param mediaId the media_id of the uploaded snap.
	 * @param recipients a list of Snapchat usernames to send to.
	 * @param story true if this should be uploaded to the sender's story as well.
	 * @param time the time (max 10) for which this snap should be visible.
	 * @param username your Snapchat username.
	 * @param authToken your Snapchat auth_token from logging in.
	 * @return true if successful, false otherwise.
	 */
	public static boolean send(String mediaId, List<String> recipients, boolean story, int time)
	{
		// Prepare parameters
		Long timestamp = getTimestamp();
		String requestToken = TokenLib.requestToken(Globals.token, timestamp);
		int snapTime = Math.min(10, time);

		// Create comma-separated recipient string
		StringBuilder sb = new StringBuilder();
		if (recipients.size() == 0)
		{
			// Can't send to nobody
			return false;
		}
		sb.append(recipients.get(0));
		for (int i = 1; i < recipients.size(); i++)
		{
			String recip = recipients.get(i);
			if (recip != null)
			{
				sb.append(",");
				sb.append(recip);
			}
		}
		String recipString = sb.toString();

		// Make parameter map
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(USERNAME_KEY, Globals.username);
		params.put(TIMESTAMP_KEY, timestamp.toString());
		params.put(REQ_TOKEN_KEY, requestToken);
		params.put(MEDIA_ID_KEY, mediaId);
		params.put(TIME_KEY, Integer.toString(snapTime));
		params.put(RECIPIENT_KEY, recipString);
		params.put(ZIPPED_KEY, "0");

		// Sending path
		String path = SEND_PATH;

		// Add to story, maybe
		if (story)
		{
			path = DOUBLE_PATH;
			params.put(CAPTION_TEXT_DISPLAY_KEY, "My Story");
			params.put(CLIENT_ID_KEY, mediaId);
			params.put(TYPE_KEY, "0");
		}

		// Execute request
		try
		{
			HttpResponse<String> resp = requestString(path, params, null);
			if (resp.getCode() == 200 || resp.getCode() == 202)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (UnirestException e)
		{
			return false;
		}
	}



	/**
	 * Set a story from media already uploaded. Added by Liam Cottle.
	 *
	 * @param mediaId the media_id of the uploaded snap.
	 * @param time the time (max 10) for which this story should be visible.
	 * @param username your Snapchat username.
	 * @param authToken your Snapchat auth_token from logging in.
	 * @return true if successful, false otherwise.
	 */
	public static boolean sendStory(String mediaId, int time, boolean video, String caption)
	{
		// Prepare parameters
		Long timestamp = getTimestamp();
		String requestToken = TokenLib.requestToken(Globals.token, timestamp);
		int snapTime = Math.min(10, time);

		// Make parameter map
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(USERNAME_KEY, Globals.username);
		params.put(TIMESTAMP_KEY, timestamp.toString());
		params.put(REQ_TOKEN_KEY, requestToken);
		params.put(MEDIA_ID_KEY, mediaId);
		params.put(CLIENT_ID_KEY, mediaId);
		params.put(TIME_KEY, Integer.toString(snapTime));
		params.put(CAPTION_TEXT_DISPLAY_KEY, caption);
		params.put(ZIPPED_KEY, "0");
		if(video)
		{
			params.put(TYPE_KEY, "1");
		}
		else
		{
			params.put(TYPE_KEY, "0");
		}

		// Execute request
		try
		{
			HttpResponse<String> resp = requestString(STORY_PATH, params, null);
			if (resp.getCode() == 200 || resp.getCode() == 202)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (UnirestException e)
		{
			return false;
		}
	}




	/**
	 * Make a change to a snap/story, eg mark it as viewed or screenshot. Added by Liam Cottle
	 *
	 * @param -- stuff
	 * @param snapId id of snap we are interacting with
	 * @param seen boolean stating if we have seen this snap or not.
	 * @param screenshot boolean stating if we have screenshot this snap or not.
	 * @param replayed integer stating how many times we have replayed this snap.
	 * @param username your Snapchat username.
	 * @param authToken your Snapchat auth_token from logging in.
	 * @param loginObject used to get 'added_friends_timestamp'
	 * @return true if successful, false otherwise.
	 */
	public static boolean updateSnap(String snapId, boolean seen, boolean screenshot, boolean replayed)
	{
		// Prepare parameters
		Long timestamp = getTimestamp();
		String requestToken = TokenLib.requestToken(Globals.token, timestamp);

		int statusInt = 0;
		int replayedInt = 0;

		if(seen)
		{
			statusInt = 0;
		}
		else if(screenshot)
		{
			statusInt = 1;
		}

		if(replayed)
		{
			replayedInt = 1;
		}

		String friendsTimestamp = "0";
		try
		{
			friendsTimestamp = Globals.loginObj.getString(SnapchatAPI.ADDED_FRIENDS_TIMESTAMP_KEY);
		}
		catch(Exception ee)
		{
			friendsTimestamp = timestamp.toString();
		}

		String jsonString = "{\"" + snapId + "\":{\"c\":" + statusInt + ",\"t\":" + timestamp + ",\"replayed\":" + replayedInt + "}}";

		String eventsString = "[]";

		// Make parameter map
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(USERNAME_KEY, Globals.username);
		params.put(TIMESTAMP_KEY, timestamp.toString());
		params.put(REQ_TOKEN_KEY, requestToken);
		params.put(ADDED_FRIENDS_TIMESTAMP_KEY, friendsTimestamp);
		params.put(JSON_KEY, jsonString);
		params.put(EVENTS_KEY, eventsString);
		//params.put(TIME_KEY, Integer.toString(snapTime));

		// Sending path
		String path = UPDATE_SNAPS_PATH;

		// Execute request
		try
		{
			HttpResponse<String> resp = requestString(path, params, null);
			if (resp.getCode() == 200 || resp.getCode() == 202)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (UnirestException e)
		{
			return false;
		}
	}

	public static boolean updateDisplayName(String newName, String friendName)
	{
		// Reference, final JSON query:
		//		{
		//		    username: "youraccount",
		//		    timestamp: 1373207221,
		//		    req_token: create_token(auth_token, 1373207221),
		//		    action: "display",
		//		    friend: "asmbarr"
		//		    display: "Varsity Bestie"
		//		}

		// Prepare parameters
		Long timestamp = getTimestamp();
		String requestToken = TokenLib.requestToken(Globals.token, timestamp);

		// Make parameter map
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(USERNAME_KEY, Globals.username);
		params.put(TIMESTAMP_KEY, timestamp.toString());
		params.put(REQ_TOKEN_KEY, requestToken);
		params.put("action", "display");
		params.put("friend", friendName);
		params.put("display", newName);

		// Sending path
		String path = FRIEND_PATH;

		// Execute request
		try
		{
			HttpResponse<String> resp = requestString("ph/friend", params, null);
			System.out.print(resp.getCode());
			if (resp.getCode() == 200 || resp.getCode() == 202)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (UnirestException e)
		{
			return false;
		}
	}

	/**
	 * Get a new timestamp to use in a request.
	 *
	 * @return a timestamp.
	 */
	public static Long getTimestamp()
	{
		Long timestamp = (new Date()).getTime() / 1000L;
		return timestamp;
	}

	/**
	 * Get a new, random media_id for uploading media to Snapchat.
	 *
	 * @param username your Snapchat username.
	 * @return a media_id as a String.
	 */
	public static String getNewMediaId(String username)
	{
		String uuid = UUID.randomUUID().toString();
		return username.toUpperCase() + "~" + uuid;
	}

	public static HttpResponse<JsonNode> requestJson(String path, Map<String, Object> params, File file) throws UnirestException
	{
		MultipartBody req = prepareRequest(path, params, file);

		// Execute and return response as JSON
		HttpResponse<JsonNode> resp = req.asJson();

		// Record
		lastRequestPath = path;
		lastResponse = resp;
		lastResponseBodyClass = JsonNode.class;

		return resp;
	}

	public static HttpResponse<String> requestString(String path, Map<String, Object> params, File file) throws UnirestException
	{
		MultipartBody req = prepareRequest(path, params, file);

		// Execute and return response as String
		HttpResponse<String> resp = req.asString();

		// Record
		lastRequestPath = path;
		lastResponse = resp;
		lastResponseBodyClass = String.class;

		return resp;
	}

	public static HttpResponse<InputStream> requestBinary(String path, Map<String, Object> params, File file) throws UnirestException
	{
		MultipartBody req = prepareRequest(path, params, file);

		// Execute and return as bytes
		HttpResponse<InputStream> resp = req.asBinary();

		// Record
		lastRequestPath = path;
		lastResponse = resp;
		lastResponseBodyClass = InputStream.class;

		return resp;
	}

	public static HttpResponse<InputStream> requestStoryBinary(String path) throws UnirestException
	{
		HttpRequest req = Unirest.get(BASE_URL + path)
				.header(JSON_TYPE_KEY, JSON_TYPE)
				.header(USER_AGENT_KEY, USER_AGENT);

		// Execute and return as bytes
		HttpResponse<InputStream> resp = req.asBinary();

		// Record
		lastRequestPath = path;
		lastResponse = resp;
		lastResponseBodyClass = InputStream.class;

		return resp;
	}

	private static MultipartBody prepareRequest(String path, Map<String, Object> params, File file)
	{
		// Set up a JSON request
		MultipartBody req = Unirest.post(BASE_URL + path)
				.header(JSON_TYPE_KEY, JSON_TYPE)
				.header(USER_AGENT_KEY, USER_AGENT)
				.fields(params);

		// Add file if there is one
		if (file != null) {
			return req.field(DATA_KEY, file);
		}
		return req;
	}
}