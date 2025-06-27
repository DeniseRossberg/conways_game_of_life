package com.example.gameoflife;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;


public class Game extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #202020;");
        Field field = new Field(80, 40, 20);
        root.setCenter(field);

        HBox buttonArea = new HBox();
        setButtonArea(buttonArea);
        setButtonSize(buttonArea);
        setButtonEvents(buttonArea, field, stage);
        root.setBottom(buttonArea);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.setTitle("Conway's Game Of Life");
        stage.show();
    }

    private void setButtonArea(HBox buttonArea) {
        Button clearField = new Button("clear field");
        Button randomField = new Button("random field");
        Button nextGeneration = new Button("next generation");
        Button start = new Button("start animation");
        Button stop = new Button("stop animation");
        Button end = new Button("end game");
        buttonArea.getChildren().addAll(clearField, randomField, nextGeneration, start, stop, end);
        buttonArea.setAlignment(Pos.CENTER);
        buttonArea.setPadding(new Insets(0, 0, 50, 0));
        buttonArea.setSpacing(50);
    }

    private void setButtonSize(HBox buttonArea) {
        Platform.runLater(() -> {
            double width = 0.0;
            for (Node node : buttonArea.getChildren()) {
                Button button = (Button) node;
                if (button.getWidth() > width) {
                    width = button.getWidth();
                }
            }
            for (Node node : buttonArea.getChildren()) {
                Button button = (Button) node;
                button.setPrefWidth(width);
                button.setMinWidth(width);
                button.setMaxWidth(width);

            }
        });
    }

    private void setButtonEvents(HBox buttonArea, Field field, Stage stage) {
        for (Node node : buttonArea.getChildren()) {
            Button button = (Button) node;
            switch (button.getText()) {
                case "clear field":
                    button.setOnAction(actionEvent -> field.clearField());
                    break;
                case "random field":
                    button.setOnAction(actionEvent -> field.generateRandomField());
                    break;
                case "next generation":
                    button.setOnAction(actionEvent -> field.setNextGeneration());
                    break;
                case "start animation":
                    button.setOnAction(actionEvent -> {
                        if (!field.isEmpty()) {
                            field.startAnimation();
                            setDisableControls(true, buttonArea, field);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("empty field");
                            alert.setHeaderText("Please fill the field before starting the animation!");
                            alert.showAndWait();
                        }
                    });

                    break;
                case "stop animation":
                    button.setOnAction(actionEvent -> {
                        field.stopAnimation();
                        setDisableControls(false, buttonArea, field);
                    });
                    break;
                case "end game":
                    button.setOnAction(actionEvent -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("end game");
                        alert.setHeaderText("Do you really want to end the game?");
                        if (alert.showAndWait().get() == ButtonType.OK) {
                            stage.close();
                        }
                    });
                    break;
            }
        }
    }

    private void setDisableControls(boolean disable, HBox buttonArea, Field field) {
        for (Node node : buttonArea.getChildren()) {
            Button button = (Button) node;
            if (!button.getText().equals("stop animation")) {
                button.setDisable(disable);
            }
        }
        field.setFieldInteraction(disable);
    }
}
