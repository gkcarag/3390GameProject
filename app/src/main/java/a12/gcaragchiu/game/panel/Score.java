package a12.gcaragchiu.game.panel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import a12.gcaragchiu.game.R;

/*
 * Shows score and level of the game
 */
public class Score {
    private Context context;
    private int enemiesLeft = 5;
    private int totalScore = 0;

    public Score(Context context) {
        this.context = context;
    }

    public void draw(Canvas canvas) {
        String points = Integer.toString(totalScore);
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Score: " + totalScore, 1000, 100, paint);
    }

    public void addPoints() {
        totalScore++;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
