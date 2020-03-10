package practise.sfedu.tetris;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen implements Screen
{
    private Texture back;
    private SpriteBatch batch;
    private BitmapFont scoreText, score;
    MainGame game;
    OrthographicCamera camera;
    String s = "Your score: ";
    String iScore = Integer.toString(TetrisGame.currentScore);
    GlyphLayout layout, scoreLayout;
    float textWidth, scoreWidth;

    public GameOverScreen(MainGame screen)
    {
        this.game = screen;
    }
    // найстраиваем для правильного отображения
    @Override
    public void show() {
        back = new Texture(Gdx.files.internal("Game_Over.png"));
        batch = new SpriteBatch();
        scoreText = new BitmapFont();
        score = new BitmapFont();
        layout = new GlyphLayout(scoreText, s);
        scoreLayout = new GlyphLayout(score, iScore);
        textWidth = layout.width;
        scoreWidth = scoreLayout.width;
        score.getData().scale(Gdx.graphics.getWidth() / 800f);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    // вывод на экран
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.5f, 0.05f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(back, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        score.draw(batch, s, Gdx.graphics.getWidth() / 2f - textWidth / 2, Gdx.graphics.getHeight() / 2f);
        score.draw(batch, iScore, Gdx.graphics.getWidth() / 2f - scoreWidth / 2, Gdx.graphics.getHeight() / 2f - scoreLayout.height * 4);
        batch.end();
        HandleInput();
    }
    // отлавливаем tap по экрану
    private void HandleInput()
    {
        if (Gdx.input.justTouched())
        {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose()
    {
        batch.dispose();
        back.dispose();
    }
}
