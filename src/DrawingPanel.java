import javax.swing.*; 

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

public class DrawingPanel extends JPanel{
	static final int RADIUS = 5;
	static final int THRESHOLD = 10;
	ArrayList<Point> points;// = new ArrayList<Point>();
	ArrayList<Point> pointsCH;// = new ArrayList<Point>();
	ArrayList<Line2D> edgesCH;
	ArrayList<Line2D> edgesGhost;
	ArrayList<Line2D> edgesGreen;


	public DrawingPanel(){
		MyMouseAdapter mouseAdapter = new MyMouseAdapter();
		addMouseListener (mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}

	public DrawingPanel(ArrayList<Point> points, ArrayList<Point> pointsCH, ArrayList<Line2D> edgesCH, ArrayList<Line2D> edgesGhost, ArrayList<Line2D> edgesGreen){
		this(); // call default constructor DrawingPanel() above
		this.points = points;
		this.pointsCH = pointsCH;
		this.edgesCH = edgesCH;
		this.edgesGhost = edgesGhost;
	    this.edgesGreen = edgesGreen;

	}
	
    protected void paintComponent(Graphics graphics){
    	super.paintComponent(graphics);
    	Graphics2D g = (Graphics2D) graphics;
    	g.setPaint (Color.BLACK);
    	g.setStroke(new BasicStroke(4));
    	if(points!=null){
			for(int i=0; i<points.size(); i++){
				Point point = points.get(i);
	   		  	int x = (int)point.getX()-RADIUS;
	    		  int y = (int)point.getY()-RADIUS;
	    		  g.fillOval(x,y,2*RADIUS,2*RADIUS);
	    	}
    	}else{}
        if(edgesGhost!=null){
          for(int i=0; i<edgesGhost.size(); i++){         
              g.draw(edgesGhost.get(i));
          }
        }else{}
        
    	// This is what I added. I will use the CH points I stored in pointsCH to paint them red AND connect lines between them in ccw order. 
    	g.setPaint (Color.RED);
    	if(pointsCH!=null){
			for(int i=0; i<pointsCH.size(); i++){
				
				// Just like the regular points, but paint it red instead.
				Point pointCH = pointsCH.get(i);
	   		  	int x = (int)pointCH.getX()-RADIUS;
	    		int y = (int)pointCH.getY()-RADIUS;
	    		g.fillOval(x,y,2*RADIUS,2*RADIUS);
	    		
	    		// Here is where for each point, I take the next point in the CCW order CH, and draw a line in between them. 
	    		int j = (i+1) % pointsCH.size();
	    		Point pointNext = pointsCH.get(j);
	   		  	int nextX = (int)pointNext.getX();
	   		  	int nextY = (int)pointNext.getY();
	   		  	g.drawLine(x + RADIUS, y + RADIUS, nextX, nextY);
	    	}
    	}else{}
        if(edgesCH!=null){
          for(int i=0; i<edgesCH.size(); i++){
              
              g.draw(edgesCH.get(i));
          }
        }else{}
    	
        g.setPaint (Color.GREEN);
        if(edgesCH!=null){
            for(int i=0; i<edgesGreen.size(); i++){
                
                g.draw(edgesGreen.get(i));
            }
        }
    }
    
    
	private class MyMouseAdapter extends MouseAdapter{
	
		public void mousePressed (MouseEvent e){
			if(e.getButton()==MouseEvent.BUTTON1){
				points.add(e.getPoint());

			} else if (e.getButton()==MouseEvent.BUTTON3){
				// Search through list and delete nearest neighbor within some threshold
				int nearestIndex = -1;
				double nearestDistance = Double.MAX_VALUE;
				for(int i=0; i<points.size(); i++){
					Point current = points.get(i);
					if(current.distance(e.getPoint())<nearestDistance){
						nearestIndex = i;
						nearestDistance=current.distance(e.getPoint());
					}
				}
				if(nearestDistance<=THRESHOLD){
					//Delete point
					points.remove(nearestIndex);
				}
			}
			repaint();
		}


	};
	
}