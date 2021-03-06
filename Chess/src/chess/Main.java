package chess;
import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintJobAttributeSet;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pieces.*;
import sockets.ChessTeam;
import sockets.Client;
import sockets.MoveMsg;
import sockets.Server;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * @author Ashish Kedia and Adarsh Mohata
 *
 */


/**
 * This is the Main Class of our project.
 * All GUI Elements are declared, initialized and used in this class itself.
 * It is inherited from the JFrame Class of Java's Swing Library. 
 * 
 */


public class Main extends JFrame implements MouseListener
{
	private static final long serialVersionUID = 1L;
	private static int THIRD_PLAYER_WIN_CODE = 0;
	//Variable Declaration
	//private static final Main instance = new Main();
	private static final int Height=700;
	private static final int Width=1110;
	private static Rook wr01,wr02,br01,br02;
	private static Knight wk01,wk02,bk01,bk02;
	private static Bishop wb01,wb02,bb01,bb02;
	private static Pawn wp[],bp[];
	private static Queen wq,bq;
	private static King wk,bk;
	private static Cell c;

	public static Cell previous;
	public static int chance=0;
	public static Cell boardState[][];
	private static ArrayList<Cell> destinationlist = new ArrayList<Cell>();

	private static JPanel board=new JPanel(new GridLayout(8,8));
	private JPanel wcombopanel=new JPanel();
	private JPanel bcombopanel=new JPanel();
	private static JPanel controlPanel;



	private static JPanel temp;

	private static JPanel displayTime;

	private static JPanel showPlayer;

	private static JPanel time;
	private static JSplitPane split;
	private static JLabel label;

	private static JLabel mov;
	private static JLabel CHNC;
	private static Time timer;
	public static Main Mainboard;

	private static boolean end=false;
	private static boolean hasJustTakenPiece = false;
	private Container content;
	private ArrayList<Player> wplayer,bplayer;
	private ArrayList<String> Wnames=new ArrayList<String>();
	private ArrayList<String> Bnames=new ArrayList<String>();
	private JComboBox<String> wcombo,bcombo;

	private static String winner=null;
	static String move;
	private JScrollPane wscroll,bscroll;
	private String[] WNames={},BNames={};
	private static JSlider timeSlider;
	private BufferedImage image;
	private static Button start;

	private static Button wselect;

	private static Button bselect;

	private static Button WNewPlayer;

	private static Button BNewPlayer;
	public static int timeRemaining=60;
	public static ChessTeam player = null;
	public static ChessTeam whoseTurn = ChessTeam.WHITE;
	public static boolean thirdPlayerTurn = false;
	public static MoveMsg moveMsg = null;;

	private static JPanel serverPanel;
	private static JPanel clientPanel;
	
	private Server server;
	public static void main(String[] args){
	

	//variable initialization
	wr01=new Rook("WR01","White_Rook.png",0);
	wr02=new Rook("WR02","White_Rook.png",0);
	br01=new Rook("BR01","Black_Rook.png",1);
	br02=new Rook("BR02","Black_Rook.png",1);
	wk01=new Knight("WK01","White_Knight.png",0);
	wk02=new Knight("WK02","White_Knight.png",0);
	bk01=new Knight("BK01","Black_Knight.png",1);
	bk02=new Knight("BK02","Black_Knight.png",1);
	wb01=new Bishop("WB01","White_Bishop.png",0);
	wb02=new Bishop("WB02","White_Bishop.png",0);
	bb01=new Bishop("BB01","Black_Bishop.png",1);
	bb02=new Bishop("BB02","Black_Bishop.png",1);
	wq=new Queen("WQ","White_Queen.png",0);
	bq=new Queen("BQ","Black_Queen.png",1);
	wk=new King("WK","White_King.png",0,7,4);
	bk=new King("BK","Black_King.png",1,0,4);
	wp=new Pawn[8];
	bp=new Pawn[8];
	for(int i=0;i<8;i++)
	{
		wp[i]=new Pawn("WP0"+(i+1),"White_Pawn.png",0);
		bp[i]=new Pawn("BP0"+(i+1),"Black_Pawn.png",1);
	}
	
	//Setting up the board
	Mainboard = new Main();
	Mainboard.setVisible(true);	
	Mainboard.setResizable(false);
	}
	
	//Constructor
	private Main()
    {
		timeRemaining=60;
		timeSlider = new JSlider();
		move="White";
		winner=null;
		board=new JPanel(new GridLayout(8,8));
		bcombopanel=new JPanel();
		wcombopanel=new JPanel();
		Wnames=new ArrayList<String>();
		Bnames=new ArrayList<String>();
		board.setMinimumSize(new Dimension(800,700));
		ImageIcon img = new ImageIcon(this.getClass().getResource("icon.png"));
		this.setIconImage(img.getImage());
		
		this.moveMsg = new MoveMsg();
		
		//Time Slider Details
		timeSlider.setMinimum(1);
		timeSlider.setMaximum(15);
		timeSlider.setValue(1);
		timeSlider.setMajorTickSpacing(2);
		timeSlider.setPaintLabels(true);
		timeSlider.setPaintTicks(true);
		timeSlider.addChangeListener(new TimeChange());
		
		
		//Fetching Details of all Players
		wplayer= Player.fetch_players();
		Iterator<Player> witr=wplayer.iterator();
		while(witr.hasNext())
			Wnames.add(witr.next().name());
				
		bplayer= Player.fetch_players();
		Iterator<Player> bitr=bplayer.iterator();
		while(bitr.hasNext())
			Bnames.add(bitr.next().name());
	    WNames=Wnames.toArray(WNames);	
		BNames=Bnames.toArray(BNames);
		
		Cell cell;
		board.setBorder(BorderFactory.createLoweredBevelBorder());
		pieces.Piece P;
		content=getContentPane();
		setSize(Width,Height);
		setTitle("Chess");
		content.setBackground(Color.black);
		controlPanel=new JPanel();
		content.setLayout(new BorderLayout());
		controlPanel.setLayout(new GridLayout(3,3));
		//controlPanel.setBorder(BorderFactory.createTitledBorder(null, "Statistics", TitledBorder.TOP,TitledBorder.CENTER, new Font("Lucida Calligraphy",Font.PLAIN,20), Color.ORANGE));

		
		
		//SOCKETS UI
		serverPanel = new JPanel();
		clientPanel = new JPanel();

		JLabel serverLabel = new JLabel("SERVER");
		final JTextField serverPortInput = new JTextField(null);
		JLabel serverPortLabel = new JLabel("Port: ");
		serverPortInput.setPreferredSize(new Dimension(50, 20));
		//server.add(portInput);
		JButton startServerButton = new JButton("Start Server");
		startServerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String port = serverPortInput.getText();
				int portNum = 0;
				try {
					portNum = Integer.parseInt(port);
				}catch (Exception ex) {
					System.out.println("port must be number");
				}
				server = new Server(portNum);
				Main.setTeam(ChessTeam.THIRD);
			}
			
		});	
		serverPanel.add(serverLabel);
		serverPanel.add(serverPortLabel);
		serverPanel.add(serverPortInput);
		serverPanel.add(startServerButton);

		
		JLabel clientLabel = new JLabel("CLIENT");
		final JTextField clientPortInput = new JTextField(null);
		JLabel clientPortLabel = new JLabel("Port: ");
		clientPortInput.setPreferredSize(new Dimension(50, 20));
		//server.add(portInput);
		final JTextField clientIpInput = new JTextField(null);
		JLabel clientIpLabel = new JLabel("IP: ");
		clientIpInput.setPreferredSize(new Dimension(150, 20));
		
		JButton connectButton = new JButton("Connect");

		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String port = clientPortInput.getText();
				String ip = clientIpInput.getText();
				int portNum = 0;
				try {
					portNum = Integer.parseInt(port);
				}catch (Exception ex) {
					System.out.println("port must be number");
				}
				Client client = new Client(ip, portNum);
			}
			
		});	
		clientPanel.add(clientLabel);
		clientPanel.add(clientPortLabel);
		clientPanel.add(clientPortInput);
		clientPanel.add(clientIpLabel);
		clientPanel.add(clientIpInput);
		clientPanel.add(connectButton);
		
		
		
		//Defining the Player Box in Control Panel
		
		
	    JPanel whitestats=new JPanel(new GridLayout(3,3));
		JPanel blackstats=new JPanel(new GridLayout(3,3));
		wcombo=new JComboBox<String>(WNames);
		bcombo=new JComboBox<String>(BNames);
		wscroll=new JScrollPane(wcombo);
		bscroll=new JScrollPane(bcombo);
		wcombopanel.setLayout(new FlowLayout());
		bcombopanel.setLayout(new FlowLayout());
		wselect=new Button("Select");
		bselect=new Button("Select");
		WNewPlayer=new Button("New Player");
		BNewPlayer=new Button("New Player");
		wcombopanel.add(wscroll);
		wcombopanel.add(wselect);
		wcombopanel.add(WNewPlayer);
		bcombopanel.add(bscroll);
		bcombopanel.add(bselect);
		bcombopanel.add(BNewPlayer);
		whitestats.add(new JLabel("Name   :"));
		whitestats.add(new JLabel("Played :"));
		whitestats.add(new JLabel("Won    :"));
		blackstats.add(new JLabel("Name   :"));
		blackstats.add(new JLabel("Played :"));
		blackstats.add(new JLabel("Won    :"));
		controlPanel.add(clientPanel);
		controlPanel.add(serverPanel);
		
		
		//Defining all the Cells
		boardState=new Cell[8][8];
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
			{	
				P=null;
				if(i==0&&j==0)
					P=br01;
				else if(i==0&&j==7)
					P=br02;
				else if(i==7&&j==0)
					P=wr01;
				else if(i==7&&j==7)
					P=wr02;
				else if(i==0&&j==1)
					P=bk01;
				else if (i==0&&j==6)
					P=bk02;
				else if(i==7&&j==1)
					P=wk01;
				else if (i==7&&j==6)
					P=wk02;
				else if(i==0&&j==2)
					P=bb01;
				else if (i==0&&j==5)
					P=bb02;
				else if(i==7&&j==2)
					P=wb01;
				else if(i==7&&j==5)
					P=wb02;
				else if(i==0&&j==4)
					P=bk;
				else if(i==0&&j==3)
					P=bq;
				else if(i==7&&j==4)
					P=wk;
				else if(i==7&&j==3)
					P=wq;
				else if(i==1)
				P=bp[j];
				else if(i==6)
					P=wp[j];
				cell=new Cell(i,j,P);
				cell.addMouseListener(this);
				board.add(cell);
				boardState[i][j]=cell;
			}
		showPlayer=new JPanel(new FlowLayout());  
		showPlayer.add(timeSlider);
		JLabel setTime=new JLabel("Set Timer(in mins):"); 
		start=new Button("Start");
		start.setBackground(Color.black);
		start.setForeground(Color.white);
	    start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//remove stuff to make space
		
				server.startGames();
				controlPanel.remove(clientPanel);
				controlPanel.remove(serverPanel);
				split.remove(temp);
				split.add(board);
				showPlayer.remove(timeSlider);
				mov=new JLabel("Move:");
				mov.setFont(new Font("Comic Sans MS",Font.PLAIN,20));
				mov.setForeground(Color.red);
				showPlayer.add(mov);
				CHNC=new JLabel(move);
				CHNC.setFont(new Font("Comic Sans MS",Font.BOLD,20));
				CHNC.setForeground(Color.blue);
				showPlayer.add(CHNC);
				displayTime.remove(start);
				displayTime.add(label);
				timer=new Time(label);
				//server.gamePlay();
				//need a way to notify  the server
				server.endWait();
			}
	    	
	    });
		start.setPreferredSize(new Dimension(120,40));
		setTime.setFont(new Font("Arial",Font.BOLD,16));
		label = new JLabel("Time Starts now", JLabel.CENTER);
		  label.setFont(new Font("SERIF", Font.BOLD, 30));
	      displayTime=new JPanel(new FlowLayout());
	      time=new JPanel(new GridLayout(3,3));
	      time.add(setTime);
	      time.add(showPlayer);
	      displayTime.add(start);
	      time.add(displayTime);
	      controlPanel.add(time);
		board.setMinimumSize(new Dimension(800,700));
		
		//The Left Layout When Game is inactive
		temp=new JPanel(){
			private static final long serialVersionUID = 1L;
			     
			@Override
		    public void paintComponent(Graphics g) {
				  try {
			          image = ImageIO.read(this.getClass().getResource("clash.jpg"));
			       } catch (IOException ex) {
			            System.out.println("not found");
			       }
			   
				g.drawImage(image, 0, 0, null);
			}         
	    };

		temp.setMinimumSize(new Dimension(800,700));
		controlPanel.setMinimumSize(new Dimension(285,700));
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,temp, controlPanel);
		
	    content.add(split);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
	
	public static void start() {
		controlPanel.remove(clientPanel);
		controlPanel.remove(serverPanel);
		split.remove(temp);
		split.add(board);
		showPlayer.remove(timeSlider);
		mov=new JLabel("Move:");
		mov.setFont(new Font("Comic Sans MS",Font.PLAIN,20));
		mov.setForeground(Color.red);
		showPlayer.add(mov);
		CHNC=new JLabel(move);
		CHNC.setFont(new Font("Comic Sans MS",Font.BOLD,20));
		CHNC.setForeground(Color.blue);
		showPlayer.add(CHNC);
		displayTime.remove(start);
		displayTime.add(label);
		timer=new Time(label);
		Time.update();
	}
	public static void setTeam(ChessTeam team) {
		player = team;
		if (player == ChessTeam.THIRD) {
			controlPanel.add(new JLabel("server started, waiting for players"));
		} else {
			controlPanel.add(new JLabel(team.name() + " connected"));
		}
		controlPanel.revalidate();
	}
	
	
	// A function to change the chance from White Player to Black Player or vice verse
	// It is made public because it is to be accessed in the Time Class
	public static void changechance()
	{

		if (boardState[getKing(chance).getx()][getKing(chance).gety()].ischeck())
		{
			chance^=1;
			gameend();
		}

		System.out.println("Move " + Main.move);

		if(destinationlist.isEmpty()==false)
			cleandestinations(destinationlist);
		if(previous!=null)
			previous.deselect();
		previous=null;
		chance^=1;
		if(!end && timer!=null)
		{
			showPlayer.remove(CHNC);
			if(Main.move=="White")
				Main.move="Black";
			else
				Main.move="White";
			CHNC.setText(Main.move);
			showPlayer.add(CHNC);
		}
	}
	
	//A function to retrieve the Black King or White King
	private static King getKing(int color)
	{
		if (color==0)
			return wk;
		else
			return bk;
	}
	
	//A function to clean the highlights of possible destination cells
    private static void cleandestinations(ArrayList<Cell> destlist)      //Function to clear the last move's destinations
    {
    	ListIterator<Cell> it = destlist.listIterator();
    	while(it.hasNext())
    		it.next().removepossibledestination();
    }
    
    //A function that indicates the possible moves by highlighting the Cells
    private static void highlightdestinations(ArrayList<Cell> destlist)
    {
    	ListIterator<Cell> it = destlist.listIterator();
    	while(it.hasNext())
    		it.next().setpossibledestination();
    }
    
    
  //Function to check if the king will be in danger if the given move is made
    private static boolean willkingbeindanger(Cell fromcell,Cell tocell)
    {
    	Cell newboardstate[][] = new Cell[8][8];
    	for(int i=0;i<8;i++)
    		for(int j=0;j<8;j++)
    		{	try { newboardstate[i][j] = new Cell(boardState[i][j]);} catch (CloneNotSupportedException e){e.printStackTrace(); System.out.println("There is a problem with cloning !!"); }}
    	
    	if(newboardstate[tocell.x][tocell.y].getpiece()!=null)
			newboardstate[tocell.x][tocell.y].removePiece();
    	
		newboardstate[tocell.x][tocell.y].setPiece(newboardstate[fromcell.x][fromcell.y].getpiece());
		if(newboardstate[tocell.x][tocell.y].getpiece() instanceof King)
		{
			((King)(newboardstate[tocell.x][tocell.y].getpiece())).setx(tocell.x);
			((King)(newboardstate[tocell.x][tocell.y].getpiece())).sety(tocell.y);
		}
		newboardstate[fromcell.x][fromcell.y].removePiece();
		if (((King)(newboardstate[getKing(chance).getx()][getKing(chance).gety()].getpiece())).isindanger(newboardstate)==true)
			return true;
		else
			return false;
    }
    
    //A function to eliminate the possible moves that will put the King in danger
    private static ArrayList<Cell> filterdestination (ArrayList<Cell> destlist, Cell fromcell)
    {
    	ArrayList<Cell> newlist = new ArrayList<Cell>();
    	Cell newboardstate[][] = new Cell[8][8];
    	ListIterator<Cell> it = destlist.listIterator();
    	int x,y;
    	while (it.hasNext())
    	{
    		for(int i=0;i<8;i++)
        		for(int j=0;j<8;j++)
        		{	try { newboardstate[i][j] = new Cell(boardState[i][j]);} catch (CloneNotSupportedException e){e.printStackTrace();}}
    		
    		Cell tempc = it.next();
    		if(newboardstate[tempc.x][tempc.y].getpiece()!=null)
    			newboardstate[tempc.x][tempc.y].removePiece();
    		newboardstate[tempc.x][tempc.y].setPiece(newboardstate[fromcell.x][fromcell.y].getpiece());
    		x=getKing(chance).getx();
    		y=getKing(chance).gety();
    		if(newboardstate[fromcell.x][fromcell.y].getpiece() instanceof King)
    		{
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).setx(tempc.x);
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).sety(tempc.y);
    			x=tempc.x;
    			y=tempc.y;
    		}
    		newboardstate[fromcell.x][fromcell.y].removePiece();
    		if ((((King)(newboardstate[x][y].getpiece())).isindanger(newboardstate)==false))
    			newlist.add(tempc);
    	}
    	return newlist;
    }
    
    //A Function to filter the possible moves when the king of the current player is under Check 
    private static ArrayList<Cell> incheckfilter (ArrayList<Cell> destlist, Cell fromcell, int color)
    {
    	ArrayList<Cell> newlist = new ArrayList<Cell>();
    	Cell newboardstate[][] = new Cell[8][8];
    	ListIterator<Cell> it = destlist.listIterator();
    	int x,y;
    	while (it.hasNext())
    	{
    		for(int i=0;i<8;i++)
        		for(int j=0;j<8;j++)
        		{	try { newboardstate[i][j] = new Cell(boardState[i][j]);} catch (CloneNotSupportedException e){e.printStackTrace();}}
    		Cell tempc = it.next();
    		if(newboardstate[tempc.x][tempc.y].getpiece()!=null)
    			newboardstate[tempc.x][tempc.y].removePiece();
    		newboardstate[tempc.x][tempc.y].setPiece(newboardstate[fromcell.x][fromcell.y].getpiece());
    		x=getKing(color).getx();
    		y=getKing(color).gety();
    		if(newboardstate[tempc.x][tempc.y].getpiece() instanceof King)
    		{
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).setx(tempc.x);
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).sety(tempc.y);
    			x=tempc.x;
    			y=tempc.y;
    		}
    		newboardstate[fromcell.x][fromcell.y].removePiece();
    		if ((((King)(newboardstate[x][y].getpiece())).isindanger(newboardstate)==false))
    			newlist.add(tempc);
    	}
    	return newlist;
    }
    
    //A function to check if the King is check-mate. The Game Ends if this function returns true.
    public static boolean checkmate(int color)
    {
    	ArrayList<Cell> dlist = new ArrayList<Cell>();
    	for(int i=0;i<8;i++)
    	{
    		for(int j=0;j<8;j++)
    		{
    			if (boardState[i][j].getpiece()!=null && boardState[i][j].getpiece().getcolor()==color)
    			{
    				dlist.clear();
    				dlist=boardState[i][j].getpiece().move(boardState, i, j);
    				dlist=incheckfilter(dlist,boardState[i][j],color);
    				if(dlist.size()!=0)
    					return false;
    			}
    		}
    	}
    	return true;
    }
	
    
    @SuppressWarnings("deprecation")
	private static void gameend()
    {
    	cleandestinations(destinationlist);
    	displayTime.disable();
    	timer.whiteCountdownTimer.stop();
    	timer.blackCountdownTimer.stop();
    	if(previous!=null)
    		previous.removePiece();

		JOptionPane.showMessageDialog(board,"Checkmate!!!\n"+winner+" wins");
		
		displayTime.add(start);
		showPlayer.remove(mov);
		showPlayer.remove(CHNC);
		showPlayer.revalidate();
		showPlayer.add(timeSlider);
		
		split.remove(board);
		split.add(temp);
		WNewPlayer.enable();
		BNewPlayer.enable();
		wselect.enable();
		bselect.enable();
		end=true;
		Mainboard.disable();
		Mainboard.dispose();
		Mainboard = new Main();
		Mainboard.setVisible(true);
		Mainboard.setResizable(false);
    }
    
    
    //call this from the mouseclick. Makes it easier to call this code manually with sent moves
    public static void moveMaybe(int x, int y, boolean overrideSequence) {

    	if (!overrideSequence) {
    		System.out.println("my turn = " + isMyTurn());
    	}
    	if (!isMyTurn() && !overrideSequence) {
    		if(!thirdPlayerTurn)
    			return;
    	}
    	c = boardState[x][y];
    	
    	
    	
    	if (previous==null)
		{
			if(c.getpiece()!=null)
			{
				if(c.getpiece().getcolor()!=chance)
					return;
				c.select();
				previous=c;
				destinationlist.clear();
				destinationlist=c.getpiece().move(boardState, c.x, c.y);
				if(c.getpiece() instanceof King)
					destinationlist=filterdestination(destinationlist,c);
				else
				{
					if(boardState[getKing(chance).getx()][getKing(chance).gety()].ischeck())
						destinationlist = new ArrayList<Cell>(filterdestination(destinationlist,c));
					else if(destinationlist.isEmpty()==false && willkingbeindanger(c,destinationlist.get(0)))
						destinationlist.clear();
				}
				highlightdestinations(destinationlist);
			}
		}
		else
		{
			if(c.x==previous.x && c.y==previous.y)
			{
				c.deselect();
				cleandestinations(destinationlist);
				destinationlist.clear();
				previous=null;
			}
			else if(c.getpiece()==null||previous.getpiece().getcolor()!=c.getpiece().getcolor())
			{

				if(c.ispossibledestination())
				{
					//SET THE MESSAGE FOR THE CLIENT SERVER CONNECTION
				    if (!overrideSequence) {
						moveMsg.setMessage(previous.x + "," + previous.y +"," + x + "," + y);
					}
				    
				    // Quick Check for the taken piece condition. If I had more time, would be more elegant.
				    
				    hasJustTakenPiece = false;
				    
					if(c.getpiece()!=null){
						c.removePiece();
						hasJustTakenPiece = true;
					}
					
					c.setPiece(previous.getpiece());
					
					if(c.getpiece() instanceof King)
						checkIfHasCastled(c);
					
					if(c.getpiece() instanceof Rook || c.getpiece() instanceof King)
						c.getpiece().hasMoved = true;
					
					// logic for pawn promotion
					
					if(checkIfPossibleToPromote(c))
						c = promotePawn(c);
					
					if(c.getpiece() instanceof Pawn){
						if(Math.abs(previous.x - c.x) == 2)
							((Pawn)c.getpiece()).hasJustSteppedTwoSpacesForward = true;
						else
							((Pawn)c.getpiece()).hasJustSteppedTwoSpacesForward = false;
						
						checkIfHasUsedEnPassant(c);
					}
					
					
					if (previous.ischeck())
						previous.removecheck();
					previous.removePiece();
					
					
					if(getKing(chance^1).isindanger(boardState))
					{
						boardState[getKing(chance^1).getx()][getKing(chance^1).gety()].setcheck();
						if (checkmate(getKing(chance^1).getcolor()))
						{
							previous.deselect();
							if(previous.getpiece()!=null)
								previous.removePiece();
							gameend();
						}
					}
					if(getKing(chance).isindanger(boardState)==false)
						boardState[getKing(chance).getx()][getKing(chance).gety()].removecheck();
					if(c.getpiece() instanceof King)
					{
						((King)c.getpiece()).setx(c.x);
						((King)c.getpiece()).sety(c.y);
					}
					changechance();
					// teams switch
					
					
					if(Main.thirdPlayerTurn){
						System.out.println("Changing thirdPlayerTurnToFalse");					
						Main.thirdPlayerTurn = false;
						setWhoseTurnWithChance();
						// change 3rd parameter to 1 for demo in class (taking another piece)
						// the WIN_CODE just returns true
						checkIfThirdPlayerHasWonOnCondition(previous, c, 1);
						Time.update();
					}

				}
				if(previous!=null)
				{
					previous.deselect();
					previous=null;
				}
				cleandestinations(destinationlist);
				destinationlist.clear();
			}
			else if(previous.getpiece().getcolor()==c.getpiece().getcolor())
			{
				previous.deselect();
				cleandestinations(destinationlist);
				destinationlist.clear();
				c.select();
				previous=c;
				destinationlist=c.getpiece().move(boardState, c.x, c.y);
				if(c.getpiece() instanceof King)
					destinationlist=filterdestination(destinationlist,c);
				else
				{
					if(boardState[getKing(chance).getx()][getKing(chance).gety()].ischeck())
						destinationlist = new ArrayList<Cell>(filterdestination(destinationlist,c));
					else if(destinationlist.isEmpty()==false && willkingbeindanger(c,destinationlist.get(0)))
						destinationlist.clear();
				}
				highlightdestinations(destinationlist);
			}
		}
		if(c.getpiece()!=null && c.getpiece() instanceof King)
		{
			((King)c.getpiece()).setx(c.x);
			((King)c.getpiece()).sety(c.y);
		}
		
		
		
		//use the coordinates to form a string to communicate what was clicked


    	
    }
    
    private static void checkIfThirdPlayerHasWonOnCondition(Cell previous, Cell c, int condID) {
		if(condID == 0)
			gameend();
		
		if(condID == 1){
			if(hasJustTakenPiece)
				gameend();
		}
	}

	//These are the abstract function of the parent class. Only relevant method here is the On-Click Fuction
    //which is called when the user clicks on a particular cell
	@Override
	public void mouseClicked(MouseEvent arg0){
		// TODO Auto-generated method stub
		c=(Cell)arg0.getSource();
		moveMaybe(c.x, c.y, false);
		
	}
    
	private static void checkIfHasUsedEnPassant(Cell pawnCellMovedTo) {
		
		if(pawnCellMovedTo.x != 2 && pawnCellMovedTo.x != 5)
			return;
		
		Pawn pawnPiece = (Pawn) pawnCellMovedTo.getpiece();
		
		
		checkIfPawnBehindAndItJustJumped(pawnCellMovedTo, pawnPiece);
		
	}

	private static void checkIfPawnBehindAndItJustJumped(Cell pawnCellMovedTo, Pawn pawnPiece) {
		
		if(pawnCellMovedTo.x == 2){
			Cell cellBehindPawn = boardState[3][pawnCellMovedTo.y];
			Piece behindPiece = cellBehindPawn.getpiece();
			
			if(!(behindPiece instanceof Pawn))
				return;
			
			if(((Pawn) behindPiece).hasJustSteppedTwoSpacesForward){
				Cell pawnCell = boardState[3][pawnCellMovedTo.y];
				pawnCell.removePiece();
			}
			
		}
		else{
			Cell cellBehindPawn = boardState[6][pawnCellMovedTo.y];
			Piece behindPiece = cellBehindPawn.getpiece();
			
			if(!(behindPiece instanceof Pawn))
				return;
			
			if(((Pawn) behindPiece).hasJustSteppedTwoSpacesForward){
				Cell pawnCell = boardState[6][pawnCellMovedTo.y];
				pawnCell.removePiece();
			}
		}
	}

	private static void checkIfHasCastled(Cell kingCell) {
		King kingPiece = (King) kingCell.getpiece();
		
		if(!kingPiece.canEvenCastle)
			return;
		
		int xCoord = kingCell.x;
		int yCoord = kingCell.y;
		
		if(xCoord != 0 && xCoord != 7)
			return;
		
		if(yCoord != 2 && yCoord != 6)
			return;
		
		handleRookMovementOnCastle(kingCell);
		
	}
	
	private static void handleRookMovementOnCastle(Cell kingCell){
		int xCoord = kingCell.x;
		int yCoord = kingCell.y;
		
		if(yCoord == 2){
			Cell rookCell = boardState[xCoord][0];
			Piece rookPiece = rookCell.getpiece();
			
			rookPiece.hasMoved = true;
			rookCell.removePiece();
			
			boardState[xCoord][3].setPiece(rookPiece);
		}else{
			Cell rookCell = boardState[7][7];
			Piece rookPiece = rookCell.getpiece();
			
			rookPiece.hasMoved = true;
			boardState[7][7].removePiece();
			
			boardState[xCoord][5].setPiece(rookPiece);
		}
	}

	private static boolean checkIfPossibleToPromote(Cell cellToCheck){
		Piece chessPiece = cellToCheck.getpiece();
		
		if(!(chessPiece instanceof Pawn))
			return false;
		
		System.out.println(cellToCheck.x != 0);
		if(cellToCheck.x != 0 && cellToCheck.x != 7)
			return false;
		
		return true;
	}
	
    private static Cell promotePawn(Cell cellToCheck) {
    	Piece chessPiece = cellToCheck.getpiece();
    	
		
		
		if(chessPiece.getcolor() == 0)
			cellToCheck.setPiece(new Queen("WQA","White_Queen.png",0));
		else
			cellToCheck.setPiece(new Queen("BQA","Black_Queen.png", 1));
		
		return cellToCheck;
	}

	//Other Irrelevant abstract function. Only the Click Event is captured.
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub		
	}
	
	
	class TimeChange implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent arg0)
		{
			timeRemaining=timeSlider.getValue()*60;
		}
	}


	public static void stopTimers() {
		// TODO Auto-generated method stub
		timer.whiteCountdownTimer.stop();
		timer.blackCountdownTimer.stop();
		
	}

	
	public static boolean isMyTurn() {
		return player == whoseTurn;
	}

	public static void setWhoseTurnWithChance() {
		System.out.println("chance is: " + Main.chance);
			if (Main.chance == 1) {
				Main.whoseTurn = ChessTeam.BLACK;
			}
			else if (Main.chance == 0) {
				Main.whoseTurn = ChessTeam.WHITE;
			}
			
			setTeam(Main.whoseTurn);
	}





}