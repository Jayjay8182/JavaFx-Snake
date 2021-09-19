package com.game.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Coord {
    public int x;
    public int y;
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Snake extends Application {
    public int dir = 1;
    public int x = 0;
    public int y = 0;
    public int i = 0;

    int min = 1;
    int max = 19;

    public int boardSize = 500;
    public int playerSize = 25;
    public int playerScore = 0;

    public Rectangle player = new Rectangle();
    public Rectangle food = new Rectangle();
    public List<Rectangle> playerParts = new ArrayList();
    public Group root;

    Random random = new Random();
    Coord playerPosition = new Coord(10, 10);
    Coord foodPosition = new Coord(random.nextInt(max + min) + min, random.nextInt(max + min) + min);

    /* 1 = left
       2 = up
       3 = right
       4 = down
    */

    @Override
    public void start(Stage stage) {
        player.setX((playerPosition.x-1) *25);
        player.setY((playerPosition.y-1) *25);
        player.setWidth(playerSize);
        player.setHeight(playerSize);
        player.setFill(Color.AQUA);

        food.setX((foodPosition.x-1) *25);
        food.setY((foodPosition.y-1) *25);
        food.setWidth(playerSize-5);
        food.setHeight(playerSize-5);
        food.setFill(Color.RED);

        root = new Group(player, food);

        Scene scene = new Scene(root, boardSize, boardSize);

        stage.setTitle("Snake!");
        stage.setScene(scene);
        scene.setFill(Color.web("#292929"));
        stage.show();

        foodEaten();
        foodEaten();
        foodEaten();

        if(playerPosition.x == foodPosition.x && playerPosition.y == foodPosition.y){
            foodEaten();
        }

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                case UP:
                    if(dir != 4){
                        dir = 2;
                    }
                    break;
                case LEFT:
                case A:
                    if(dir != 3) {
                        dir = 1;
                    }
                    break;
                case DOWN:
                case S:
                    if(dir != 2) {
                        dir = 4;
                    }
                    break;
                case RIGHT:
                case D:
                    if(dir != 1) {
                        dir = 3;
                    }
                    break;
            }
        });

        Timeline GameFrames = new Timeline(
                new KeyFrame(Duration.seconds(0.075),
                        event -> {
                            if(!playerParts.isEmpty()){
                                playerParts.get(0).setX((playerPosition.x-1)*25);
                                playerParts.get(0).setY((playerPosition.y-1)*25);
                            }

                            for (int i = playerParts.size() - 1; i >= 1; i--) {
                                playerParts.get(i).setX(playerParts.get(i - 1).getX());
                                playerParts.get(i).setY(playerParts.get(i - 1).getY());
                            }

                            switch (dir){
                                // left
                                case 1:
                                    x -= playerSize;
                                    playerPosition.x -= 1;
                                    break;
                                // up
                                case 2:
                                    y -= playerSize;
                                    playerPosition.y -= 1;
                                    break;
                                // right
                                case 3:
                                    x += playerSize;
                                    playerPosition.x += 1;
                                    break;
                                // down
                                case 4:
                                    y += playerSize;
                                    playerPosition.y += 1;
                                    break;
                            }

                            for(Rectangle i : playerParts){
                                if((playerPosition.x-1)*25 == (int)i.getX() && (playerPosition.y-1)*25 == (int)i.getY()){
                                    restart();
                                }
                            }

                            if(playerPosition.x == foodPosition.x && playerPosition.y == foodPosition.y){
                                foodEaten();
                                playerScore++;
                                stage.setTitle("Snake! "+playerScore);
                            }

                            if (playerPosition.x > 20 || playerPosition.x < 1){
                                restart();
                            }
                            if (playerPosition.y > 20 || playerPosition.y < 1){
                                restart();
                            }
                            refreshBoard();
                        }));

        GameFrames.setCycleCount(Timeline.INDEFINITE);
        GameFrames.play();
    }

    public void refreshBoard() {
        player.setX((playerPosition.x-1) *25);
        player.setY((playerPosition.y-1) *25);

        food.setX((foodPosition.x-1) *25);
        food.setY((foodPosition.y-1) *25);
    }

    public void restart(){
        dir = 1;
        x = 0;
        y = 0;
        i = 0;
        playerScore = 0;

        for(Rectangle i : playerParts){
            root.getChildren().remove(i);
        }

        playerParts.clear();
        playerPosition = new Coord(10, 10);
        foodPosition = new Coord(random.nextInt(max + min) + min, random.nextInt(max + min) + min);

        foodEaten();
        foodEaten();
        foodEaten();

    }

    public void foodEaten(){
        foodPosition = new Coord(random.nextInt(max + min) + min, random.nextInt(max + min) + min);
        playerParts.add(i, new Rectangle());
        playerParts.get(i).setWidth(playerSize-5);
        playerParts.get(i).setHeight(playerSize-5);
        playerParts.get(i).setFill(Color.BLUE);

        switch (dir) {
            // left
            case 1:
                playerParts.get(i).setX(((playerPosition.x-1)*25) + playerSize);
                playerParts.get(i).setY((playerPosition.y-1)*25);
                break;
            // up
            case 2:
                playerParts.get(i).setX((playerPosition.x-1)*25);
                playerParts.get(i).setY(((playerPosition.y-1)*25) + playerSize);
                break;
            // right
            case 3:
                playerParts.get(i).setX(((playerPosition.x-1)*25) - playerSize);
                playerParts.get(i).setY((playerPosition.y-1)*25);
                break;
            // down
            case 4:
                playerParts.get(i).setX((playerPosition.x-1)*25);
                playerParts.get(i).setY(((playerPosition.y-1)*25) - playerSize);
                break;
        }
        root.getChildren().add(playerParts.get(i));
        i++;
    }

    public static void main(String[] args) {
        launch();
    }
}