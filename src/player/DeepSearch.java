package player;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import domain.*;
import domain.State.Turn;

public class DeepSearch extends IterativeDeepeningAlphaBetaSearch<State, Action, Turn> {

	public DeepSearch(Game<State, Action, Turn> game, double utilMin, double utilMax,
			int time) {
		super(game, utilMin, utilMax, time);
	}
	
	@Override
	protected double eval(State state, Turn turn) {
		super.eval(state, turn);
		return this.game.getUtility(state, turn);	
	}
}
