package practise.sfedu.tetris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import practise.sfedu.tetris.MainGame;
import practise.sfedu.tetris.TetrisGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tetris SFedU Practis";
		config.width = 250;
		config.height = 566;

		new LwjglApplication(new MainGame(), config);
	}
}
