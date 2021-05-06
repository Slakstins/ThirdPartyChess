package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread
{
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;

    private RequestHandler blackRequestHandler = null;
    private RequestHandler whiteRequestHandler = null;
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


        this.stopServer();
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
    
    public void getConnections() {
    	
        Socket blackSocket = null;
        Socket whiteSocket = null;
        while( running )
        {
            try
            {
                System.out.println( "Listening for a connection" );

                // Call accept() to receive the next connection
                if (blackSocket == null) {
                	  blackSocket = serverSocket.accept();
                      // Pass the socket to the RequestHandler thread for processing
                      blackRequestHandler = new RequestHandler( blackSocket );
                      blackRequestHandler.setChessTeam(ChessTeam.BLACK);
                      blackRequestHandler.sendGreetings(ChessTeam.BLACK);
                      
                      //BREAK FOR TESTING
                      
                      
                      
                      break;
                }
                else if (whiteSocket == null) {
                	  whiteSocket = serverSocket.accept();
                      // Pass the socket to the RequestHandler thread for processing
                      whiteRequestHandler = new RequestHandler( whiteSocket );
                      whiteRequestHandler.setChessTeam(ChessTeam.WHITE);
                      whiteRequestHandler.sendGreetings(ChessTeam.WHITE);
                } else {
                	System.out.println("players full");
                	break;
                }
              
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run()
    {
        running = true;
        //obtain connections -- handle disconnects here if time
        this.getConnections();
        //connect turn logic here!
        blackRequestHandler.takeTurn();
        running = false;

    }


}