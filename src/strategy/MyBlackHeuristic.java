package strategy;

import domain.State;

public class MyBlackHeuristic extends MyHeuristic
{

	public MyBlackHeuristic(State state) {
		super(state);
		// TODO Auto-generated constructor stub
	}
	
	//Variabili Globali - Possibile Spostamento in MyHeuristic
	protected static final int up = 0;    //Utili per Array
	protected static final int down = 1;
	protected static final int left = 2;
	protected static final int right = 3;
	
	protected int rowK = this.king[0];    //Riga in cui si trova il Re
	protected int columnK = this.king[1]; //Colonna in cui si trova il Re
	
	/*
	 * Return black heuristic evaluation
	 */
	@Override
	public double getEvaluation() {
		// TODO Auto-generated method stub
		
		//Numero pedine bianche in gioco Range [0,1]
		double ratioWhite = this.white.size() / this.TOTWHITE;  
		
		//Numero pedine nere in gioco Range [0,1]
		double ratioBlack = this.black.size() / this.TOTBLACK;

		//Distanza dal goal
		double distanceGoal = this.eatKing(); 
		double ratioGoal;
		if(distanceGoal == 0)
		{
			ratioGoal = 10.0;  //VITTORIA   
		}
		else if(distanceGoal == 0.5)
		{
			ratioGoal = 0.8;  //Metà dell'opera
		}
		else if(distanceGoal == 1)
		{
			ratioGoal = 0.3;  //Metà dell'opera
		}
		else
		{
			ratioGoal = 0.1; //Lontani
		}
			
		//Evitare che il Bianco Vinca 
		int winWhiteRow = this.freeKingRow();
		int winWhiteColumn = this.freeKingColumn();
		double ratioWinWhiteRow, ratioWinWhiteColumn;
		
		if(winWhiteRow == 0 || distanceGoal == 0)    //Nessun Pericolo
		{
			ratioWinWhiteRow = 0.45; 
		}
		else
		{
			ratioWinWhiteRow = 0;    //Dobbiamo muoverci perchè altrimenti si perde
		}
		
		if(winWhiteColumn == 0 || distanceGoal == 0) //Nessun Pericolo
		{
			ratioWinWhiteColumn = 0.45;
		}
		else
		{
			ratioWinWhiteColumn = 0; //Dobbiamo muoverci perchè altrimenti si perde
		}
		
		
		double ratioLose;
		if(this.whiteWin())
		{
			ratioLose = -0.8;
		}
		else
		{
			ratioLose = 0;
		}
		
		/*TODO:QUI VIENE FATTO IL TUNING E IL BILANCIAMENTO DEI VALORI! */		
 		return (1 -ratioWhite)*0.2 + (ratioBlack*0.2) + ratioGoal + ratioWinWhiteColumn + ratioWinWhiteRow + ratioLose;
 		
 		//Vecchia
 		//return (ratioBlack*0.2) + ratioGoal + ratioWinWhiteColumn + ratioWinWhiteRow + ratioLose;
	}
	
	//Funzione che indica se e da quante pedine si è circondati da pedine avversarie
	private int[] nearPawn(int row, int column, String pawn)
	{
		//Restituisce 1 se la casella è occupata da una pedina avversaria, 0 se è libera
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
		
		return box;
	}
	
	//Conta il numero di pedine adiacenti
	private int countNear(int row, int column, String pawn)
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
	private boolean inThrone() 
	{
		boolean result = false;
		if(rowK == 4 && columnK == 4)
    	{
			result = true;
    	}
		return result;
	}
	
	//Verifica se il Re è adiacente al trono e restituisce inoltre su quale lato
	private int nearThrone()
	{
		int result = -1;
		if(rowK == 3 && columnK == 4)
		{
			result = up;
		}
		else if(rowK == 5 && columnK == 4)
		{
			result = down;
		}
		else if(rowK == 4 && columnK == 3)
		{
			result = left;
		}
		else if(rowK == 4 && columnK == 5)
		{
			result = right;
		}
		
		return result;
	}
	
	//Funzione che restituisce un BOOLEAN, True se la pedina è adiacente ad un campo, False altrimenti.
	private boolean nearCamp(int row, int column)
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
	private int busyRowPawn(int direction, int row, int column, String pawn) //La direzione potrà essere Left o Right
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
		return result;
	}
	
	//Funzione che verifica se nella parte di colonna della scacchiera desiderata sono presenti pedine
	private int busyColumnPawn(int direction, int row, int column, String pawn) //La direzione potrà essere Up o Down
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
						else if(state.getPawn(i, columnK).equalsPawn(other))
						{
							result = 2;
							free = false;
						}
					}
				}
			}
			return result;
		}
	
	
	
	
	
	//Questa funzione verifica la distanza dal goal, cioè la possibilità di mangiare il Re
	private double eatKing() 
	{
	
	   
	   //  Se il Re è nel TRONO servono 4 pedine per catturarlo, 
	   //  Se il Re è adiacente al TRONO servono 3 pedine per catturarlo,
	   //  Se il Re è adiacente ad un CAMPO serve 1 pedina per catturarlo,
	   //  altrimenti servono 2 pedine per catturarlo
	    
		double distanceGoal = 4; //Numero di pedine necessarie macanti a catturare il re
		int[] nearKing = this.nearPawn(rowK, columnK, "B");     //Controlla se ci sono pedine nere vicino al Re 
	    boolean kingNearCamp = this.nearCamp(rowK, columnK);    //Usa la funzione nearCamp per vedere se il Re è vicino ad un CAMPO nemico
	    int countNearPawn = this.countNear(rowK, columnK, "B"); //Conta quante sono le pedine adiacenti
      	int nearThrone = this.nearThrone();                     //Verifica se il Re è adiacente al trono
  		
      	if(this.inThrone()) //Se il Re è nel trono servono 4 pedine per vincere
    	{
      	    if(countNearPawn == 4)  //Se è circondato.. VINTO
      	    {
      	    	distanceGoal = 0;
      	    }
      	    else if(countNearPawn == 3) //Se in 3 sono vicini controlliamo il quarto spazio
      	    {
      	    	if(nearKing[left] == 0) //Se lo spazio mancante è a sinistra
      	    	{
         	    	//Si verifica se la casella vuota sia raggiungibile da una pedina nera
      	    		if((busyRowPawn(left, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK-1, "B") == 1) || (busyColumnPawn(down, rowK, columnK-1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      	    	else if(nearKing[right] == 0) //Se lo spazio mancante è a destra 
      	    	{
      	    	    //Si verifica se la casella vuota sia raggiungibile da una pedina nera
      	    		if((busyRowPawn(right, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK+1, "B") == 1) || (busyColumnPawn(down, rowK, columnK+1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      	    	else if(nearKing[up] == 0) //Se lo spazio mancante è sopra 
      	    	{
      	    	    //Si verifica se la casella vuota sia raggiungibile da una pedina nera
      	    		if((busyColumnPawn(up, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK-1, columnK, "B") == 1) || (busyRowPawn(right, rowK-1, columnK, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      	    	else if(nearKing[down] == 0)//Se lo spazio mancante è sotto
      	    	{
      	    	    //Si verifica se la casella vuota sia raggiungibile da una pedina nera
      	    		if((busyColumnPawn(down, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK+1, columnK, "B") == 1) || (busyRowPawn(right, rowK+1, columnK, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      	    	else
      	    	{
      	    		distanceGoal = 1;
      	    	}
      	    }
      	    else if(countNearPawn == 2) //Se i pedoni neri vicini sono due cambiamo solo la distanza del Goal senza verifiche
      	    {
      	    	distanceGoal = 2;
      	    }
      	    else if(countNearPawn == 1) //Se è presente un solo pedone nero vicino cambiamo solo la distanza del Goal senza verifiche
    	    {
    	    	distanceGoal = 3;
    	    }
    	}
      	else if(nearThrone >= 0) //Se il re è adiacente al trono servono tre pedine per vincere
      	{
      		if(countNearPawn == 3)
      		{
      			distanceGoal = 0; //Se è circondato da tre pedine Nere abbiamo vinto
      		}
      		else if(countNearPawn == 2)
      		{
          		if(nearThrone == up) //Se il Re si trova nella casella sopra al trono
          		{
          			if(nearKing[left] == 0) //Se la pedina mancante è a sinistra
          	    	{
          	    		//Si verifica se la casella vuota sia raggiungibile da una pedina nera
          			    if((busyRowPawn(left, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK-1, "B") == 1) || (busyColumnPawn(down, rowK, columnK-1, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		}
          	    		
          	    	}
          	    	else if(nearKing[right] == 0) //Se la pedina mancante è a destra 
          	    	{
          	    	    //Si verifica se la casella vuota sia raggiungibile da una pedina nera
          	    		if((busyRowPawn(right, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK+1, "B") == 1) || (busyColumnPawn(down, rowK, columnK+1, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		}          	    		
          	    	}
          	    	else if(nearKing[up] == 0) //Se la pedina mancante è quella sopra
          	    	{
          	    	    //Si verifica se la casella vuota sia raggiungibile da una pedina nera
          	    		if((busyColumnPawn(up, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK-1, columnK, "B") == 1) || (busyRowPawn(right, rowK-1, columnK, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		}
          	    	}
          	    	else
          	    	{
          	    		distanceGoal = 1;
          	    	}
          	    }
          		else if(nearThrone == down) //Se il Re si trova nella casella sotto al trono
          		{
          			if(nearKing[left] == 0) //Se la pedina mancante è a sinistra
          	    	{
          	    		//Si verifica se la casella vuota sia raggiungibile da una pedina nera
          				if((busyRowPawn(left, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK-1, "B") == 1) || (busyColumnPawn(down, rowK, columnK-1, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		}
          	    	}
          	    	else if(nearKing[right] == 0) //Se la pedina mancante è a destra 
          	    	{
          	    	    //Si verifica se la casella vuota sia raggiungibile da una pedina nera
          	    		if((busyRowPawn(right, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK+1, "B") == 1) || (busyColumnPawn(down, rowK, columnK+1, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		} 
          	    	}
          	    	else if(nearKing[down] == 0) //Se lo spazio mancante è sotto
          	    	{
          	    	    //Si verifica se la casella vuota sia raggiungibile da una pedina nera
          	    		if((busyColumnPawn(down, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK+1, columnK, "B") == 1) || (busyRowPawn(right, rowK+1, columnK, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		}
          	    	}
          	    	else
          	    	{
          	    		distanceGoal = 1;
          	    	}
      	    	}
          		else if(nearThrone == left) //Se il Re si trova nella casella a sinistra del trono
          		{
          			if(nearKing[left] == 0) //Se lo spazio mancante è a sinistra
          	    	{
          	    		//Si verifica se la casella vuota sia raggiungibile da una pedina nera
          				if((busyRowPawn(left, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK-1, "B") == 1) || (busyColumnPawn(down, rowK, columnK-1, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		}
          	    	}
          	    	else if(nearKing[up] == 0) //Se lo spazio mancante è sopra 
          	    	{
          	    	    //Si verifica se la casella vuota sia raggiungibile da una pedina nera
          	    		if((busyColumnPawn(up, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK-1, columnK, "B") == 1) || (busyRowPawn(right, rowK-1, columnK, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		}
          	    	}
          	    	else if(nearKing[down] == 0) //Se lo spazio mancante è sotto
          	    	{
          	    	    //Si verifica se la casella vuota sia raggiungibile da una pedina nera
          	    		if((busyColumnPawn(down, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK+1, columnK, "B") == 1) || (busyRowPawn(right, rowK+1, columnK, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		}
          	    	}
          	    	else
          	    	{
          	    		distanceGoal = 1;
          	    	}
          		}
          		else  //Se il Re è Adiacente a destra
          		{
          			if(nearKing[right] == 0) //Se lo spazio mancante è a destra 
          	    	{
          	    	    //Si verifica se la casella vuota sia raggiungibile da una pedina nera
          				if((busyRowPawn(right, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK+1, "B") == 1) || (busyColumnPawn(down, rowK, columnK+1, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		} 
          	    	}
          	    	else if(nearKing[up] == 0) //Se lo spazio mancante è sopra 
          	    	{
          	    	    //Si verifica se la casella vuota sia raggiungibile da una pedina nera
          	    		if((busyColumnPawn(up, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK-1, columnK, "B") == 1) || (busyRowPawn(right, rowK-1, columnK, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		}
          	    	}
          	    	else if(nearKing[down] == 0) //Se lo spazio mancante è sotto
          	    	{
          	    		//Si verifica se la casella vuota sia raggiungibile da una pedina nera
          	    		if((busyColumnPawn(down, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK+1, columnK, "B") == 1) || (busyRowPawn(right, rowK+1, columnK, "B") == 1))
          	    		{
          	    			distanceGoal = 0.5;
          	    		}
          	    		else
          	    		{
          	    			distanceGoal = 1;
          	    		}
          	    	}
          	    	else
          	    	{
          	    		distanceGoal = 1;
          	    	}
          		}
      		}
          	else if(countNearPawn == 1) //Se solo una pedina adiacente cambiamo solo il valore della distanza dal goal
      		{
      			distanceGoal = 2;
      		}
      		else
      		{
      			distanceGoal = 3;
      		}
      	}
      	else if(kingNearCamp == true) //Se il Re è vicino ai campi avversari  ////////////////////////////////////////////////////////////
      	{
      		if(rowK == 3 && columnK == 1)
      		{
      			if(nearKing[right] == 0 || nearKing[up] == 0)  
      	    	{
      	    		if((busyRowPawn(right, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK+1, "B") == 1) || (busyColumnPawn(down, rowK, columnK+1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    		
      	    		if((busyColumnPawn(up, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK-1, columnK, "B") == 1) || (busyRowPawn(right, rowK-1, columnK, "B") == 1))
          	    	{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      		else if(rowK == 5 && columnK == 1)
      		{
      			if(nearKing[right] == 0 || nearKing[down] == 0)  
      	    	{
      				if((busyRowPawn(right, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK+1, "B") == 1) || (busyColumnPawn(down, rowK, columnK+1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    		
      	    		if((busyColumnPawn(down, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK+1, columnK, "B") == 1) || (busyRowPawn(right, rowK+1, columnK, "B") == 1))
          	    	{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      		else if(rowK == 4 && columnK == 2)
      		{
      			if(nearKing[right] == 1)  
      	    	{
      	    		distanceGoal = 1;
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      		if(rowK == 3 && columnK == 7)
      		{
      			if(nearKing[left] == 0 || nearKing[up] == 0)  
      	    	{
      				if((busyRowPawn(left, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK-1, "B") == 1) || (busyColumnPawn(down, rowK, columnK-1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    		
      	    		if((busyColumnPawn(up, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK-1, columnK, "B") == 1) || (busyRowPawn(right, rowK-1, columnK, "B") == 1))
          	    	{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      		else if(rowK == 5 && columnK == 7)
      		{
      			if(nearKing[left] == 0 || nearKing[down] == 0)  
      	    	{
      				if((busyRowPawn(left, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK-1, "B") == 1) || (busyColumnPawn(down, rowK, columnK-1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    		
      	    		if((busyColumnPawn(down, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK+1, columnK, "B") == 1) || (busyRowPawn(right, rowK+1, columnK, "B") == 1))
          	    	{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      		else if(rowK == 4 && columnK == 6)
      		{
      			if(nearKing[left] == 1)  
      	    	{
      	    		distanceGoal = 1;
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      		if(rowK == 1 && columnK == 3)
      		{
      			if(nearKing[left] == 0 || nearKing[down] == 0)  
      	    	{
      				if((busyRowPawn(left, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK-1, "B") == 1) || (busyColumnPawn(down, rowK, columnK-1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    		
      	    		if((busyColumnPawn(down, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK+1, columnK, "B") == 1) || (busyRowPawn(right, rowK+1, columnK, "B") == 1))
          	    	{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      		else if(rowK == 1 && columnK == 5)
      		{
      			if(nearKing[right] == 0 || nearKing[down] == 0)  
      	    	{
      				if((busyRowPawn(right, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK+1, "B") == 1) || (busyColumnPawn(down, rowK, columnK+1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    		
      	    		if((busyColumnPawn(down, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK+1, columnK, "B") == 1) || (busyRowPawn(right, rowK+1, columnK, "B") == 1))
          	    	{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      		else if(rowK == 2 && columnK == 4)
      		{
      			if(nearKing[down] == 1)  
      	    	{
      	    		distanceGoal = 1;
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      		if(rowK == 7 && columnK == 3)
      		{
      			if(nearKing[left] == 0 || nearKing[up] == 0)  
      	    	{
      				if((busyRowPawn(left, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK-1, "B") == 1) || (busyColumnPawn(down, rowK, columnK-1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    		
      	    		if((busyColumnPawn(up, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK-1, columnK, "B") == 1) || (busyRowPawn(right, rowK-1, columnK, "B") == 1))
          	    	{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      		else if(rowK == 7 && columnK == 5)
      		{
      			if(nearKing[right] == 0 || nearKing[up] == 0)  
      	    	{
      				if((busyRowPawn(right, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK+1, "B") == 1) || (busyColumnPawn(down, rowK, columnK+1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    		
      	    		if((busyColumnPawn(up, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK-1, columnK, "B") == 1) || (busyRowPawn(right, rowK-1, columnK, "B") == 1))
          	    	{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      		else if(rowK == 6 && columnK == 4)
      		{
      			if(nearKing[up] == 1)  
      	    	{
      	    		distanceGoal = 1;
      	    	}
      			else
      			{
      				distanceGoal = 0;
      			}
      		}
      	}	
      	else  //In caso il Re si trovi su una parte neutra della scacchiera 
      	{
      		if(countNearPawn > 2)
      		{
      			distanceGoal = 0;
      		}
      		else if(countNearPawn == 0)
      		{
      			distanceGoal = 2;
      		}
      		else if(countNearPawn == 1)
      		{
      			if(nearKing[left] == 0 && nearKing[right] == 1) //Se lo spazio mancante è a sinistra
      	    	{
      				if((busyRowPawn(left, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK-1, "B") == 1) || (busyColumnPawn(down, rowK, columnK-1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      			else if(nearKing[right] == 0  && nearKing[left] == 1) //Se lo spazio mancante è a destra 
      	        {
      				if((busyRowPawn(right, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK+1, "B") == 1) || (busyColumnPawn(down, rowK, columnK+1, "B") == 1))
      	    		{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      	    	else if(nearKing[up] == 0 && nearKing[down] == 1) //Se lo spazio mancante è sopra 
      	    	{
      	    		if((busyColumnPawn(up, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK-1, columnK, "B") == 1) || (busyRowPawn(right, rowK-1, columnK, "B") == 1))
          	    	{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      	    	else //Se lo spazio mancante è sotto
      	    	{
      	    		if((busyColumnPawn(down, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK+1, columnK, "B") == 1) || (busyRowPawn(right, rowK+1, columnK, "B") == 1))
          	    	{
      	    			distanceGoal = 0.5;
      	    		}
      	    		else
      	    		{
      	    			distanceGoal = 1;
      	    		}
      	    	}
      		}
      		else  //Se sono due pedine adiacenti nere al Re controllare che siano su stessa riga o colonna
      		{
      			if(state.getPawn(rowK, columnK-1).equalsPawn("B") && state.getPawn(rowK, columnK+1).equalsPawn("B"))
      			{
      				distanceGoal = 0;
      			}
      			else if(state.getPawn(rowK-1, columnK).equalsPawn("B") && state.getPawn(rowK+1, columnK).equalsPawn("B"))
      			{
      				distanceGoal = 0;
      			}
      			else
      			{
      				distanceGoal = 1;
      			}
      		}
      	}
      	
      	return distanceGoal;

	}
	
	
	//Controlla se il Re si trova in una riga da cui può raggiungere un "Escape" 
	private int freeKingRow() 
	{
		int[] result = new int[2];
		result[0] = -1; //Verifica se c'è una pedina a sinistra
		result[1] = -1; //Verifica se c'è una pedina a destra
		int busyRowLeft	 = busyRowPawn(left, rowK, columnK, "B");
		int busyRowRight = busyRowPawn(right, rowK, columnK, "B");
		//1-2-6-7 Sono le righe da cui il re può vincere orrizontalmente, ovviamente se si trova in 0-8 ha già vinto!
		
		if(rowK == 2 || rowK == 6) 
		{
			if(busyRowLeft != 0)
			{
				result[0] = 1;
			}
			
			if(busyRowRight != 0)
			{
				result[1] = 1;
			}
		}
		else if(rowK == 1 || rowK == 7) 
		{
			if((columnK <= 4) && (busyRowLeft != 0))
			{
				result[0] = 1;
			}
			
			if((columnK >= 4) && (busyRowRight != 0))
			{
				result[1] = 1;
			}
		}
		else
		{
			result[0] = 0;
			result[1] = 0;
		}
		
		if(result[0] == -1 || result[1] == -1)
		{
			return 1;
		}
		else
		{
			return 0;
		}	
	}
		
	//Controlla se il Re si trova in una colonna da cui può raggiungere un "Escape" 
	private int freeKingColumn()
	{
		int[] result = new int[2];
		result[0] = -1; //Verifica se c'è una pedina sopra
		result[1] = -1; //Verifica se c'è una pedina sotto
		int busyColumnUp   = busyColumnPawn(up, rowK, columnK, "B");
		int busyColumnDown = busyColumnPawn(down, rowK, columnK, "B");
		//1-2-6-7 Sono le colone da cui il re può vincere orrizontalmente, ovviamente se si trova in 0-8 ha già vinto!
		
		if(columnK == 2 || columnK == 6) 
		{
			if(busyColumnUp != 0)
			{
				result[0] = 1;
			}
			
			if(busyColumnDown != 0)
			{
				result[1] = 1;
			}
		}
		else if(columnK == 1 || columnK == 7) 
		{
			if((rowK <= 4) && (busyColumnUp != 0))
			{
				result[0] = 1;
			}
			
			if((rowK >= 4) && (busyColumnDown != 0))
			{
				result[1] = 1;
			}
		}
		else
		{
			result[0] = 0;
			result[1] = 0;
		}
		
		if(result[0] == -1 || result[1] == -1)
		{
			return 1;
		}
		else
		{
			return 0;
		}				
	}

    //Indica se il Bianco ha vinto e restituisce un boolean 
	private boolean whiteWin()
	{
		if(rowK == 0 || rowK == 8)
		{
			return true;
		}
		else if(columnK == 0 || columnK == 8)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
