package com.example.snake.snakeproject;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by glyrio on 21/12/17.
 */

public class CellGrid implements GameObject {

    private int nRows = 0;
    private int nCols = 40;
    private int cellSize = 0;
    private int[][] cells;
    private int gap;

    public CellGrid(int cellSize, int rCols, int nRows, int gap) {
        this.cellSize = cellSize;
        this.nCols = nCols;
        this.nRows = nRows;
        this.gap = gap;

        cells = new int [nRows][nCols];

        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                cells[r][c] = 0; //0-empty, 1-ocuppied
            }
        }

    }

    public void occupyCell(int r, int c, int value) {
        this.cells[r][c] = value;
    }

    public boolean isOccupied(int r, int c) {
        if (this.cells[r][c] == 1)
            return true;
        return false;
    }

    public boolean hasFood(int r, int c) {
        if (this.cells[r][c] == 2)
            return true;
        return false;
    }

    public boolean isOutOfBounds(int r, int c) {
        if ((r < 0) || (r >= this.nRows) || (c < 0) || ( c >= this.nCols))
            return true;
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                Rect rectangle = new Rect(c*cellSize, (r*cellSize)+gap, (c+1)*cellSize, ((r+1)*cellSize)+gap);
                if (cells[r][c] == 1)
                    paint.setColor(Color.WHITE);
                else if (cells[r][c] == 2)
                    paint.setColor(Color.RED);
                else
                    paint.setColor(Color.BLACK);
                canvas.drawRect(rectangle, paint);
            }
        }
    }

    @Override
    public void update() {
    }
}
