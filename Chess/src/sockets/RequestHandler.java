package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class RequestHandler extends Thread
{
    private Socket socket;
    RequestHandler( Socket socket )
    {
        this.socket = socket;
    }
    
    private void protocol(BufferedReader in, PrintWriter out) {
    	


		String line = "server whoops";
		try {
			//HAND SHAKE
			// Send message to client
			out.println("hi client!");
			out.flush();
			//receive message from client
			line = in.readLine();
			System.out.println("received: " + line);

			while (!line.equals("exit")) {

				line = in.readLine();
				System.out.println("received: " + line);
			}
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void run()
    {
        try
        {

            // Get input and output streams
            BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            PrintWriter out = new PrintWriter( socket.getOutputStream() );

            //run the protocol
            protocol(in, out);

            // Close our connection
            in.close();
            out.close();
            socket.close();

            System.out.println( "Connection closed" );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}