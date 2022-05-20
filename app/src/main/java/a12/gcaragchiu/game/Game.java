package a12.gcaragchiu.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import a12.gcaragchiu.game.panel.GameOver;
import a12.gcaragchiu.game.object.Circle;
import a12.gcaragchiu.game.object.Enemy;
import a12.gcaragchiu.game.object.Player;
import a12.gcaragchiu.game.object.Spell;
import a12.gcaragchiu.game.panel.Joystick;
import a12.gcaragchiu.game.panel.Level;
import a12.gcaragchiu.game.panel.Performance;
import a12.gcaragchiu.game.panel.Score;

//Will manage all objects and updates all states/renders all objects
public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    private final Score score;
    private GameLoop gameLoop;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Spell> spellList = new ArrayList<Spell>();
    private int joystickPointerId = 0;
    private int numberOfSpellsToCast = 0;
    private GameOver gameOver;
    private Performance performance;
    private GameDisplay gameDisplay;
    private Context context;
    //private Level level;


    public Game(Context context) {
        super(context);

        score = new Score(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this, surfaceHolder, (GameActivity)context, score);
        gameOver = new GameOver(context);

        //initialize game panels
        performance = new Performance(context, gameLoop);
        joystick = new Joystick(275, 700, 70, 40);
        //level = new Level(context);


        //initialize game objects
        player = new Player(context, joystick, 2*500, 500, 30);

        // game display
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //handle touch
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(joystick.getIsPressed()) {
                    //if pressed already, attack
                    numberOfSpellsToCast++;
                } else if(joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    //
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else {
                    //not already pressed at all
                    spellList.add(new Spell(getContext(), player));
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()) {
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerId == event.getPointerId(event.getActionIndex())) {
                    // let go of joystick
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if(gameLoop.getState().equals(Thread.State.TERMINATED)) {
            gameLoop = new GameLoop(this, surfaceHolder, (GameActivity)context, score);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        player.draw(canvas, gameDisplay);

        for(Enemy enemy : enemyList) {
            enemy.draw(canvas, gameDisplay);
        }

        for(Spell spell : spellList) {
            spell.draw(canvas, gameDisplay);
        }

        joystick.draw(canvas);
        performance.draw(canvas);
        score.draw(canvas);
        //level.draw(canvas);

        //death sequence
        if(player.getHealthPoints() <= 0) {
            gameOver.draw(canvas);
        }
    }

    public void update() {
        if(player.getHealthPoints() <= 0) {
            //endGame();
            gameLoop.endGame();
            //return;
        }
        joystick.update();
        player.update();
        if(Enemy.readyToSpawn()) {
            Enemy enemy = new Enemy(getContext(), player);
            enemyList.add(enemy);
        }


        while(numberOfSpellsToCast > 0) {
            spellList.add(new Spell(getContext(), player));
            numberOfSpellsToCast--;
        }
        for (Enemy enemy : enemyList) {
            enemy.update();
        }

        for (Spell spell : spellList) {
            spell.update();
        }

        //check collision
        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while(iteratorEnemy.hasNext()) {
            Circle enemy = iteratorEnemy.next();
            if(Circle.isColliding(enemy, player)) {
                //remove if collision
                iteratorEnemy.remove();
                player.setHealthPoints(player.getHealthPoints() - 1);
                continue;
            }
            Iterator<Spell> iteratorSpell = spellList.iterator();
            while(iteratorSpell.hasNext()) {
                Circle spell = iteratorSpell.next();
                if(Circle.isColliding(spell, enemy)) {
                    iteratorSpell.remove();
                    iteratorEnemy.remove();
                    score.addPoints();
                    break;
                }
            }
        }

        gameDisplay.update();
    }

    public void pause() {
        gameLoop.stopLoop();
    }


}
