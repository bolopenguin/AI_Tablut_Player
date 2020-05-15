package strategy;

import java.util.ArrayList;
import java.util.List;

import domain.State;

public abstract class StrategyUtils {
	
	protected static final double TOTWHITE = 9.0;
	protected static final double TOTBLACK = 16.0;
	protected static final int up = 0;  
	protected static final int down = 1;
	protected static final int left = 2;
	protected static final int right = 3;
		
	protected int[] king; 			      // posizione del king
	protected List<int[]> white; 	      // posizione dei bianchi
	protected List<int[]> black; 	      // posizione dei neri
	protected State state;			      // stato attuale del gioco
	
	protected StrategyUtils(State state) 
	{
		this.king = new int[2];
		this.white = new ArrayList<int[]>();
		this.black = new ArrayList<int[]>();
		this.state = state;
		
		int[] pos; // posizione (esempio "z6")
		for (int i = 0; i < state.getBoard().length; i++) 
		{
			for (int j = 0; j < state.getBoard().length; j++)
			{
				if (state.getPawn(i, j).equalsPawn("W") || state.getPawn(i, j).equalsPawn("K")) 
				{
					if (state.getPawn(i, j).equalsPawn("K")) 
					{
						this.king[0] = i;
						this.king[1] = j;
					}
					pos = new int[2];
					pos[0] = i;
					pos[1] = j;
					this.white.add(pos);
				} 
				else if (state.getPawn(i, j).equalsPawn("B")) 
				{
					pos = new int[2];
					pos[0] = i;
					pos[1] = j;
					this.black.add(pos);
				}
			}
		}
	}
	
	
	//Funzione che indica se, da quante e da quali pedine si è circondati 
	protected int[] nearPawn(int row, int column, String pawn)
	{
		//Restituisce all'indice corrispondente se nella casella al lato corrispondente di quella indicata si trova una pedina del tipo richiesto
		//0 se è libera, - se è occupata dal colore opposto
		
		int[] box = new int[4]; 
		box[up] = 0;    
		box[down] = 0;
		box[left] = 0;
		box[right] = 0;
		String other;
			
		if(pawn == "B")
		{
			other = "W";
			}
			else
			{
				other = "B";
			}	
			
			if(state.getPawn(row-1, column).equalsPawn(pawn))
			{
				box[up] = 1;
			}
			else if(state.getPawn(row-1, column).equalsPawn(other))
			{
				box[up] = -1;
			}
				
			
			if(state.getPawn(row+1, column).equalsPawn(pawn))
			{
				box[down] = 1;
			}
			else if(state.getPawn(row-1, column).equalsPawn(other))
			{
				box[down] = -1;
			}
			
			if(state.getPawn(row, column-1).equalsPawn(pawn))
			{
				box[left] = 1;
			}
			else if(state.getPawn(row-1, column).equalsPawn(other))
			{
				box[left] = -1;
			}
			
			if(state.getPawn(row, column+1).equalsPawn(pawn))
			{
				box[right] = 1;
			}
		else if(state.getPawn(row-1, column).equalsPawn(other))
		{
			box[right] = -1;
		}
			
		return box;  //Restituisce un array
	}
		
	//Conta il numero di pedine adiacenti del colore richiesto
	protected int countNear(int row, int column, String pawn)
	{
		int counter = 0;
		int[] nearPawn = new int[4];
		nearPawn = this.nearPawn(row, column, pawn);
		for(int k = 0; k < 4; k++)
		{
			if(nearPawn[k] > 0)
			{
				counter = counter + nearPawn[k];
			}
		}
		return counter;
	}
	
	//Verifica se il Re è nel Trono
	protected boolean inThrone() 
	{
		boolean result = false;
		if(this.king[0] == 4 && this.king[1] == 4)
	   	{
			result = true;
	   	}
		return result;
	}
		
	//Verifica se il Re è adiacente al trono e restituisce inoltre su quale lato
	protected int nearThrone()
	{
		int result = -1;
		if(this.king[0] == 3 && this.king[1] == 4)
		{
			result = up;
		}
		else if(this.king[0] == 5 && this.king[1] == 4)
		{
			result = down;
		}
		else if(this.king[0] == 4 && this.king[1] == 3)
		{
			result = left;
		}
		else if(this.king[0] == 4 && this.king[1] == 5)
		{
			result = right;
		}
			
		return result;
	}
				
	//Funzione che restituisce un BOOLEAN, True se la pedina è adiacente ad un campo, False altrimenti.
	protected boolean nearCamp(int row, int column)
	{
			
			
		if((row == 1 && (column == 3 || column == 5)) || (row == 7 && (column == 3 || column == 5)) || ((row == 2 || row == 6 ) && column == 4)
			|| (row == 3 && (column == 1 || column == 7)) || (row == 5 && (column == 1 || column == 7)) || ((column == 2 || column == 6 ) && row == 4)	)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
		
	//Funzione che verifica se nella parte di riga della scacchiera desiderata sono presenti pedine
	protected int busyRowPawn(int direction, int row, int column, String pawn) //La direzione potrà essere Left o Right
	{
		int result = 0;
		boolean free = true;
		String other;
		if(pawn == "B")
		{
			other = "W";
		}
		else
		{
			other = "B";
		}
		if((row >= 9 || column >= 9) || (row < 0 || column < 0)) 
		{
			return result;
		}
		
		
		if(direction == left)
		{   
			if(column >= 1) 
			{
				for(int j = column-1; j >= 0 && free == true; j--)
			    {
					if(state.getPawn(row, j).equalsPawn(pawn))
					{
						result = 1;
		  			    free = false;
		  			}
					else if(state.getPawn(row, j).equalsPawn(other))
					{
						result = 2;
		  			    free = false;
		  			}
				}
			}
		}
		else
		{
			if(column <= 7) 
			{
				for(int j = column+1; j <= 8 && free == true; j++)
		    	{
		    		if(state.getPawn(row, j).equalsPawn(pawn))
		    		{
		    			result = 1;
		    			free = false;
			   		}
			   		else if(state.getPawn(row, j).equalsPawn(other))
			   		{
			   			result = 1;
			   			free = false;
			   		}
			   	}	
			}
		}
		return result;  //Result sarà 1 se nella direzione richiesta la prima pedina che si incontra è del colore desiderato
			            //Result sarà 0 se non si incontrano pedine, -1 se del colore opposto
	}
		
	//Funzione che verifica se nella parte di colonna della scacchiera desiderata sono presenti pedine
	protected int busyColumnPawn(int direction, int row, int column, String pawn) //La direzione potrà essere Up o Down
	{
		int result = 0;
		boolean free = true;
		String other;
		if(pawn == "B")
		{
			other = "W";
		}
		else
		{
			other = "B";
		}
				
		if((row >= 9 || column >= 9) || (row < 0 || column < 0)) 
		{
			return result;
		}
		
		
		if(direction == up)
		{
			if(row >= 1)
			{
				for(int i = row-1; i >= 0 && free == true; i--)
			    {
					if(state.getPawn(i, column).equalsPawn(pawn))
	  	    		{
	  	    	    	result = 1;
	  	  		    	free = false;
	  	   		    }
	  	    		else if(state.getPawn(i, column).equalsPawn(other))
	  	    		{
	  	    		   	result = 2;
	  	    	    	free = false;
	  			    }	
	  			}
    		}
		}
		else
		{
			if(row <= 7)
			{ 
				for(int i = row+1; i <= 8 && free == true; i++)
				{ 
					if(state.getPawn(i, column).equalsPawn(pawn)) 
				    {
						result = 1;
						free = false;
					}
					else if(state.getPawn(i, column).equalsPawn(other))
					{
						result = 2;
						free = false;
					}
				}
			}
		}
		return result; //Result sarà 1 se nella direzione richiesta la prima pedina che si incontra è del colore desiderato
                       //Result sarà 0 se non si incontrano pedine, -1 se del colore opposto
	}
		
	//Funzione che indica se è possibile mangiare DA SOPRA ( Sopra - Sinistra - Destra )
	protected boolean eatUp(int row, int column, String pawn)
	{
		if((row >= 9 || column >= 9) || (row < 0 || column < 0))
		{
			return false;
		}
		
		if((busyColumnPawn(up, row, column, pawn) == 1) || (busyRowPawn(left, row-1, column, pawn) == 1) || (busyRowPawn(right, row-1, column, pawn) == 1))
	 	{
	  		return true;
	  	}
	  	else
	  	{
	  		return false;
	  	}
	}
		
	//Funzione che indica se è possibile mangiare DA SOTTO ( Sotto - Sinistra - Destra )
	protected boolean eatDown(int row, int column, String pawn)
	{
		if((row >= 9 || column >= 9) || (row < 0 || column < 0))
		{
			return false;
		}
		
		if((busyColumnPawn(down, row, column, pawn) == 1) || (busyRowPawn(left, row+1, column, pawn) == 1) || (busyRowPawn(right, row+1, column, pawn) == 1))
	 	{
	 		return true;
	 	}
	  	else
	  	{
	  		return false;
	  	}
	}
		
	//Funzione che indica se è possibile mangiare DA SINISTRA ( Sinistra - Sopra - Sotto )
	protected boolean eatLeft(int row, int column, String pawn)
	{
		if((row >= 9 || column >= 9) || (row < 0 || column < 0))
		{
			return false;
		}
		
		if((busyRowPawn(left, row, column, pawn) == 1) || (busyColumnPawn(up, row, column-1, pawn) == 1) || (busyColumnPawn(down, row, column-1, pawn) == 1))
	  	{
	  		return true;
	 	}
	 	else
	 	{
	 		return false;
	 	}
	}
			
	//Funzione che indica se è possibile mangiare DA DESTRA ( Destra - Sopra - Sotto  )
	protected boolean eatRight(int row, int column, String pawn)
	{
		if((row >= 9 || column >= 9) || (row < 0 || column < 0)) 
		{
			return false;
		}
		
		if((busyRowPawn(right, row, column, pawn) == 1) || (busyColumnPawn(up, row, column+1, pawn) == 1) || (busyColumnPawn(down, row, column+1, pawn) == 1))
	  	{
	  		return true;
	  	}
	  	else
	  	{
	  		return false;
	 	}
	}
	
	//Funzione che verifica che una pedina non sia adiacente al Re
	protected boolean kingNotNear(int row, int column)
	{
		boolean result = true;
		if((this.king[0] == row+1 || this.king[0] == row-1) && (this.king[1] == column))
		{
			result = false;
		}
		else if((this.king[0] == row) && (this.king[1] == column+1 || this.king[1] == column-1))
		{
			result = false;
		}
		return result;
	}
				
	public abstract double getEvaluation();
}
