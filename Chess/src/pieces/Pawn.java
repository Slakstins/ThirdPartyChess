package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Pawn Class inherited from the piece
 *
 */
public class Pawn extends Piece{
	
	public boolean hasJustSteppedTwoSpacesForward = false;
	
	public int lastRowPosition = -1;
	
	//COnstructors
	public Pawn(String i,String p,int c)
	{
		setId(i);
		setPath(p);
		setColor(c);
	}
	
	//Move Function Overridden
	public ArrayList<Cell> move(Cell state[][],int x,int y)
	{
		//Pawn can move only one step except the first chance when it may move 2 steps
		//It can move in a diagonal fashion only for attacking a piece of opposite color
		//It cannot move backward or move forward to attact a piece
		
		hasJustSteppedTwoSpacesForward = false;
		
		possiblemoves.clear();
		
		checkIfCanEnPassant(state, possiblemoves, x, y);
		
		if(getcolor()==0)
		{
			if(x==0)
				return possiblemoves;
			if(state[x-1][y].getpiece()==null)
			{
				possiblemoves.add(state[x-1][y]);
				if(x==6)
				{
					if(state[4][y].getpiece()==null)
						possiblemoves.add(state[4][y]);
				}
			}
			if((y>0)&&(state[x-1][y-1].getpiece()!=null)&&(state[x-1][y-1].getpiece().getcolor()!=this.getcolor()))
				possiblemoves.add(state[x-1][y-1]);
			if((y<7)&&(state[x-1][y+1].getpiece()!=null)&&(state[x-1][y+1].getpiece().getcolor()!=this.getcolor()))
				possiblemoves.add(state[x-1][y+1]);
		}
		else
		{
			if(x==8)
				return possiblemoves;
			if(state[x+1][y].getpiece()==null)
			{
				possiblemoves.add(state[x+1][y]);
				if(x==1)
				{
					if(state[3][y].getpiece()==null)
						possiblemoves.add(state[3][y]);
				}
			}
			if((y>0)&&(state[x+1][y-1].getpiece()!=null)&&(state[x+1][y-1].getpiece().getcolor()!=this.getcolor()))
				possiblemoves.add(state[x+1][y-1]);
			if((y<7)&&(state[x+1][y+1].getpiece()!=null)&&(state[x+1][y+1].getpiece().getcolor()!=this.getcolor()))
				possiblemoves.add(state[x+1][y+1]);
		}
		return possiblemoves;
	}

	private void checkIfCanEnPassant(Cell[][] state, ArrayList<Cell> possiblemoves, int x, int y) {
		if(x != 3 && x != 4)
			return;
		
		if(x == 3 && this.getcolor() == 1)
			return;
		
		if(x == 4 && this.getcolor() == 0)
			return;
		
		int yLeft = y - 1;
		int yRight = y + 1;
		
		if(yLeft >= 0){
			Cell pawnLeftHandCell = state[x][yLeft];
			Piece leftHandCellPiece = pawnLeftHandCell.getpiece();
			
			if((leftHandCellPiece instanceof Pawn)){
				if(((Pawn) leftHandCellPiece).hasJustSteppedTwoSpacesForward == true){
					
					if(x == 3){
						possiblemoves.add(state[2][yLeft]);
					}
					else{
						possiblemoves.add(state[5][yLeft]);
					}
					
					
				}
			}
				
		}
		
		if(yRight <= 7){
			Cell pawnRightHandCell = state[x][yRight];
			Piece rightHandCellPiece = pawnRightHandCell.getpiece();
			
			if((rightHandCellPiece instanceof Pawn)){
				if(((Pawn) rightHandCellPiece).hasJustSteppedTwoSpacesForward == true){
					
					if(x == 3){
						possiblemoves.add(state[2][yRight]);
					}
					else{
						possiblemoves.add(state[5][yRight]);
					}
					
					
				}
			}
		}
		
	}
}
