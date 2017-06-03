import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import api.Friend;
import api.Story;

public class SendSnapView extends JPanel
{
	private	JPanel topPanel;
	private Friend[] friendList;
	private Story[] storyList;
	private final EmptyBorder padding = new EmptyBorder(0, 10, 0, 0);

	private static final int STORY_ICON_DIAMETER = 40;

	public SendSnapView()
	{
		this.removeAll();
		rebuild();
	}

	public void rebuild()
	{
		Color topColor = new Color (155, 85, 160);
		this.setBackground(new Color(0x666666));

		BoxLayout boxLayout4 = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(boxLayout4);
		this.add(Box.createVerticalGlue());

		JTextPane friends = new JTextPane();
		friends.setText("Send");
		friends.setHighlighter(null);
		friends.setBackground(topColor);
		friends.setEditable(false);
		friends.setPreferredSize(new Dimension(300, 55));
		friends.setFont(Globals.getFont(2, 40.0f));
		friends.setForeground(Color.WHITE);

		StyledDocument doc = friends.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		this.setPreferredSize(new Dimension(300, 750));

		JPanel listContainer = new JPanel();
		listContainer.setBorder(null);
		BoxLayout boxLayout1 = new BoxLayout(listContainer, BoxLayout.Y_AXIS);
		listContainer.setLayout(boxLayout1);
		listContainer.add(Box.createVerticalGlue());
		this.add(friends);

		friendList = Globals.getFriends();
		storyList = Globals.getStories();

		//Send to my story
		JPanel mainStoryPanel = new JPanel();
		JPanel storyRenderer = new JPanel();

		mainStoryPanel.setBackground(new Color(0xFFFFFF));

		final JTextPane myStory = new JTextPane();

		myStory.setHighlighter(null);
		myStory.setEditable(false);
		myStory.setBackground(new Color(0xFFFFFF));
		myStory.setFont(Globals.getFont(1, 20.0f));
		myStory.setText("My Story");
		myStory.setBorder(this.padding);
		myStory.setVisible(true);
		myStory.setForeground(new Color(0x000000));

		storyRenderer.setBackground(new Color(0xFFFFFF));
		storyRenderer.setPreferredSize(new Dimension(300, 30));

		BoxLayout boxLayoutStory = new BoxLayout(storyRenderer, BoxLayout.Y_AXIS);
		storyRenderer.setLayout(boxLayoutStory);
		storyRenderer.add(Box.createVerticalGlue());
		storyRenderer.add(myStory, BorderLayout.EAST);

		mainStoryPanel.add(storyRenderer, BorderLayout.EAST);
		mainStoryPanel.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent arg0)
			{
				//TODO Select friend to send
			}

			public void mouseEntered(MouseEvent arg0) { }
			public void mouseExited(MouseEvent arg0) { }
			public void mousePressed(MouseEvent arg0) { }
			public void mouseReleased(MouseEvent arg0) { }		
		});

		storyRenderer.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent arg0)
			{
				//TODO Select friend to send
			}

			public void mouseEntered(MouseEvent arg0) { }
			public void mouseExited(MouseEvent arg0) { }
			public void mousePressed(MouseEvent arg0) { }
			public void mouseReleased(MouseEvent arg0) { }		
		});
		
		myStory.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent arg0)
			{
				//TODO Select friend to send
			}

			public void mouseEntered(MouseEvent arg0) { }
			public void mouseExited(MouseEvent arg0) { }
			public void mousePressed(MouseEvent arg0) { }
			public void mouseReleased(MouseEvent arg0) { }		
		});

		listContainer.add(mainStoryPanel);


		for (int i = 0; i < friendList.length; i++)
		{
			JPanel mainPanel = new JPanel();
			JPanel renderer = new JPanel();

			mainPanel.setBackground(new Color(0xFFFFFF));

			final Friend f = friendList[i];

			final JTextPane username = new JTextPane();

			final JTextPane displayName = new JTextPane();
			displayName.setHighlighter(null);
			displayName.setEditable(false);
			displayName.setFont(Globals.getFont(1, 20.0f));
			displayName.setText(Globals.ellipsize(f.getDisplayName(), 18));
			displayName.setForeground(new Color(0x000000));
			displayName.setBackground(new Color(0xFFFFFF));
			displayName.setVisible(true);
			displayName.setBorder(this.padding);

			username.setHighlighter(null);
			username.setEditable(false);
			username.setBackground(new Color(0xFFFFFF));
			username.setFont(Globals.getFont(1, 14.0f));
			username.setText(Globals.ellipsize(f.getUsername(), 18));
			username.setBorder(this.padding);
			username.setVisible(true);
			if (!f.getDisplayName().equals(f.getUsername()))
				username.setForeground(new Color(0x666666));
			else
				username.setForeground(new Color(0xFFFFFF));

			renderer.setBackground(new Color(0xFFFFFF));
			renderer.setPreferredSize(new Dimension(300, 50));

			BoxLayout boxLayout2 = new BoxLayout(renderer, BoxLayout.Y_AXIS);
			renderer.setLayout(boxLayout2);
			renderer.add(Box.createVerticalGlue());
			renderer.add(displayName, BorderLayout.EAST);
			renderer.add(username, BorderLayout.EAST);

			mainPanel.add(renderer, BorderLayout.EAST);
			mainPanel.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent arg0)
				{
					//TODO Select friend to send
				}

				public void mouseEntered(MouseEvent arg0) { }
				public void mouseExited(MouseEvent arg0) { }
				public void mousePressed(MouseEvent arg0) { }
				public void mouseReleased(MouseEvent arg0) { }		
			});

			renderer.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent arg0)
				{
					//TODO Select friend to send
				}

				public void mouseEntered(MouseEvent arg0) { }
				public void mouseExited(MouseEvent arg0) { }
				public void mousePressed(MouseEvent arg0) { }
				public void mouseReleased(MouseEvent arg0) { }		
			});
			username.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent arg0)
				{
					//TODO Select friend to send
				}

				public void mouseEntered(MouseEvent arg0) { }
				public void mouseExited(MouseEvent arg0) { }
				public void mousePressed(MouseEvent arg0) { }
				public void mouseReleased(MouseEvent arg0) { }		
			});
			displayName.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent arg0)
				{
					//TODO Select friend to send
				}

				public void mouseEntered(MouseEvent arg0) { }
				public void mouseExited(MouseEvent arg0) { }
				public void mousePressed(MouseEvent arg0) { }
				public void mouseReleased(MouseEvent arg0) { }		
			});

			listContainer.add(mainPanel);
		}
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(300, 750));
		scrollPane.setViewportView(listContainer);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setBorder(null);

		this.add(scrollPane);

		this.setVisible(true);
	}

	public ArrayList<Story> getFriendStory(Friend f)
	{
		ArrayList<Story> stories = new ArrayList<Story>(0);
		for (int i = 0; i < storyList.length; i++)
		{
			if (storyList[i].getSender().equals(f.getUsername()))
			{
				stories.add(storyList[i]);
			}
			else if (storyList[i].getSender().compareTo(f.getUsername()) > 1)
			{
				break;
			}
		}
		return stories.size() != 0 ? stories : null;
	}

	private BufferedImage getScaledImage(BufferedImage src, int w, int h)
	{
		int finalw = w;
		int finalh = h;
		double factor = 1.0d;
		if(src.getWidth() > src.getHeight()){
			factor = ((double)src.getHeight()/(double)src.getWidth());
			finalh = (int)(finalw * factor);                
		}else{
			factor = ((double)src.getWidth()/(double)src.getHeight());
			finalw = (int)(finalh * factor);
		}   


		BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(src, 0, 0, finalw, finalh, null);
		g2.dispose();
		return resizedImg;
	}

	public BufferedImage getImage(String path)
	{
		InputStream logInStream = ActivityView.class.getResourceAsStream (path);
		try
		{
			BufferedImage logInImg = null;
			logInImg = ImageIO.read(logInStream);
			return logInImg;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public BufferedImage overlay(BufferedImage image, BufferedImage overlay)
	{
		// create the new image, canvas size is the max. of both image sizes
		BufferedImage combined = new BufferedImage(STORY_ICON_DIAMETER, STORY_ICON_DIAMETER, BufferedImage.TYPE_INT_ARGB);

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.drawImage(overlay, 0, 0, null);
		g.dispose();
		return combined;
	}
}