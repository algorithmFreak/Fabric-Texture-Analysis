import java.io.FileWriter;
import java.io.BufferedWriter;
import java.awt.Graphics;
import java.awt.Color;

import java.util.Vector;

class SDProjection
{
	int rgbs[][];
	long vertical[];
	long horizontal[];
	final int N = 5;
	
	Vector lclVert[];
	Vector lclHorizon[];
		
	FabricWindow fb = null;
	
	SDProjection(int[][] rgbs,FabricWindow fb)
	{
		this.rgbs = rgbs;
		this.fb = fb;
		vertical = new long[rgbs[0].length];
		horizontal = new long[rgbs.length];
		
		calculateVerticalProjection();
		calculateHorizontalProjection();
		
		movingAverage(horizontal);
		movingAverage(vertical);
		
		calLocalMinima();
		writeProjectionData();
		
		drawGrid();
		
		
		/*
		calLocalMinima();
		writeProjectionData();
		*/
	}
		
	void calculateVerticalProjection()
	{
		for(int i = 0; i < rgbs.length; i++)
		{
			vertical[i] = 0;
			for(int j = 0; j < rgbs[i].length; j++)
				vertical[i] += rgbs[i][j];
		}
		//movingAverage(vertical);
	}
	
	void calculateHorizontalProjection()
	{
		for(int i = 0; i < rgbs.length; i++)
		{
			horizontal[i] = 0;
			for(int j = 0; j < rgbs[i].length; j++)
				horizontal[i] += rgbs[j][i];
		}
		//movingAverage(horizontal);
	}
	
	void movingAverage(long[] projection)
	{
	    long[] a = new long[N];
		int length = projection.length;
        long sum = 0l;
		int i = 0;
        for (i = 0; i < length; i++) 
		{
			sum -= a[i % N];
            a[i % N] = projection[i];
			sum += a[i % N];
            if (i >= N-1) 
			{
				projection[i - N + 1] = sum / N;				
			}
        }
		i = i - N + 1;
		for(; i < length; i++)
			projection[i] = 0l;
	}
	
	void writeProjectionData()
	{
		FileWriter fw = null;
		BufferedWriter bw = null;
		try
		{
			fw = new FileWriter("ProjectionImageData.txt");
			bw = new BufferedWriter(fw);
			
			bw.write(" Vertical Projection");
			bw.newLine();
			bw.newLine();
			
			for(int i = 0; i < vertical.length; i++)
			{
				if( i % 50 == 0)
					bw.newLine();
				bw.write(vertical[i] + "\t" );
				
			}
			
			bw.newLine();
			bw.newLine();
			bw.newLine();
			bw.write(" Horizontal Projection");
			bw.newLine();
			bw.newLine();
			
			for(int i = 0; i < horizontal.length; i++)
			{
				if( i % 50 == 0)
					bw.newLine();
				bw.write(horizontal[i] + "\t");
			}
			
			bw.newLine();
			bw.newLine();
			bw.newLine();
			bw.write(" Vertical Local Minima");
			bw.newLine();
			bw.newLine();
			
			for(int i = 0; i < lclVert[0].size(); i++)
			{
				if( i % 10 == 0)
					bw.newLine();
				bw.write(lclVert[0].get(i) + "\t");
			}
			
			bw.newLine();
			bw.newLine();
			bw.newLine();
			bw.write(" Horizontal Local Minima");
			bw.newLine();
			bw.newLine();
			
			for(int i = 0; i < lclHorizon[0].size(); i++)
			{
				if( i % 10 == 0)
					bw.newLine();
				bw.write(lclHorizon[0].get(i) + "\t");
			}
			
			bw.close();
			fw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	void calLocalMinima()
	{
		//lclVert = new long[vertical.length][2];
		lclVert = new Vector[2];
		lclVert[0] = new Vector(50,10);
		lclVert[1] = new Vector(50,10);
		//lclVert[0][0] = 0;
		//lclVert[0][1] = vertical[0];
		lclVert[0].add(vertical[0]);
		boolean flag = false;
		boolean write = false;
		int j = 1;
		for(int i = 1; i < vertical.length -1; i++)
		{
			if( vertical[i] < vertical[i+1] && !flag)
			{
				flag = true;
				write = true;
			}
			else if( vertical[i] > vertical[i+1] )
				flag = false;
		
			if(write)
			{
				//lclVert[j][0] = i;
				//lclVert[j++][1] = vertical[i];
				lclVert[0].add(vertical[i]);
				lclVert[1].add(i);
				write = false;
			}
		}
		
		//lclHorizon = new long[horizontal.length][2];
		lclHorizon = new Vector[2];
		lclHorizon[0] = new Vector(50,10);
		lclHorizon[1] = new Vector(50,10);
		//lclHorizon[0][0] = 0;
		//lclHorizon[0][1] = horizontal[0];
		flag = false;
		write = false;
		j = 1;
		for(int i = 1; i < horizontal.length -1; i++)
		{
			if(horizontal[i] < horizontal[i+1] && !flag)
			{
				flag = true;
				write = true;
			}
			else if( horizontal[i] > horizontal[i+1] )
				flag = false;
		
			if(write)
			{
				//lclHorizon[j][0] = i;
				//lclHorizon[j++][1] = horizontal[i];
				lclHorizon[0].add(horizontal[i]);
				lclHorizon[1].add(i);
				write = false;
			}
		}
	}
	
	int getWeftCount()
	{
		return lclHorizon[0].size() - 1;
	}
	int getWarpCount()
	{
		return lclVert[0].size() - 1;
	}
	
	void drawGrid()
	{
		Graphics g = fb.pnlImage.getGraphics();
		g.setColor(Color.CYAN);
		int j = 0;
		for(int i = 0; i < lclVert[1].size(); i++)
		{
			j = (Integer)lclVert[1].get(i);
			g.drawLine(j,0,j,600);
		}
		
		for(int i = 0; i < lclHorizon[1].size(); i++)
		{
			j = (Integer)lclHorizon[1].get(i);
			g.drawLine(0,j,600,j);
		}
	}
}