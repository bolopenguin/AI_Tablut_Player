package domain;

import domain.State;
/**
 * Contains the rules of the game
 *
 */
public interface Game {

	public boolean checkMove(int columnFrom, int columnTo, int rowFrom, int rowTo, State state);

}
