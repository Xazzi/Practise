package practise.sfedu.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Background extends Actor
{
	// определяем поля
    private Texture back;

    // конструктор принимает хэндл текстуры
    Background(FileHandle texture)
    {
        this.back = new Texture(texture);
    }
    // отрисовываем фон
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.draw(back, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

}
