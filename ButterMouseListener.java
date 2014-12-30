import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import java.awt.Color;


public class ButterMouseListener extends MouseAdapter
{
	FabricWindow fb;
	
	ButterMouseListener(FabricWindow fb)
	{
		this.fb = fb;
	}
	// calls butterworthFilter method and filters the 8-bit grayscale image and reduces the noise from it
	public void mouseClicked(MouseEvent me)
	{
		fb.iDetails.setInfoDetails("\n==> Checking exceptions...\n");
		if(fb.imageRead.fileName == null)		// If no input image is provided
		{
			JOptionPane.showMessageDialog(fb, "Input the image", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		fb.iDetails.setInfoDetails("==> Applying butterworth filter...\n");
		ResizeImageFilter.resize(fb, fb.imageRead.buffImg , 600, 600);
		System.out.println(fb.imageRead.buffImg);
		fb.imageRead.buffImg = fb.butter.butterworthFilter(fb, fb.imageRead.buffImg, "temp.jpg", 10, 15);
		fb.imageRead.img = ResizeImageFilter.crop(fb, fb.imageRead.buffImg);
		fb.imageRead.convert();
		if(fb.imageRead.buffImg == null)		// If input image is not 8-bit grayscale image
		{
			JOptionPane.showMessageDialog(fb, "Butterworth requires..\n8-bit grayscale image", "Error", JOptionPane.ERROR_MESSAGE);
			fb.iDetails.setInfoDetails("	Error in image..\n");
			return;
		}

		fb.imageRead.draw(fb);		//Display the filtered image
		fb.status.setText("Butterworth complete..");		// sets the process status to Butterworth complete
		fb.iDetails.setInfoDetails("==> Operation 2 completed..\n");
		fb.lblButter.setForeground(new Color(0, 171, 0));
		fb.lblButter.setHorizontalAlignment(SwingConstants.RIGHT);
	
		fb.lblButter.removeMouseListener(this);
		
		fb.lblHistogram.setEnabled(true);
		fb.lblHistogram.addMouseListener(new HistogramMouseListener(fb));
	}
		
	public void mouseEntered(MouseEvent me)
	{
		fb.lblButter.setForeground(Color.MAGENTA);
		fb.lblButter.setHorizontalAlignment(SwingConstants.CENTER);
		fb.iPanel.setInfo("Requires grayscale image as input.. Reduces noice of image through FFT");
	}
	
	public void mouseExited(MouseEvent me)
	{
		fb.lblButter.setForeground(Color.BLACK);
		fb.lblButter.setHorizontalAlignment(SwingConstants.LEFT);
	}
}