import java.applet.Applet;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Canvas;
import java.awt.Font;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

/*
 * FabricTextureAnalysis class 

*/

public class FabricTextureAnalysis
{
	public static void main(String [] awz) throws Exception
	{
        new FabricWindow("Fabric Texture Analysis");
	}

}

/*
	The FabricWindow class generates the main window which let you select and then analyze the selected image.
	This class extends JFrame class and implements ActionListener
*/
class FabricWindow extends JFrame implements ActionListener
{
	JButton bttnOpen;
	JTextField txtFilePath;
	ImageCanvas pnlImage;
	JLabel ftaTitle;
	JLabel opTitle;
	JLabel opDetails;

	JLabel lblButter;
	JLabel lblGray;
	JLabel lblHistogram;
	JLabel lblProjection;
	
	JLabel status;
	Toolkit toolkit;
	Dimension screen;
	Graphics g;
	InfoPanel iPanel;
	InfoDetailsPanel iDetails;
	
	HistogramEqualization histogramEqualization = null;
	ImageRead imageRead = null;
	ButterworthLowPass butter = null;
	
	/*
		FabricWindow Constructor creates Window for texture image analysis.
		This constructor throw exceptions
	*/
	FabricWindow(String title) throws Exception
	{
		super(title); 		// sets title of the window
		
		setWindow();
		addBrowser();		
		addSeparators();
		addStatusBar();
		setTitles();
		addLabels();
		
		//	creates panel where the operation information will be displayed
		iPanel = new InfoPanel();
		add(iPanel);
		
		//	creates panel where the operation information will be displayed
		iDetails = new InfoDetailsPanel();
		add(iDetails);
		
		// creates canvas window where image is displayed after each stage
		pnlImage = new ImageCanvas();
		add(pnlImage);
		
		setVisible(true);	// display the main window
	}
	
	void setWindow() throws Exception
	{
		toolkit = Toolkit.getDefaultToolkit();
		screen = toolkit.getScreenSize();		// gets the screensize
		
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		imageRead = new ImageRead();
		butter = new ButterworthLowPass();
		//Sets the size of Fabric Window 
		setLayout(null);
		setBounds(0, 0, screen.width, screen.height - 35);
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
	}
	
	void addBrowser()
	{
		//Create the textfield and button which let you browse for image on which 			analysis is to be performed
		txtFilePath = new JTextField();
		txtFilePath.setBounds(20, 20, 250, 30);
		add(txtFilePath);		
		bttnOpen = new JButton("Open");
		bttnOpen.setBounds(275, 20, 100, 30);
		add(bttnOpen);
		bttnOpen.addActionListener(this);		
	}
	void addSeparators()
	{		
		//Devides the main window into 3 sections using separators
		add(new JSeparator()).setBounds(20, 65, 250, 5);
		add(new JSeparator()).setBounds(1096, 65, 250, 5);
		add(new JSeparator()).setBounds(20, 575, 250, 5);
		add(new JSeparator()).setBounds(1096, 575, 250, 5);
		add(new JSeparator()).setBounds(1096, 320, 250, 5);
		add(new JSeparator(SwingConstants.VERTICAL)).setBounds(290, 70, 5, 500);
		add(new JSeparator(SwingConstants.VERTICAL)).setBounds(1071, 70, 5, 500);
	}
	void addStatusBar()
	{
		// Creates the status bar and display current status of process
		status = new JLabel("Status Bar..");
		status.setBounds(0, getHeight() - 50, getWidth(), 20);
		status.setFont(new Font("Trebuchet MS",Font.PLAIN,14));
		status.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.RED));
		add(status);
	
	}
	
	void setTitles()
	{
		//Sets the Title of project " Fabric Texture Analysis
		ftaTitle = new JLabel("Fabric Texture Analysis", SwingConstants.CENTER);
		ftaTitle.setBounds(0, 0, screen.width, 30);
		ftaTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
		ftaTitle.setVerticalAlignment(SwingConstants.TOP);
		ftaTitle.setForeground(Color.RED);
		add(ftaTitle);
		
		// Sets title of operation information panel
		opTitle = new JLabel("Operation Information");
		opTitle.setBounds(1096, 40, 250, 20);
		opTitle.setFont(new Font("Trebuchet MS",Font.PLAIN,18));
		opTitle.setForeground(Color.MAGENTA);
		add(opTitle);
		
		// Sets title of operation details panel
		opDetails = new JLabel("Operation Details");
		opDetails.setBounds(1096, 295, 250, 20);
		opDetails.setFont(new Font("Trebuchet MS",Font.PLAIN,18));
		opDetails.setForeground(Color.MAGENTA);
		add(opDetails);
	}
	
	void addLabels()
	{
		// Create the Gray Scale label from which RGB image will be converted into GrayScale
		lblGray = new JLabel(" Gray Scale  >>>");
		lblGray.setBounds(20,75,250,30);
		lblGray.setFont(new Font("Trebuchet MS",Font.PLAIN,18));
		add(lblGray);
		
		// creates the the label for butterwirth filter
		lblButter = new JLabel(" Butter Worth Filter  >>>");
		lblButter.setBounds(20,115,250,30);
		lblButter.setFont(new Font("Trebuchet MS",Font.PLAIN,18));
		add(lblButter);
		
		// creates the label for Histogram equalization which let you equalize the image
		lblHistogram = new JLabel(" Histogram Equilization  >>>");
		lblHistogram.setBounds(20,155,250,30);
		lblHistogram.setFont(new Font("Trebuchet MS",Font.PLAIN,18));
		add(lblHistogram);
		
		lblProjection = new JLabel("Spatial Domain Projection  >>>");
		lblProjection.setBounds(20,195,250,30);
		lblProjection.setFont(new Font("Trebuchet MS",Font.PLAIN,18));
		add(lblProjection);
		
		lblGray.setEnabled(false);
		lblButter.setEnabled(false);
		lblHistogram.setEnabled(false);
		lblProjection.setEnabled(false);
	}
	
	// handles the users action for searching the image
	public void actionPerformed(ActionEvent ae) 
	{
		if(ae.getSource() == bttnOpen)
		{
			imageRead.showImage(this);			
			setMouseListeners();
		}
	}
	
	void setMouseListeners()
	{
		lblGray.setHorizontalAlignment(SwingConstants.LEFT);
		lblButter.setHorizontalAlignment(SwingConstants.LEFT);
		lblHistogram.setHorizontalAlignment(SwingConstants.LEFT);
		lblProjection.setHorizontalAlignment(SwingConstants.LEFT);
		
		lblButter.setEnabled(false);
		lblHistogram.setEnabled(false);
		lblProjection.setEnabled(false);
		lblGray.setEnabled(true);
		lblGray.setForeground(Color.BLACK);
		lblButter.setForeground(Color.BLACK);
		lblHistogram.setForeground(Color.BLACK);
		lblProjection.setForeground(Color.BLACK);
		
		lblGray.addMouseListener(new GrayMouseListener(this));
	}
}


/*
	ImageCanvas class is subclass Canvas class which gives the space for image to be displayed
	This class provides the paint method to display image on it after each stage
*/
class ImageCanvas extends Canvas
{
	BufferedImage buffImg;
	ImageCanvas()
	{
		// sets the size of window to 600x600
		setBounds(380, 70, 600, 600);
	}
	public void paint(Graphics g)
	{
		if(buffImg != null)
		{
			// if provided displays the image on window
			g.drawImage(buffImg, 0, 0, buffImg.getWidth(), buffImg.getHeight(), null);
		}
	}
	void draw(BufferedImage buffImg)
	{
		this.buffImg = buffImg;
		repaint();
	}
}

/*
	InfoPanel class creates the Panel where the operation information about the current status of process
	will be displayed.
	This class extends Jpanel class
*/
class InfoPanel extends JPanel
{
	JTextArea info;
	JScrollPane jsp;
	InfoPanel()
	{
		setBounds(1072, 66, 270, 200);
		setLayout(null);
		//setBorder(new LineBorder(Color.RED, 2));
		
		// creates textarea and sets line and word wrapping on
		info = new JTextArea();
		info.setBounds(10, 10, 250, 180);		
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		info.setFont(new Font("Courier New", Font.PLAIN, 18));
		info.setForeground(new Color(0, 27, 70));
		info.setEditable(false);
		
		// creates the scrollpane and display scrollbar if needed to display the information
		jsp = new JScrollPane(info);
		jsp.setBounds(10, 10, 250, 180);
		add(jsp);
	}

	// Sets the operation information for each process stage.
	// Called after hovering mouse on any of the process stage
	void setInfo(String text)
	{
		info.setText(text);
	}
}

class InfoDetailsPanel extends JPanel
{
	JTextArea details;
	JScrollPane jsp;
	InfoDetailsPanel()
	{
		setBounds(1072, 320, 270, 255);
		setLayout(null);
		//setBorder(new LineBorder(Color.RED, 2));
		
		// creates textarea and sets line and word wrapping on
		details = new JTextArea();
		details.setBounds(10, 10, 250, 235);
		details.setLineWrap(true);
		details.setWrapStyleWord(true);
		details.setFont(new Font("Consolas", Font.PLAIN, 12));
		details.setForeground(new Color(0, 27, 70));
		//details.setEditable(false);
		
		// creates the scrollpane and display scrollbar if needed to display the information
		jsp = new JScrollPane(details);
		jsp.setBounds(10, 10, 250, 235);
		add(jsp);
	}

	// Sets the operation information for each process stage.
	// Called after hovering mouse on any of the process stage
	void setInfoDetails(String text)
	{
		details.append(text);
	}
}