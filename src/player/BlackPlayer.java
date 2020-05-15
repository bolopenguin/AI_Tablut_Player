package player;

import java.io.IOException;
import java.net.UnknownHostException;

public class BlackPlayer {

	public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
		String[] array = new String[]{"black",  "60", "localhost"};
		Player.main(array);

	}

}
