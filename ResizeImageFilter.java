import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.RenderingHints;
import java.awt.Graphics2D;

public class ResizeImageFilter
{ 
	/**
	public Image filter(Canvas can, Image image)
	resize to 600x600 image
	*/
	public static BufferedImage resize(FabricWindow fb, BufferedImage img, int newW, int newH)
	{
		int w = img.getWidth();
		int h = img.getHeight();
		
		fb.iDetails.setInfoDetails("==> Applying image resize algorithm...\n");
		BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		fb.iDetails.setInfoDetails("	Temp. image created..\n");
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		fb.iDetails.setInfoDetails("	Image ready to dispose..\n");
		g.dispose();
		
		return dimg;
	}
	
	public static Image crop(FabricWindow fb, BufferedImage img)
	{
		CropImageFilter c = new CropImageFilter(0, 0, 600, 600);
		FilteredImageSource f = new FilteredImageSource(img.getSource(), c);
		return fb.createImage(f);
	}
}
