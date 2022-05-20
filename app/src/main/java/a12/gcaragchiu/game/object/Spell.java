package a12.gcaragchiu.game.object;

import android.content.Context;

import androidx.core.content.ContextCompat;

import a12.gcaragchiu.game.GameLoop;
import a12.gcaragchiu.game.R;
import a12.gcaragchiu.game.object.Circle;
import a12.gcaragchiu.game.object.Player;

public class Spell extends Circle {

    public static final double SPEED_PIXELS_PER_SECOND = 400.00;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;

    public Spell(Context context, Player spellcaster) {
        super(
                context,
                ContextCompat.getColor(context, R.color.spell),
                spellcaster.getPositionX(),
                spellcaster.getPositionY(),
                20
        );
        velocityX = spellcaster.getDirectionX()*MAX_SPEED;
        velocityY = spellcaster.getDirectionY()*MAX_SPEED;
    }

    @Override
    public void update() {
        positionX += velocityX;
        positionY += velocityY;

    }
}
