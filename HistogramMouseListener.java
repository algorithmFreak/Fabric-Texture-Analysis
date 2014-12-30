import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import java.awt.Color;


public class HistogramMouseListener extends MouseAdapter
{
	FabricWindow fb;
	
	HistogramMouseListener(FabricWindow fb)
	{
		this.fb = fb;
	}
	//	perform histogram equalization on filtered image and displays it in same canvas window			
	public void mouseClicked(MouseEvent me)
	{
		fb.histogramEqualization = new HistogramEqualization(fb.imageRead.buffImg, fb.pnlImage);
		fb.status.setText("Histogram Equilization complete..");
		fb.lblHistogram.setForeground(new Color(0, 171, 0));
		fb.lblHistogram.setHorizontalAlignment(SwingConstants.RIGHT);
		
		fb.lblHistogram.removeMouseListener(this);
		
		fb.lblProjection.setEnabled(true);
		fb.lblProjection.addMouseListener(new SDProjectionMouseListener(fb));
	}
	
	public void mouseEntered(MouseEvent me)
	{
		fb.lblHistogram.setForeground(Color.MAGENTA);
		fb.lblHistogram.setHorizontalAlignment(SwingConstants.CENTER);
		fb.iPanel.setInfo("Makes white pixels more brighter and black pixels more darker..");
	}
		
	public void mouseExited(MouseEvent me)
	{
		fb.lblHistogram.setForeground(Color.BLACK);
		fb.lblHistogram.setHorizontalAlignment(SwingConstants.LEFT);
	}
}