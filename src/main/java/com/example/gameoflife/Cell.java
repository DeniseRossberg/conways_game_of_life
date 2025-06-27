package com.example.gameoflife;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {
    private int state;

    public Cell(int cellSize) {
        super(cellSize, cellSize);
        state = 0;
        setFill(Color.BLACK);
        setMouseEvents();
    }

    private void setMouseEvents() {
        this.setOnMouseEntered(event -> this.setCursor(Cursor.HAND));
        this.setOnMouseExited(event -> this.setCursor(Cursor.DEFAULT));
        this.setOnMouseClicked(event -> setStateAndColor());
        this.setOnMouseDragEntered(event -> {
            event.consume();
            setStateAndColor();
        });
    }

    public void setStateAndColor() {
        if (state == 1) {
            state = 0;
            setFill(Color.BLACK);
        } else {
            state = 1;
            setFill(Color.GREEN);
        }
    }

    public void setStateAndColor(int state) {
        this.state = state;
        if (this.state == 0) {
            this.setFill(Color.BLACK);
        } else {
            this.setFill(Color.GREEN);
        }
    }

    public int getState() {
        return state;
    }
}
