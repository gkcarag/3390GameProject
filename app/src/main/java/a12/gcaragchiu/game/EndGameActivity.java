package a12.gcaragchiu.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        //grab score
        Intent intent = getIntent();
        String displayScore = intent.getStringExtra("score");

        //set display text score to your score
        TextView textView = findViewById(R.id.displayScoreText);
        textView.setText("Your Score: " + displayScore);

        //wait for button to play again
        Button btn = findViewById(R.id.btnPlayAgain);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}