package practise.sfedu.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import static practise.sfedu.tetris.TetrisGame.GameOver;
import static practise.sfedu.tetris.TetrisGame.RowsToDelete;
import static practise.sfedu.tetris.TetrisGame.TurnFigure;
import static practise.sfedu.tetris.TetrisGame.field;
import static practise.sfedu.tetris.TetrisGame.lastFastDrop;
import static practise.sfedu.tetris.TetrisGame.rowFilling;

class Figure extends Actor
{
    public List<Coord> dots;
    public TextureRegion texture;
    public Coord left, right, bottom, top;
    public boolean isStopped;
    public String name;
    public TetrisGame.FiguresEnum figuresEnum;
    static boolean NeedToRotate = false;
    static boolean NeedToMoveRight = false;
    static boolean NeedToMoveLeft = false;
    static boolean NeedToMoveFastDrop = false;
    // конструктор создающий новую фигуру
    Figure(TetrisGame.FiguresEnum figure)
    {
        figuresEnum = figure;
        name = figure.figureName;
        dots = new ArrayList<Coord>();
        for (int i = 0; i < figure.figureCoords.size(); i++)
        {
            dots.add(figure.figureCoords.get(i));
        }
        texture = figure.figureTexture;
        setBounds(Config.FieldWidth / 2 - 2 , Config.FieldHeight - 2, 1,1);
        left = GetLeft();
        right = GetRight();
        bottom = GetBottom();
        top = GetTop();
    }
    // получаем верхнюю точку фигуры
    private Coord GetTop()
    {
        int x = dots.get(0).x;
        int y = dots.get(0).y;
        for (Coord c : dots)
        {
            if (y < c.y)
            {
                x = c.x;
                y = c.y;
            }
        }
        return new Coord(x,y);
    }

    // получаем нижнюю точку фигуры
    private Coord GetBottom()
    {
        int x = dots.get(0).x;
        int y = dots.get(0).y;
        for (Coord c : dots)
        {
            if (y > c.y)
            {
                x = c.x;
                y = c.y;
            }

        }
        return new Coord(x,y);
    }
    // получаем левую точку фигуры
    private Coord GetLeft()
    {
        int x = dots.get(0).x;
        int y = dots.get(0).y;
        for (Coord c : dots)
        {
            if (x > c.x)
            {
                x = c.x;
                y = c.y;
            }
        }
        return new Coord(x,y);
    }
    // получаем правую точку фигуры
    private Coord GetRight()
    {
        int x = dots.get(0).x;
        int y = dots.get(0).y;
        for (Coord c : dots)
        {
            if (x < c.x)
            {
                x = c.x;
                y = c.y;
            }
        }
        return new Coord(x,y);
    }
    // можно ли повернуть игуру
    private boolean CanFigureDrop(int dy)
    {
        if ((int)getY() > 10 && (int)getY() < Config.FieldHeight)
        {
            for (Coord coord : dots)
            {
                if (field[coord.x + (int)getX()][coord.y + dy + (int)getY()])
                    return false;
            }
        }
        else return false;
        return true;
    }
    // спуск фигуры
    public void Drop()
    {
        if ((int)getY() > 0)
        {
            if (TimeUtils.millis() - TetrisGame.lastDropTime > Config.GameSpeed)
            {
                if (CanFigureDrop(-1))
                {
                    setY(getY() - 1);
                    TetrisGame.lastDropTime = TimeUtils.millis();
                }
                else
                {
                    Stop();
                    return;
                }
            }
        }
        else
        {
            Stop();
            return;
        }
    }
    // проверка на конец игры
    private void GameOverCheck()
    {
        if (getY() + top.y >= Config.FieldHeight - 4)
            GameOver = true;
    }
    // остановка спуска фигуры
    public void Stop()
    {
        isStopped = true; // ставим флаг остановки для фигуры
        for (Coord c : dots) 
        {
            field[c.x + (int)getX()][c.y + (int)getY()] = true; // для каждой точки обозначаем ячейку поля занятой
            TetrisGame.rowFilling[c.y + (int)getY()]++;
        }
        GameOverCheck();
        CheckFullRows((int)getX(), (int)getY());
        getStage().addActor(TetrisGame.SpawnFigure());

    }
    // проверка на заполненной линий
    private void CheckFullRows(int getx, int gety)
    {
        for (int i = 0; i < Config.FieldHeight; i++)
        {
            if (rowFilling[i] == Config.FieldWidth - 4)
            {
                RowsToDelete.add(i);
            }
        }
        if (!RowsToDelete.isEmpty())
            DropLine(gety);
    }
    // заполненные линии убираем и спускаем фигуры
    private void DropLine(int gety)
    {
        for (int i = RowsToDelete.size() - 1; i >= 0; i--)
        {
            int row = RowsToDelete.get(i);
            for (int j = 0; j < this.getStage().getActors().items.length; j++) // удаляем точки в фигурах, которые находятся на заполненных линиях
            {
                Actor a = this.getStage().getActors().items[j];
                if (!(a instanceof Figure))
                    continue;
                for (int k = ((Figure) a).dots.size() - 1; k >= 0; k--)
                {
                    if (((Figure) a).dots.get(k).y + a.getY()  == row)
                        ((Figure) a).dots.remove(k);
                }
            }
            /////////////////////// Это вызывает NullPointerException!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
/*          // удаляем фигуры без точек
            for (int k = this.getStage().getActors().items.length - 1; k >= 0 ; k--)
            {
                Actor a = this.getStage().getActors().items[k];
                if (!(a instanceof Figure))
                    continue;
                if (((Figure) a).dots.isEmpty()) // если фигура не отрисовывается, то удаляем ее
                {
                    this.getStage().getActors().items[k].remove();
                }
            }
*/
            ////////////////////////////
            for (int y = row; y < Config.FieldHeight; y++)
            {
                for (int x = 0; x < Config.FieldWidth; x++)
                {
                    field[x][y] = field[x][y + 1];
                }
            }
            for (int k = row; k < TetrisGame.rowFilling.length - 2; k++)
            {
                TetrisGame.rowFilling[k] = TetrisGame.rowFilling[k + 1];
            }
            for (int m = 0; m < this.getStage().getRoot().getChildren().size; m++)
            {
                Actor a = this.getStage().getActors().items[m];
                if (!(a instanceof Figure))
                    continue;
                for (int k = ((Figure) a).dots.size() - 1; k >= 0; k--)
                {
                    if (((Figure) a).dots.get(k).y + a.getY()  >= row)
                        {
                            Coord c = new Coord(((Figure) a).dots.get(k).x, ((Figure) a).dots.get(k).y - 1);
                            ((Figure) a).dots.set(k, c);
                        }
                }
            }
        }
        // начисялем очки взависимости от количества убранных линий
        switch (RowsToDelete.size())
        {
            case 1:
                TetrisGame.currentScore += 100;
                break;
            case 2:
                TetrisGame.currentScore += 300;
                break;
            case 3:
                TetrisGame.currentScore += 700;
                break;
            case 4:
                TetrisGame.currentScore += 1500;
                break;
            default:
        }
        RowsToDelete.clear();
    }
    // можем ли повернуть фигуру
    private boolean CanFigureRotate()
    {
        TetrisGame.FiguresEnum f = TurnFigure(this.name);
        int x = f.figureCoords.get(0).x;
        for (Coord c : f.figureCoords)
        {
            if (x < c.x)
            {
                x = c.x;
            }
        }
        if (this.getX() + x < Config.FieldWidth - 2 && CheckRotate())
            return true;
        else
            return false;
    }
    private boolean CheckRotate()
    {
        int x1, y, x2;
        for (Coord c : dots)
        {
            x1 = (int)getX() - left.x;
            x2 = (int)getX() + right.x;
            y = (int)getY() + c.y;
            y = (int)getY() + c.y;
            if ((x1 > 0 && x1 < Config.FieldWidth - 2 || x1 > 0 && x1 < Config.FieldWidth) && y > 0 && y < Config.FieldHeight + 10)
            {
                if (field[x1][y] || field[x2][y])
                    return false;
            }
        }
        return true;
    }
    // поворачиваем фигуру
    public void Rotate()
    {
        if (CanFigureRotate())
        {
            if (TimeUtils.millis() - TetrisGame.lastRotateTime > 150)
            {
                TetrisGame.FiguresEnum figure = TetrisGame.TurnFigure(name);
                figuresEnum = figure;
                name = figure.figureName;
                dots = new ArrayList<Coord>();
                for (int i = 0; i < figure.figureCoords.size(); i++) {
                    dots.add(figure.figureCoords.get(i));
                }
                texture = figure.figureTexture;
                left = GetLeft();
                right = GetRight();
                bottom = GetBottom();
                top = GetTop();
                TetrisGame.lastRotateTime = TimeUtils.millis();
            }
        }
    }
    // можем ли сдвинуть влево
    boolean CanFigureMoveLeft()
    {
        if ((int)getX() > 2) // 2 куба слева и 2 куба справа скрыты под рамкой
        {
            for (Coord coord : dots)
            {
                if (field[coord.x - 1 + (int)getX()][coord.y + (int)getY()])
                    return false;
            }
        }
        else return false;
        return true;
    }
    // сдвигаем влево
    private void MoveLeft()
    {
        if ((int)getX() > 2 - left.x && CanFigureMoveLeft())
        {
            setX(getX() - 0.15f);
            TetrisGame.lastMoveTime = TimeUtils.millis();
        }
    }
    // можем ли сдвинуть вправо
    boolean CanFigureMoveRight()
    {
        if ((int)getX() < Config.FieldWidth - 2) // 2 куба слева и 2 куба справа скрыты под рамкой
        {
            for (Coord coord : dots)
            {
                if (field[coord.x + 1 + (int)getX()][coord.y + (int)getY()])
                    return false;
            }
        }
        else return false;
        return true;
    }
    // сдвигаем вправо
    private void MoveRight()
    {
        if (getX() < Config.FieldWidth - 3 - right.x && CanFigureMoveRight())
        {
            setX(getX() + 0.15f);
            TetrisGame.lastMoveTime = TimeUtils.millis();
        }
    }
    // быстрый спуск фигуры
    private void FastDrop()
    {
        while ( CanFigureDrop(-1))
        {
            setY(getY() - 1f);
        }
        lastFastDrop = TimeUtils.millis();
    }

    // отрисовка фигур
    @Override
    public void draw(Batch batch, float parentAlpha)
    {

        if (!isStopped)
        {
            if (NeedToMoveLeft || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            {
                MoveLeft();
                NeedToMoveLeft = false;
            }
            if (NeedToMoveRight || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            {
                MoveRight();
                NeedToMoveRight = false;
            }
            if ((NeedToMoveFastDrop || Gdx.input.isKeyPressed(Input.Keys.DOWN)) && (TimeUtils.millis() - lastFastDrop > 250))
            {
                FastDrop();
                NeedToMoveFastDrop = false;
            }
            else NeedToMoveFastDrop = false;
            if (NeedToRotate || Gdx.input.isKeyPressed(Input.Keys.UP))
            {
                Rotate();
                NeedToRotate = false;
            }
            Drop();
        }
        for (Coord block : dots)
        {
            int x = (int)getX();
            int y = (int)getY();
            batch.draw(texture, block.x + x, block.y + y - 5, getWidth(), getHeight());
        }
    }
}
