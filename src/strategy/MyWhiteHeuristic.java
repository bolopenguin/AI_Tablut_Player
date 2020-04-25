package strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import domain.State;

public class MyWhiteHeuristic extends MyHeuristic{

	public MyWhiteHeuristic(State state) {
		super(state);
		// TODO Auto-generated constructor stub
	}
	

	/*
	 * Return white heuristic evaluation
	 */
	@Override
	public double getEvaluation() {
		// TODO Auto-generated method stub
		
		//Numero pedine bianhe in gioco Range [0,1]
		double ratioWhite = this.white.size() / this.TOTWHITE;
		
		//Numero pedine nere in gioco Range [0,1]
		double ratioBlack = this.black.size() / this.TOTBLACK;

		//Distanza dall'obiettivo Range [0,1]
		double goalDistanceRatio = this.goalDistance();

		/*TODO:QUI VIENE FATTO IL TUNING E IL BILANCIAMENTO DEI VALORI! */		
		return (1 - ratioBlack) * 0.4 + ratioWhite * 0.2 + goalDistanceRatio * 0.4;
	}
	
	private double goalDistance() {
		
		double ratio = 0.0;
		
		/*indice 0: riga, indice 1: colonna*/
		if(this.king[0] == 4 && this.king[1] == 4) {
			//King nel castello
			ratio = 0.2;
		}else if( (this.king[0] >= 3 && this.king[0] <= 5) &&
					(this.king[1] >= 3 && this.king[1] <=5) ){
			//King a distanza 1 dal castello
			ratio = 0.4;
		}else if( (this.king[0] >= 2 && this.king[0] <= 6) &&
				(this.king[1] >= 2 && this.king[1] <=6) ) {
			//King a distanza 2 dal castello
			ratio = 0.6;
		}else if( (this.king[0] >= 1 && this.king[0] <= 7) &&
				(this.king[1] >= 1 && this.king[1] <=7) ) {
			//King a distanza 3 dal castello
			ratio = 0.8;
		}else{
			//Win
			ratio = Double.POSITIVE_INFINITY;
		}
		
		return ratio;
	}

}
