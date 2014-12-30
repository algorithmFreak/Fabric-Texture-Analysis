import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import java.awt.Color;

public class GrayMouseListener extends MouseAdapter
{
	FabricWindow fb;
	
	GrayMouseListener(FabricWindow fb)
	{
		this.fb = fb;
	}
	
	// Calls the method showGrayScale function through which RGB image is converted 
	// into grayscale and displayed in same canvas window
	public void mouseClicked(MouseEvent me)
	{
		fb.imageRead.img = ResizeImageFilter.resize(fb, fb.imageRead.buffImg, 600, 600);
		fb.imageRead.showGrayScale(fb);
		fb.status.setText("Grayscale complete..");		// sets process status to "Grayscale Complete
		fb.lblGray.setForeground(new Color(0, 171, 0));
		fb.lblGray.setHorizontalAlignment(SwingConstants.RIGHT);
		fb.lblGray.removeMouseListener(this);			// Removes the mouse listener from the grayscale option
		
		fb.lblButter.setEnabled(true);
		fb.lblButter.addMouseListener(new ButterMouseListener(fb));
		fb.iDetails.setInfoDetails("==> Operation 1 complete...\n");
	}
	
	public void mouseEntered(MouseEvent me)
	{
		fb.lblGray.setForeground(Color.MAGENTA);
		fb.lblGray.setHorizontalAlignment(SwingConstants.CENTER);
		fb.iPanel.setInfo("This part converts image into grayscale as,\n\n0.33 * RED\n0.56 * GREEN\n0.11 * BLUE");
	}
	
	public void mouseExited(MouseEvent me)
	{
		fb.lblGray.setForeground(Color.BLACK);
		fb.lblGray.setHorizontalAlignment(SwingConstants.LEFT);
	}
}