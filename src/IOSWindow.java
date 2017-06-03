import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class IOSWindow extends JFrame
{
	private static final long serialVersionUID = -1689129581118421003L;

	private MainView mainView;
	private FriendsView friendsView;
	private ActivityView activityView;
	private SendSnapView sendSnapView;
	private JPanel container;
	
	private JPanel rightSide;

	public IOSWindow()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
		this.setPreferredSize(new Dimension(1240, 750));
		this.setResizable(false);

		mainView = new MainView(this);
		friendsView = new FriendsView();
		activityView = new ActivityView();
		sendSnapView = new SendSnapView();

		rightSide = new JPanel();
		rightSide.setLayout(new CardLayout());
		rightSide.add(friendsView, "friends");
		rightSide.add(sendSnapView, "send");
		
		container = new JPanel();
		BoxLayout boxLayout2 = new BoxLayout(container, BoxLayout.X_AXIS);
		container.setLayout(boxLayout2);
		container.add(Box.createHorizontalGlue());
		container.add(activityView);
		container.add(mainView);
		container.add(rightSide);

		this.add(container, BorderLayout.WEST);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void notifyPhotoTaken()
	{
		((CardLayout)(rightSide.getLayout())).show(rightSide, "send");
		//this.validate();
	}
}
