package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread
{
	private int port;
	private String ip;
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
             PrintStream out = new PrintStream( socket.getOutputStream() );
             BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );

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

    private void protocol(BufferedReader in, PrintStream out) {

            String line = null;
			try {
				//HAND SHAKE
				// Read data from the server 
				line = in.readLine();
				System.out.println("Received: " + line);

				//send data to the server
				out.println("hi server!");
				
				
				
				Scanner scanner = new Scanner(System.in);
				//send data from console input
				while (!line.equals("exit")) {
					line = scanner.nextLine();
					out.println(line);
				}
				//send the exit message
				out.println(line);
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
}
