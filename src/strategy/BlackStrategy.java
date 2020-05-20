package strategy;

import domain.State;

public class BlackStrategy extends StrategyUtils
{

	protected int rowK = this.king[0];    //Riga in cui è posizionato il Re
	protected int columnK = this.king[1]; //Colonna in cui è posizionato il Re

	public BlackStrategy(State state) 
	{
		super(state);
	}
	
	public double getEvaluation() 
	{
		
		double ratioWhite;    //Numero pedine Bianche in gioco Range[0,1]
		double ratioBlack;    //Numero pedine Nere in gioco Range[0,1]
		double ratioGoal;     //Probabilità di riuscire a mangiare il Re e vincere la partita
		double ratioWinWhite; //Serve ad impedire che il Bianco si predisponga per vincere 
		double ratioLose;     //Se si verifica il Bianco ha vinto
		double ratioPosition; //Verifica che la pedina che ha affiancato il Re sia al sicuro
		double ratioRun;      //Verifica di non venire mangiato in gabbia
		
		//Conteggio Pedine
		ratioWhite = this.white.size() / StrategyUtils.TOTWHITE;  
		ratioBlack = this.black.size() / StrategyUtils.TOTBLACK;

		//Calcolo Distanza dal goal
		double distanceGoal = this.eatKing(); 
		if(distanceGoal == 0)
		{
			ratioGoal = 10.0;  //VITTORIA   
		}
		else if(distanceGoal == 0.5)
		{
			ratioGoal = 0.8;  //Manca l'ultima mossa per vincere
		}
		else if(distanceGoal == 1)
		{
			ratioGoal = 0.3;  //Manca una pedina per vincere ma non è possibile farlo in questa mossa
		}
		else
		{
			ratioGoal = 0.1; //Manca più di una pedina per vincere
		}
			
		//Evitare che il Bianco si disponga per vincere 
		int winWhiteRow = this.freeKingRow();
		int winWhiteColumn = this.freeKingColumn();
		
		if((winWhiteColumn == 0 && winWhiteRow == 0) || distanceGoal == 0)    //Nessun Pericolo
		{
			ratioWinWhite = 0.45; 
		}
		else
		{
			ratioWinWhite = 0;    //Dobbiamo muoverci perchè altrimenti si perde
		}
		
		//Il Bianco Vince
		if(this.whiteWin())
		{
			ratioLose = -10;
		}
		else
		{
			ratioLose = 0;
		}
		
		//Evitare che il nero adiacente al Re venga mangiato
		if((!this.securityPosition()) && (distanceGoal >= 1) && (winWhiteColumn == 0 && winWhiteRow == 0))
		{
			ratioPosition = -0.5;
		}
		else
		{
			ratioPosition = 0;
		}
		
		if(this.runAway())
		{
			ratioRun = -0.3;
		}
		else
		{
			ratioRun = 0;
		}
		
		//Se non è possibile mangiare il Re allora aumentare l'importanza di mangiare i bianchi
		//Potrebbe essere controproducente perchè potrebbe pensare che conviene sempre mangiare anche se il re è libero!
		if(inThrone() && (countNear(rowK, columnK, "W") >= 3))
		{
			return (1 - ratioWhite)*0.4 + (ratioBlack*0.2) + ratioGoal + ratioWinWhite + ratioLose + ratioPosition + ratioRun;
		}
		else if((nearThrone() >= 0) && (countNear(rowK, columnK, "W") >= 2))
		{
			return (1 - ratioWhite)*0.3 + (ratioBlack*0.2) + ratioGoal + ratioWinWhite + ratioLose + ratioPosition + ratioRun;
		}
		else
		{
			return (1 - ratioWhite)*0.2 + (ratioBlack*0.2) + ratioGoal + ratioWinWhite + ratioLose + ratioPosition + ratioRun;
		}
	}
	
	//Questa funzione verifica la distanza dal goal, cioè la possibilità di mangiare il Re
	private double eatKing() 
	{
	   //  Se il Re è nel TRONO servono 4 pedine per catturarlo, 
	   //  Se il Re è adiacente al TRONO servono 3 pedine per catturarlo,
	   //  Se il Re è adiacente ad un CAMPO serve 1 pedina per catturarlo,
	   //  altrimenti servono 2 pedine per catturarlo
	    
		double distanceGoal = 4; //Numero di pedine necessarie macanti a catturare il re
		int[] nearKing = nearPawn(rowK, columnK, "B");     //Controlla se ci sono pedine nere vicino al Re 
	    boolean kingNearCamp = nearCamp(rowK, columnK);    //Usa la funzione nearCamp per vedere se il Re è vicino ad un CAMPO nemico
	    int countNearPawn = countNear(rowK, columnK, "B"); //Conta quante sono le pedine adiacenti
      	int nearThrone = nearThrone();                     //Verifica se il Re è adiacente al trono
  		
      	if(inThrone()) //Se il Re è nel trono servono 4 pedine per vincere
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
      	    		distanceGoal = 0;
      	    	}
      			else
      			{
      				if(nearKing[up] == 1 && nearKing[down] == 1)
      				{
      					distanceGoal = 0;
      				}
      				else if(nearKing[up] == 1 && nearKing[down] == 0)
      				{
      					if((busyColumnPawn(down, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK+1, columnK, "B") == 1) || (busyRowPawn(right, rowK+1, columnK, "B") == 1))
      					{
      						distanceGoal = 0.5;
      					}
      				}
      				else if(nearKing[up] == 0 && nearKing[down] == 1)
      				{
      					if((busyColumnPawn(up, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK-1, columnK, "B") == 1) || (busyRowPawn(right, rowK-1, columnK, "B") == 1))
      					{
      						distanceGoal = 0.5;
      					}
      				}
      				else
      				{
      					distanceGoal = 1;
      				}
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
      	    		distanceGoal = 0;
      	    	}
      			else
      			{
      				if(nearKing[up] == 1 && nearKing[down] == 1)
      				{
      					distanceGoal = 0;
      				}
      				else if(nearKing[up] == 1 && nearKing[down] == 0)
      				{
      					if((busyColumnPawn(down, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK+1, columnK, "B") == 1) || (busyRowPawn(right, rowK+1, columnK, "B") == 1))
      					{
      						distanceGoal = 0.5;
      					}
      				}
      				else if(nearKing[up] == 0 && nearKing[down] == 1)
      				{
      					if((busyColumnPawn(up, rowK, columnK, "B") == 1) || (busyRowPawn(left, rowK-1, columnK, "B") == 1) || (busyRowPawn(right, rowK-1, columnK, "B") == 1))
      					{
      						distanceGoal = 0.5;
      					}
      				}
      				else
      				{
      					distanceGoal = 1;
      				}
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
      	    		distanceGoal = 0;
      	    	}
      			else
      			{
      				if(nearKing[left] == 1 && nearKing[right] == 1)
      				{
      					distanceGoal = 0;
      				}
      				else if(nearKing[left] == 1 && nearKing[right] == 0)
      				{
      					if((busyRowPawn(left, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK-1, "B") == 1) || (busyColumnPawn(down, rowK, columnK-1, "B") == 1))
      					{
      						distanceGoal = 0.5;
      					}
      				}
      				else if(nearKing[left] == 0 && nearKing[right] == 1)
      				{
      					if((busyRowPawn(right, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK+1, "B") == 1) || (busyColumnPawn(down, rowK, columnK+1, "B") == 1))
      					{
      						distanceGoal = 0.5;
      					}
      				}
      				else
      				{
      					distanceGoal = 1;
      				}
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
      	    		distanceGoal = 0;
      	    	}
      			else
      			{
      				if(nearKing[left] == 1 && nearKing[right] == 1)
      				{
      					distanceGoal = 0;
      				}
      				else if(nearKing[left] == 1 && nearKing[right] == 0)
      				{
      					if((busyRowPawn(left, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK-1, "B") == 1) || (busyColumnPawn(down, rowK, columnK-1, "B") == 1))
      					{
      						distanceGoal = 0.5;
      					}
      				}
      				else if(nearKing[left] == 0 && nearKing[right] == 1)
      				{
      					if((busyRowPawn(right, rowK, columnK, "B") == 1) || (busyColumnPawn(up, rowK, columnK+1, "B") == 1) || (busyColumnPawn(down, rowK, columnK+1, "B") == 1))
      					{
      						distanceGoal = 0.5;
      					}
      				}
      				else
      				{
      					distanceGoal = 1;
      				}
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
		//1-2-6-7 Sono le colone da cui il re può vincere orrizzontalmente, ovviamente se si trova in 0-8 ha già vinto!
		
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
	
	//Verifica che i pedoni neri adiacenti al Re siano al sicuro
	private boolean securityPosition()
	{
		boolean result = true;
		int [] nearKing = nearPawn(rowK, columnK, "B");
		
		if(nearKing[up] == 1)
		{
			if(rowK-1 >= 0) 
			{
				if(state.getPawn(rowK-1, columnK).equalsPawn("W"))
				{
					result = false;
				}
				else if(state.getPawn(rowK-1, columnK).equalsPawn("B"))
				{
					result = true;
				}
				
				if((!state.getPawn(rowK-1, columnK).equalsPawn("B")) && eatUp(rowK-1, columnK, "W"))
				{
					result = false;
				}
			}
		}
		
		if(nearKing[down] == 1)
		{
			if(rowK+1 <= 8) 
			{
				if(state.getPawn(rowK+1, columnK).equalsPawn("W"))
				{
					result = false;
				}
				else if(state.getPawn(rowK+1, columnK).equalsPawn("B"))
				{
					result = true;
				}
				
				if((!state.getPawn(rowK+1, columnK).equalsPawn("B")) && eatDown(rowK+1, columnK, "W"))
				{
					result = false;
				}
			}
		}
		
		
		if(nearKing[left] == 1)
		{
			if(columnK-1 >= 0) 
			{
				if(state.getPawn(rowK, columnK-1).equalsPawn("W"))
				{
					result = false;
				}
				else if(state.getPawn(rowK, columnK-1).equalsPawn("B"))
				{
					result = true;
				}
				if((!state.getPawn(rowK, columnK-1).equalsPawn("B")) && eatLeft(rowK, columnK-1, "W"))
				{
					result = false;
				}
			}
		}
		
		
		if(nearKing[right] == 1)
		{
			if(columnK+1 <= 8) 
			{
				if(state.getPawn(rowK, columnK+1).equalsPawn("W"))
				{
					result = false;
				}
				else if(state.getPawn(rowK, columnK+1).equalsPawn("B"))
				{
					result = true;
				}

				if((!state.getPawn(rowK, columnK+1).equalsPawn("B")) && eatRight(rowK, columnK+1, "W"))
				{
					result = false;
				}
			}
		}
		
		return result;
	}
	
	
	private boolean runAway()
	{
		boolean result = false;
		int[] box = new int[4]; 
		boolean ver = true;
		
		if(state.getPawn(1, 4).equalsPawn("B"))
		{
			box = nearPawn(1,4,"W");
			if(countNear(1,4, "W") > 2)
			{
				result = true;
				ver = false;
			}
			else if(((box[left] == 1) && (box[right] == 1)) && ver == true)
			{
				if(eatDown(1,4, "W"))
				{
					result = true;
					ver = false;
				}
			}
			else if(((box[down] == 1) && (box[right] == 1)) && ver == true)
			{
				if(eatLeft(1,4, "W"))
				{
					result = true;
					ver = false;
				}
			}
			else if(((box[down] == 1) && (box[left] == 1)) && ver == true)
			{
				if(eatRight(1,4, "W"))
				{
					result = true;
					ver = false;
				}
			}
		}
		
		ver = true;
		if(state.getPawn(7, 4).equalsPawn("B"))
		{
			box = nearPawn(7,4,"W");
			if(countNear(7,4, "W") > 2)
			{
				result = true;
				ver = false;
			}
			else if(((box[left] == 1) && (box[right] == 1)) && ver == true)
			{
				if(eatUp(7,4, "W"))
				{
					result = true;
					ver = false;
				}
			}
			else if(((box[down] == 1) && (box[right] == 1)) && ver == true)
			{
				if(eatLeft(7,4, "W"))
				{
					result = true;
					ver = false;
				}
			}
			else if(((box[down] == 1) && (box[left] == 1)) && ver == true)
			{
				if(eatRight(7,4, "W"))
				{
					result = true;
					ver = false;
				}
			}
		}
		
		ver = true;
		if(state.getPawn(4, 1).equalsPawn("B"))
		{
			box = nearPawn(4,1,"W");
			if(countNear(4,1, "W") > 2)
			{
				result = true;
				ver = false;
			}
			else if(((box[up] == 1) && (box[down] == 1)) && ver == true)
			{
				if(eatRight(4,1, "W"))
				{
					result = true;
					ver = false;
				}
			}
			else if(((box[up] == 1) && (box[right] == 1)) && ver == true)
			{
				if(eatDown(4,1, "W"))
				{
					result = true;
					ver = false;
				}
			}
			else if(((box[down] == 1) && (box[right] == 1)) && ver == true)
			{
				if(eatUp(4,1, "W"))
				{
					result = true;
					ver = false;
				}
			}
		}
		
		ver = true;
		if(state.getPawn(4, 7).equalsPawn("B"))
		{
			box = nearPawn(4,7,"W");
			if(countNear(4,7, "W") > 2)
			{
				result = true;
				ver = false;
			}
			else if(((box[up] == 1) && (box[down] == 1)) && ver == true)
			{
				if(eatLeft(4,7, "W"))
				{
					result = true;
					ver = false;
				}
			}
			else if(((box[up] == 1) && (box[left] == 1)) && ver == true)
			{
				if(eatDown(4,7, "W"))
				{
					result = true;
					ver = false;
				}
			}
			else if(((box[down] == 1) && (box[left] == 1)) && ver == true)
			{
				if(eatUp(4,7, "W"))
				{
					result = true;
					ver = false;
				}
			}
		}
		
		return result;
	}
}
