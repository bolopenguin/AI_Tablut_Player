package domain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import domain.State.Turn;
import strategy.*;


public class GameTablut implements Game, aima.core.search.adversarial.Game<State, Action, State.Turn> {

	/*
	private String gameLogName;
	public  File gameLog;
	private FileHandler fh;
	public Logger loggGame;
	*/
	
	private List<String> citadels;

	
	public GameTablut(String logs_folder) {
		this(new StateTablut(), logs_folder);
	}

	private GameTablut(State state, String logs_folder) {
		super();

		/*
		Path p = Paths.get(logs_folder + File.separator + new Date().getTime() + "_gameLog.txt");
		p = p.toAbsolutePath();
		this.gameLogName = p.toString();
		File gamefile = new File(this.gameLogName);
		try {
			File f = new File(logs_folder);
			f.mkdirs();
			if (!gamefile.exists()) {
				gamefile.createNewFile();
			}
			this.gameLog = gamefile;
			fh = null;
			fh = new FileHandler(gameLogName, true);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		this.loggGame = Logger.getLogger("GameLog");
		loggGame.addHandler(this.fh);
		this.fh.setFormatter(new SimpleFormatter());
		loggGame.setLevel(Level.FINE);
		loggGame.fine("Inizio partita");
		
		*/
		
		this.citadels = new ArrayList<String>();
		
		this.citadels.add("a4");
		this.citadels.add("a5");
		this.citadels.add("a6");
		this.citadels.add("b5");
		this.citadels.add("d1");
		this.citadels.add("e1");
		this.citadels.add("f1");
		this.citadels.add("e2");
		this.citadels.add("i4");
		this.citadels.add("i5");
		this.citadels.add("i6");
		this.citadels.add("h5");
		this.citadels.add("d9");
		this.citadels.add("e9");
		this.citadels.add("f9");
		this.citadels.add("e8");
	}

	
	
	
	
	
	private State checkCaptureWhitePawnRight(State state, Action a) {
		// controllo se mangio a destra
		if (a.getColumnTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))
								&& !(a.getColumnTo() + 2 == 8 && a.getRowTo() == 4)
								&& !(a.getColumnTo() + 2 == 4 && a.getRowTo() == 0)
								&& !(a.getColumnTo() + 2 == 4 && a.getRowTo() == 8)
								&& !(a.getColumnTo() + 2 == 0 && a.getRowTo() == 4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
		}
		return state;
	}
	
	private State checkCaptureWhitePawnLeft(State state, Action a) {

		// controllo se mangio a sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
								&& !(a.getColumnTo() - 2 == 8 && a.getRowTo() == 4)
								&& !(a.getColumnTo() - 2 == 4 && a.getRowTo() == 0)
								&& !(a.getColumnTo() - 2 == 4 && a.getRowTo() == 8)
								&& !(a.getColumnTo() - 2 == 0 && a.getRowTo() == 4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
		}
		return state;
	}
	
	private State checkCaptureWhitePawnUp(State state, Action a) {
		
		// controllo se mangio sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
								&& !(a.getColumnTo() == 8 && a.getRowTo() - 2 == 4)
								&& !(a.getColumnTo() == 4 && a.getRowTo() - 2 == 0)
								&& !(a.getColumnTo() == 4 && a.getRowTo() - 2 == 8)
								&& !(a.getColumnTo() == 0 && a.getRowTo() - 2 == 4)))) {
			state.removePawn(a.getRowTo() - 1, a.getColumnTo());
		}
		return state;
	}
	
	private State checkCaptureWhitePawnDown(State state, Action a) {
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
								&& !(a.getColumnTo() == 8 && a.getRowTo() + 2 == 4)
								&& !(a.getColumnTo() == 4 && a.getRowTo() + 2 == 0)
								&& !(a.getColumnTo() == 4 && a.getRowTo() + 2 == 8)
								&& !(a.getColumnTo() == 0 && a.getRowTo() + 2 == 4)))) {
			state.removePawn(a.getRowTo() + 1, a.getColumnTo());
		}
		return state;
	}
	
	private State checkWhiteWin(State state, Action a) {
		// controllo se ho vinto
		if (a.getRowTo() == 0 || a.getRowTo() == state.getBoard().length - 1 || a.getColumnTo() == 0
				|| a.getColumnTo() == state.getBoard().length - 1) {
			if (state.getPawn(a.getRowTo(), a.getColumnTo()).equalsPawn("K")) {
				state.setTurn(State.Turn.WHITEWIN);
			}
		}
		return state;
	}
	
	
	private State checkCaptureWhite(State state, Action a) {

		this.checkCaptureWhitePawnRight(state, a);
		this.checkCaptureWhitePawnLeft(state, a);
		this.checkCaptureWhitePawnUp(state, a);
		this.checkCaptureWhitePawnDown(state, a);
		this.checkWhiteWin(state, a);

		return state;
	}

	
	
	
	
	
	
	
	private State checkCaptureBlackKingLeft(State state, Action a) {
		// ho il re sulla sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("K")) {
			// re sul trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e5")) {
				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 3).equalsPawn("B")
						&& state.getPawn(5, 4).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e4")) {
				if (state.getPawn(2, 4).equalsPawn("B") && state.getPawn(3, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("f5")) {
				if (state.getPawn(5, 5).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e6")) {
				if (state.getPawn(6, 4).equalsPawn("B") && state.getPawn(5, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("f5")) {
				if (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
		}
		return state;
	}

	private State checkCaptureBlackKingRight(State state, Action a) {
		// ho il re sulla destra
		if (a.getColumnTo() < state.getBoard().length - 2
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("K"))) {
			// re sul trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e5")) {
				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(5, 4).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e4")) {
				if (state.getPawn(2, 4).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e6")) {
				if (state.getPawn(5, 5).equalsPawn("B") && state.getPawn(6, 4).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("d5")) {
				if (state.getPawn(3, 3).equalsPawn("B") && state.getPawn(5, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("d5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e5")) {
				if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
		}
		return state;
	}

	private State checkCaptureBlackKingDown(State state, Action a) {
		// ho il re sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("K")) {
			// System.out.println("Ho il re sotto");
			// re sul trono
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(5, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e4")) {
				if (state.getPawn(3, 3).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("d5")) {
				if (state.getPawn(4, 2).equalsPawn("B") && state.getPawn(5, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("f5")) {
				if (state.getPawn(4, 6).equalsPawn("B") && state.getPawn(5, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
		}
		return state;
	}

	private State checkCaptureBlackKingUp(State state, Action a) {
		// ho il re sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("K")) {
			// re sul trono
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e6")) {
				if (state.getPawn(5, 3).equalsPawn("B") && state.getPawn(5, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("d5")) {
				if (state.getPawn(4, 2).equalsPawn("B") && state.getPawn(3, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("f5")) {
				if (state.getPawn(4, 6).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))) {
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
		}
		return state;
	}

	private State checkCaptureBlackPawnRight(State state, Action a) {
		// mangio a destra
		if (a.getColumnTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("W")) {
			if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			}
			if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			}
			if (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 2).equals("e5")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			}

		}

		return state;
	}

	private State checkCaptureBlackPawnLeft(State state, Action a) {
		// mangio a sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("W")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
						|| (state.getBox(a.getRowTo(), a.getColumnTo() - 2).equals("e5")))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
		}
		return state;
	}

	private State checkCaptureBlackPawnUp(State state, Action a) {
		// controllo se mangio sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo() - 2, a.getColumnTo()).equals("e5")))) {
			state.removePawn(a.getRowTo() - 1, a.getColumnTo());
		}
		return state;
	}

	private State checkCaptureBlackPawnDown(State state, Action a) {
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo() + 2, a.getColumnTo()).equals("e5")))) {
			state.removePawn(a.getRowTo() + 1, a.getColumnTo());
		}
		return state;
	}

	private State checkCaptureBlack(State state, Action a) {

		this.checkCaptureBlackPawnRight(state, a);
		this.checkCaptureBlackPawnLeft(state, a);
		this.checkCaptureBlackPawnUp(state, a);
		this.checkCaptureBlackPawnDown(state, a);
		this.checkCaptureBlackKingRight(state, a);
		this.checkCaptureBlackKingLeft(state, a);
		this.checkCaptureBlackKingDown(state, a);
		this.checkCaptureBlackKingUp(state, a);

		return state;
	}
	

	
	@Override
	public List<Action> getActions(State arg0) {
		
		State state = arg0;
		
		// Liste per tenere il conto dei bianchi e dei neri, e delle loro posizioni
		List<int[]> white = new ArrayList<int[]>();
		List<int[]> black = new ArrayList<int[]>(); // uguale per i neri

		// Buffer per indicare la posizione e salvarla poi nelle liste
		int[] buf;

		// Popolo le due liste
		for (int i = 0; i < state.getBoard().length; i++) {
			for (int j = 0; j < state.getBoard().length; j++) {
				if (state.getPawn(i, j).equalsPawn("W") || state.getPawn(i, j).equalsPawn("K")) {
					buf = new int[2];
					buf[0] = i;
					buf[1] = j;
					white.add(buf);
				} else if (state.getPawn(i, j).equalsPawn("B")) {
					buf = new int[2];
					buf[0] = i;
					buf[1] = j;
					black.add(buf);
				}
			}
		}

		// Sposto il contenuto delle liste all'interno di un iteratore
		List<Action> actions = new ArrayList<Action>();
		Iterator<int[]> it = null;

		switch (state.getTurn()) {
		case WHITE:
			it = white.iterator(); 
			break;
		case BLACK:
			it = black.iterator(); 
			break;
		// Il default è per quando la partita termina, si restituisce la lista di azioni vuota	
		default:
			return actions; 
		}

		
		int colonna = 0;
		int riga = 0;
		
		Action action = null;
		try {
			action = new Action("z0", "z0", state.getTurn());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String fromString = null;
		String toString = null;
		boolean ctrl;

		// Finché ci pedine nell'iteratore eseguo il ciclo
		while (it.hasNext()) {
			buf = it.next();
			colonna = buf[1];
			riga = buf[0];
			
			// Trovo tutte le azioni possibili tenendo la riga fissa e spostando sulle colonne
			for (int j = 0; j < state.getBoard().length; j++) {

				ctrl = true;
				// se vale false significa che la mossa non è valida e non va salvata
				ctrl = checkMove(colonna, j, riga, riga, state);

				// se vale true invece la mossa è valida e la salvo nella lista di azioni
				if (ctrl) {

					char colNew = (char) j;
					char colNewConverted = (char) Character.toLowerCase(colNew + 97);

					char colOld = (char) colonna;
					char colonOldConverted = (char) Character.toLowerCase(colOld + 97);

					toString = new StringBuilder().append(colNewConverted).append(riga + 1).toString();
					fromString = new StringBuilder().append(colonOldConverted).append(riga + 1).toString();

					try {
						action = new Action(fromString, toString, state.getTurn());
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					actions.add(action);
				}
			}

			// Come sopra ma questa volta tengo la colonna fissa e muovo la riga
			for (int i = 0; i < state.getBoard().length; i++) {

				ctrl = true;
				ctrl = checkMove(colonna, colonna, riga, i, state);

				if (ctrl) {

					char col = (char) colonna;
					char colConverted = (char) Character.toLowerCase(col + 97);

					toString = new StringBuilder().append(colConverted).append(i + 1).toString();
					fromString = new StringBuilder().append(colConverted).append(riga + 1).toString();
					try {
						action = new Action(fromString, toString, state.getTurn());
					} catch (IOException e) {
						e.printStackTrace();
					}
					actions.add(action);
				}
			}
		}
		return actions;
	}
	
	@Override
	public boolean checkMove(int columnFrom, int columnTo, int rowFrom, int rowTo, State state) {
		// Se la mossa cerca di andare fuori dalla tavola è innammissibile 
		if (columnFrom > state.getBoard().length - 1 || rowFrom > state.getBoard().length - 1
				|| rowTo > state.getBoard().length - 1 || columnTo > state.getBoard().length - 1 || columnFrom < 0
				|| rowFrom < 0 || rowTo < 0 || columnTo < 0)
			return false;

		// controllo che non vada sul trono
		if (state.getPawn(rowTo, columnTo).equalsPawn(State.Pawn.THRONE.toString()))
			return false;

		// controllo la casella di arrivo
		if (!state.getPawn(rowTo, columnTo).equalsPawn(State.Pawn.EMPTY.toString()))
			return false;

		if (this.citadels.contains(state.getBox(rowTo, columnTo))
				&& !this.citadels.contains(state.getBox(rowFrom, columnFrom)))
			return false;

		if (this.citadels.contains(state.getBox(rowTo, columnTo))
				&& this.citadels.contains(state.getBox(rowFrom, columnFrom))) {
			if (rowFrom == rowTo) {
				if (columnFrom - columnTo > 5 || columnFrom - columnTo < -5)
					return false;
			} else {
				if (rowFrom - rowTo > 5 || rowFrom - rowTo < -5)
					return false;
			}
		}

		// controllo se cerco di stare fermo
		if (rowFrom == rowTo && columnFrom == columnTo)
			return false;

		// controllo se sto muovendo una pedina giusta
		if (state.getTurn().equalsTurn(State.Turn.WHITE.toString())) {
			if (!state.getPawn(rowFrom, columnFrom).equalsPawn("W")
					&& !state.getPawn(rowFrom, columnFrom).equalsPawn("K"))
				return false;
		}

		if (state.getTurn().equalsTurn(State.Turn.BLACK.toString())) {
			if (!state.getPawn(rowFrom, columnFrom).equalsPawn("B"))
				return false;
		}

		// controllo di non scavalcare pedine
		if (rowFrom == rowTo) {
			if (columnFrom > columnTo) {
				for (int i = columnTo; i < columnFrom; i++) {
					if (!state.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString()))
						return false;
					if (this.citadels.contains(state.getBox(rowFrom, i))
							&& !this.citadels.contains(state.getBox(rowFrom, columnFrom)))
						return false;
				}
			} else {
				for (int i = columnFrom + 1; i <= columnTo; i++) {
					if (!state.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString()))
						return false;
					if (this.citadels.contains(state.getBox(rowFrom, i))
							&& !this.citadels.contains(state.getBox(rowFrom, columnFrom)))
						return false;
				}
			}
		} else {
			if (rowFrom > rowTo) {
				for (int i = rowTo; i < rowFrom; i++) {
					if (!state.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString()))
						return false;
					if (this.citadels.contains(state.getBox(i, columnFrom))
							&& !this.citadels.contains(state.getBox(rowFrom, columnFrom)))
						return false;
				}
			} else {
				for (int i = rowFrom + 1; i <= rowTo; i++) {
					if (!state.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString()))
						return false;
					if (this.citadels.contains(state.getBox(i, columnFrom))
							&& !this.citadels.contains(state.getBox(rowFrom, columnFrom))) {
						return false;
					}
				}
			}
		
		}
		
		// Se arrivo qua allora la mossa è valida
		return true;
	}


	@Override
	public State getInitialState() {
		return new StateTablut();
	}

	@Override
	public Turn getPlayer(State arg0) {
		return arg0.getTurn();
	}

	@Override
	public Turn[] getPlayers() {
		Turn[] result = {Turn.BLACK, Turn.WHITE}; 
		return result;
	}

	@Override
	public State getResult(State arg0, Action a) {	
		
		State state = arg0.clone();
		//Creo una nuova tavola
		State.Pawn[][] newBoard = state.getBoard();

		// Metto nella tavola la pedina mossa in base all'azione
		if (state.getTurn().equalsTurn("W")) {
			if (state.getPawn(a.getRowFrom(), a.getColumnFrom()).equalsPawn("K"))
				newBoard[a.getRowTo()][a.getColumnTo()] = State.Pawn.KING;
			else
				newBoard[a.getRowTo()][a.getColumnTo()] = State.Pawn.WHITE;
		} else {
			newBoard[a.getRowTo()][a.getColumnTo()] = State.Pawn.BLACK;
		}

		if (a.getColumnFrom() == 4 && a.getRowFrom() == 4)
			newBoard[a.getRowFrom()][a.getColumnFrom()] = State.Pawn.THRONE;
		else
			newBoard[a.getRowFrom()][a.getColumnFrom()] = State.Pawn.EMPTY;

		// Salvo la tavola aggiornata
		state.setBoard(newBoard);

		// Controllo se delle pedine vengono mangiate
		if (state.getTurn().equalsTurn("B")) {
			state = this.checkCaptureBlack(state, a);
		} else {
			state = this.checkCaptureWhite(state, a);
		}

		// Cambio il turno
		if (state.getTurn().equalsTurn(State.Turn.WHITE.toString()))
			state.setTurn(State.Turn.BLACK);
		else if (state.getTurn().equalsTurn(State.Turn.BLACK.toString()))
			state.setTurn(State.Turn.WHITE);

		return state;
	}

	@Override
	public double getUtility(State state, Turn turn) {
		if ((turn.equalsTurn("B") && state.getTurn().equalsTurn("BW"))
				|| (turn.equalsTurn("W") && state.getTurn().equalsTurn("WW")))
			return Double.POSITIVE_INFINITY;
		else if ((turn.equalsTurn("B") && state.getTurn().equalsTurn("WW"))
				|| (turn.equalsTurn("W") && state.getTurn().equalsTurn("BW")))
			return Double.NEGATIVE_INFINITY;
		
		StrategyUtils a = null;
		if (turn.equalsTurn("W"))
			a = new WhiteStrategy(state);
		else a = new BlackStrategy(state);
		return a.getEvaluation();
	}

	@Override
	public boolean isTerminal(State arg0) {
		if (arg0.getTurn().equalsTurn("WW") || arg0.getTurn().equalsTurn("BW") || arg0.getTurn().equalsTurn("D"))
			return true;
		return false;
	}



	public List<String> getCitadels() {
		return citadels;
	}

	public void setCitadels(List<String> citadels) {
		this.citadels = citadels;
	}
}
