
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import utils.ActivityJPanel;
import api.Snap;

public class ActivityView extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4589038100903391506L;
	private	JPanel topPanel;
	private Snap[] snapList;

	public ActivityView()
	{
		Color topColor = new Color (9, 170, 139);
		this.setBackground(new Color(1.0f,1.0f,1.0f,0.0f));

		BoxLayout boxLayout4 = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(boxLayout4);
		this.add(Box.createVerticalGlue());

		JTextPane snaptop = new JTextPane();
		snaptop.setText("Activity Feed");
		snaptop.setHighlighter(null);
		snaptop.setBackground(topColor);
		snaptop.setEditable(false);
		snaptop.setPreferredSize(new Dimension(300, 55));
		snaptop.setFont(Globals.getFont(2, 40.0f));
		snaptop.setForeground(Color.WHITE);

		StyledDocument doc = snaptop.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		this.setPreferredSize(new Dimension(300, 750));

		final JPanel listContainer = new JPanel();
		BoxLayout boxLayout1 = new BoxLayout(listContainer, BoxLayout.Y_AXIS);
		listContainer.setLayout(boxLayout1);
		listContainer.add(Box.createVerticalGlue());
		this.add(snaptop);

		snapList = Globals.snaps;
		for (int i = 0; i < snapList.length; i++)
		{
			final ActivityJPanel mainPanel = new ActivityJPanel();
			final JPanel renderer = new JPanel();

			mainPanel.setBackground(new Color(0xFFFFFF));

			final Snap f = snapList[i];

			final JTextPane displayName = new JTextPane();
			displayName.setHighlighter(null);
			displayName.setEditable(false);
			displayName.setFont(Globals.getFont(1, 20.0f));
			if (f.isIncoming())
			{
				String username = f.getSender();
				for (int j = 0; j < Globals.getFriends().length; j++)
				{
					if (Globals.getFriends()[j].getUsername().equals(username))
						username = Globals.getFriends()[j].getDisplayName();
				}
				displayName.setText(username);
			}
			else
			{
				String username = f.getRecipient();
				for (int j = 0; j < Globals.getFriends().length; j++)
				{
					if (Globals.getFriends()[j].getUsername().equals(username))
						username = Globals.getFriends()[j].getDisplayName();
				}
				displayName.setText(username);
			}
			displayName.setForeground(new Color(0x000000));
			displayName.setVisible(true);
			displayName.setBackground(new Color(0xFFFFFF));
			displayName.setBorder(new EmptyBorder(10, 20, 0, 0));

			ImageIcon icon = null;
			System.out.println(f.getState());
			if (f.isIncoming())
			{
				if (f.getState() == Snap.VIEWED)
				{
					if (f.getType() == Snap.TYPE_IMAGE || f.getType() == Snap.TYPE_FRIEND_REQUEST_IMAGE || f.getType() == Snap.TYPE_FRIEND_REQUEST)
					{
						icon = getImage("/res/received_read.png");
					}
					else if (f.getType() == Snap.TYPE_VIDEO || f.getType() == Snap.TYPE_VIDEO_NOAUDIO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO_NOAUDIO)
					{
						icon = getImage("/res/received_video_read.png");
					}
				}
				else if (f.getState() == Snap.SENT || f.getState() == Snap.DELIVERED)
				{
					if (f.getType() == Snap.TYPE_IMAGE || f.getType() == Snap.TYPE_FRIEND_REQUEST_IMAGE || f.getType() == Snap.TYPE_FRIEND_REQUEST)
					{
						icon = getImage ("/res/received_unread.png");
					}
					else if (f.getType() == Snap.TYPE_VIDEO || f.getType() == Snap.TYPE_VIDEO_NOAUDIO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO_NOAUDIO)
					{
						icon = getImage("/res/received_video_unread.png");
					}
				}
				else if (f.getState() == Snap.SCREENSHOT)
				{
					if (f.getType() == Snap.TYPE_IMAGE || f.getType() == Snap.TYPE_FRIEND_REQUEST_IMAGE || f.getType() == Snap.TYPE_FRIEND_REQUEST)
					{
						icon = getImage("/res/screenshot.png");
					}
					else if (f.getType() == Snap.TYPE_VIDEO || f.getType() == Snap.TYPE_VIDEO_NOAUDIO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO_NOAUDIO)
					{
						icon = getImage("/res/video_screenshot.png");
					}
				}
			}
			else
			{
				if (f.getState() == Snap.SENT || f.getState() == Snap.DELIVERED)
				{
					if (f.getType() == Snap.TYPE_IMAGE || f.getType() == Snap.TYPE_FRIEND_REQUEST_IMAGE || f.getType() == Snap.TYPE_FRIEND_REQUEST)
					{
						icon = getImage ("/res/delivered_unread.png");
					}
					else if (f.getType() == Snap.TYPE_VIDEO || f.getType() == Snap.TYPE_VIDEO_NOAUDIO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO_NOAUDIO)
					{
						icon = getImage("/res/delivered_video_unread.png");
					}
				}
				else if (f.getState() == Snap.VIEWED)
				{
					if (f.getType() == Snap.TYPE_IMAGE || f.getType() == Snap.TYPE_FRIEND_REQUEST_IMAGE)
					{
						icon = getImage("/res/delivered_read.png");
					}
					else if (f.getType() == Snap.TYPE_VIDEO || f.getType() == Snap.TYPE_VIDEO_NOAUDIO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO_NOAUDIO)
					{
						icon = getImage("/res/delivered_video_read.png");
					}
				}
				else if (f.getState() == Snap.SCREENSHOT)
				{
					if (f.getType() == Snap.TYPE_IMAGE || f.getType() == Snap.TYPE_FRIEND_REQUEST_IMAGE)
					{
						icon = getImage("/res/screenshot.png");
					}
					else if (f.getType() == Snap.TYPE_VIDEO || f.getType() == Snap.TYPE_VIDEO_NOAUDIO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO || f.getType() == Snap.TYPE_FRIEND_REQUEST_VIDEO_NOAUDIO)
					{
						icon = getImage("/res/video_screenshot.png");
					}
				}
			}
			final JButton imageButton = new JButton();
			if (icon != null)
			{
				Image newimg = icon.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
				imageButton.setIcon(new ImageIcon(newimg));
			}
			imageButton.setFocusable(false);
			imageButton.setBorder(null);
			imageButton.setPreferredSize(new Dimension(40, 40));
			imageButton.setBackground(new Color(0xFFFFFF));

			renderer.setBackground(new Color(0xFFFFFF));
			renderer.setPreferredSize(new Dimension(300, 50));

			BoxLayout boxLayout2 = new BoxLayout(renderer, BoxLayout.Y_AXIS);
			renderer.setLayout(boxLayout2);
			renderer.add(Box.createVerticalGlue());
			renderer.add(displayName, BorderLayout.EAST);

			renderer.setVisible(true);

			mainPanel.add(imageButton, BorderLayout.EAST);
			mainPanel.add(renderer, BorderLayout.CENTER);

			mainPanel.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent arg0)
				{
					for (int i = 0; i < listContainer.getComponents().length; i++)
					{
						if (listContainer.getComponents()[i] instanceof ActivityJPanel)
						{
							((ActivityJPanel)listContainer.getComponents()[i]).deselect();
						}
						mainPanel.select();
					}
					if (arg0.getClickCount() == 2)
					{
						if (f.isImage())
						{	
							//TODO Resize image to screen size
							//TODO Show the amount of time left
							Thread showImage = new Thread()
							{
								public void run()
								{
									try
									{
										Globals.showImage(ImageIO.read(new ByteArrayInputStream(SnapchatAPI.getSnap(f))), f.getTime());
									}
									catch (IOException e)
									{
										e.printStackTrace();
									}
								}  
							};
							showImage.start();
							boolean screenshot = false; //TODO Check for screenshot in as many ways as possible
							boolean replayed = false; //TODO Check for replay in as many ways as possible
							//SnapchatAPI.updateSnap(f.getId(), true, screenshot, replayed);
						}
						else
						{
							byte[] videoData = SnapchatAPI.getSnap(f);
							//TODO Resize image to screen size
							//TODO Show the amount of time left
//							Thread showVideo = new Thread()
//							{
//								public void run()
//								{
//									try
//									{
//										Globals.showVideo(videoData, f.getTime());
//									}
//									catch (IOException e)
//									{
//										e.printStackTrace();
//									}
//								}  
//							};
//							showVideo.start();
							Globals.showVideo(videoData);
							boolean screenshot = false; //TODO Check for screenshot in as many ways as possible
							boolean replayed = false; //TODO Check for replay in as many ways as possible
							//SnapchatAPI.updateSnap(f.getId(), true, screenshot, replayed, Globals.username, Globals.token, Globals.loginObj);
						}
					}
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
					System.out.println("Clicked!");
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
					System.out.println("Clicked!");
				}

				public void mouseEntered(MouseEvent arg0) { }
				public void mouseExited(MouseEvent arg0) { }
				public void mousePressed(MouseEvent arg0) { }
				public void mouseReleased(MouseEvent arg0) { }		
			});

			imageButton.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent arg0)
				{
					System.out.println("Clicked!");
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

		this.add(scrollPane);

		this.setVisible(true);
	}

	public ImageIcon getImage(String path)
	{
		InputStream logInStream = ActivityView.class.getResourceAsStream (path);
		try
		{
			BufferedImage logInImg = null;
			logInImg = ImageIO.read(logInStream);
			return new ImageIcon(logInImg);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
