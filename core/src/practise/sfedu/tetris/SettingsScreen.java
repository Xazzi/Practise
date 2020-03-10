package practise.sfedu.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class SettingsScreen implements Screen {

    private final MainGame window;
    SpriteBatch batch;
    OrthographicCamera camera; // область просмотра нашей игры
    Vector3 temp = new Vector3(); // временный вектор для "захвата" входных координат
    private static float BUTTON_RESIZE_FACTOR = 800f; // задаём относительный размер
        // поля для рисования
    Texture backgroundTexture;
    Sprite   backgroundSprite;
    Texture      musicTexture;
    Sprite        musicSprite;
    Texture      soundTexture;
    Sprite        soundSprite;
    Texture       backTexture;
    Sprite         backSprite;
    Texture          clearTex;
    Sprite              clear;
    // функция записи новых настроек
    void WriteSettings()
    {
        FileHandle file = Gdx.files.local("settings.txt");
        if (Config.Music)
        {
            if (Config.Sound)
            {
                file.writeString("music-true;sound-true;", false);
            }
            else
            {
                file.writeString("music-true;sound-false;", false);
            }
        }
        else
        {
            if (Config.Sound)
            {
                file.writeString("music-false;sound-true;", false);
            }
            else
            {
                file.writeString("music-false;sound-false;", false);
            }
        }

    }
    // ловим tap по экарну
    void HandleTouch()
    {
        // Проверяем были ли касание по экрану?
        if(Gdx.input.justTouched())
        {
            // Получаем координаты касания и устанавливаем эти значения в временный вектор
            temp.set(Gdx.input.getX(),Gdx.input.getY(), 0);
            // получаем координаты касания относительно области просмотра нашей камеры
            camera.unproject(temp);
            float touchX = temp.x;
            float touchY = temp.y;
            // обработка касания по кнопке Music
            if ((touchX >= musicSprite.getX())
                    && touchX  <= (musicSprite.getX() + musicSprite.getWidth())
                    && (touchY >=  musicSprite.getY())
                    && touchY  <= (musicSprite.getY() + musicSprite.getHeight()) )
            {
                if (Config.Sound) Config.bttn_tap.play();
                musicTexture.dispose();
                if (Config.Music)
                {
                    musicTexture = new Texture(Gdx.files.internal("MusicOff_Button.png"));
                }
                else
                {
                    musicTexture = new Texture(Gdx.files.internal("MusicOn_Button.png"));
                }
                musicSprite.setTexture(musicTexture);
                Config.Music = !Config.Music; // Вкл/выкл музыку
                WriteSettings();
            }
            // обработка касания по кнопке Sound
            if ((touchX >= soundSprite.getX())
                    && touchX  <= (soundSprite.getX() + soundSprite.getWidth())
                    && (touchY >=  soundSprite.getY())
                    && touchY  <= (soundSprite.getY() + soundSprite.getHeight()) )
            {
                if (Config.Sound) Config.bttn_tap.play();
                if (Config.Sound)
                {
                    soundTexture = new Texture(Gdx.files.internal("SoundOff_Button.png"));
                }
                else
                {
                    soundTexture = new Texture(Gdx.files.internal("SoundOn_Button.png"));
                }
                soundSprite.setTexture(soundTexture);
                Config.Sound = !Config.Sound; // Вкл/выкл звуки
                WriteSettings();
            }
            // нажатие по кнопке назад
            if ((touchX >= backSprite.getX())
                    && touchX  <= (backSprite.getX() + backSprite.getWidth())
                    && (touchY >=  backSprite.getY())
                    && touchY  <= (backSprite.getY() + backSprite.getHeight()) )
            {
                if (Config.Sound) Config.bttn_tap.play();
                this.window.setScreen(new MenuScreen(this.window)); // переход к экрану меню
            }
            // нажатие по кнопке clear
            if ((touchX >= clear.getX())
                    && touchX  <= (clear.getX() + clear.getWidth())
                    && (touchY >=  clear.getY())
                    && touchY  <= (clear.getY() + clear.getHeight()) )
            {
                if (Config.Sound) Config.bttn_tap.play();
                FileHandle f = Gdx.files.local("highscore.txt"); // очищаем рекорд
                f.writeString("0", false);
            }
        }
    }
    // считываем текущие настройки
    private void ReadSettings()
    {
        FileHandle file = Gdx.files.local("settings.txt");
        if (!file.exists())
        {
            file.writeString("music-true;sound-true;", false);
        }
        String text = file.readString();
        String[] arr = text.split(";");
        for (String s : arr)
        {
            if (s.contains("music"))
            {
                if (s.contains("true")) Config.Music = true;
                else Config.Music = false;
            }
            if (s.contains("sound"))
            {
                if (s.contains("true")) Config.Sound = true;
                else Config.Sound = false;
            }
        }
    }

    public SettingsScreen(MainGame settingScreen)
    {
        this.window = settingScreen;
        ReadSettings();
        // получаем размеры экрана устройства пользователя и записываем их в переменнные высоты и ширины
        float height = Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();
        // устанавливаем переменные высоты и ширины в качестве области просмотра нашей игры
        camera = new OrthographicCamera(width, height);
        // этим методом мы центруем камеру на половину высоты и половину ширины
        camera.setToOrtho(false);
        batch = new SpriteBatch();

        backgroundTexture = new Texture(Gdx.files.internal("Tetris_BG.jpg"));
        if (Config.Music) musicTexture = new Texture(Gdx.files.internal("MusicOn_Button.png"));
        else musicTexture = new Texture(Gdx.files.internal("MusicOff_Button.png"));
        if (Config.Sound) soundTexture = new Texture(Gdx.files.internal("SoundOn_Button.png"));
        else soundTexture = new Texture(Gdx.files.internal("SoundOff_Button.png"));
        backgroundSprite = new Sprite(backgroundTexture);
        musicSprite =  new Sprite();
        soundSprite =  new Sprite();
        musicSprite.setTexture(musicTexture);
        soundSprite.setTexture(soundTexture);
        backTexture = new Texture(Gdx.files.internal("Back.png"));
        backSprite = new Sprite(backTexture);
        clearTex = new Texture(Gdx.files.internal("Clear.png"));
        clear = new Sprite(clearTex);

        backgroundSprite.setSize(width, height);
        musicSprite.setSize(musicSprite.getWidth() * (width / BUTTON_RESIZE_FACTOR), musicSprite.getHeight() * (width / BUTTON_RESIZE_FACTOR));
        soundSprite.setSize(soundSprite.getWidth() * (width / BUTTON_RESIZE_FACTOR), soundSprite.getHeight() * (width / BUTTON_RESIZE_FACTOR));
        musicSprite.setPosition((width / 2 - musicSprite.getWidth() / 2) - 2 * musicSprite.getWidth(), height / 2.5f);
        soundSprite.setPosition((width / 2 - soundSprite.getWidth() / 2) + 2 * soundSprite.getWidth(), height / 2.5f);
        backSprite.setSize(backSprite.getWidth(), backSprite.getHeight());
        backSprite.setPosition(0 + backSprite.getWidth() / 2, height - backSprite.getWidth() * 2);
        clear.setSize(clear.getWidth() * (width / BUTTON_RESIZE_FACTOR), clear.getHeight() * (width / BUTTON_RESIZE_FACTOR));
        clear.setPosition(width / 2 - clear.getWidth() / 2, height / 3);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.5f, 0.05f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        backgroundSprite.draw(batch);
        backSprite.draw(batch);
        musicSprite.draw(batch);
        soundSprite.draw(batch);
        clear.draw(batch);
        HandleTouch();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // устанавливаем переменные высоты и ширины в качестве области просмотра нашей игры
        camera = new OrthographicCamera(width, height);
        // этим методом мы центруем камеру на половину высоты и половину ширины
        camera.setToOrtho(false);
        batch = new SpriteBatch();

        backgroundTexture = new Texture(Gdx.files.internal("Tetris_BG.jpg"));
        if (Config.Music) musicTexture = new Texture(Gdx.files.internal("MusicOn_Button.png"));
        else musicTexture = new Texture(Gdx.files.internal("MusicOff_Button.png"));
        if (Config.Sound) soundTexture = new Texture(Gdx.files.internal("SoundOn_Button.png"));
        else soundTexture = new Texture(Gdx.files.internal("SoundOff_Button.png"));
        backgroundSprite = new Sprite(backgroundTexture);
        musicSprite =  new Sprite(musicTexture);
        soundSprite =  new Sprite(soundTexture);

        backgroundSprite.setSize(width, height);
        musicSprite.setSize(musicSprite.getWidth() * (width / BUTTON_RESIZE_FACTOR), musicSprite.getHeight() * (width / BUTTON_RESIZE_FACTOR));
        soundSprite.setSize(soundSprite.getWidth() * (width / BUTTON_RESIZE_FACTOR), soundSprite.getHeight() * (width / BUTTON_RESIZE_FACTOR));
        musicSprite.setPosition((width / 2 - musicSprite.getWidth() / 2) - 2 * musicSprite.getWidth(), height / 2.5f);
        soundSprite.setPosition((width / 2 - soundSprite.getWidth() / 2) + 2 * soundSprite.getWidth(), height / 2.5f);

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
