package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread
{
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;

    public Server( int port )
    {
        this.port = port;
        System.out.println( "Start server on port: " + port );
		if (port < 1024 || port > 49151) {
			System.out.println("portnum must be between 1024 and 49151");
			return;
		}
        this.startServer();
        //NEED TO SHUT DOWN AT SOME POINT

        // Automatically shutdown in 1 minute
        try
        {
            //Thread.sleep( 60000 );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        //this.stopServer();
    }

    public void startServer()
    {
        try
        {
            serverSocket = new ServerSocket( port );
            this.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void stopServer()
    {
        running = false;
        this.interrupt();
    }

    @Override
    public void run()
    {
        running = true;
        while( running )
        {
            try
            {
                System.out.println( "Listening for a connection" );

                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();

                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler( socket );
                requestHandler.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


}