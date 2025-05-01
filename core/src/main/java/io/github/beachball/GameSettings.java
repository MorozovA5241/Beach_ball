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



    // Object sizes

    public static final int OBJECT_WIDTH = 3500 ;
    public static final int OBJECT_HEIGHT = 80;

    // Images for textures

    public static final String OBJECT_IMG_PATH = "smileface.png";
    public static final String BUTTON_IMG_PATH = "button.png";
    public static final String BALL_IMAGE_PATH = "ball.jpg";
    public static final String PLAYER_IMAGE_PATH = "bluePlayer.png";
    public static final String LEFTBUTTON_IMAGE_PATH = "left.png";
    public static final String RIGHTBUTTON_IMAGE_PATH = "right.png";
    public static final String UPBUTTON_IMAGE_PATH = "up.png";


    public static final short PLAYER_BIT = 2;
    public static final short FLOOR_BIT = 4;
    public static final short BALL_BIT = 8;
    public static final short BAFFLE_BIT = 16;
    public static final short SIMPLE_BIT = -1;
}
