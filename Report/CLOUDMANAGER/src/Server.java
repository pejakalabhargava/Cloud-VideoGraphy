import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

 class server {
    public static void main(String[] args) throws IOException {
	//creating a  server socket.
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }
		//create a client socket n accpet server connection. 

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
	//serverSocket.setKeepAlive(true);
	//create an input and output stream.
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
		int i=0;
       int time=1; 
       Connection con = null,con2=null,con3=null;
try {
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    con = DriverManager.getConnection("jdbc:mysql://localhost/network","root", "root");
    //if(!con.isClosed())
       //System.out.println("Successfully connected to " +
       //"MySQL server using TCP/IP...");
     } 
		catch(Exception e1) {
     System.err.println("Exception: " + e1.getMessage());
     } 
      while(true)
      {
   try
      {
  
      String query = "SELECT * FROM LINK where TIME="+(time++);;
       Statement st = con.createStatement();
       ResultSet rs = st.executeQuery(query);
       boolean Records=rs.next();
       if(!Records){
           System.out.println("No data Returned");
           return;
         }
      else{
       do
       { 
        int f = rs.getInt("FROM");
        int t = rs.getInt("TO");
	int flag=rs.getInt("flag");
//        System.out.println(f+" "+t);
        
		             try
                  {
                   // con2 = DriverManager.getConnection("jdbc:mysql:///network","root", "root");
					   //con3 = DriverManager.getConnection("jdbc:mysql:///network","root", "root");
					String from="";
					String to="";

                   String query2 = "SELECT NAME FROM ELEMENTS WHERE ID="+f;
                   String query3 = "SELECT NAME FROM ELEMENTS WHERE ID="+t;
                              
                   Statement st2 = con.createStatement();
                   Statement st3 = con.createStatement();
                   ResultSet rs2 = st2.executeQuery(query2);
                   ResultSet rs3 = st3.executeQuery(query3);
                   while(rs2.next()){
					  from = rs2.getString("NAME");
  				  }
                   while(rs3.next()){
					  to = rs3.getString("NAME");
  				  }
					//sfor(int j=0;j<50000;j++)
                  
                   
                   
                    out.println(from+" "+to+" "+flag+" "+(time-1));
					  }
                  catch (SQLException ex)
                {
                        System.err.println(ex.getMessage());
                       }        
				}while (rs.next());
      }
      }
      catch (SQLException ex)
         {
         System.err.println(ex.getMessage());
          }
        
       try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
/*try{  
con.close();
}
catch (SQLException ex)
{
System.err.println(ex.getMessage());
}
*/					
    }
}
