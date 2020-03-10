package practise.sfedu.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ContactsScreen implements Screen
{
    MainGame game;
    Texture backGroundTexture;
    Sprite backSprite;
    Texture tableTexture;
    Sprite table;
    Texture back_bTexture;
    Sprite  back_b;
    float height, width;
    SpriteBatch batch;
    Vector3 temp;
    OrthographicCamera camera;

    public ContactsScreen(MainGame game)
    {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        temp = new Vector3();
        batch = new SpriteBatch();
        backGroundTexture = new Texture(Gdx.files.internal("Tetris_BG.jpg"));
        backSprite = new Sprite(backGroundTexture);
        tableTexture = new Texture(Gdx.files.internal("ContactTable.png"));
        table = new Sprite(tableTexture);
        back_bTexture = new Texture(Gdx.files.internal("Back.png"));
        back_b = new Sprite(back_bTexture);
        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();
        backSprite.setSize(width, height);
        table.setSize(table.getWidth(), table.getHeight());
        table.setPosition(width / 2 - table.getWidth() / 2, height / 2);
        back_b.setSize(back_b.getWidth(), back_b.getHeight());
        back_b.setPosition( back_b.getWidth() / 2, Gdx.graphics.getHeight() - back_b.getWidth() * 2);
    }
    void HandleInput()
    {
        {
            // Получаем координаты касания и устанавливаем эти значения в временный вектор
            temp.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            // получаем координаты касания относительно области просмотра нашей камеры
            camera.unproject(temp);
            float touchX = temp.x;
            float touchY = temp.y;

            if (Gdx.input.justTouched()) {
                if ((touchX >= back_b.getX())
                        && touchX <= (back_b.getX() + back_b.getWidth())
                        && (touchY >= back_b.getY())
                        && touchY <= (back_b.getY() + back_b.getHeight())) {
                    if (Config.Sound) Config.bttn_tap.play();
                    this.game.setScreen(new MenuScreen(this.game));
                }
            }
        }
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.5f, 0.05f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        backSprite.draw(batch);
        table.draw(batch);
        back_b.draw(batch);
        HandleInput();
        batch.end();

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
    public void dispose() {

    }
}
