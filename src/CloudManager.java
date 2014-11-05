/* 
 Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

This file is part of the MontyApplet dynamic graph drawing system.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: MontyApplet.java,v 1.2 2004/02/01 13:22:59 pjm2 Exp $

*/

import java.applet.Applet;
import java.awt.*;
import java.util.StringTokenizer;
import java.sql.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class CloudManager extends Applet implements ActionListener
{
private boolean _firstAdd = true;
    private boolean _running = true;
    //private TextArea _textArea = new TextArea("", 4, 10, TextArea.SCROLLBARS_VERTICAL_ONLY);
    private GraphDrawing _drawing = new GraphDrawing();
    private DirectedGraph _graph = new DirectedGraph();
    	 CloudManager ma=this;
	 capture cp;
	 convert videoEncoder;
	 int i=0;
	 
	 Button start1,exit,restart,stop,search1;
  TextField text;
	 Panel p1;
    private boolean flag=false;
	public CloudManager(){
		 
	convert c= new convert();
    
       c.start();
   
            cp=new capture();
		  cp.start();
	
	}
     
    public void init() {
    	 
        setLayout(new BorderLayout());
        setSize(1200,700);
//     try {
            // an applet thing
            //  String from,to;
            /*	double j,k;
            for(int i=1;i<10;i++){
            j=(Math.random() % 10);
            k=(Math.random() % 10);
            from="router"+(j);
            to="router"+(k);
             */
   //start1=new Button("START   MANAGER   NOW");
         
     // start1.setBackground(new Color(255,1,10));
       // start1.setSize(10,10);
        
        //add(start1);
        
        //add(exit);
        //start1.addActionListener(this);
//if(flag==true){
        add(_drawing, BorderLayout.CENTER);
            validate();
            _drawing.clearImage();

new Thread() {
                @Override
                public void run() {
            Socket kkSocket = null;
            PrintWriter out = null;
            BufferedReader in = null;

           try {
                kkSocket = new Socket("localhost", 4444);
                out = new PrintWriter(kkSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host: localhost.");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: localhost.");
                System.exit(1);
            }

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;
        try {

            while ((fromServer = in.readLine()) != null) {

                //System.out.println("Server: " + fromServer);
                String[] contents = fromServer.split(" ");
                if (contents[2].equals("0")) {

                    _graph.getEdge().remove(contents[0] + contents[1]);
                } else {

                    addLine(contents[0], contents[1],contents[3]);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CloudManager.class.getName()).log(Level.SEVERE, null, ex);
        }

         /* try{  out.close();
            in.close();
            stdIn.close();
            kkSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(MontyApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    */
          }}.start();
  //search1=new Button("Search");
//	 		      search1.setSize(new Dimension(350,350));  
//	 		      search1.setBackground(new Color(234,8,7));
//	 		      add(search1);
//	 		     search1.addActionListener(this);
	 		     	 		  
//	 		     text = new TextField("Enter Element to be searched");
//	      		text.setSize(100,20);
//	 		     add(text);
      		
          
	  //  exit.addActionListener(this);
        
        /*Validates this container and all of its subcomponents.
          The validate method is used to cause a
          container to lay out its subcomponents again. It should be invoked when this container's 
          subcomponents are modified (added to or removed from the container, or layout-related information changed) 
          after the container has been displayed.
         */
        
        // This makes the bufferedimage the correct size as well
        
        }

    @Override
    public void start() {
                        
	  }   


    @Override
	  public void stop() {
        // an applet thing
    }

    public void destroy() {
        // an applet thing
        // We must do this to stop the thread that we start, otherwise the
        // user's browser can eat up CPU even when they've left the page!
        _running = false;
    }
    
    public void addLine(String From,String To,String time) {
       // _textArea.append("<" + from + "> " + msg + "\n");
        String msg=From+" "+To;
       
        StringTokenizer tok = new StringTokenizer(msg, " []\t\r\n");
        if (!tok.hasMoreTokens()) {
            return;
        }
        
        synchronized (_graph) {
            while (tok.hasMoreTokens()) {
            String token = tok.nextToken();
            String newToken = tok.nextToken();
                _graph.addNode(token, Math.random()*10, Math.random()*10, false,time);
                _graph.addNode(newToken, Math.random()*10, Math.random()*10, false,time);
                
                _graph.addEdge(token, newToken);
            /*    
             _drawing.fillWhite();
             _graph.draw(_drawing.getImage(),getCodeBase(),ma);
             _drawing.paintNow();
            
   */          }
            //_graph.addEdge(token, "[NODE2]");
        }
        
       // _textArea.append("<Monty> " + _graph.getSentence() + "\n");
        
        if (_firstAdd) {
            _firstAdd = false;
             
            new Thread() {
                @Override
                public void run() {
                    try {
                        while (_running) {
             _drawing.fillWhite();
              _graph.draw(_drawing.getImage(),getCodeBase(),ma);
              //Graphics g = getGraphics();
              //g.drawString(time, 50, 50);
              _drawing.paintNow();
                            
                         }
                    }
                    catch (Exception e) {
                        // Woo, something got jibbled.
                    }
                }
            }.start();
        
         }
    
         }
    public void actionPerformed(ActionEvent e) 
    {
 
            if(e.getSource()==start1)
          {
	 		     start1.setVisible(false);
	 		    //Panel p2=new Panel();
	 		    //p2.setLayout(new FlowLayout());
	 		    //p2.setSize(new Dimension(100,100));
	 		   
	/* 		      
	 		      restart=new Button("RESTART");
	 		      restart.setSize(new Dimension(250,250));  
	 		      restart.setBackground(new Color(222,88,27));
	 		      p1.add(restart);
	 		     restart.addActionListener(this);
*/
	 		      //stop=new Button("STOP");
	 		      //stop.setSize(new Dimension(250,250));  
	 		      //stop.setBackground(new Color(233,88,27));
	 		      //p1.add(stop);
	 		     //stop.addActionListener(this);
//	 		      add(p1,BorderLayout.BEFORE_FIRST_LINE);

	 //		     search1=new Button("Search");
	 //		      search1.setSize(new Dimension(350,350));  
	 //		      search1.setBackground(new Color(234,8,7));
	 //		      p2.add(search1);
	 //		     search1.addActionListener(this);
	 		     	 		  
	 //		     text = new TextField("Enter Element to be searched");
	   //   		text.setSize(100,20);
	 	//	     p2.add(text);
	      		//add(p2,BorderLayout.PAGE_END);

                //Panel p2=new Panel();
                //p2.setLayout(new FlowLayout());
	 		    //p2.setSize(new Dimension(100,100));
	 		   // exit=new Button("EXIT");
	 		     // exit.setSize(new Dimension(250,250));  
	 		      //exit.setBackground(new Color(233,88,27));
	 		      //p2.add(exit);
	 		     //exit.addActionListener(this);
               		     //this.start();	 
                           	
  flag=true;
            }
               else
            	if(e.getSource()==stop){
            		
            		this.stop();
            		
            	}else
                	if(e.getSource()==restart){
                		
                		this.start();
                		}else
                        	if(e.getSource()==exit){
                        		
                        		System.exit(0);
                        		
                        	}else
                        		if(e.getSource()==search1){
                        			String text1=text.getText();
                        			text.setText(_graph.search(text1));
                        		
                        		
     }
    }      
   
    
    
}

