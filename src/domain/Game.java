package domain;

import domain.State;
/**
 * Contains the rules of the game
 *
 */
public interface Game {

	public int checkMove(int columnFrom, int columnTo, int rowFrom, int rowTo, int ctrl, State state);


}
