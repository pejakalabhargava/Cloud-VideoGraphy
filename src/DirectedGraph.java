/* 
Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

This file is part of the MontyApplet dynamic graph drawing system.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: DirectedGraph.java,v 1.2 2004/02/01 13:22:59 pjm2 Exp $

*/

import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.*;

//import javax.imageio.*;
//import java.io.*;
import java.net.URL;
  
public class DirectedGraph {
	double minX = Double.POSITIVE_INFINITY;
    double maxX = Double.NEGATIVE_INFINITY;
    double minY = Double.POSITIVE_INFINITY;
    double maxY = Double.NEGATIVE_INFINITY;
    Image image;
  
	public DirectedGraph() {
       //addNode("[NODE1]", 0, 0, true);
        //addNode("[NODE2]", 10, 10, true);
    }
    
    public void addNode(String label, double x, double y, boolean fixed,String time) {
        if (!_nodes.containsKey(label)) {
            _nodes.put(label, new Node(label, x, y, fixed,time));
        }
    }
    
     // Only use this to add an edge *when both nodes exist*
    public void addEdge(String from, String to) {
        Node source = (Node)_nodes.get(from);
        Node target = (Node)_nodes.get(to);
        source.addEdgeTo(target);
        Edge edge = new Edge(source, target);
        _edges.put(from+to, edge);
    }
    
    
    public Hashtable getNode(){
    return _nodes;
    }
    
    public Hashtable getEdge(){
    return _edges;
    }
    
    /*
    public String getSentence() {
        StringBuffer sentence = new StringBuffer(160);
        Node node = (Node)_nodes.get("[start]");
        Vector v = node.getEdgesTo();
        node = (Node)v.elementAt((int)(Math.random()*v.size()));
        v = node.getEdgesTo();
        sentence.append(node.getLabel());
        while (v.size() > 0) {
            node = (Node)(node.getEdgesTo().elementAt((int)(Math.random()*v.size())));
            v = node.getEdgesTo();
            if (node.getLabel().equals("[end]")) {
                break;
            }
            sentence.append(" ");
            sentence.append(node.getLabel());
        }
        return sentence.toString();
    }
    */
    public synchronized void draw(Image imag,URL codebase, CloudManager monty) {
    	 
    	image=imag;
    	Image im;
        Node[] nodes = new Node[_nodes.size()];
        Enumeration e = _nodes.elements();
        int i = 0;
        while(e.hasMoreElements()) {
            nodes[i] = (Node)e.nextElement();
            i++;
        }
        
        Edge[] edges = new Edge[_edges.size()];
        e = _edges.elements();
        i = 0;
        while(e.hasMoreElements()) {
            edges[i] = (Edge)e.nextElement();
            i++;
        }
        
        for (int it = 0; it < 1; it++) {
            
            // Nodes
            for (int a = 0; a < nodes.length; a++) {
                for (int b = a + 1; b < nodes.length; b++) {
                    Node nodeA = nodes[a];
                    Node nodeB = nodes[b];
                    
                    double deltaX = nodeB._x - nodeA._x;
                    double deltaY = nodeB._y - nodeA._y;
                    
                    double distanceSquared = deltaX*deltaX + deltaY*deltaY;
                    
                    if (distanceSquared == 0) {
                        deltaX = Math.random()/10 + 0.1;
                        deltaY = Math.random()/10 + 0.1;
                        distanceSquared = deltaX*deltaX + deltaY*deltaY;
                    }
                    
                    double distance = Math.sqrt(distanceSquared);
                    
                    double repulsiveForce = (_K*_K/distance);
                    
                    nodeB._fx = nodeB._fx + repulsiveForce*deltaX/distance;
                    nodeB._fy = nodeB._fy + repulsiveForce*deltaY/distance;
                    nodeA._fx = nodeA._fx - repulsiveForce*deltaX/distance;
                    nodeA._fy = nodeA._fy - repulsiveForce*deltaY/distance;
                }
            }
            
        }
        
        //Edges
        for (i = 0; i < edges.length; i++) {
            Edge edge = edges[i];
            Node nodeA = edge.getSource();
            Node nodeB = edge.getTarget();
            
            double deltaX = nodeB._x - nodeA._x;
            double deltaY = nodeB._y - nodeA._y;
            
            double distanceSquared = deltaX*deltaX + deltaY*deltaY;

            // Avoid division by zero error or Nodes flying off to
            // infinity.  Pretend there is an arbitrary distance between
            // the Nodes.
            if (distanceSquared == 0) {
                deltaX = Math.random()/10 + 0.1;
                deltaY = Math.random()/10 + 0.1;
                distanceSquared = deltaX*deltaX + deltaY*deltaY;
            }
            
            double distance = Math.sqrt(distanceSquared);
            distanceSquared = distance*distance;
            
            double attractiveForce = (distanceSquared - _idealEdgeLength*_idealEdgeLength)/_K;
            
            nodeB._fx = nodeB._fx - attractiveForce*deltaX/distance;
            nodeB._fy = nodeB._fy - attractiveForce*deltaY/distance;
            nodeA._fx = nodeA._fx + attractiveForce*deltaX/distance;
            nodeA._fy = nodeA._fy + attractiveForce*deltaY/distance;
            
        }
        
      //  double minX = Double.POSITIVE_INFINITY;
        //double maxX = Double.NEGATIVE_INFINITY;
        //double minY = Double.POSITIVE_INFINITY;
        //double maxY = Double.NEGATIVE_INFINITY;
		BufferedImage img = null;
        
        for (i = 0 ; i < nodes.length; i++) {
            Node node = nodes[i];
            
            //if (!node._fixed) {
                double xMovement = _C4*node._fx;
                double yMovement = _C4*node._fy;
                node._x = node._x + xMovement;
                node._y = node._y + yMovement;
                
                // Reset the forces
                node._fx = 0;
                node._fy = 0;
            //}

            if (node._x > maxX) {
                maxX = node._x;
            }
            if (node._x < minX) {
                minX = node._x;
            }
            if (node._y > maxY) {
                maxY = node._y;
            }
            if (node._y < minY) {
                minY = node._y;
            }

        }
        
        Graphics g = image.getGraphics();
        int width = image.getWidth(null) - 30;
        int height = image.getHeight(null) - 30;
       // g.drawRect(0, 0, 50, 50);
        //g.drawString("CURRENT TIME:"+time, 20, 20);
        //draw edges
         for(i = 0; i < edges.length; i++) {
        //{
        	g.setColor(Color.blue);
            Edge edge = edges[i];
            Node nodeA = edge.getSource();
            Node nodeB = edge.getTarget();
            int x1 = (int)(width*(nodeA._x-minX)/(maxX-minX)) + 15;
            int y1 = (int)(height*(nodeA._y-minY)/(maxY-minY)) + 15;
            int x2 = (int)(width*(nodeB._x-minX)/(maxX-minX)) + 15;
            int y2 = (int)(height*(nodeB._y-minY)/(maxY-minY)) + 15;
            //Graphics2D g2d = (Graphics2D)g;   
     		g.drawLine(x1, y1, x2, y2);
            
            double arrowHeadAngle = 0.3;
            double arrowHeadSize = 10;
            
            // draw edge arrows
            double theta = Math.atan2(x2 - x1, y2 - y1);
            int meanX = (x1+x2)/2;
            int meanY = (y1+y2)/2;
            theta = theta - arrowHeadAngle;
            int ax = (int)(arrowHeadSize*Math.sin(theta));
            int ay = (int)(arrowHeadSize*Math.cos(theta));
            g.drawLine(meanX, meanY, meanX - ax, meanY - ay);
            theta = theta + 2*arrowHeadAngle;
            ax = (int)(arrowHeadSize*Math.sin(theta));
            ay = (int)(arrowHeadSize*Math.cos(theta));
            g.drawLine(meanX, meanY, meanX - ax, meanY - ay);
        }
        //}
        
        // draw nodes
        
        for (i =0; i<nodes.length; i++)
        {
            Node node = nodes[i];
            
            g.setColor(node._fixed ? Color.black : Color.red);
            int x3 = (int)(width*(node._x-minX)/(maxX-minX)) + 15;
            int y3 = (int)(height*(node._y-minY)/(maxY-minY)) + 15;
            
            if(node.getLabel().contains("router")){
            im=monty.getImage(codebase, "router1.png");
            g.drawImage(im,x3 - 40,y3 - 10,monty);
            //g.fillRect(x1 - 5, y1 - 5, 25, 25);
            g.setColor(Color.black);
            g.drawString(node.getLabel(), x3+5, y3-15);
            }else
            	if(node.getLabel().contains("node")){
            		im=monty.getImage(codebase, "node1.jpeg");
                    g.drawImage(im,x3 - 40,y3 - 10,monty);
                    //g.fillRect(x1 - 5, y1 - 5, 25, 25);
                    g.setColor(Color.black);
                    g.drawString(node.getLabel(), x3+5, y3-15);
                    }else
                    	if(node.getLabel().contains("switch")){
                    		im=monty.getImage(codebase, "switch.jpg");
                            g.drawImage(im,x3 - 40,y3 - 10,monty);
                            //g.fillRect(x1 - 5, y1 - 5, 25, 25);
                            g.setColor(Color.black);
                            g.drawString(node.getLabel(), x3+5, y3-15);
                        }else
                            if(node.getLabel().contains("server")){
            im=monty.getImage(codebase, "server1.png");
            g.drawImage(im,x3 - 40,y3 - 10,monty);
            //g.fillRect(x1 - 5, y1 - 5, 25, 25);
            g.setColor(Color.black);
            g.drawString(node.getLabel(), x3+5, y3-15);
            }else
                if(node.getLabel().contains("network")){
            im=monty.getImage(codebase, "network.png");
            g.drawImage(im,x3 - 40,y3 - 10,monty);
            //g.fillRect(x1 - 5, y1 - 5, 25, 25);
            g.setColor(Color.black);
            g.drawString(node.getLabel(), x3+5, y3-15);
            }else
                if(node.getLabel().contains("hub")){
            im=monty.getImage(codebase, "hub2.png");
            g.drawImage(im,x3 - 40,y3 - 10,monty);
            //g.fillRect(x1 - 5, y1 - 5, 25, 25);
            g.setColor(Color.black);
            g.drawString(node.getLabel(), x3+5, y3-15);
            }
        g.drawString("At Time:"+node.t+"units", x3+15, y3);  
        }
    
    }
   
    public String search(String text1) {
		// TODO Auto-generated method stub
	//if(_nodes.values().contains(text1)){
		//return "PRESENT";
	//}
	//else
		//return "NOT PRESENT";

		int width = image.getWidth(null) - 30;
        int height = image.getHeight(null) - 30;
        Graphics g = image.getGraphics();
        Node[] nodes = new Node[_nodes.size()];
     Enumeration e = _nodes.elements();
     int i=0;
     while(e.hasMoreElements()) {
         nodes[i] = (Node)e.nextElement();
        // System.out.println(nodes[i].getLabel());
        if( nodes[i].getLabel().contentEquals(text1)){
        	int x1 = (int)(width*(nodes[i]._x-minX)/(maxX-minX)) + 15;
            int y1 = (int)(height*(nodes[i]._y-minY)/(maxY-minY)) + 15;
        	g.setColor(new Color(255,0,0));
           int j=0;
        	while(j<100000){
        	    
        		g.drawString("ELEMENT IS HERE", x1-30, y1-30);
                g.drawRect(x1-30, y1-30,80,80);
        		j++;
           }
        	return "PRESENT";         
       }
       
        i++;
     }
     return "NOT PRESENT";

	}

    private Hashtable _nodes = new Hashtable();
    private Hashtable _edges = new Hashtable();
    private double _K = 2;
    private double _idealEdgeLength = _K;
    private double _C4 = 0.01;
    
}
    