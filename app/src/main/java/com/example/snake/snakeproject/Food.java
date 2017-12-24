package com.example.snake.snakeproject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by glyrio on 21/12/17.
 */

public class Food implements GameObject{
    private int row;
    private int col;

    public Food(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public void draw(Canvas canvas) {
    }

    @Override
    public void update() {
    }
}
