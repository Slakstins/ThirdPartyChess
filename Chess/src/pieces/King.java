package pieces;

import java.util.ArrayList;

import chess.Cell;

public class King extends Piece{
	
	private int x,y; //Extra variables for King class to keep a track of king's position
	public boolean hasCastledWithLeftRook = false;
	public boolean hasCastledWithRightRook = false;
	public boolean canEvenCastle = false;
	
	//King Constructor
	public King(String i,String p,int c,int x,int y)
	{
		setx(x);
		sety(y);
		setId(i);
		setPath(p);
		setColor(c);
	}
	
	//general value access functions
	public void setx(int x)
	{
		this.x=x;
	}
	public void sety(int y)
	{
		this.y=y;
	}
	public int getx()
	{
		return x;
	}
	public int gety()
	{
		return y;
	}
	//Move Function for King Overridden from Pieces
	public ArrayList<Cell> move(Cell state[][],int x,int y)
	{
		//King can move only one step. So all the adjacent 8 cells have been considered.
		possiblemoves.clear();
		
		checkIfCanCastle(possiblemoves, state);
		
		
		if(possiblemoves.size() == 0)
			canEvenCastle = false;
		else
			canEvenCastle = true;
		
		int posx[]={x,x,x+1,x+1,x+1,x-1,x-1,x-1};
		int posy[]={y-1,y+1,y-1,y,y+1,y-1,y,y+1};
		for(int i=0;i<8;i++)
			if((posx[i]>=0&&posx[i]<8&&posy[i]>=0&&posy[i]<8))
				if((state[posx[i]][posy[i]].getpiece()==null||state[posx[i]][posy[i]].getpiece().getcolor()!=this.getcolor()))
					possiblemoves.add(state[posx[i]][posy[i]]);
				
		return possiblemoves;
	}
	
	
	
	private void checkIfCanCastle(ArrayList<Cell> possibleMoves, Cell[][] state) {
		
		if(this.hasMoved || isindanger(state))
			return;
		
		// white king
		if(this.getcolor() == 0){
			checkForWhiteKing(possibleMoves, state);
		}
		else{
			checkForBlackKing(possibleMoves, state);
		}
		
		return;
		
	}

	private void checkForBlackKing(ArrayList<Cell> possibleMoves, Cell[][] state) {
		Piece leftRook = state[0][0].getpiece();
		Piece rightRook = state[0][7].getpiece();
		
		checkBlackKingLeftRook(leftRook, possibleMoves, state);
		checkBlackKingRightRook(rightRook, possibleMoves, state);
		
		
	}

	private void checkBlackKingRightRook(Piece rightRook, ArrayList<Cell> possibleMoves, Cell[][] state) {
		if(rightRook == null)
			return;
		
		if(!(rightRook instanceof Rook))
			return;
		
		if(rightRook.hasMoved)
			return;
		
		Cell rightBishopCell = state[0][5];
		Cell rightKnightCell = state[0][6];
		
		if(rightBishopCell.getpiece() != null || rightKnightCell.getpiece() != null)
			return;
		
		if(isindanger(state, 0, 5) || isindanger(state, 0, 6))
			return;
		
		possiblemoves.add(state[0][6]);
		
		
	}

	private void checkBlackKingLeftRook(Piece leftRook, ArrayList<Cell> possibleMoves, Cell[][] state) {
		if(leftRook == null)
			return;
		
		if(!(leftRook instanceof Rook))
			return;
		
		if(leftRook.hasMoved)
			return;
		
		Cell leftBishopCell = state[0][2];
		Cell leftKnightCell = state[0][1];
		
		if(leftBishopCell.getpiece() != null || leftKnightCell.getpiece() != null)
			return;
		
		if(isindanger(state, 0, 2) || isindanger(state, 0, 3))
			return;
		
		possiblemoves.add(state[0][2]);
		
	}

	private void checkForWhiteKing(ArrayList<Cell> possibleMoves, Cell[][] state) {
		Piece leftRook = state[7][0].getpiece();
		Piece rightRook = state[7][7].getpiece();
		
		checkWhiteKingLeftRook(leftRook, possibleMoves, state);
		checkWhiteKingRightRook(rightRook, possibleMoves, state);
		
	}

	private void checkWhiteKingRightRook(Piece rightRook, ArrayList<Cell> possibleMoves, Cell[][] state) {
		if(rightRook == null)
			return;
		
		if(!(rightRook instanceof Rook))
			return;
		
		if(rightRook.hasMoved)
			return;
		
		Cell rightBishopCell = state[7][5];
		Cell rightKnightCell = state[7][6];
		
		if(rightBishopCell.getpiece() != null || rightKnightCell.getpiece() != null)
			return;
		
		if(isindanger(state, 7, 5) || isindanger(state, 7, 6))
			return;
		
		possiblemoves.add(state[7][6]);
		
		
	}

	private void checkWhiteKingLeftRook(Piece leftRook, ArrayList<Cell> possibleMoves, Cell[][] state) {
		if(leftRook == null)
			return;
		
		if(!(leftRook instanceof Rook))
			return;
		
		if(leftRook.hasMoved)
			return;
		
		Cell leftBishopCell = state[7][2];
		Cell leftKnightCell = state[7][1];
		
		if(leftBishopCell.getpiece() != null || leftKnightCell.getpiece() != null)
			return;
		
		if(isindanger(state, 7, 2) || isindanger(state, 7, 3))
			return;
		
		possiblemoves.add(state[7][2]);
		
		
		
	}

	//Function to check if king is under threat
	//It checks whether there is any piece of opposite color that can attack king for a given board state
	public boolean isindanger(Cell state[][])
    {
		
		//Checking for attack from left,right,up and down
    	for(int i=x+1;i<8;i++)
    	{
    		if(state[i][y].getpiece()==null)
    			continue;
    		else if(state[i][y].getpiece().getcolor()==this.getcolor())
    			break;
    		else
    		{
    			if ((state[i][y].getpiece() instanceof Rook) || (state[i][y].getpiece() instanceof Queen))
    				return true;
    			else
    				break;
    		}
    	}
    	for(int i=x-1;i>=0;i--)
    	{
    		if(state[i][y].getpiece()==null)
    			continue;
    		else if(state[i][y].getpiece().getcolor()==this.getcolor())
    			break;
    		else
    		{
    			if ((state[i][y].getpiece() instanceof Rook) || (state[i][y].getpiece() instanceof Queen))
    				return true;
    			else
    				break;
    		}
    	}
    	for(int i=y+1;i<8;i++)
    	{
    		if(state[x][i].getpiece()==null)
    			continue;
    		else if(state[x][i].getpiece().getcolor()==this.getcolor())
    			break;
    		else
    		{
    			if ((state[x][i].getpiece() instanceof Rook) || (state[x][i].getpiece() instanceof Queen))
    				return true;
    			else
    				break;
    		}
    	}
    	for(int i=y-1;i>=0;i--)
    	{
    		if(state[x][i].getpiece()==null)
    			continue;
    		else if(state[x][i].getpiece().getcolor()==this.getcolor())
    			break;
    		else
    		{
    			if ((state[x][i].getpiece() instanceof Rook) || (state[x][i].getpiece() instanceof Queen))
    				return true;
    			else
    				break;
    		}
    	}
    	
    	//checking for attack from diagonal direction
    	int tempx=x+1,tempy=y-1;
		while(tempx<8&&tempy>=0)
		{
			if(state[tempx][tempy].getpiece()==null)
			{
				tempx++;
				tempy--;
			}
			else if(state[tempx][tempy].getpiece().getcolor()==this.getcolor())
				break;
			else
			{
				if (state[tempx][tempy].getpiece() instanceof Bishop || state[tempx][tempy].getpiece() instanceof Queen)
    				return true;
    			else
    				break;
			}
		}
		tempx=x-1;tempy=y+1;
		while(tempx>=0&&tempy<8)
		{
			if(state[tempx][tempy].getpiece()==null)
			{
				tempx--;
				tempy++;
			}
			else if(state[tempx][tempy].getpiece().getcolor()==this.getcolor())
				break;
			else
			{
				if (state[tempx][tempy].getpiece() instanceof Bishop || state[tempx][tempy].getpiece() instanceof Queen)
    				return true;
    			else
    				break;
			}
		}
		tempx=x-1;tempy=y-1;
		while(tempx>=0&&tempy>=0)
		{
			if(state[tempx][tempy].getpiece()==null)
			{
				tempx--;
				tempy--;
			}
			else if(state[tempx][tempy].getpiece().getcolor()==this.getcolor())
				break;
			else
			{
				if (state[tempx][tempy].getpiece() instanceof Bishop || state[tempx][tempy].getpiece() instanceof Queen)
    				return true;
    			else
    				break;
			}
		}
		tempx=x+1;tempy=y+1;
		while(tempx<8&&tempy<8)
		{
			if(state[tempx][tempy].getpiece()==null)
			{
				tempx++;
				tempy++;
			}
			else if(state[tempx][tempy].getpiece().getcolor()==this.getcolor())
				break;
			else
			{
				if (state[tempx][tempy].getpiece() instanceof Bishop || state[tempx][tempy].getpiece() instanceof Queen)
    				return true;
    			else
    				break;
			}
		}
		
		//Checking for attack from the Knight of opposite color
		int posx[]={x+1,x+1,x+2,x+2,x-1,x-1,x-2,x-2};
		int posy[]={y-2,y+2,y-1,y+1,y-2,y+2,y-1,y+1};
		for(int i=0;i<8;i++)
			if((posx[i]>=0&&posx[i]<8&&posy[i]>=0&&posy[i]<8))
				if(state[posx[i]][posy[i]].getpiece()!=null && state[posx[i]][posy[i]].getpiece().getcolor()!=this.getcolor() && (state[posx[i]][posy[i]].getpiece() instanceof Knight))
				{
					return true;
				}
		
		
		//Checking for attack from the Pawn of opposite color
		int pox[]={x+1,x+1,x+1,x,x,x-1,x-1,x-1};
		int poy[]={y-1,y+1,y,y+1,y-1,y+1,y-1,y};
		{
			for(int i=0;i<8;i++)
				if((pox[i]>=0&&pox[i]<8&&poy[i]>=0&&poy[i]<8))
					if(state[pox[i]][poy[i]].getpiece()!=null && state[pox[i]][poy[i]].getpiece().getcolor()!=this.getcolor() && (state[pox[i]][poy[i]].getpiece() instanceof King))
					{
						return true;
					}
		}
		if(getcolor()==0)
		{
			if(x>0&&y>0&&state[x-1][y-1].getpiece()!=null&&state[x-1][y-1].getpiece().getcolor()==1&&(state[x-1][y-1].getpiece() instanceof Pawn))
				return true;
			if(x>0&&y<7&&state[x-1][y+1].getpiece()!=null&&state[x-1][y+1].getpiece().getcolor()==1&&(state[x-1][y+1].getpiece() instanceof Pawn))
				return true;
		}
		else
		{
			if(x<7&&y>0&&state[x+1][y-1].getpiece()!=null&&state[x+1][y-1].getpiece().getcolor()==0&&(state[x+1][y-1].getpiece() instanceof Pawn))
				return true;
			if(x<7&&y<7&&state[x+1][y+1].getpiece()!=null&&state[x+1][y+1].getpiece().getcolor()==0&&(state[x+1][y+1].getpiece() instanceof Pawn))
				return true;
		}
    	return false;
    }
}