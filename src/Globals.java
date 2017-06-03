import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import json.JSONObject;
import api.Friend;
import api.Snap;
import api.Story;

public class Globals
{
	public static String token;
	public static String username;

	public static boolean isFirstUsernameBoxView = true;
	public static boolean isFirstPasswordBoxView = true;

	public static Snap[] snaps;
	public static Snap[] downloadableSnaps;
	private static Friend[] friends;
	private static ArrayList<Friend> bestFriends;
	private static Story[] stories;

	public static int webcamHeight;
	public static int webcamWidth;

	public static JSONObject loginObj;

	public static Color chatColor = new Color(0, 169, 223);
	public static Color videoColor = new Color(0x9C559B);
	public static Color photoColor = new Color(232, 39, 84);
	public static Color unsentColor = new Color(135, 135, 135);

	public static JFrame viewingFrame;

	public static int screenWidth;
	public static int screenHeight;

	public static void initializeViewingFrame()
	{
		viewingFrame = new JFrame();
		viewingFrame.setTitle("Viewing snap...");
		viewingFrame.setLayout(new BorderLayout());
		viewingFrame.setBackground(new Color(0xFFFFFF));
	}

	public static void showImage(BufferedImage image, int time)
	{
		viewingFrame.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		viewingFrame.setLayout(new BorderLayout());
		viewingFrame.setBackground(new Color(0xFFFFFF));

		JButton panel = new JButton(new ImageIcon(image));
		panel.setFocusable(false);
		panel.setBorder(null);

		viewingFrame.add(panel, BorderLayout.CENTER);
		viewingFrame.pack();
		viewingFrame.setLocationRelativeTo(null);
		viewingFrame.setVisible(true);

		try
		{
			Thread.sleep(time * 1000);
		}
		catch(InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
		removeImage();
	}

	public static void showVideo(byte[] videoData)
	{
		//		EmbeddedMediaPlayer mb = new EmbeddedMediaPlayer();
		//		try {
		//			mb.launch();
		//		} catch (Exception e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		viewingFrame.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		//		viewingFrame.setLayout(new BorderLayout());
		//		viewingFrame.setBackground(new Color(0xFFFFFF));
		//
		//		JButton panel = new JButton(new ImageIcon(image));
		//		panel.setFocusable(false);
		//		panel.setBorder(null);
		//
		//		viewingFrame.add(panel, BorderLayout.CENTER);
		//		viewingFrame.pack();
		//		viewingFrame.setLocationRelativeTo(null);
		//		viewingFrame.setVisible(true);
		//
		//		try
		//		{
		//			Thread.sleep(time * 1000);
		//		}
		//		catch(InterruptedException ex)
		//		{
		//			Thread.currentThread().interrupt();
		//		}
		//		removeImage();
	}

	public static void removeImage()
	{
		viewingFrame.removeAll();
		viewingFrame.dispose();
		initializeViewingFrame();
	}

	public static Friend[] getFriends()
	{
		return friends;
	}

	public static ArrayList<Friend> getBestFriends()
	{
		return bestFriends;
	}
	
	public static void setBestFriends(ArrayList<Friend> b)
	{
bestFriends = b;
	}

	public static void setFriends(Friend[] f)
	{
		Arrays.sort(f);
		friends = f;
	}

	public static Story[] getStories()
	{
		return stories;
	}

	public static void setStories(Story[] s)
	{
		stories = s;
		Arrays.sort(stories);
	}

	public static Font getFont(int weight, float fontSize)
	{
		Font font = null;
		String fName = "/font/";
		switch (weight)
		{
		case 0:
			fName += "Lato-Hairline.ttf";
			break;
		case 1:
			fName += "Lato-Thin.ttf";
			break;
		case 2:
			fName += "Lato-Light.ttf";
			break;
		case 3:
			fName += "Lato-Regular.ttf";
			break;
		case 4:
			fName += "Lato-Medium.ttf";
			break;
		case 5:
			fName += "Lato-Semibold.ttf";
			break;
		case 6:
			fName += "Lato-Bold.ttf";
			break;
		case 7:
			fName += "Lato-Heavy.ttf";
			break;
		case 8:
			fName += "Lato-Black.ttf";
			break;
		}
		try
		{
			InputStream is = LoginScreen.class.getResourceAsStream(fName);
			font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(fontSize);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.err.println(fName + " not loaded.  Using serif font.");
			font = new Font("serif", Font.PLAIN, 40);
		}
		return font;
	}

	public static String ellipsize(String text, int max)
	{
	    return text.length() > max ? text.substring(0, max) + "..." : text;
	}
}
