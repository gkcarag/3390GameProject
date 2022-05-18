package a12.gcaragchiu.game.object;

import android.content.Context;

import androidx.core.content.ContextCompat;

import a12.gcaragchiu.game.GameLoop;
import a12.gcaragchiu.game.Joystick;
import a12.gcaragchiu.game.R;

/*
Player is main character. Player is <- Circle <- GameObject
 */
public class Player extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND = 400.00;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final Joystick joystick;

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
    }

    public void update() {
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;

        positionX += velocityX;
        positionY += velocityY;
    }
}
