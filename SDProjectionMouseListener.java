import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import java.awt.Color;


public class SDProjectionMouseListener extends MouseAdapter
{
	FabricWindow fb;
	
	SDProjectionMouseListener(FabricWindow fb)
	{
		this.fb = fb;
	}
	//	
	public void mouseClicked(MouseEvent me)
	{
		SDProjection sdp = new SDProjection(fb.histogramEqualization.rgbsEqualized,fb);
		System.out.println("Weft Count:" + sdp.getWeftCount());
		System.out.println("Warp Count:" + sdp.getWarpCount());
		fb.status.setText("Spatial Domain Integral Projection complete..");
		fb.lblProjection.setForeground(new Color(0, 171, 0));
		fb.lblProjection.setHorizontalAlignment(SwingConstants.RIGHT);
		
		fb.lblProjection.removeMouseListener(this);
		
		//fb.lblProjection.nsetEnabled(true);
		//fb.lblProjection.addMouseListener(new SDProjectionMouseListener(fb));
	}
	
	public void mouseEntered(MouseEvent me)
	{
		fb.lblProjection.setForeground(Color.MAGENTA);
		fb.lblProjection.setHorizontalAlignment(SwingConstants.CENTER);
		fb.iPanel.setInfo("Spatial Domain Integral Projection calculates local minima to detect the warn and weft separation lines....");
	}
		
	public void mouseExited(MouseEvent me)
	{
		fb.lblProjection.setForeground(Color.BLACK);
		fb.lblProjection.setHorizontalAlignment(SwingConstants.LEFT);
	}
}