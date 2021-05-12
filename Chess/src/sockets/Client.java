package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import chess.Main;
import chess.Time;

public class Client extends Thread
{
	private int port;
	private String ip;
    private PrintStream out;
    private BufferedReader in;
    public Client( String ip, int port)
    {
		if (port < 1024 || port > 49151) {
			System.out.println("portnum must be between 1024 and 49151");
			return;
		}
		this.port = port;
		this.ip = ip;
		this.start();
       
    }

    public void run(){
    	 try
         {
             // Connect to the server
             Socket socket = new Socket( ip, port );

             // Create input and output streams to read from and write to the server
             out = new PrintStream( socket.getOutputStream() );
             in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );

             protocol(in, out);
             // Close our streams
             in.close();
             out.close();
             socket.close();
            
             System.out.println("connection closed");
             //is this an okay way to end a thread?
             this.interrupt();
         }
         catch( Exception e )
         {
             e.printStackTrace();
         }
    }
    
    public void sendLine(String line) {
    	System.out.println("sending... " + line);
    	out.println(line);
    	out.flush();
    	System.out.println("sent: " + line);
    }
    
    public String receiveLine() {
    	System.out.println("receiving...");
    	String line = null;
    	try {
			line = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("received: " + line);
		return line;
    }

    private void protocol(BufferedReader in, PrintStream out) {

			//HAND SHAKE
			// Read greeting from the server 
			String line = receiveLine();
			//receive which team this side is
			if (line.equals("black")) {
				Main.setTeam(ChessTeam.BLACK);
			}
			else if (line.equals("white")) {
				Main.setTeam(ChessTeam.WHITE);
			}

			//send greeting to the server
			sendLine("Hi server!");
			
			//wait for your turn
			while(!line.equals("exit")) {

				line = receiveLine();
				
				if (line.equals("your turn")) {

					MoveMsg moveMsg = new MoveMsg();
					Turn turn = new Turn(moveMsg);
					String outcome = turn.begin();
					this.sendLine(outcome);

				}
				
				if (line.startsWith("move:")) {
					String move = line.substring(5);
					Server.makeClick(move);
					
				}
				
				if (line.equals(SocketTimer.OUT_OF_TIME)) {
					//DO SOMETHING
					
				}
				
				
			}
			
			
			
			
    }
}
