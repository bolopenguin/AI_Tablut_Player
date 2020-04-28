package player;



import java.io.IOException;
import java.net.UnknownHostException;

import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.framework.Metrics;
import domain.*;
import domain.State.Turn;

public class Player extends TablutClient {

	// Tempo massimo per fare la ricerca (forse da ridurre un pochino per non rischiare)
	private static final int TIME = 60;
	
	// Costruttore
	public Player(String player, String name) throws UnknownHostException, IOException {
		super(player, name);
	}
	
	// Inizializzazione del Player
	public static void main(String[] args){
		
		String role = "";
		String name = "ChesaniCheCasa";
		Player player = null;
		
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
			player = new Player(role, name);
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
		
		
		GameTablut game = new GameTablut(99, 0, "garbage", "fake", "fake");
		DeepSearch search = new DeepSearch(game, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, TIME -3);
		search.setLogEnabled(true);

		
		while (true) {
			// Leggo lo stato della scacchiera dal Server
			try {
				this.read();
			} catch (ClassNotFoundException | IOException e1) {
				System.err.println("Errore: connessione col server persa");
				System.exit(-1);
			}
			
			// Inizializzo il gioco e lo stato			
			//System.out.println("Current state:");
			state = this.getCurrentState();
			//System.out.println(state.toString());
			
			if (this.getPlayer().equals(Turn.WHITE)) {
				// TURNO DEL BIANCO
				if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {
					System.out.println("Choosing the best move... ");
					this.chooseTheMove(state,game, search);
				}
				// AVVERSARIO
				else if (state.getTurn().equals(StateTablut.Turn.BLACK) ) {
					System.out.println("Waiting for your opponent move... ");
				}
				// FINE PARTITA
				else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN) | state.getTurn().equals(StateTablut.Turn.BLACKWIN) | state.getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("Partita Finita");
					System.exit(0);
				}
			} else {
				// TURNO DEL NERO
				if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
					System.out.println("Choosing the best move... ");
					this.chooseTheMove(state,game, search);
				}
				// AVVERSARIO
				else if (state.getTurn().equals(StateTablut.Turn.WHITE)) {
					System.out.println("Waiting for your opponent move... ");
                }
				// FINE PARTITA
				else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN) | state.getTurn().equals(StateTablut.Turn.BLACKWIN) | state.getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("Partita Finita");
					System.exit(0);
				}

			}

		}
	}
	
	
	
	private void chooseTheMove(State state, GameTablut game, DeepSearch search) {		
		Action action = null;
		action = search.makeDecision(state);
		System.out.println(action.toString());
		printStatistics(search);
		
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
	
	private void printStatistics (AdversarialSearch<State, Action> algorithm) {
		Metrics metrics = algorithm.getMetrics();
		for (String key : metrics.keySet()) {
			String value = metrics.get(key);
			System.out.println("["+key+"]:"+value);
		}
	}
}






