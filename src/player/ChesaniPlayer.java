package player;



import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import aima.core.search.adversarial.AlphaBetaSearch;
import domain.*;
import domain.State.Turn;
import exceptions.*;

public class ChesaniPlayer extends TablutClient {

	// Tempo massimo per fare la ricerca (forse da ridurre un pochino per non rischiare)
	private static final int TIME = 60;
	// Profondità di ricerca dell'albero
	private static final int DEPTH = 7;

	// Costruttore
	public ChesaniPlayer(String player, String name) throws UnknownHostException, IOException {
		super(player, name);
	}
	
	// Inizializzazione del Player
	public static void main(String[] args){
		
		String role = "";
		String name = "ChesaniCheCasa";
		ChesaniPlayer player = null;
		
		if (args.length < 1) {
			System.out.println("Parameters error");
			System.exit(-1);
		} else {
			role = (args[0]);
		}
		if(role.isEmpty()) {
			System.out.println("You must specify which player you are (WHITE or BLACK)");
			System.exit(-1);
		}
		System.out.println("Selected client: " + role);

		try {
			player = new ChesaniPlayer(role, name);
		} catch (IOException e) {
			System.err.println("Errore: non riesco a raggiungere il server");
			System.exit(-1);
		}
		player.run();
	}
	
	// Esecuzione del Player
	@Override
	public void run() {
		// Mi connetto e presento al server
		try {
			this.declareName();
		} catch (Exception e) {
			System.err.println("Errore: connessione col server persa");
			System.exit(-1);
		}

		// Iniziallizo la partita (inizia il bianco)
		
		State state = new StateTablut();
		state.setTurn(State.Turn.WHITE);
		System.out.println("Ashton Tablut game");

		System.out.println("You are player " + this.getPlayer().toString() + "!");
		
		
		while (true) {
			// Leggo lo stato della scacchiera dal Server
			try {
				this.read();
			} catch (ClassNotFoundException | IOException e1) {
				System.err.println("Errore: connessione col server persa");
				System.exit(-1);
			}
			
			// Inizializzo il gioco e lo stato
			
			ChesaniGameAshtonTablut game = new ChesaniGameAshtonTablut(99, 0, "garbage", "fake", "fake", TIME, DEPTH);
			
			System.out.println("Current state:");
			state = this.getCurrentState();
			System.out.println(state.toString());
			
			if (this.getPlayer().equals(Turn.WHITE)) {
				// TURNO DEL BIANCO
				if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {
					this.doTheMoveChesani(state,game);
				}
				// AVVERSARIO
				else if (state.getTurn().equals(StateTablut.Turn.BLACK) ) {
					System.out.println("Waiting for your opponent move... ");
				}
				// FINE PARTITA
				else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN) | state.getTurn().equals(StateTablut.Turn.BLACKWIN) | state.getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("END GAME");
					System.exit(0);
				}
			} else {
				// TURNO DEL NERO
				if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
					this.doTheMoveChesani(state,game);
				}
				// AVVERSARIO
				else if (state.getTurn().equals(StateTablut.Turn.WHITE)) {
					System.out.println("Waiting for your opponent move... ");
                }
				// FINE PARTITA
				else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN) | state.getTurn().equals(StateTablut.Turn.BLACKWIN) | state.getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("END GAME");
					System.exit(0);
				}

			}

		}
	}
	
	
	
	private void doTheMoveChesani(State state, ChesaniGameAshtonTablut game) {
		ChesaniIterativeDeepeningAlphaBetaSearch abS = new ChesaniIterativeDeepeningAlphaBetaSearch(game, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, TIME -3);
		
		Action action = null;
		action = abS.makeDecision(state);
		
		try {
			this.write(action);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Errore: connessione col server persa");
			System.exit(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Errore: connessione col server persa");
			System.exit(-1);
		}
	}
}







