package practise.sfedu.tetris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;

public class MainGame extends Game
{
	// главный класс, дающий возможность менять экраны
    @Override
    public void create()
    {
        setScreen((Screen) new MenuScreen(this));
    }
}
