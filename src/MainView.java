

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;

public class MainView extends JPanel implements WebcamListener, WindowListener, UncaughtExceptionHandler, ItemListener
{
	private static final long serialVersionUID = 1L;

	private Webcam webcam = null;
	private WebcamPanel webcamPanel = null;
	private WebcamPicker picker = null;
	private JButton send;
	private JTextPane topBar;
	private JPanel bottomUI;
	private JPanel container;

	private JLabel panel;

	private final IOSWindow w;

	private final Color TOP_BAR_COLOR = new Color (0x00AAEB);

	public MainView(final IOSWindow window)
	{
		this.w = window;
		this.setOpaque(true);
		this.setLayout(new BorderLayout());

		send = new JButton (new ImageIcon(this.getScaledImage(this.getImage("/res/take_photo.png"), 100, 100)));
		send.setPreferredSize(new Dimension(100, 100));
		send.setBackground(null);
		send.setBorder(null);

		reload();

		send.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				BufferedImage snap = webcam.getImage();
				if (snap.getHeight() > snap.getWidth())
				{
					panel = new JLabel(new ImageIcon(getScaledImage(snap, ((int)(((double)snap.getWidth()) / ((double)snap.getHeight()) * ((double)Globals.webcamHeight))), Globals.webcamHeight)));
					panel.setPreferredSize(new Dimension(((int)(((double)snap.getWidth()) / ((double)snap.getHeight()) * ((double)Globals.webcamHeight))), Globals.webcamHeight));
				}
				else
				{
					panel = new JLabel(new ImageIcon(getScaledImage(snap, Globals.webcamWidth, ((int)(((double)snap.getHeight()) / ((double)snap.getWidth()) * ((double)Globals.webcamWidth))))));
					panel.setPreferredSize(new Dimension(Globals.webcamWidth, ((int)(((double)snap.getHeight()) / ((double)snap.getWidth()) * ((double)Globals.webcamWidth)))));
				}
				panel.setBackground(new Color(0xFFFFFF));

				photoTaken();

				//				try
				//				{
				//					MainView.this.sendSnap(new String[] { "asmbarr" }, snap, false);
				//				}
				//				catch (FileNotFoundException e)
				//				{
				//					e.printStackTrace();
				//				}
			}
			public void mouseEntered(MouseEvent arg0) { }
			public void mouseExited(MouseEvent arg0) { }
			public void mousePressed(MouseEvent arg0) { }
			public void mouseReleased(MouseEvent arg0) { }
		});
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		webcam.close();
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		System.out.println("webcam viewer resumed");
		this.webcamPanel.resume();
	}

	@Override
	public void windowIconified(WindowEvent e) {
		System.out.println("webcam viewer paused");
		this.webcamPanel.pause();
	}

	@Override
	public void webcamOpen(WebcamEvent we) {
		System.out.println("webcam open");
	}

	@Override
	public void webcamClosed(WebcamEvent we) {
		System.out.println("webcam closed");
	}

	@Override
	public void webcamDisposed(WebcamEvent we) {
		System.out.println("webcam disposed");
	}

	@Override
	public void webcamImageObtained(WebcamEvent we) {
		// do nothing
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.err.println(String.format("Exception in thread %s", t.getName()));
		e.printStackTrace();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() != webcam) {
			if (webcam != null) {

				this.webcamPanel.stop();

				remove(this.webcamPanel);

				webcam.removeWebcamListener(this);
				webcam.close();

				webcam = (Webcam) e.getItem();
				webcam.setViewSize(WebcamResolution.VGA.getSize());
				webcam.addWebcamListener(this);

				System.out.println("selected " + webcam.getName());

				this.webcamPanel = new WebcamPanel(webcam, false);

				add(this.webcamPanel, BorderLayout.CENTER);
				((Window) this.getParent()).pack();

				Thread t = new Thread() {

					@Override
					public void run() {
						webcamPanel.start();
					}
				};
				t.setName("example-stoper");
				t.setDaemon(true);
				t.setUncaughtExceptionHandler(this);
				t.start();
			}
		}
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

	private BufferedImage getScaledImage(BufferedImage src, int w, int h)
	{
		int finalw = w;
		int finalh = h;
		double factor = 1.0d;
		if(src.getWidth() > src.getHeight()){
			factor = ((double)src.getHeight()/(double)src.getWidth());
			finalh = (int)(finalw * factor);                
		}
		else
		{
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

	public void sendSnap(String[] recipient, BufferedImage image, boolean isVid) throws FileNotFoundException
	{
		// Try uploading a file
		String medId = SnapchatAPI.upload(image, isVid);

		// Try sending it
		List<String> recipients = Arrays.asList(recipient);

		// Send and print
		System.out.println("Sending...");
		boolean postStory = true; //set as true to make this your story as well...

		// TODO(samstern): User-specified time, not automatically 10 seconds
		boolean result = SnapchatAPI.send(medId, recipients, postStory, 10);
		if (result)
		{
			System.out.println("Sent.");
		}
		else
		{
			System.out.println("Could not send.");
		}
	}

	public void loadSnaptop()
	{
		topBar = new JTextPane();
		topBar.setBackground(TOP_BAR_COLOR);

		topBar.setText("Snaptop");
		topBar.setHighlighter(null);
		topBar.setEditable(false);
		topBar.setFont(Globals.getFont(2, 40.0f));
		topBar.setPreferredSize(new Dimension(Globals.webcamWidth, 55));
		topBar.setForeground(Color.WHITE);

		StyledDocument doc = topBar.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
	}

	public void loadWebcam()
	{
		this.picker = new WebcamPicker();
		this.picker.addItemListener(this);

		this.webcam = picker.getSelectedWebcam();

		if (webcam == null) {
			System.out.println("No webcams found...");
			System.exit(1);
		}

		Globals.webcamHeight = WebcamResolution.VGA.getSize().height;
		Globals.webcamWidth = WebcamResolution.VGA.getSize().width;

		this.webcam.setViewSize(WebcamResolution.VGA.getSize());
		this.setPreferredSize(new Dimension(Globals.webcamWidth, 750));
		this.webcam.addWebcamListener(MainView.this);

		this.webcamPanel = new WebcamPanel(webcam, false);
		this.webcamPanel.setFPSDisplayed(false);
		this.webcamPanel.setVisible(true);
	}

	public void loadBottomUI()
	{
		bottomUI = new JPanel();
		bottomUI.setBackground(new Color(0xDDDDDD));
		bottomUI.setPreferredSize(new Dimension(Globals.webcamWidth, 750 - topBar.getHeight() - Globals.webcamHeight));
		bottomUI.add(send, BorderLayout.CENTER);
	}

	public void reload()
	{
		container = new JPanel();
		BoxLayout boxLayout1 = new BoxLayout(container, BoxLayout.Y_AXIS);
		container.setLayout(boxLayout1);
		container.add(Box.createVerticalGlue());

		loadSnaptop();
		loadWebcam();
		loadBottomUI();

		container.add(this.topBar);
		container.add(this.webcamPanel);
		container.add(this.bottomUI);

		this.add(container, BorderLayout.NORTH);
		this.setVisible(true);

		Thread t = new Thread()
		{
			public void run()
			{
				webcamPanel.start();
			}
		};
		t.setName("example-starter");
		t.setDaemon(true);
		t.setUncaughtExceptionHandler(this);
		t.start();
	}

	public void photoTaken()
	{
		this.removeAll();
		
		container = new JPanel();
		BoxLayout boxLayout1 = new BoxLayout(container, BoxLayout.Y_AXIS);
		container.setLayout(boxLayout1);
		container.add(Box.createVerticalGlue());
		
		loadSnaptop();
		loadBottomUI();
		
		send.setVisible (false);
		send.repaint();

		panel.setBackground(new Color (0xFF0000));
		
		container.add(this.topBar);
		container.add(this.panel);
		container.add(this.bottomUI);

		this.add(container, BorderLayout.NORTH);

		w.notifyPhotoTaken();
	}
}
