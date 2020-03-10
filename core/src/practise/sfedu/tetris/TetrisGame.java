package practise.sfedu.tetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static practise.sfedu.tetris.MenuScreen.BUTTON_RESIZE_FACTOR;


public class TetrisGame extends ApplicationAdapter implements Screen
{
	protected final MainGame screen;
	SpriteBatch batch;
	SpriteBatch bgBatch;
	OrthographicCamera camera;
	Texture backgroundTexture;
	static Music theme_music;
	Background back;
	BitmapFont score;
	static TextureRegion[] blockTextures = new TextureRegion[Config.FiguresCount];
	static public boolean[][] field;
	static Stage stage;
	static Stage bgStage;
    static FiguresEnum[] arrayFigures = new FiguresEnum[19]; // отсюда вызывать спавн новой фигуры
	static int[] rowFilling;
	static List<Integer> RowsToDelete = new ArrayList<Integer>();
	static boolean GameOver;
	static int currentScore = 0;
	static int lastCurrentScore = 0;
	static long lastDropTime, lastRotateTime, lastMoveTime, lastFastDrop;
	Vector3 temp = new Vector3(); // временный вектор для "захвата" входных координат

	FileHandle file = Gdx.files.local("highscore.txt");
	Texture       backTexture;
	Sprite         backSprite;


	public TetrisGame(MainGame menuScreen)
	{
		this.screen = menuScreen;
	}
	// создаем массив фигур, которые будут спавниться
	private void initFigures()
    {
        arrayFigures[0]  = new FiguresEnum("O",  blockTextures[6], 0,0,1,0,0,1,1,1);
        arrayFigures[1]  = new FiguresEnum("I1", blockTextures[5], 0,0,0,1,0,2,0,3);
        arrayFigures[2]  = new FiguresEnum("I2", blockTextures[5], 0,0,1,0,2,0,3,0);
        arrayFigures[3]  = new FiguresEnum("Z1", blockTextures[3], 0,1,1,1,1,0,2,0);
        arrayFigures[4]  = new FiguresEnum("Z2", blockTextures[3], 0,0,0,1,1,1,1,2);
        arrayFigures[5]  = new FiguresEnum("S1", blockTextures[4], 0,0,1,0,1,1,2,1);
        arrayFigures[6]  = new FiguresEnum("S2", blockTextures[4], 0,2,0,1,1,1,1,0);
        arrayFigures[7]  = new FiguresEnum("J1", blockTextures[2], 0,0,1,0,1,1,1,2);
        arrayFigures[8]  = new FiguresEnum("J2", blockTextures[2], 0,0,0,1,1,0,2,0);
        arrayFigures[9]  = new FiguresEnum("J3", blockTextures[2], 0,0,0,1,0,2,1,2);
        arrayFigures[10] = new FiguresEnum("J4", blockTextures[2], 0,1,1,1,2,1,2,0);
        arrayFigures[11] = new FiguresEnum("L1", blockTextures[1], 0,0,0,1,0,2,1,0);
        arrayFigures[12] = new FiguresEnum("L2", blockTextures[1], 0,0,0,1,1,1,2,1);
        arrayFigures[13] = new FiguresEnum("L3", blockTextures[1], 0,2,1,2,1,1,1,0);
        arrayFigures[14] = new FiguresEnum("L4", blockTextures[1], 0,0,1,0,2,0,2,1);
        arrayFigures[15] = new FiguresEnum("T1", blockTextures[0], 0,1,1,1,1,0,2,1);
        arrayFigures[16] = new FiguresEnum("T2", blockTextures[0], 0,1,1,0,1,1,1,2);
        arrayFigures[17] = new FiguresEnum("T3", blockTextures[0], 0,0,1,0,1,1,2,0);
        arrayFigures[18] = new FiguresEnum("T4", blockTextures[0], 0,0,0,1,0,2,1,1);
    }
    // функция возвращает новую фигуру повернутую на 90 градусов
    public static FiguresEnum TurnFigure(String name)
    {
        switch (name)
        {
            case "I1" : return arrayFigures[2];
            case "I2" : return  arrayFigures[1];
            case "Z1" : return  arrayFigures[4];
            case "Z2" : return  arrayFigures[3];
            case "S1" : return  arrayFigures[6];
            case "S2" : return  arrayFigures[5];
            case "J1" : return  arrayFigures[8];
            case "J2" : return  arrayFigures[9];
            case "J3" : return arrayFigures[10];
            case "J4" : return  arrayFigures[7];
            case "L1" : return arrayFigures[12];
            case "L2" : return arrayFigures[13];
            case "L3" : return arrayFigures[14];
            case "L4" : return arrayFigures[11];
            case "T1" : return arrayFigures[16];
            case "T2" : return arrayFigures[17];
            case "T3" : return arrayFigures[18];
            case "T4" : return arrayFigures[15];
            default   : return  arrayFigures[0];
        }
    }
    // разделяем текстуры фигур на блоки
    private void SplitBlocks()
    {
		Texture tex = new Texture("All_Tetris_Blocks_30px.png");
		TextureRegion[][] tmp = new TextureRegion(tex).split(tex.getWidth() / Config.FiguresCount, tex.getHeight());
		int index = 0;
		for (int j = Config.FiguresCount - 1; j >= 0; j--)
		{
			blockTextures[index++] = tmp[0][j];
		}
    }
    // создание новой фигуры на поле
    public static Figure SpawnFigure()
	{
		Random rnd = new Random();
		int index = rnd.nextInt(19);
		Figure actor = new Figure(arrayFigures[index]);
		return actor;
	}
	// ловим нажатия по экрану
	void HandleInput()
	{            // Получаем координаты касания и устанавливаем эти значения в временный вектор
		temp.set(Gdx.input.getX(),Gdx.input.getY(), 0);
		// получаем координаты касания относительно области просмотра нашей камеры
		camera.unproject(temp);
		float touchX = temp.x;
		float touchY = temp.y;

		if (Gdx.input.justTouched())
		{
			if ((touchX >= backSprite.getX())
					&& touchX  <= (backSprite.getX() + backSprite.getWidth())
					&& (touchY >=  backSprite.getY())
					&& touchY  <= (backSprite.getY() + backSprite.getHeight()) )
			{
				if (Config.Sound) Config.bttn_tap.play();
				this.screen.setScreen(new MenuScreen(this.screen));
			}
		}
	}

	@Override
	public void show() {
		NewGame();
		batch = new SpriteBatch();
		bgBatch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new FitViewport(Config.FieldWidth, Config.FieldHeight));
		bgStage = new Stage(new ScreenViewport());
		backgroundTexture = new Texture(Gdx.files.internal("Tetris_UI.png"));
		stage.addActor(SpawnFigure());
		back = new Background(Gdx.files.internal("Tetris_UI.png"));
		bgStage.addActor(back);
		rowFilling  = new int[Config.FieldHeight];
		GameOver = false;
		Gdx.input.setInputProcessor(new GestureDetector(new MyInputProcessor()));
        theme_music = Gdx.audio.newMusic(Gdx.files.internal("Tetris_OldSchool_Music.mp3"));
		score = new BitmapFont();
		score.getData().scale(Gdx.graphics.getWidth() / 800f);
		backTexture = new Texture(Gdx.files.internal("Back.png"));
		backSprite = new Sprite(backTexture);
		backSprite.setSize(backSprite.getWidth() * (Gdx.graphics.getWidth() / BUTTON_RESIZE_FACTOR), backSprite.getHeight() * (Gdx.graphics.getWidth() / BUTTON_RESIZE_FACTOR));
		backSprite.setPosition(0 + backSprite.getWidth() / 2, Gdx.graphics.getHeight() - backSprite.getWidth() * 2);

		if (Config.Music)
		{
			theme_music.setLooping(true);
			theme_music.setVolume(0.5f);
			theme_music.play();
		}

	}
	private void NewGame()
	{
		currentScore = 0;
		Config.GameSpeed = 1000;
		lastCurrentScore = 0;
		SplitBlocks();
		initFigures();
		field = new boolean[Config.FieldWidth][Config.FieldHeight + 2];
		GameOver = false;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.0f, 0.05f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		stage.draw();
		bgStage.draw();
		batch.end();
		bgBatch.begin();
		score.draw(bgBatch, "Score: " + currentScore, Gdx.graphics.getWidth() / 6f,Gdx.graphics.getHeight() / 1.2f);
		HandleInput();
		backSprite.draw(bgBatch);
		bgBatch.end();
		if (currentScore - lastCurrentScore >= 500) // каждые 500 очков
		{
			lastCurrentScore = currentScore;
			if (Config.GameSpeed >= 200) Config.GameSpeed /= 1.25f; // увеличиваем скорость игры
		}
		if (GameOver)
		{
			if (Config.HighScore < currentScore || Config.HighScore == 0)
			{
				file.writeString(Integer.toString(currentScore), false); // если конец игры проверяем на рекорд 
			}
			this.screen.setScreen(new GameOverScreen(screen)); // выходим на экран поражения
		}
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose ()
	{
		bgBatch.dispose();
		backgroundTexture.dispose();
		score.dispose();
		bgStage.dispose();
		batch.dispose();
		stage.dispose();
	}
	// класс фигур
	public class FiguresEnum
	{
	    public String figureName;
	    public List<Coord> figureCoords;
	    public TextureRegion figureTexture;
        FiguresEnum(String name, TextureRegion tex, int ... coords)
        {
        	figureCoords = new ArrayList<Coord>();
            this.figureName = name;
            this.figureTexture = tex;
            for (int i = 0; i < coords.length; i+=2)
            {
                figureCoords.add(new Coord(coords[i], coords[i + 1]));
            }
        }
	}
}
