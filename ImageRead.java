import java.awt.Graphics;
import java.awt.Image;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.awt.image.VolatileImage;

import java.io.File;

import javax.imageio.ImageIO;

import java.util.Properties;
import javax.imageio.stream.FileImageOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;



public class ImageRead implements Runnable
{	
	BufferedImage buffImg = null;
	Image img = null;
	String fileName = null;
	Thread t;

	/*
	 public void showImage(FabricWindow fw)
	 showImage method let the user select the image and display it.
	 Arg: fw is FabricWindow on which image is to be displayed
	*/
	public void showImage(FabricWindow fw)
	{
		// Creates filechooser and sets file filter for selecting only image files
		JFileChooser jfc = new JFileChooser("./Samples");
		FileNameExtensionFilter filter =  new FileNameExtensionFilter("Image File", "png", "gif", "jpg", "jpeg");
		jfc.setFileFilter(filter);
		int isFileSelected = jfc.showOpenDialog(fw);		// selects the dialogbox and let user select the image
		
		buffImg = null;
		
		// if user successfully selects image file
		if(isFileSelected == 0)
		{
			fw.txtFilePath.setText(jfc.getSelectedFile().toString());
			try 
			{
				// reads the selected image file into BufferedImage object buffImg
				buffImg = ImageIO.read(new File((fileName = fw.txtFilePath.getText())));
				Graphics g;
				img = buffImg;
				g = fw.pnlImage.getGraphics();
				t = new Thread(this);
				t.start();
				t.join();
				fw.pnlImage.draw(buffImg);	// display selected image on canvas window
				System.out.println("Image Size:" + buffImg.getWidth() + "x" + buffImg.getHeight() );
				//g.drawImage(buffImg, 0, 0, buffImg.getWidth(), buffImg.getHeight(), null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/*
	 public void showGrayScale(FabricWindow fabricWindow)
	 Converts the RGB image into grayscale using GraySacleFilter.
	 Arg: Window on which the image to be displayed.
	*/
	public void showGrayScale(FabricWindow fabricWindow)
	{
		// Initialize the GrayScaleFilter to filter the RGB image and create grayscale image
		fabricWindow.iDetails.setInfoDetails("==> Applying grayscale filter...\n");
		GrayScaleFilter gray = new GrayScaleFilter();
		if(img == null)
		{
			JOptionPane.showMessageDialog(fabricWindow, "Input the image", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// filters RGB image and converts into grayscale and stores into Image object img
		img = gray.filter(fabricWindow.pnlImage, img);
		fabricWindow.iDetails.setInfoDetails("	Grayscale filter applied..\n");
		
		Graphics g;
		g = fabricWindow.pnlImage.getGraphics();
		
		fabricWindow.iDetails.setInfoDetails("	Processing image..\n");
		convert();
		
		// Display the grayscale image
		g.drawImage(buffImg, 0, 0, null);
		System.out.println("Gray Scale:" +buffImg.getWidth() + "x" + buffImg.getHeight() );
		fabricWindow.iDetails.setInfoDetails("	Image ready to be drawn..\n");
	}
	
	public void draw(FabricWindow fabricWindow)
	{
		Graphics g;
		g = fabricWindow.pnlImage.getGraphics();
		g.drawImage(img, 0, 0, null);
	}
	
	public void run()
	{
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) { }
	}
	
	void convert()
	{
		// Converts the Image object img into BufferedImage object buffImg for further processing
		try {
			int pixels[] = new int[600 * 600];
			int iw = img.getWidth(null), ih = img.getHeight(null);
			PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels, 0, iw);
			pg.grabPixels();
			buffImg = new BufferedImage(iw, ih, BufferedImage.TYPE_BYTE_GRAY);
			buffImg.setRGB(0, 0, iw, ih, pixels, 0, iw);	
		}
		catch(Exception e) { 
			e.printStackTrace(); 
		}
	}
}
