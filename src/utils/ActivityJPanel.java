package utils;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;


public class ActivityJPanel extends JPanel
{
	private static final long serialVersionUID = 298176245274445654L;

	public void select()
	{
		setComponentColor(this, new Color(0xDDDDDD));
	}
	
	public void deselect()
	{
		setComponentColor(this, new Color(0xFFFFFF));
	}
	
	public void setComponentColor(Component c, Color col)
	{
		c.setBackground(col);
		if (c instanceof JPanel)
		{
			Component[] comps = ((JPanel)c).getComponents();
			for (int i = 0; i < comps.length; i++)
			{
				setComponentColor(comps[i], col);
			}
		}
	}
}
