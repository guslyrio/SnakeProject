package com.example.snake.snakeproject;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glyrio on 21/12/2017.
 */

class Part {
    private int row = 0;
    private int col = 0;

    public Part(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }
}

public class Player implements GameObject {

    private int direction = 0;
    private boolean dead = false;
    private int score = 0;
    private int totalScore = 0;
    private List<Part> body = new ArrayList<Part>();
    private CellGrid grid;

    public Player(int col, int row, CellGrid grid) {
        dead = false;

        //insert snake parts from tail to head. It will help to move. See move method.
        body.add(new Part(row, col-2));
        body.add(new Part(row, col-1));
        body.add(new Part(row, col));

        this.grid = grid;
        grid.occupyCell(body.get(0).getRow(), body.get(0).getCol(), 1);
        grid.occupyCell(body.get(1).getRow(), body.get(1).getCol(), 1);
        grid.occupyCell(body.get(2).getRow(), body.get(2).getCol(),1);
    }

    public void move() {
        //get head position
        int headRow = body.get(body.size()-1).getRow();
        int headCol = body.get(body.size()-1).getCol();

        int newHeadRow = headRow;
        int newHeadCol = headCol;

        switch (this.direction) {
            case 1:
                newHeadRow = headRow - 1;
                break;
            case 2:
                newHeadRow = headRow + 1;
                break;
            case 3:
                newHeadCol = headCol - 1;
                break;
            case 4:
                newHeadCol = headCol + 1;
                break;
            default:
                return;
        }

        //Check if player is able to move.
        if ( grid.isOutOfBounds(newHeadRow, newHeadCol) ||
             grid.isOccupied(newHeadRow, newHeadCol) ) {
            dead = true;
            return;
        }

        //if snake has eaten a food increase score
        // (snake size increases automatic as we added a new head)
        if (grid.hasFood(newHeadRow, newHeadCol)) {
            score++;
            totalScore++;
        }
        //otherwise, remove its tail to keep snake size
        else {
            grid.occupyCell(body.get(0).getRow(), body.get(0).getCol(), 0);
            body.remove(0);
        }

        //insert another part into new head position
        body.add(new Part(newHeadRow, newHeadCol));
        grid.occupyCell(newHeadRow, newHeadCol, 1);
    }

    public boolean isDead() { return dead; }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalScore() {
        return totalScore;
    }

    @Override
    public void draw(Canvas canvas) {
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public void update() {
        if (!dead)
            this.move();
    }

}
