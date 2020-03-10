package practise.sfedu.tetris;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class MyInputProcessor implements GestureListener {


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (Config.Sound) Config.tap.play();
        Figure.NeedToRotate = true;
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button)
    {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        Config.xDrop = x;
        Config.yDrop = y;
        if (deltaX < -1) Figure.NeedToMoveLeft = true;
        if (deltaX >  1) Figure.NeedToMoveRight = true;

        if (deltaY > 3 && (deltaX > -1 && deltaX < 1))
        {
            Figure.NeedToMoveFastDrop = true;
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}