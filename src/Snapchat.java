import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import json.JSONObject;
import api.Snap;
import api.Story;

public class Snapchat
{	
	private static LoginScreen l;
	private static String username;

	public static void main(String[] args) throws Exception
	{
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Globals.screenWidth = gd.getDisplayMode().getWidth();
		Globals.screenHeight = gd.getDisplayMode().getHeight();
		l = new LoginScreen();
		//oldMain();
//		Globals.loginObj = SnapchatAPI.login("aneeshashutosh", "Amaranth87");
//		IOSWindow w = new IOSWindow();
	}

	public static void oldMain() throws Exception
	{
		// Get username and password
		Scanner scanner = new Scanner(System.in);
		System.out.println("Snapchat username: ");
		String username = scanner.nextLine();
		System.out.println("Snapchat password: ");
		String password = scanner.nextLine();

		// Test logging in
		System.out.println("Logging in...");
		JSONObject loginObj = SnapchatAPI.login(username, password);
		if (loginObj != null) {
			System.out.println("Logged in.");
		} else {
			System.out.println("Failed to log in.");
			return;
		}

		// Get the auth_token
		String token = loginObj.getString(SnapchatAPI.AUTH_TOKEN_KEY);

		while(true)
		{
			// Ask the user what they want to do
			System.out.println();
			System.out.println("Choose an option:");
			System.out.println("\t1) Download un-viewed snaps");
			System.out.println("\t2) Send a snap");
			System.out.println("\t3) Set a Story");
			System.out.println("\t4) Download Stories");
			System.out.println("\t5) Send a video Story");
			System.out.println();

			int option = scanner.nextInt();
			scanner.nextLine();
			switch (option)
			{
				case 1:
					fetchSnaps(loginObj, username, token);
					break;
				case 2:
					System.out.println("Enter path to image file:");
					String snapFileName = scanner.nextLine();
					System.out.println("Enter recipient Snapchat username:");
					String recipient = scanner.nextLine();
					sendSnap(username, new String[] { recipient }, snapFileName, token);
					break;
				case 3:
					System.out.println("Enter path to image file:");
					String storyFileName = scanner.nextLine();
					setStory(username, storyFileName, token);
					break;
				case 4:
					Story[] storyObjs = SnapchatAPI.getStories();
					Story[] downloadable = Story.filterDownloadable(storyObjs);
					for (Story s : downloadable) {
						String extension = ".jpg";
						if(!s.isImage()){
							extension = ".mp4";
						}
						System.out.println("Downloading story from " + s.getSender());
						byte[] storyBytes = SnapchatAPI.getStory(s);
						File storyFile = new File(s.getSender() + "-" + s.getId() + extension);
						FileOutputStream storyOs = new FileOutputStream(storyFile);
						storyOs.write(storyBytes);
						storyOs.close();
					}
					System.out.println("Done.");
					break;
				case 5:
					System.out.println("Enter path to video file:");
					String storyVideoFileName = scanner.nextLine();
					setStory(username, storyVideoFileName, token);
					break;
				default:
					System.out.println("Invalid option.");
					break;
			}
		}
	}

	public static void fetchSnaps(JSONObject loginObj, String username, String token) throws IOException {
		// Try fetching all snaps
		System.out.println("Fetching snaps...");
		Snap[] snapObjs = SnapchatAPI.getSnaps();
		Snap[] downloadable = Snap.filterDownloadable(snapObjs);
		for (Snap s : downloadable) {
			// TODO(samstern): Support video
			if (s.isImage()) {
				System.out.println("Downloading snap from " + s.getSender());
				byte[] snapBytes = SnapchatAPI.getSnap(s);
				File snapFile = new File(s.getSender() + "-" + s.getId() + ".jpg");
				FileOutputStream snapOs = new FileOutputStream(snapFile);
				snapOs.write(snapBytes);
				snapOs.close();
			}
		}
		System.out.println("Done.");
	}

	public static void sendSnap(String username, String[] recipient, String filename, String token)
			throws FileNotFoundException {

		// Try uploading a file
		File file = new File(filename);
		String medId = SnapchatAPI.upload(file, false);

		// Try sending it
		List<String> recipients = Arrays.asList(recipient);

		// Send and print
		System.out.println("Sending...");
		boolean postStory = true; //set as true to make this your story as well...

		// TODO(samstern): User-specified time, not automatically 10 seconds
		boolean result = SnapchatAPI.send(medId, recipients, postStory, 10);
		if (result) {
			System.out.println("Sent.");
		} else {
			System.out.println("Could not send.");
		}
	}

	public static void sendSnap(String username, String recipient, BufferedImage img, String token)
			throws FileNotFoundException {

		String medId = SnapchatAPI.upload(img, false);

		// Try sending it
		List<String> recipients = new ArrayList<String>();
		recipients.add(recipient);

		// Send and print
		System.out.println("Sending...");
		boolean postStory = false; //set as true to make this your story as well...

		// TODO(samstern): User-specified time, not automatically 10 seconds
		boolean result = SnapchatAPI.send(medId, recipients, postStory, 10);
		if (result) {
			System.out.println("Sent.");
		} else {
			System.out.println("Could not send.");
		}
	}

	public static void setStory(String username, String filename, String token) throws FileNotFoundException {

		boolean video = false; //TODO(liamcottle) upload video snaps from command line.
		// Try uploading a file
		File file = new File(filename);
		String medId = SnapchatAPI.upload(file, video);

		// Send and print
		System.out.println("Setting...");
		boolean postStory = false; //set as true to make this your story as well...

		// TODO(samstern): User-specified time, not automatically 10 seconds
		boolean result = SnapchatAPI.sendStory(medId, 10, video, "My Story");
		if (result) {
			System.out.println("Set.");
		} else {
			System.out.println("Could not set.");
		}
	}

	public static void setStory(String username, String filename, String token, boolean video) throws FileNotFoundException {
		if (!video)
		{
			setStory(username, filename, token);
		}
		else
		{
			// Try uploading a file
			File file = new File(filename);
			String medId = SnapchatAPI.upload(file, video);

			// Send and print
			System.out.println("Setting...");
			boolean postStory = false; //set as true to make this your story as well...

			// TODO(samstern): User-specified time, not automatically 10 seconds
			boolean result = SnapchatAPI.sendStory(medId, 30, video, filename);
			if (result) {
				System.out.println("Set.");
			} else {
				System.out.println("Could not set.");
			}	
		}

	}
}