package com.example.gameoflife;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.Random;

public class Field extends GridPane {
    private Cell[][] matrix;
    private int width;
    private int height;
    private int cellSize;
    private Timeline timeline;
    private KeyFrame keyFrame;

    public Field(int width, int height, int cellSize) {
        super();
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        matrix = new Cell[width][height];
        keyFrame = new KeyFrame(Duration.seconds(0.7), event -> setNextGeneration());
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        this.setAlignment(Pos.CENTER);
        this.setVgap(1);
        this.setHgap(1);
        fillMatrix();
        setMouseEvents();
    }

    private void fillMatrix() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrix[i][j] = new Cell(cellSize);
                this.add(matrix[i][j], i, j);
            }
        }
    }

    private void setMouseEvents() {
        this.setOnDragDetected(
                event -> {
                    if (event.isPrimaryButtonDown()) {
                        event.consume();
                        this.startFullDrag();
                    }
                });
    }

    public void setNextGeneration() {
        int[][] nextGeneration = getNextGeneration();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrix[i][j].setStateAndColor(nextGeneration[i][j]);
            }
        }
    }

    private int[][] getNextGeneration() {
        int[][] nextGeneration = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int state = matrix[i][j].getState();
                int aliveNeighbors = countAliveNeighbors(i, j);

                if (state == 1 && (aliveNeighbors < 2 || aliveNeighbors > 3)) {
                    nextGeneration[i][j] = 0;
                } else if (state == 0 && aliveNeighbors == 3) {
                    nextGeneration[i][j] = 1;
                } else {
                    nextGeneration[i][j] = state;
                }
            }
        }
        return nextGeneration;
    }

    private int countAliveNeighbors(int x, int y) {
        int[][] directions = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};
        int aliveNeighbors = 0;
        for (int dir[] : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                aliveNeighbors += matrix[nx][ny].getState();
            }
        }
        return aliveNeighbors;
    }

    public void startAnimation(){
       timeline.play();
    }

    public void stopAnimation(){
        timeline.stop();
    }

    public void clearField() {
        for (Cell[] line : matrix) {
            for (Cell cell : line) {
                cell.setStateAndColor(0);
            }
        }
    }

    public void generateRandomField() {
        Random rand = new Random();
        for (Cell[] line : matrix) {
            for (Cell cell : line) {
                cell.setStateAndColor(rand.nextInt(2));
            }
        }
    }

    public boolean isEmpty(){
        for(Cell[] line: matrix){
            for(Cell cell : line){
                if(cell.getState() == 1){
                    return false;
                }
            }
        }
        return true;
    }

    public void setFieldInteraction(boolean disable){
        for (Cell[] row : matrix) {
            for (Cell cell : row) {
                cell.setDisable(disable);
            }
        }
    }
}
