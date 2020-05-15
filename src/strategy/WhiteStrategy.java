package strategy;

import domain.State;

public class WhiteStrategy extends StrategyUtils{

	public WhiteStrategy(State state) {
		super(state);
		// TODO Auto-generated constructor stub
	}
	

	/*
	 * Return white heuristic evaluation
	 */
	@Override
	public double getEvaluation() {
		// TODO Auto-generated method stub
		
		double ratioWhite;
		double ratioBlack;
		double ratioGoalDistance;
		double ratioSaveKing;
		double ratioStrategy;
		
		double ratioWhiteWeight; 
		double ratioBlackWeight; 
		double ratioGoalDistanceWeight; 
		double ratioSaveKingWeight;
		double ratioStrategyWeight;
		
		//Numero pedine bianhe in gioco Range [0,1]
		ratioWhite = this.white.size() / TOTWHITE;
		
		//Numero pedine nere in gioco Range [0,1]
		ratioBlack = this.black.size() / TOTBLACK;

		//Distanza dall'obiettivo Range [0,1]
		int kingDistance = this.kingDistance();

		//Ratio in funzione della distanza Range[0,1]
		switch(kingDistance) {
			case 0: ratioGoalDistance = 0.2; break; 				//King nel castello
			case 1: ratioGoalDistance = 0.1; break; 				//King a distanza 1
			case 2: ratioGoalDistance = 0.4; break; 				//King a distanza 2
			case 3: ratioGoalDistance = 0.6; break; 				//King a distanza 3
			case 4: ratioGoalDistance = Double.POSITIVE_INFINITY; 	//Vinto
			default: ratioGoalDistance = 0.0; break;
		}
		
		//Strategia in funzione della posizione del King Range[0,1]
		switch(kingDistance) {
			case 0: ratioStrategy = this.castleStrategy(); break; 	//King nel castello
			case 1: ratioStrategy = this.castleStrategy(); break; 	//King a distanza 1
			case 2: ratioStrategy = this.countFreeWays()/4; break; 	//King a distanza 2
			case 3: ratioStrategy = this.countFreeWays()/4; break; 	//King a distanza 3
			case 4: ratioStrategy = Double.POSITIVE_INFINITY; 		//Vinto
			default: ratioStrategy = 0.0; break;
		}
		
		
		//Numero pedine nere che circondano il king Range[0,1]
		//double ratioKingSurrounded = this.getKingSurrounded();
		
		//Possibilità di liberare il King
		ratioSaveKing = this.getSaveKing();

		//double ratio = this.black.size() - this.white.size();
		//if(ratio < 0) ratio = ratio * (-1);
		
		int nBlackNear = this.countNear(this.king[0], this.king[1], "B");
		if(nBlackNear >= 1) {
			//DEFENSIVE MODE: priorità difendere il King
			ratioWhiteWeight = 0.2;
			ratioBlackWeight = 0.4;
			ratioGoalDistanceWeight = 0.1;
			ratioSaveKingWeight = 0.1;
			ratioStrategyWeight = 0.2;
		}else {
			//OFFENSIVE MODE: priorità arrivare al Goal
			ratioWhiteWeight = 0.2;
			ratioBlackWeight = 0.3;
			ratioGoalDistanceWeight = 0.2;
			ratioSaveKingWeight = 0.0;
			ratioStrategyWeight = 0.3;
		}

		return (1 - ratioBlack) * ratioBlackWeight + 
				ratioWhite * ratioWhiteWeight + 
				ratioGoalDistance * ratioGoalDistanceWeight + 
				ratioSaveKing * ratioSaveKingWeight +
				ratioStrategy * ratioStrategyWeight;
		
	}
	
	//Strategia che prevede di occupare le diagonali
	private double castleStrategy() {
		double ratio = 0.0;
		int n = 0;
		int tot = 12;
		//Posizioni diagonali: 1-1, 2-2, 3-3, 5-5, 6-6, 7-7
		//1-7, 2-6, 3-5, 5-3, 6-2, 7-1
		if(this.state.getPawn(1, 1).equalsPawn("W")) n+=1;
		if(this.state.getPawn(2, 2).equalsPawn("W")) n+=1;
		if(this.state.getPawn(3, 3).equalsPawn("W")) n+=1;
		if(this.state.getPawn(5, 5).equalsPawn("W")) n+=1;
		if(this.state.getPawn(6, 6).equalsPawn("W")) n+=1;
		if(this.state.getPawn(7, 7).equalsPawn("W")) n+=1;
		if(this.state.getPawn(1, 7).equalsPawn("W")) n+=1;
		if(this.state.getPawn(2, 6).equalsPawn("W")) n+=1;
		if(this.state.getPawn(3, 5).equalsPawn("W")) n+=1;
		if(this.state.getPawn(5, 3).equalsPawn("W")) n+=1;
		if(this.state.getPawn(6, 2).equalsPawn("W")) n+=1;
		if(this.state.getPawn(7, 1).equalsPawn("W")) n+=1;
		
		ratio = n/tot;
		
		return ratio;
	}
	
	//Cerco di salvare il King mettendo un pedone bianco in posizione
	//da poter mangiare il pedone nero che lo blocca
	private double getSaveKing() {
		double ratio = 0.0;
		
		int distanceUp = this.getDistanceFromEnemyUp();
		int distanceRight = this.getDistanceFromEnemyRight();
		int distanceLeft = this.getDistanceFromEnemyLeft();
		int distanceBottom = this.getDistanceFromEnemyBottom();
		
		int minUp = 10;
		int minLeft = 10;
		int minRight = 10;
		int minBottom = 10;
		int distance = 10;
		
		if(distanceUp == 1) {
			//Un pedone in alto cerca di bloccare il King
			distanceUp = this.king[0] - distanceUp;
			//From 0 to distanceUp
			for(int i = 0; i< distanceUp; i++) {
				for(int j = 0; j < 9; j++) {
					if(this.state.getPawn(i, j).equalsPawn("W")) {
						distance = distanceUp - i;
						if(distance > minUp) minUp = distance;
					}
				}
			}
			if(minUp == 1) ratio = 0.5;
			if(minUp > 1) ratio = 0.2;
		}else if(distanceRight == 1) {
			//Un pedone a destra cerca di bloccare il King
			distanceRight = this.king[1] + distanceRight;
			//From distanceRight + 1 to 9
			for(int j = distanceRight+1; j< 9; j++) {
				for(int i = 0; i < 9; i ++) {
					if(this.state.getPawn(i, j).equalsPawn("W")) {
						distance = j - distanceRight;
						if(distance < minRight) minRight = distance;
					}
				}
				
			}
			if(minRight == 1) ratio = 0.5;
			if(minRight > 1) ratio = 0.2;
		}else if(distanceLeft == 1) {
			//Un pedone a sinistra cerca di bloccare il King
			distanceLeft = this.king[1] - distanceLeft;
			//From 0 to distanceLeft
			for(int j = 0; j< distanceLeft; j++) {
				for(int i = 0; i<9; i++) {
					if(this.state.getPawn(i, j).equalsPawn("W")) {
						distance = distanceLeft - j;
						if(distance < minLeft) minLeft = distance;
					}
				}
				
			}
			if(minLeft == 1) ratio = 0.5;
			if(minLeft > 1) ratio = 0.2;
		}else if(distanceBottom == 1) {
			//Un pedone in basso cerca di bloccare il King
			distanceBottom = this.king[0] + distanceBottom;
			//From distanceBottom+1 to 9
			for(int i = distanceBottom+1; i< 9; i++) {
				for(int j = 0; j < 9; j++) {
					if(this.state.getPawn(i, j).equalsPawn("W")) {
						distance = i - distanceBottom;
						if(distance < minBottom) minBottom = distance;
					}
				}
			}
			if(minBottom == 1) ratio = 0.5;
			if(minBottom > 1) ratio = 0.2;
		}
		
		return ratio;
	}
	
	
	/*Controlla la distanza delle pedine nere sopra al King
	 * 0     -> Non c'è nessuna pedina nera
	 * 1     -> Pedina nera circonda il King
	 * > 1   -> Pedina nera a distanza >1 dal King
	 */
	private int getDistanceFromEnemyUp() {
		int distance = 0;
		boolean found = false;
		int kingX = this.king[0];
		int kingY = this.king[1];
		
		//Controllo se sopra al King c'è un accampamento
		if((kingX == 1 && kingY == 3) ||
			(kingX == 2 && kingY == 4) ||
			(kingX == 1 && kingY == 5) ||
			(kingX == 5 && kingY == 1)||
			(kingX == 5 && kingY == 7)) {
			//King vicino ad un accampamento
			distance = 1;
		}else {
			for(int i = kingX-1; i >= 0 && !found; i--) {
				if(this.state.getPawn(i, kingY).equalsPawn("B")) {
					found = true;
					distance = kingX - i;
				}else if(this.state.getPawn(i, kingY).equalsPawn("W")) {
					found = true;
					distance = 0;
				}
			}
			
			
		}
		return distance;
	}
	
	/*Controlla la distanza delle pedine nere a destra del King
	 * 0   -> Non c'è nessuna pedina nera
	 * 1   -> Pedina nera circonda il King
	 * > 1 -> Pedina nera a distanza >1 dal King
	 */
	private int getDistanceFromEnemyRight() {
		int distance = 0;
		boolean found = false;
		int kingX = this.king[0];
		int kingY = this.king[1];
		
		//Controllo se a destra del King c'è un accampamento
		if((kingX == 1 && kingY == 3) ||
			(kingX == 3 && kingY == 7) ||
			(kingX == 4 && kingY == 6) ||
			(kingX == 5 && kingY == 7)||
			(kingX == 7 && kingY == 3)) {
			//King vicino ad un accampamento
			distance = 1;
		}else {
			for(int j = kingY+1; j < 9 && !found; j++) {
				if(this.state.getPawn(kingX, j).equalsPawn("B")) {
					found = true;
					distance = j - kingY;
				}else if(this.state.getPawn(kingX, j).equalsPawn("W")) {
					found = true;
					distance = 0;
				}
			}
		}
		return distance;
	}
	
	
	/*Controlla la distanza delle pedine nere a sinistra del King
	 * 0   -> Non c'è nessuna pedina nera
	 * 1   -> Pedina nera circonda il King
	 * > 1 -> Pedina nera a distanza >1 dal King
	 */	
	private int getDistanceFromEnemyLeft() {
		int distance = 0;
		boolean found = false;
		int kingX = this.king[0];
		int kingY = this.king[1];
		
		//Controllo se a sinistra del King c'è un accampamento
		if((kingX == 1 && kingY == 5) ||
			(kingX == 3 && kingY == 1) ||
			(kingX == 4 && kingY == 2) ||
			(kingX == 5 && kingY == 1)||
			(kingX == 7 && kingY == 5)) {
			//King vicino ad un accampamento
			distance = 1;
		}else {
			for(int j = kingY-1; j >= 0 && !found; j--) {
				if(this.state.getPawn(kingX, j).equalsPawn("B")) {
					found = true;
					distance = kingY - j;
				}else if(this.state.getPawn(kingX, j).equalsPawn("W")) {
					found = true;
					distance = 0;
				}
			}
		}
		return distance;
	}
	
	/*Controlla la distanza delle pedine nere sotto al King
	 * 0   -> Non c'è nessuna pedina nera
	 * 1   -> Pedina nera circonda il King
	 * > 1 -> Pedina nera a distanza >1 dal King
	 */	
	private int getDistanceFromEnemyBottom() {
		int distance = 0;
		boolean found = false;
		int kingX = this.king[0];
		int kingY = this.king[1];
		
		//Controllo se sotto al King c'è un accampamento
		if((kingX == 3 && kingY == 1) ||
			(kingX == 3 && kingY == 7) ||
			(kingX == 7 && kingY == 3) ||
			(kingX == 6 && kingY == 4)||
			(kingX == 7 && kingY == 5)) {
			//King vicino ad un accampamento
			distance = 1;
		}else {
			for(int i = kingX+1; i < 9 && !found; i++) {
				if(this.state.getPawn(i, kingY).equalsPawn("B")) {
					found = true;
					distance = i - kingX;
				}else if(this.state.getPawn(i, kingY).equalsPawn("W")) {
					found = true;
					distance = 0;
				}
			}
		}
		return distance;
	}
	
	/*
	private double goalDistance() {
		double ratio = 0.0;
		
		int kingDistance = this.kingDistance();
		int nBlackNear = this.countNear(this.king[0], this.king[1], "B");
		int nWhiteNear = this.countNear(this.king[0], this.king[1], "W");
		int totNear = nBlackNear + nWhiteNear;
		
		int distanceEnemyTop = this.getDistanceFromEnemyUp();
		int distanceEnemyBottom = this.getDistanceFromEnemyBottom();
		int distanceEnemyLeft = this.getDistanceFromEnemyLeft();
		int distanceEnemyRight = this.getDistanceFromEnemyRight();
		
		int nearCastle = this.nearCastle();	
		int countFreeWays = this.countFreeWays();
		
		
		switch(kingDistance) {
			case 0:
				//King nel castello
				if(totNear == 4) {
					//King circondato da 4 pedoni
					switch(nBlackNear) {
						case 4:
							//Perso
							ratio = Double.NEGATIVE_INFINITY;
							break;
						case 3:
							//3B - 1W
							ratio = 0.0;
							break;
						case 2:
							//2B - 2W
							ratio = 0.1;
							break;
						case 1:
							//1B - 1W
							ratio = 0.2;
						case 0:
							//0B - 4W
							ratio = 0.3;
							break;
					}
				}else if(totNear == 3) {
					//King circondato da 3 pedoni
					switch(nBlackNear) {
						case 3:
							//3B - 0W
							ratio = 0.0;
							break;
						case 2:
							//2B - 1W
							ratio = 0.2;
							break;
						case 1:
							//1B - 2W
							ratio = 0.3;
						case 0:
							//0B - 3W
							ratio = 0.4;
							break;
					}
				}else if(totNear == 2) {
					//King circondato da 2 pedoni
					switch(nBlackNear) {
						case 2:
							//2B - 0W
							ratio = 0.2;
							break;
						case 1:
							//1B - 1W
							ratio = 0.3;
						case 0:
							//0B - 2W
							ratio = 0.4;
							break;
					}
				}else if(totNear == 1) {
					//King circondato da 1 pedone
					switch(nBlackNear) {
						case 1:
							//1B - 0W
							ratio = 0.3;
							break;
						case 0:
							//0B - 1W
							ratio = 0.5;
							break;
					}
				}else {
					//King circondato da 0 pedoni
					ratio = 0.5;
				}
				break;
			case 1:
				//King a distanza 1
				
				if(nearCastle != -1) {
					//King vicino al castello
					if(nBlackNear == 3) {
						//King circonado da 3 pedoni neri = Perso
						ratio = Double.NEGATIVE_INFINITY;
					}else {
						if(countFreeWays == 3) ratio = 0.4;
						if(countFreeWays == 2) ratio = 0.3;
						if(countFreeWays == 1) ratio = 0.2;
						if(countFreeWays == 0) ratio = 0.1;
					}
					
				} else {
					
					//King circondato da due pedoni neri = Perso
					if((distanceEnemyTop == 1 && distanceEnemyBottom == 1) ||
						(distanceEnemyLeft == 1 && distanceEnemyRight ==1)) ratio = Double.NEGATIVE_INFINITY;
					else {
						
						if(countFreeWays == 4) ratio = 0.5;
						if(countFreeWays == 3) ratio = 0.4;
						if(countFreeWays == 2) ratio = 0.3;
						if(countFreeWays == 1) ratio = 0.2;
						if(countFreeWays == 0) ratio = 0.1;
						
					}	
					
				}				
				
				break;
			case 2:
				//King a distanza 2
				ratio = 0.3 + this.getGoalWays();
				break;
			case 3:
				//King a distanza 3
				ratio = 0.3 + this.getGoalWays();
			case 4:
				//Win
				ratio = Double.POSITIVE_INFINITY;
		}
		
		return ratio;
	}
	*/
	
	private int countFreeWays() {
		int count = 0;
		int distanceEnemyTop = this.getDistanceFromEnemyUp();
		int distanceEnemyBottom = this.getDistanceFromEnemyBottom();
		int distanceEnemyLeft = this.getDistanceFromEnemyLeft();
		int distanceEnemyRight = this.getDistanceFromEnemyRight();
		
		if(distanceEnemyTop == 0) count ++;
		if(distanceEnemyBottom == 0) count ++;
		if(distanceEnemyLeft == 0) count ++;
		if(distanceEnemyRight == 0) count ++;
		
		return count;
	}
	
	
	//Restituisce distanza del King dal castello
	private int kingDistance() {
		
		int distance = 0;
		
		/*indice 0: riga, indice 1: colonna*/
		if(this.king[0] == 4 && this.king[1] == 4) {
			//King nel castello
			distance = 0;
		}else if( (this.king[0] >= 3 && this.king[0] <= 5) &&
					(this.king[1] >= 3 && this.king[1] <=5) ){
			//King a distanza 1 dal castello
			distance = 1;
		}else if( (this.king[0] >= 2 && this.king[0] <= 6) &&
				(this.king[1] >= 2 && this.king[1] <=6) ) {
			//King a distanza 2 dal castello
			distance = 2;
		}else if( (this.king[0] >= 1 && this.king[0] <= 7) &&
				(this.king[1] >= 1 && this.king[1] <=7) ) {
			//King a distanza 3 dal castello
			distance = 3;
		}else{
			//Win
			distance = 4;
		}
		
		return distance;
	}
	
	//Verifica se il Re è adiacente al trono e restituisce inoltre su quale lato
	/*
	private int nearCastle() {
		int result = -1;
		if(this.king[0] == 3 && this.king[1] == 4) result = up;
		else if(this.king[0] == 5 && this.king[1] == 4) result = down;
		else if(this.king[0] == 4 && this.king[1] == 3) result = left;
		else if(this.king[0] == 4 && this.king[1] == 5) result = right;
		
		return result;
	}
	*/
	
	//Aumenta la valutazione a pedoni negli angoli
	//Non usato
	/*
	private double getCornersRatio() {
		double ratio = 0.0;
		if(this.state.getPawn(0, 0).equals("W")) {
			ratio += 0.2;
		}
		if(this.state.getPawn(8, 8).equals("W")) {
			ratio += 0.2;
		}
		if(this.state.getPawn(0, 8).equals("W")) {
			ratio += 0.2;
		}
		if(this.state.getPawn(8, 0).equals("W")) {
			ratio += 0.2;
		}
		return ratio;
	}
	*/
	
	//Aggiunge 0.2 al ratio della goalDistance se il King ha una strada per vincere
	//Non usato
	/*
	private double getGoalWays() {
		int numberOfWays = 4;
		boolean goalUp = true;
		boolean goalDown = true;
		boolean goalLeft = true;
		boolean goalRight = true;
		
		if(this.king[0] == 3 || this.king[0] == 4 || this.king[0] == 5) {
			goalLeft = false;
			goalRight = false;
			numberOfWays -= 2;
		}else {
			for(int j = this.king[1]; j < 9 && goalRight; j++) {
				//Goal libero a destra
				if(!this.state.getPawn(this.king[0], j).equalsPawn("O")) {
					goalRight = false;
					numberOfWays -= 1;
				}
				
			}
			for(int j = 0; j < this.king[1] && goalLeft; j++) {
				//Goal libero a sinistra
				if(!this.state.getPawn(this.king[0], j).equalsPawn("O")) {
					goalLeft = false;
					numberOfWays -= 1;
				}
			}
		}
		
		if(this.king[1] == 3 || this.king[1] == 4 || this.king[1] == 5) {
			goalUp = false;
			goalDown = false;
			numberOfWays -= 2;
		}else {
			for(int i = 0; i < this.king[0] && goalUp; i++) {
				//Goal libero in alto
				if(!this.state.getPawn(i, this.king[1]).equalsPawn("O")) {
					goalUp = false;
					numberOfWays -= 1;
				}
			}
			
			for(int i = this.king[0]; i < 9 && goalDown; i++) {
				//Goal libero in basso
				if(!this.state.getPawn(i, this.king[1]).equalsPawn("O")) {
					goalDown = false;
					numberOfWays -= 1;
				}
			}
			
		}
		
		if (numberOfWays == 0) return 0.0;
		if (numberOfWays > 1) return Double.POSITIVE_INFINITY;
		else return 0.5;
		
	}
	*/
	
	//Restituisce ratio di pedoni neri per zona Range[0,1]
	//Non usato
	/*
	private double getBlack(int startX, int endX, int startY, int endY) {
		double nBlack = 0.0;
		double totQuartile = 9.0;

		for(int i=0; i<this.black.size(); i++) {
			if(this.black.get(i)[0] >= startX && this.black.get(i)[0] <= endX &&
				this.black.get(i)[1] >= startY && this.black.get(i)[1] <= endY) {
				nBlack += 1.0;
			}
		}
		
		return nBlack / totQuartile;
	}
	*/
	
	//Probabilità di trovare una via per il goal nel quadrante in cui si trova il King
	//Non usato
	/*
	private double getQuartileRatio() {
		double ratio = 0.0;
		
		if(this.king[0] <= 3 && this.king[1] <= 3) {
			//Re nel quadrante in alto a sinistra della scacchiera
			ratio = 1.0 - this.getBlack(1, 3, 1, 3);
		}else if(this.king[0] > 4 && this.king[1] <= 3 ){
			//Re nel quadrante in basso a sinistra della scacchiera
			ratio = 1.0 - this.getBlack(5, 7, 1, 3);
		}else if(this.king[0] <= 3 && this.king[1] > 4 ){
			//Re nel quadrante in alto a destra della scacchiera
			ratio = 1.0 - this.getBlack(1, 3, 5, 7);
		}else if(this.king[0] > 4 && this.king[1] > 4 ){
			//Re nel quadrante in basso a destra della scacchiera
			ratio = 1.0 - this.getBlack(5, 7, 5, 7);
		}else if( (this.king[0] == 2 || this.king[0] == 3)  && this.king[1] == 4){
			//Re nella metà alta della scacchiera
			ratio = ((1.0 - this.getBlack(1, 3, 1, 3)) + (1.0 - this.getBlack(1, 3, 5, 7))) /2;
		}else if( (this.king[0] == 5 || this.king[0] == 6)  && this.king[1] == 4){
			//Re nella metà bassa della scacchiera
			ratio = ((1.0 - this.getBlack(5, 7, 1, 3)) + (1.0 - this.getBlack(5, 7, 5, 7))) /2;
		}else if(this.king[0] == 4 && (this.king[1] == 2 || this.king[1] == 3 )){
			//Re nella metà sinistra della scacchiera
			ratio = ((1.0 - this.getBlack(5, 7, 1, 3)) + (1.0 - this.getBlack(1, 3, 1, 3))) /2;
		}else if(this.king[0] == 4 && (this.king[1] == 5 || this.king[1] == 6 )){
			//Re nella metà destra della scacchiera
			ratio = ((1.0 - this.getBlack(1, 3, 5, 7)) + (1.0 - this.getBlack(5, 7, 5, 7))) /2;
		}
		
		return ratio;
	}
	*/
}
