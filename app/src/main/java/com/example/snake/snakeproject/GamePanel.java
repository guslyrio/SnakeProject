package com.example.snake.snakeproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by glyrio on 18/12/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    //Game messages
    private Rect r = new Rect();

    //Cell Grid dimensions;
    private int topGap = 50;
    private int nRows = 0;
    private int nCols = 40;
    private int cellSize = 0;
    private CellGrid cellGrid;
    private long timeToRestart = 0;

    //Snake
    private Player player;

    //Food
    private List<Food> foods = new ArrayList<Food>();
    private int nFoods = 10;

    private Point eventStarts;
    private Point eventEnds;
    //private int moveDirection = 0; //0- STOPPED 1- UP 2- DOWN 3- LEFT 4- RIGHT

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        //Cell Grid initialization related to screen dimensions.
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        cellSize = screenWidth/nCols;
        nRows = (screenHeight - topGap)/this.cellSize;
        cellGrid = new CellGrid(cellSize, nCols, nRows, topGap);

        //Player initialization on grid center.
        initializePlayer();
        //Random initialize food
        initializeFood();

        //event control
        eventStarts = new Point(0, 0);
        eventEnds = new Point(0, 0);

        setFocusable(true);
    }

    private void initializeGrid() {
        cellGrid = new CellGrid(cellSize, nCols, nRows, topGap);;
    }

    private void initializePlayer() {
        int startC = nCols/2;
        int startR = nRows/2;
        player = new Player(startC, startR, cellGrid);
    }

    private void initializeFood() {
        foods = new ArrayList<Food>();
        nFoods = 10;

        //Random food initialization
        Random random = new Random();
        int i = 0;
        while (i < nFoods) {
            int foodRow = random.nextInt(nRows - 1) + 1;
            int foodCol = random.nextInt(nCols - 1) + 1;
            if (!cellGrid.isOccupied(foodRow, foodCol)) {
                Food food = new Food(foodRow, foodCol);
                cellGrid.occupyCell(foodRow, foodCol, 2);
                foods.add(food);
            }
            else {
                i--;
            }
            i++;
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(true) {
            try {
                thread.setRunning(false);
                thread.join();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int moveDirection = 0;

        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                eventStarts.set( (int) event.getX(), (int) event.getY() );
                eventEnds.set( (int) event.getX(), (int) event.getY() );
            case MotionEvent.ACTION_UP:
                eventEnds.set( (int) event.getX(), (int) event.getY() );
        }
        int deltaX = eventEnds.x - eventStarts.x;
        int deltaY = eventEnds.y - eventStarts.y;

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            //horizontal movement
            if (deltaX > 0) {
                moveDirection = 4;// move right
            }
            else if (deltaX < 0) {
                moveDirection = 3;// move left
            }
        }
        else if (Math.abs(deltaX) < Math.abs(deltaY)) {
            //vertical movement
            if (deltaY > 0) {
                moveDirection = 2;// move down
            }
            else if (deltaY < 0) {
                moveDirection = 1;// move up
            }
        }
        player.setDirection(moveDirection);
        return true;
        //return super.onTouchEvent(event);
    }

    public void restart() {
        //grid initialization
        initializeGrid();

        //Player initialization on grid center.
        initializePlayer();
        //Random initialize food
        initializeFood();

        //event control
        eventStarts = new Point(0, 0);
        eventEnds = new Point(0, 0);
    }

    public void update() {
        player.update();
        if (player.getScore() >= nFoods) {
            player.setScore(0);
            //Random initialize food
            initializeFood();
        }
        //save death time in timeToRestart
        if (player.isDead() && (timeToRestart == 0)) {
            timeToRestart = System.currentTimeMillis();
            return;
        }
        //Check among of seconds passed from death time. If more then 2, restart.
        if (player.isDead() && (System.currentTimeMillis() - timeToRestart >= 2000))
            restart();
    }

    private void drawCenteredText(Canvas canvas, Paint paint, String text) {
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.BLACK);
        cellGrid.draw(canvas);

        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);

        //draw score
        canvas.drawText("Score: " + player.getTotalScore(), 50, 100, paint);

        if (player.isDead()) {
            drawCenteredText(canvas, paint, "Game Over");
        }
    }
}
