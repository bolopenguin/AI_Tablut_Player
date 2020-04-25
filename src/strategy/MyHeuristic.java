package strategy;

import java.util.ArrayList;
import java.util.List;

import domain.State;

public abstract class MyHeuristic {
	
	protected static final double TOTWHITE = 9.0;
	protected static final double TOTBLACK = 16.0;

	protected int[] king; 			// posizione del king
	protected List<int[]> white; 	// posizione dei bianchi
	protected List<int[]> black; 	// posizione dei neri
	protected State state;			// stato attuale del gioco
	
	protected MyHeuristic(State state) {
		this.king = new int[2];
		this.white = new ArrayList<int[]>();
		this.black = new ArrayList<int[]>();
		this.state = state;
		
		int[] pos; // posizione (esempio "z6")
		for (int i = 0; i < state.getBoard().length; i++) {
			for (int j = 0; j < state.getBoard().length; j++) {
				if (state.getPawn(i, j).equalsPawn("W") || state.getPawn(i, j).equalsPawn("K")) {
					if (state.getPawn(i, j).equalsPawn("K")) {
						this.king[0] = i;
						this.king[1] = j;
					}
					pos = new int[2];
					pos[0] = i;
					pos[1] = j;
					this.white.add(pos);
				} else if (state.getPawn(i, j).equalsPawn("B")) {
					pos = new int[2];
					pos[0] = i;
					pos[1] = j;
					this.black.add(pos);
				}
			}
		}
	}
	
	public abstract double getEvaluation();
}
