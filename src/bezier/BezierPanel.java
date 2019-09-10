package bezier;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

class BezierPanel extends JComponent
{
	private static int SIZE = 6;
	private int current,current2,current3,current4;
	private Point2D[] points;
	private Point2D[] points2;
	private Point2D[] points3;
	private Point2D[] points4;
	
	private Point2D pressPoint2d,releasPoint2d;
	public Image image;
	
	public String[] getPoints(){
		String[] s = {String.valueOf(points[0].getX()),String.valueOf(points[0].getY()),
					String.valueOf(points[1].getX()),String.valueOf(points[1].getY()),
					String.valueOf(points[2].getX()),String.valueOf(points[2].getY()),
					String.valueOf(points[3].getX()),String.valueOf(points[3].getY()),
					String.valueOf(points2[1].getX()),String.valueOf(points2[1].getY()),
					String.valueOf(points2[2].getX()),String.valueOf(points2[2].getY()),
					String.valueOf(points2[3].getX()),String.valueOf(points2[3].getY()),
					String.valueOf(points3[1].getX()),String.valueOf(points3[1].getY()),
					String.valueOf(points3[2].getX()),String.valueOf(points3[2].getY()),
					String.valueOf(points3[3].getX()),String.valueOf(points3[3].getY()),
					String.valueOf(points4[1].getX()),String.valueOf(points4[1].getY()),
					String.valueOf(points4[2].getX()),String.valueOf(points4[2].getY())};
		return s;
	}
	public void setBg(int type){
		try {
			if (type == 1) {
				image = ImageIO.read(getClass().getResource("/image/0000.bmp"));
			} else {
				image = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setRotate(int t){
		double x0,y0,x,y;
		double zx = 127,zy = 127;
		double x1 = points[0].getX(),y1 = points[0].getY();
		double x2 = points2[3].getX(),y2 = points2[3].getY();
		double x3 = points[3].getX(),y3 = points[3].getY();
		double x4 = points3[3].getX(),y4 = points3[3].getY();
		zx = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))  
			    / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));  
			  
		zy = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))  
			    / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));  
		if (Double.isInfinite(zx) || Double.isNaN(zx) ||
			Double.isInfinite(zy) || Double.isNaN(zy) ||
			zx < 0 || zx > 254 || zy < 0 || zy > 254) {
			zx = 127;
			zy = 127;
		}
		for (int i = 0; i < 4; i++) {
			x = points[i].getX();
			y = points[i].getY();
			x0= (x - zx)*Math.cos(Math.PI/180*t) - (y - zy)*Math.sin(Math.PI/180*t) + zx ;
		    y0= (x - zx)*Math.sin(Math.PI/180*t) + (y - zy)*Math.cos(Math.PI/180*t) + zy ;
		    points[i].setLocation(x0, y0);
		}
		for (int i = 1; i < 4; i++) {
			x = points2[i].getX();
			y = points2[i].getY();
			x0= (x - zx)*Math.cos(Math.PI/180*t) - (y - zy)*Math.sin(Math.PI/180*t) + zx ;
		    y0= (x - zx)*Math.sin(Math.PI/180*t) + (y - zy)*Math.cos(Math.PI/180*t) + zy ;
		    points2[i].setLocation(x0, y0);
		}
		for (int i = 1; i < 4; i++) {
			x = points3[i].getX();
			y = points3[i].getY();
			x0= (x - zx)*Math.cos(Math.PI/180*t) - (y - zy)*Math.sin(Math.PI/180*t) + zx ;
		    y0= (x - zx)*Math.sin(Math.PI/180*t) + (y - zy)*Math.cos(Math.PI/180*t) + zy ;
		    points3[i].setLocation(x0, y0);
		}
		for (int i = 1; i < 3; i++) {
			x = points4[i].getX();
			y = points4[i].getY();
			x0= (x - zx)*Math.cos(Math.PI/180*t) - (y - zy)*Math.sin(Math.PI/180*t) + zx ;
		    y0= (x - zx)*Math.sin(Math.PI/180*t) + (y - zy)*Math.cos(Math.PI/180*t) + zy ;
		    points4[i].setLocation(x0, y0);
		}
		repaint();
	}
	public void setZoom(int type){
		double c = 1.1;
		double pianYu = 0.0;
		if (type == 0) {//放大
			c = 1.05;
			pianYu = -6.9;
		} else {//缩小
			c = 1/1.05;
			pianYu = 6.2;
		}
		for (int i = 0; i < 4; i++) {
			points[i].setLocation(points[i].getX()*c+pianYu, points[i].getY()*c+pianYu);
		}
		for (int i = 1; i < 4; i++) {
			points2[i].setLocation(points2[i].getX()*c+pianYu, points2[i].getY()*c+pianYu);
		}
		for (int i = 1; i < 4; i++) {
			points3[i].setLocation(points3[i].getX()*c+pianYu, points3[i].getY()*c+pianYu);
		}
		for (int i = 1; i < 3; i++) {
			points4[i].setLocation(points4[i].getX()*c+pianYu, points4[i].getY()*c+pianYu);
		}
		repaint();
	}
	public void setShape(String[] ps){
		for (int i = 0; i < 4; i++) {
			points[i].setLocation(Double.valueOf(ps[i*2]),Double.valueOf(ps[i*2+1]));
		}
		for (int i = 1; i < 4; i++) {
			points2[i].setLocation(Double.valueOf(ps[i*2+6]),Double.valueOf(ps[i*2+7]));
		}
		for (int i = 1; i < 4; i++) {
			points3[i].setLocation(Double.valueOf(ps[i*2+12]),Double.valueOf(ps[i*2+13]));
		}
		for (int i = 1; i < 3; i++) {
			points4[i].setLocation(Double.valueOf(ps[i*2+18]),Double.valueOf(ps[i*2+19]));
		}
		repaint();
	}
	public void setShape(int type){
		for (int i = 0; i < 4; i++) {
			points[i].setLocation(Data.ZB[type][i*2],Data.ZB[type][i*2+1]);
		}
		for (int i = 1; i < 4; i++) {
			points2[i].setLocation(Data.ZB[type][i*2+6],Data.ZB[type][i*2+7]);
		}
		for (int i = 1; i < 4; i++) {
			points3[i].setLocation(Data.ZB[type][i*2+12],Data.ZB[type][i*2+13]);
		}
		for (int i = 1; i < 3; i++) {
			points4[i].setLocation(Data.ZB[type][i*2+18],Data.ZB[type][i*2+19]);
		}
		repaint();
	}
	public void setShape_color(int type){
		for (int i = 0; i < 4; i++) {
			//System.out.print(Data.ZBcolor[type][i*2]+"/////"+Data.ZBcolor[type][i*2+1]+"////");
			points[i].setLocation(Data.ZBcolor[type][i*2],Data.ZBcolor[type][i*2+1]);
		}
		for (int i = 1; i < 4; i++) {
			//System.out.print(Data.ZBcolor[type][i*2+6]+"/////"+Data.ZBcolor[type][i*2+7]+"////");
			points2[i].setLocation(Data.ZBcolor[type][i*2+6],Data.ZBcolor[type][i*2+7]);
		}
		for (int i = 1; i < 4; i++) {
			//System.out.print(Data.ZBcolor[type][i*2+12]+"////"+Data.ZBcolor[type][i*2+13]+"////");
			points3[i].setLocation(Data.ZBcolor[type][i*2+12],Data.ZBcolor[type][i*2+13]);
		}
		for (int i = 1; i < 3; i++) {
			//System.out.print(Data.ZBcolor[type][i*2+18]+"////"+Data.ZBcolor[type][i*2+19]+"////");
			points4[i].setLocation(Data.ZBcolor[type][i*2+18],Data.ZBcolor[type][i*2+19]);
		}
		repaint();
	}
	public BezierPanel()
	{
		
		Point2D p1= new Point2D.Double(240 ,120);
		Point2D p2= new Point2D.Double(200, 100);
		Point2D p3= new Point2D.Double(170, 70);
		Point2D p4= new Point2D.Double(125 ,20);
		
		Point2D p21 = new Point2D.Double(100 ,50);
		Point2D p22 = new Point2D.Double(70, 100);
		Point2D p23 = new Point2D.Double(20, 125);
		
		Point2D p31 = new Point2D.Double(50, 150);
		Point2D p32 = new Point2D.Double(100 ,190);
		Point2D p33 = new Point2D.Double(125, 240);
		
		Point2D p41 = new Point2D.Double(140, 220);
		Point2D p42 = new Point2D.Double(180 ,180);
		
		points = new Point2D[]{p1,p2,p3,p4};
		points2 = new Point2D[]{p4,p21,p22,p23};
		points3 = new Point2D[]{p23,p31,p32,p33};
		points4 = new Point2D[]{p33,p41,p42,p1};
		
		addMouseListener(new MouseAdapter()
		{
			
			public void mousePressed(MouseEvent event)
			{
				pressPoint2d = null;
				Point2D p = event.getPoint();
				for(int i = 0; i < points.length; i++)
				{
					double x = points[i].getX() - SIZE/2;
					double y = points[i].getY() - SIZE/2;
					Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
					if(r.contains(p))
					{
						current = i;
						//break;
						return;
					}
				}
				
				for(int i = 0; i < points2.length; i++)
				{
					double x = points2[i].getX() - SIZE/2;
					double y = points2[i].getY() - SIZE/2;
					Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
					if(r.contains(p))
					{
						current2 = i;
						//break;
						return;
					}
				}
				
				for(int i = 0; i < points3.length; i++)
				{
					double x = points3[i].getX() - SIZE/2;
					double y = points3[i].getY() - SIZE/2;
					Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
					if(r.contains(p))
					{
						current3 = i;
						//break;
						return;
					}
				}
				for(int i = 0; i < points4.length; i++)
				{
					double x = points4[i].getX() - SIZE/2;
					double y = points4[i].getY() - SIZE/2;
					Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
					if(r.contains(p))
					{
						current4 = i;
						//break;
						return;
					}
				}
				if (current == -1 && current2 == -1 && current3 == -1 && current4 == -1) {
					JComponent component = (JComponent)event.getSource();
					component.setCursor(new Cursor(Cursor.HAND_CURSOR));
					pressPoint2d = p;
				}
			}
			
			public void mouseReleased(MouseEvent event)
			{
				current = -1;
				current2 = -1;
				current3 = -1;
				current4 = -1;
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter()
		{
				public void mouseMoved(MouseEvent e) {
					JComponent component = (JComponent)e.getSource();
					Point2D p = e.getPoint();
					boolean yes = false;
					/*try {
						Robot robot = new Robot();
						Color pixelColor = robot.getPixelColor((int)(p.getX() + component.getLocationOnScreen().x), (int)(p.getY() + component.getLocationOnScreen().y));
						System.out.println(pixelColor);
					} catch (AWTException e1) {
						e1.printStackTrace();
					}*/
					for(int i = 0; i < points.length; i++)
					{
						double x = points[i].getX() - SIZE/2;
						double y = points[i].getY() - SIZE/2;
						Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
						if(r.contains(p))
						{
							yes = true;
						}
					}
					for(int i = 0; i < points2.length; i++)
					{
						double x = points2[i].getX() - SIZE/2;
						double y = points2[i].getY() - SIZE/2;
						Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
						if(r.contains(p))
						{
							yes = true;
						}
					}
					
					for(int i = 0; i < points3.length; i++)
					{
						double x = points3[i].getX() - SIZE/2;
						double y = points3[i].getY() - SIZE/2;
						Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
						if(r.contains(p))
						{
							yes = true;
						}
					}
					for(int i = 0; i < points4.length; i++)
					{
						double x = points4[i].getX() - SIZE/2;
						double y = points4[i].getY() - SIZE/2;
						Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
						if(r.contains(p))
						{
							yes = true;
						}
					}
					if (yes) {
						component.setCursor(new Cursor(Cursor.HAND_CURSOR));
					} else {
						component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
			public void mouseDragged(MouseEvent event)
			{
				if (current == -1 && current2 == -1 && current3 == -1 && current4 == -1) {
					if (pressPoint2d!=null) {
						JComponent component = (JComponent)event.getSource();
						component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						releasPoint2d = event.getPoint();
						double x = releasPoint2d.getX() - pressPoint2d.getX();
						double y = releasPoint2d.getY() - pressPoint2d.getY();
						pressPoint2d = releasPoint2d;
						for (int i = 0; i < 4; i++) {
							points[i].setLocation(points[i].getX()+x, points[i].getY()+y);
						}
						for (int i = 1; i < 4; i++) {
							points2[i].setLocation(points2[i].getX()+x, points2[i].getY()+y);
						}
						for (int i = 1; i < 4; i++) {
							points3[i].setLocation(points3[i].getX()+x, points3[i].getY()+y);
						}
						for (int i = 1; i < 3; i++) {
							points4[i].setLocation(points4[i].getX()+x, points4[i].getY()+y);
						}
						repaint();
					}
					return;
				}
				double xx = event.getPoint().getX();
				double yy = event.getPoint().getY();
				if(current != -1)
					points[current].setLocation(xx, yy);
				if (current2 != -1) {
					points2[current2].setLocation(xx, yy);
				}
				if (current3 != -1) {
					points3[current3].setLocation(xx, yy);
				}
				if (current4 != -1) {
					points4[current4].setLocation(xx, yy);
				}
				
				/*if(current != -1)
					points[current] = event.getPoint();
				if (current2 != -1) {
					points2[current2] = event.getPoint();
				}
				if (current3 != -1) {
					points3[current3] = event.getPoint();
				}
				if (current4 != -1) {
					points4[current4] = event.getPoint();
				}*/
				repaint();
			}
		});
		current = -1;
		current2 = -1;
		current3 = -1;
		current4 = -1;
	}
	
	public Point2D cubicBezier(double t, Point2D[] p)
	{
		Point2D[] temp = new Point2D[p.length];
		for(int k=0; k < p.length; k++)
			temp[k]=p[k];
		for(int i=0; i< 3; i++)
		{
			for(int j = 0; j < 4-i-1 ; j++)
			{
				double x = (1-t)*temp[j].getX() + t*temp[j+1].getX();
				double y = (1-t)*temp[j].getY()+ t*temp[j+1].getY();
				temp[j] = new Point2D.Double(x,y);
			}
		}
		return temp[0];
		
		/*double tmpx,tmpy;
		tmpx=(-p[0].getX()+3*p[1].getX()-3*p[2].getX()+p[3].getX())*t*t*t+
				(3*p[0].getX()-6*p[1].getX()+3*p[2].getX())*t*t+(-3*p[0].getX()+3*p[1].getX())*t+p[0].getX(); 
		tmpy=(-p[0].getY()+3*p[1].getY()-3*p[2].getY()+p[3].getY())*t*t*t+(3*p[0].getY()-6*p[1].getY()+
				3*p[2].getY())*t*t+(-3*p[0].getY()+3*p[1].getY())*t+p[0].getY(); 
		Point2D point2d = new Point2D.Double(tmpx,tmpy);
		return point2d;*/
	}
	
	public void drawBezier(Graphics g, Point2D[] p)
	{
		Graphics2D g2 = (Graphics2D)g;  //g是Graphics对象
		g2.setStroke(new BasicStroke(0.5f));
		g.setColor(Color.red);
		for(double t = 0; t < 1; t+=0.002)
		{
			Point2D p1= cubicBezier(t,p);
			Point2D p2 = cubicBezier(t+0.001,p);
			g.drawLine((int)Math.round(p1.getX()),(int)Math.round(p1.getY()),(int)Math.round(p2.getX()),(int)Math.round(p2.getY()));
		}
		g.setColor(Color.black);
	}
	public void paintComponent(Graphics g)
	{
		Graphics2D g2=(Graphics2D)g;

		g.setColor(Color.white);//这里设置背景颜色

		g.fillRect(0, 0, this.getWidth(), this.getHeight());//这里填充背景颜色
		if (image != null) {
			g.drawImage(image, 0, 0,254,257, null);
		}
		
		g.setColor(Color.black);
		if(points == null) return;
		g2.drawLine(0, 127, 254, 127);
		g2.drawLine(127, 0, 127, 257);
		//Graphics2D g2 = (Graphics2D) g;
		drawBezier(g,points);
		drawBezier(g,points2);
		drawBezier(g,points3);
		drawBezier(g,points4);
		for(int i = 0; i < points.length; i++)
		{
			if (i == 0) {
				g.setColor(Color.blue);
			} else if (i == 3) {
				g.setColor(Color.magenta);
			} else {
				g.setColor(Color.black);
			}
			double x = points[i].getX() - SIZE/2;
			double y = points[i].getY() - SIZE/2;
			g2.drawString(String.valueOf(i+1), (float)(x+SIZE+2), (float)(y+SIZE));
			Rectangle2D rectangle2d = new Rectangle2D.Double(x, y, SIZE, SIZE);
			g2.fill(rectangle2d);
		}
		
		for(int i = 1; i < points2.length; i++)
		{
			if (i == 3) {
				g.setColor(Color.magenta);
			} else {
				g.setColor(Color.black);
			}
			double x = points2[i].getX() - SIZE/2;
			double y = points2[i].getY() - SIZE/2;
			g2.drawString(String.valueOf(i+4), (float)(x+SIZE+2), (float)(y+SIZE));
			g2.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
		}
		
		for(int i = 1; i < points3.length; i++)
		{
			if (i == 3) {
				g.setColor(Color.magenta);
			} else {
				g.setColor(Color.black);
			}
			double x = points3[i].getX() - SIZE/2;
			double y = points3[i].getY() - SIZE/2;
			g2.drawString(String.valueOf(i+7), (float)(x+SIZE+2), (float)(y+SIZE));
			g2.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
		}
		g.setColor(Color.black);
		for(int i = 1; i < points4.length-1; i++)
		{
			double x = points4[i].getX() - SIZE/2;
			double y = points4[i].getY() - SIZE/2;
			g2.drawString(String.valueOf(i+10), (float)(x+SIZE+2), (float)(y+SIZE));
			g2.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
		}
		//g2.drawLine(0, 127, 254, 127);
		//g2.drawLine(127, 0, 127, 254);
	}
}
