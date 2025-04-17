package io.github.beachball;

public class GameSettings {
    // Device settings

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    // Physics settings

    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 6;
    public static final float SCALE = 0.05f;

    public static float SHIP_FORCE_RATIO = 10;

    // Object sizes

    public static final int OBJECT_WIDTH = 3500 ;
    public static final int OBJECT_HEIGHT = 100;

    // Images for textures

    public static final String OBJECT_IMG_PATH = "smileface.png";
    public static final String BUTTON_IMG_PATH = "button.png";


    public static final short PLAYER_BIT = 2;
    public static final short FLOOR_BIT = 4;
}
