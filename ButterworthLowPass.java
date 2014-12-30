import java.awt.image.*;
import com.pearsoneduc.ip.io.*;
import com.pearsoneduc.ip.op.*;
import com.pearsoneduc.ip.util.IntervalTimer;

/*
	ButterworhLowPass class define method to filter the grayscale image 
*/
public class ButterworthLowPass implements Runnable
{
	FabricWindow fb;
	Thread progressor;
	boolean isStopped = false;

	// this method requires the the image to filter and the order of lowpass filter and the radius also
	public BufferedImage butterworthFilter(FabricWindow fb, BufferedImage buffImg, String opFile, int order, float radius)
	{
		this.fb = fb;
		fb.iDetails.setInfoDetails("Wait a while . .");
		BufferedImage outputImage = null;
		try
		{
			progressor = new Thread(this, "Progress");
			progressor.setPriority(7);
			progressor.start();
			//ImageDecoder input = ImageFile.createImageDecoder(ipFile);
			ImageEncoder output = ImageFile.createImageEncoder(opFile);
			int n = order;
			float r = Math.max(0.05f, Math.min(0.95f, radius));

			BufferedImage inputImage = buffImg;
			ImageFFT fft = new ImageFFT(inputImage);
			System.out.println("Order " + n + " filter, radius = " + r + "...");
			IntervalTimer timer = new IntervalTimer();
			timer.start();
			fft.transform();
			fft.butterworthLowPassFilter(n, r);
			fft.transform();
			System.out.println("Filtering finished [" + timer.stop() + " sec]");

			outputImage = fft.toImage(null);
			output.encode(outputImage);
		
			System.out.println("After BWLPF:" + outputImage.getWidth() + "x" + outputImage.getHeight());
			isStopped = true;
			progressor.join();
		}
		catch (Exception e)
		{
			System.err.println(e);
			return null;
		}
		return outputImage;
	}

	public void run()
	{
		while(!isStopped)
		{
			try
			{
				fb.iDetails.setInfoDetails(" .");
				Thread.sleep(500);
				//fb.status.revalidate();				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}