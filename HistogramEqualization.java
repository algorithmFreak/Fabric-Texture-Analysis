import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import java.io.FileWriter;
import java.io.BufferedWriter;

import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JTextField;

/*
	HistogramEqualization class defines methods to equlaize the image and to create histogram of image
*/
public class HistogramEqualization {
    PanelImageEqualized pnlImageEqualized = new PanelImageEqualized();
	int[][] rgbsEqualized ;

    HistogramEqualization(BufferedImage img, Canvas pnlImage) {
        equalize(img, pnlImage);
    }

	// equalizes the given butterworth lowpass filtered the image
    private void equalize(BufferedImage img, Canvas pnlImage) {
		int w, h;
        w = img.getWidth();
        h = img.getHeight();
		System.out.println(w+" "+h);

		System.out.println("Before Equalize:" + w + "x" + h );
        int[][] rgbs = new int[w][h];
        int[] rgbsForDisplay = new int[w * h];
        rgbsEqualized = new int[w][h];

        int[] histogramValue = new int[256];
        int[] histogramValueEqualized = new int[256];
        int[] cdf = new int[256];
        int cdfMin = 0;
        int grayVal;
        int maxValue = 0;
        int maxValueEqualized = 0;
        int temp = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
				// Get the RGB for each pixel and convert into grayscale
                grayVal = img.getRGB(i, j);
                grayVal = grayVal & 0xff;
                rgbs[i][j] = grayVal;
                rgbsForDisplay[temp] = grayVal;
                histogramValue[grayVal] = histogramValue[grayVal] + 1;
                if (histogramValue[grayVal] > maxValue) {
                    maxValue = histogramValue[grayVal];
                }
                temp++;
            }
        }

        cdfMin = cdf[0] = histogramValue[0];
        for (int i = 1; i < histogramValue.length; i++) {
            cdf[i] = histogramValue[i] + cdf[i - 1];
        }

        int newPixelValueEqualized = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                grayVal = rgbs[i][j];
                grayVal = grayVal & 0xff;
                newPixelValueEqualized = (int) ((cdf[grayVal] - cdfMin) * 255) / ((w * h) - cdfMin);
                rgbsEqualized[i][j] = newPixelValueEqualized;
                histogramValueEqualized[newPixelValueEqualized] = histogramValueEqualized[newPixelValueEqualized] + 1;
                if (histogramValueEqualized[newPixelValueEqualized] > maxValueEqualized) {
                    maxValueEqualized = histogramValueEqualized[newPixelValueEqualized];
                }
            }
        }
		
        pnlImageEqualized.draw(rgbsEqualized, w, h, pnlImage);
		writeImageData(w,h);
    }
	
	public void writeImageData(int width, int height)
	{
		try
		{
			FileWriter fw = new FileWriter("histogramImageData.txt");
			System.out.println(height + " " + width);
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i = 0; i < height; i++)
			{
				for(int j = 0; j < width; j++)
				{
					bw.write(rgbsEqualized[j][i] + "\t");
				}
				bw.newLine();
			}
			bw.close();
			fw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	int[][] getImageData()
	{
		return rgbsEqualized;
	}
}

class PanelImageEqualized {
    int newColor = 0;

    public void draw(int[][] rgb, int wi, int hi, Canvas pnlImage) {
        Graphics g = pnlImage.getGraphics();
        if (wi != 0) {
            for (int i = 0; i < wi; i++) {
                for (int j = 0; j < hi; j++) {
                    newColor = rgb[i][j] << 16 | rgb[i][j] << 8 | rgb[i][j];
                    g.setColor(new Color(newColor));
                    g.drawLine(i, j, i, j);
                }
            }
        }
    }
}