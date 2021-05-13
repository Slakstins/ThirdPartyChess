package pieces;

import java.util.ArrayList;

import chess.Cell;


/**
 * This is the Piece Class. It is an abstract class from which all the actual pieces are inherited.
 * It defines all the function common to all the pieces
 * The move() function an abstract function that has to be overridden in all the inherited class
 * It implements Cloneable interface as a copy of the piece is required very often
 */
public abstract class Piece implements Cloneable{

	//Member Variables
	private int color;
	private String id=null;
	private String path;
	protected ArrayList<Cell> possiblemoves = new ArrayList<Cell>();  //Protected (access from child classes)
	public abstract ArrayList<Cell> move(Cell pos[][],int x,int y);  //Abstract Function. Must be overridden
	public boolean hasMoved = false;
	//Id Setter
	public void setId(String id)
	{
		this.id=id;
	}
	
	//Path Setter
	public void setPath(String path)
	{
		this.path=path;
	}
	
	//Color Setter
	public void setColor(int c)
	{
		this.color=c;
	}
	
	//Path getter
	public String getPath()
	{
		return path;
	}
	
	//Id getter
	public String getId()
	{
		return id;
	}
	
	//Color Getter
	public int getcolor()
	{
		return this.color;
	}
	
	//Function to return the a "shallow" copy of the object. The copy has exact same variable value but different reference
	public Piece getcopy() throws CloneNotSupportedException
	{
		return (Piece) this.clone();
	}
	
	//Function to check if king is under threat
		//It checks whether there is any piece of opposite color that can attack king for a given board state
		public boolean isindanger(Cell state[][], int x, int y)
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