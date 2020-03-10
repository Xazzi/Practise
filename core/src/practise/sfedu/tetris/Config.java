package practise.sfedu.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Config
{
    static final int FieldWidth   = 14; 	 // количество блоков в длину
    static final int FieldHeight  = 32; 	 // количество блоков в высоту
    static final int FiguresCount = 7; 		 // количество различных фигурок (без учета их под другим углом)
    static float     GameSpeed    = 1000f;	 // скорость опускания фигурок
    static boolean   Music;					 // включена ли музыка
    static boolean   Sound;					 // включены ли звуки
    static int       HighScore;				 // лучший рекорд
    static com.badlogic.gdx.audio.Sound bttn_tap = Gdx.audio.newSound(Gdx.files.internal("b_tap.mp3")); // Звук клика по кнопке
    static com.badlogic.gdx.audio.Sound tap = Gdx.audio.newSound(Gdx.files.internal("tap.mp3")); // Звук обычного клика
    static float xDrop;
    static float yDrop;
}
