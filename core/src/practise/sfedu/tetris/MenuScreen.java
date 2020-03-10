package practise.sfedu.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import static practise.sfedu.tetris.TetrisGame.theme_music;

public class MenuScreen implements Screen {

    protected final MainGame game;
    SpriteBatch batch;
    OrthographicCamera camera; // область просмотра нашей игры
    Vector3 temp = new Vector3(); // временный вектор для "захвата" входных координат
    // поля для рисования
    Texture startButtonTexture;
    Texture exitButtonTexture;
    Texture settingsButtonTexture;
    Texture backGroundTexture;
    Sprite  startButtonSprite;
    Sprite  backGroundSprite;
    Sprite  exitButtonSprite;
    Sprite  settingsButtonSprite;
    Texture contactTexture;
    Sprite contact;
    GlyphLayout highscore;
    BitmapFont font = new BitmapFont();
    String score = "High Score: ";
    float widthScore;
    String high;

    public static float BUTTON_RESIZE_FACTOR = 800f; // задаём относительный размер
    public static float START_VERT_POSITION_FACTOR = 2.7f; // задаём позицию конпки start
    // считываем настройки из файла
    private void ReadSettings()
    {
        FileHandle file = Gdx.files.local("settings.txt");
        if (!file.exists())
        {
            file.writeString("music-true;sound-true;", false);
        }
        String text = file.readString();
        // разделяем настройки на подстроки и считываем каждую натсройку
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
        FileHandle highscoreFile = Gdx.files.local("highscore.txt");
        if (!highscoreFile.exists()) highscoreFile.writeString("0", false);
        high = highscoreFile.readString();
        Config.HighScore = Integer.parseInt(high);
    }

    public MenuScreen(MainGame mainGame) {
        this.game = mainGame;
        ReadSettings();
        font.getData().scale(Gdx.graphics.getWidth() / 800f);
        highscore = new GlyphLayout(font, score + high);
        widthScore = highscore.width;
        if (theme_music != null) theme_music.stop();
        // получаем размеры экрана устройства пользователя и записываем их в переменнные высоты и ширины
        float height = Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();
        // устанавливаем переменные высоты и ширины в качестве области просмотра нашей игры
        camera = new OrthographicCamera(width, height);
        // этим методом мы центруем камеру на половину высоты и половину ширины
        camera.setToOrtho(false);
        batch = new SpriteBatch();
        // инициализируем текстуры и спрайты
        startButtonTexture = new Texture(Gdx.files.internal("Tetris_PlayButton.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("Exit_Button.png"));
        backGroundTexture = new Texture(Gdx.files.internal("Tetris_BG.jpg"));
        settingsButtonTexture = new Texture(Gdx.files.internal("Settings_Button.png"));
        startButtonSprite = new Sprite(startButtonTexture);
        exitButtonSprite = new Sprite(exitButtonTexture);
        backGroundSprite = new Sprite(backGroundTexture);
        settingsButtonSprite = new Sprite(settingsButtonTexture);
        contactTexture = new Texture(Gdx.files.internal("ContactUs_Button.png"));
        contact = new Sprite(contactTexture);
        // устанавливаем размер и позиции
        startButtonSprite.setSize(startButtonSprite.getWidth() * (width/BUTTON_RESIZE_FACTOR), startButtonSprite.getHeight()*(width/BUTTON_RESIZE_FACTOR));
        backGroundSprite.setSize(width, height);
        startButtonSprite.setPosition((width / 2f - startButtonSprite.getWidth() / 2) , width / START_VERT_POSITION_FACTOR * 2f);
        settingsButtonSprite.setSize(settingsButtonSprite.getWidth() * (width/BUTTON_RESIZE_FACTOR), settingsButtonSprite.getHeight() * (width/BUTTON_RESIZE_FACTOR));
        settingsButtonSprite.setPosition((width/2f - settingsButtonSprite.getWidth() / 2) - settingsButtonSprite.getWidth(), width / START_VERT_POSITION_FACTOR * 1.3f);
        exitButtonSprite.setSize(exitButtonSprite.getWidth() * (width/BUTTON_RESIZE_FACTOR), exitButtonSprite.getHeight() * (width/BUTTON_RESIZE_FACTOR));
        exitButtonSprite.setPosition((width/2f - exitButtonSprite.getWidth() / 2) + exitButtonSprite.getWidth(), width / START_VERT_POSITION_FACTOR * 1.3f);
        contact.setSize(contact.getWidth() * (width/BUTTON_RESIZE_FACTOR), contact.getHeight() * (width/BUTTON_RESIZE_FACTOR));
        contact.setPosition(width / 2 - contact.getWidth() / 2, settingsButtonSprite.getY() - contact.getHeight() * 1.5f);
    }

    void handleTouch()
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
            // обработка касания по кнопке Start
            if ((touchX >= startButtonSprite.getX())
                    && touchX  <= (startButtonSprite.getX() + startButtonSprite.getWidth())
                    && (touchY >=  startButtonSprite.getY())
                    && touchY  <= (startButtonSprite.getY() + startButtonSprite.getHeight()) )
            {
                if (Config.Sound) Config.bttn_tap.play();
                game.setScreen(new TetrisGame(game)); // Переход к экрану игры
            }
            // обработка касания по кнопке выхода
            if ((touchX >= exitButtonSprite.getX())
                    && touchX  <= (exitButtonSprite.getX() + exitButtonSprite.getWidth())
                    && (touchY >=  exitButtonSprite.getY())
                    && touchY  <= (exitButtonSprite.getY() + exitButtonSprite.getHeight()) )
            {
                if (Config.Sound) Config.bttn_tap.play();
                Gdx.app.exit(); // Закрытие приложения
            }
            // обработка касания по кнопке настроек
            if ((touchX >= settingsButtonSprite.getX())
                    && touchX  <= (settingsButtonSprite.getX() + settingsButtonSprite.getWidth())
                    && (touchY >=  settingsButtonSprite.getY())
                    && touchY  <= (settingsButtonSprite.getY() + settingsButtonSprite.getHeight()) )
            {
                if (Config.Sound) Config.bttn_tap.play();
                game.setScreen(new SettingsScreen(game)); // переход к настройкам
            }
            // обработка касания по кнопке контакты
            if ((touchX >= contact.getX())
                    && touchX  <= (contact.getX() + contact.getWidth())
                    && (touchY >=  contact.getY())
                    && touchY  <= (contact.getY() + contact.getHeight()) )
            {
                if (Config.Sound) Config.bttn_tap.play();
                game.setScreen(new ContactsScreen(game)); // переход к настройкам
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
        backGroundSprite.draw(batch);
        startButtonSprite.draw(batch);
        settingsButtonSprite.draw(batch);
        exitButtonSprite.draw(batch);
        contact.draw(batch);
        font.draw(batch, score + high, Gdx.graphics.getWidth() / 2 - widthScore / 2, Gdx.graphics.getHeight() - highscore.height * 2);
        handleTouch();
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
    public void dispose()
    {
        settingsButtonTexture.dispose();
        startButtonTexture.dispose();
        exitButtonTexture.dispose();
    }
}
