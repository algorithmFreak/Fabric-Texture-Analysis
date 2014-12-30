import java.awt.Canvas;
import java.awt.Image;

import java.awt.image.RGBImageFilter;
import java.awt.image.FilteredImageSource;

public class GrayScaleFilter extends RGBImageFilter
{ 
	/**
	public Image filter(Canvas can, Image image)
	returns filtered grayscale image
	*/
	public Image filter(Canvas can, Image image)
	{
		return can.createImage(new FilteredImageSource(image.getSource(), this)); 
	}
 
	/**
	public int filterRGB(int x, int y, int rgb) 
	converts RGB mode of pixel into grayscale
	*/
	public int filterRGB(int x, int y, int rgb) 
	{ 
		int r = (rgb >> 16) & 0xff; 
		int g = (rgb >> 8) & 0xff; 
		int b = rgb & 0xff; 
		int k = (int) (.56 * g + .33 * r + .11 * b); 
		return (0xff000000 | k << 16 | k << 8 | k); 
	}
}
