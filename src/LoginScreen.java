import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import json.JSONObject;

public class LoginScreen extends JFrame
{
	private static final long serialVersionUID = -8062402777190420208L;

	public LoginScreen()
	{
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(600, 750));
		this.setTitle("Snapchat");
		this.setResizable(false);

		InputStream imgStream = LoginScreen.class.getResourceAsStream ("/res/logo.jpg");
		BufferedImage logo = null;
		try
		{
			logo = ImageIO.read(imgStream);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		this.setIconImage(logo);

		setScreenToDefault();

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void setScreenToDefault()
	{
		this.getContentPane().removeAll();
		this.setBackground(new Color(0xFF, 0xFC, 0x00));
		InputStream logInStream = LoginScreen.class.getResourceAsStream ("/res/login.png");
		InputStream signUpStream = LoginScreen.class.getResourceAsStream ("/res/signUp.png");
		ImageIcon logInIcon = null;
		ImageIcon signUpIcon = null;
		try
		{
			BufferedImage logInImg = null;
			BufferedImage signUpImg = null;
			logInImg = ImageIO.read(logInStream);
			signUpImg = ImageIO.read(signUpStream);
			logInIcon = new ImageIcon(logInImg);
			signUpIcon = new ImageIcon(signUpImg);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		JButton logIn = new JButton(logInIcon);
		JButton signUp = new JButton(signUpIcon);
		logIn.setPreferredSize(new Dimension(600, 150));
		signUp.setPreferredSize(new Dimension(600, 150));

		logIn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				setScreenToLogIn();
			}	
		});

		signUp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				setScreenToSignUp();
			}
		});

		JPanel buttonPanel = new JPanel();
		BoxLayout boxLayout1 = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
		buttonPanel.setLayout(boxLayout1);
		buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(logIn);
		buttonPanel.add(signUp);
		logIn.setFocusable(false);
		signUp.setFocusable(false);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.getContentPane().repaint();
	}

	public void setScreenToLogIn()
	{
		this.getContentPane().removeAll();
		this.setBackground(new Color(0xffffff));

		InputStream logInStream = LoginScreen.class.getResourceAsStream ("/res/login_2.png");
		ImageIcon logInIcon = null;
		try
		{
			BufferedImage logInImg = null;
			logInImg = ImageIO.read(logInStream);
			logInIcon = new ImageIcon(logInImg);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		final JButton logInButton = new JButton(logInIcon);
		logInButton.setPreferredSize(new Dimension(600, 150));
		logInButton.setVisible(false);

		final JPasswordField passwordField = new JPasswordField("Password: ");
		
		final JTextField usernameField = new JTextField("Username: ");
		Globals.isFirstUsernameBoxView = true;
		usernameField.setPreferredSize(new Dimension(620, 100));
		usernameField.setBackground(new Color(0xF0F0F0));
		usernameField.setBorder(new EmptyBorder(0, 40, 0, 0));
		usernameField.setFont(Globals.getFont(1, 40.0f));
		usernameField.addFocusListener(new FocusListener()
		{
			public void focusGained(FocusEvent e)
			{
				if (Globals.isFirstUsernameBoxView)
					usernameField.setText("");
				Globals.isFirstUsernameBoxView = false;
			}

			public void focusLost(FocusEvent e)
			{

			}
		});
		usernameField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
				if (!usernameField.getText().equals(""))
				{
					showLogin();
				}
			}
			public void removeUpdate(DocumentEvent e)
			{
				if (usernameField.getText().equals("") || String.valueOf(passwordField.getPassword()).equals(""))
				{
					hideLogin();
				}
			}
			public void insertUpdate(DocumentEvent e)
			{
				if (!usernameField.getText().equals(""))
				{
					showLogin();
				}
			}

			public void showLogin()
			{
				logInButton.setVisible(true);
			}
			
			public void hideLogin()
			{
				logInButton.setVisible(false);
			}
		});

		passwordField.setPreferredSize(new Dimension(620, 100));
		passwordField.setBackground(new Color(0xF0F0F0));
		passwordField.setBorder(new EmptyBorder(5, 40, 0, 0));
		passwordField.setFont(Globals.getFont(1, 40.0f));
		passwordField.addFocusListener(new FocusListener()
		{
			public void focusGained(FocusEvent e)
			{
				if (Globals.isFirstPasswordBoxView)
					passwordField.setText("");
				Globals.isFirstPasswordBoxView = false;
			}

			public void focusLost(FocusEvent e)
			{
				// nothing
			}
		});
		passwordField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
				if (!String.valueOf(passwordField.getPassword()).equals(""))
				{
					showLogin();
				}
			}
			public void removeUpdate(DocumentEvent e)
			{
				if (usernameField.getText().equals("") || String.valueOf(passwordField.getPassword()).equals(""))
				{
					hideLogin();
				}
			}
			public void insertUpdate(DocumentEvent e)
			{
				if (!String.valueOf(passwordField.getPassword()).equals(""))
				{
					showLogin();
				}
			}

			public void showLogin()
			{
				logInButton.setVisible(true);
			}
			
			public void hideLogin()
			{
				logInButton.setVisible(false);
			}
		});

		JPanel buttonPanel = new JPanel();
		BoxLayout boxLayout1 = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
		buttonPanel.setLayout(boxLayout1);
		buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(usernameField);
		buttonPanel.add(passwordField);
		this.add(buttonPanel, BorderLayout.NORTH);

		logInButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Globals.loginObj = SnapchatAPI.login(usernameField.getText(), String.valueOf(passwordField.getPassword()));
				if (Globals.loginObj != null && Globals.loginObj.has(SnapchatAPI.AUTH_TOKEN_KEY))
				{
					//TODO Launch tutorial/main view
				}
				else
				{
					passwordField.setText("");
					JOptionPane.showMessageDialog(LoginScreen.this, "That's not the right password. Sorry!", null, JOptionPane.ERROR_MESSAGE);
					logInButton.setVisible(false);
					return;
				}
				Globals.token = Globals.loginObj.getString(SnapchatAPI.AUTH_TOKEN_KEY);
			}
		});


		JPanel loginPanel = new JPanel();
		BoxLayout boxLayout2 = new BoxLayout(loginPanel, BoxLayout.Y_AXIS);
		loginPanel.setLayout(boxLayout2);
		loginPanel.add(Box.createVerticalGlue());
		loginPanel.add(logInButton);
		this.add(loginPanel, BorderLayout.SOUTH);

		this.getContentPane().repaint();
		this.pack();
	}

	public void setScreenToSignUp()
	{
		this.getContentPane().removeAll();
		this.setBackground(new Color(0xF5, 0xF5, 0xF5));
		this.getContentPane().repaint();
	}
}
