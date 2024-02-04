package Snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeApp extends Application implements EventHandler<ActionEvent> {


    private Pane root;
    private List<Rectangle> snakeSegments;
    private Rectangle redPixel;
    private static final int TILE_SIZE = 20;
    private static final int GRID_SIZE = 20;
    private Rectangle screenBounds;
    private int snakeX = 0;
    private int snakeY = 0;
    private int points = 0;
    private static final int INITIAL_SPEED = 300; 
    private int speed = INITIAL_SPEED;
    private Button buttonUp, buttonDown, buttonLeft, buttonRight;
    private Label pointsLabel;
    private Direction currentDirection = Direction.RIGHT; 

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SnakeFX");
        root = new Pane();
        Scene scene = new Scene(root, GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);

        createSnake();
        createScreenBounds();
        createRedPixel();
        createButtons();
        createPointsLabel();

  
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.getRoot().requestFocus();

        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialiser la timeline 
        KeyFrame keyFrame = new KeyFrame(Duration.millis(speed), this::moveSnake);
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void createSnake() {
        snakeSegments = new ArrayList<>();
        addSnakeSegment();
    }

    private void addSnakeSegment() {
        Rectangle segment = new Rectangle(TILE_SIZE, TILE_SIZE, Color.BLACK);
        snakeSegments.add(segment);
        root.getChildren().add(segment);
        updateSnake();
    }

    private void updateSnake() {
        for (int i = 0; i < snakeSegments.size(); i++) {
            Rectangle segment = snakeSegments.get(i);
            segment.setTranslateX(snakeX * TILE_SIZE);
            segment.setTranslateY(snakeY * TILE_SIZE);
        }
    }

    private void createScreenBounds() {
        screenBounds = new Rectangle(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE, Color.TRANSPARENT);
        screenBounds.setStroke(Color.BLACK);
        root.getChildren().add(screenBounds);
    }

    private void createRedPixel() {
        redPixel = new Rectangle(TILE_SIZE, TILE_SIZE, Color.RED);
        placeRedPixelRandomly();
        root.getChildren().add(redPixel);
    }

    private void placeRedPixelRandomly() {
        Random rand = new Random();
        int x, y;

        do {
            x = rand.nextInt(GRID_SIZE);
            y = rand.nextInt(GRID_SIZE);
        } while (isRedPixelOnSnake(x, y));

        redPixel.setTranslateX(x * TILE_SIZE);
        redPixel.setTranslateY(y * TILE_SIZE);
    }

    private boolean isRedPixelOnSnake(int x, int y) {
        for (Rectangle segment : snakeSegments) {
            if (x == (int) segment.getTranslateX() / TILE_SIZE && y == (int) segment.getTranslateY() / TILE_SIZE) {
                return true;
            }
        }
        return false;
    }

    private void createButtons() {
        buttonUp = new Button("Haut");
        buttonDown = new Button("Bas");
        buttonLeft = new Button("Gauche");
        buttonRight = new Button("Droite");

        buttonUp.setOnAction(this);
        buttonDown.setOnAction(this);
        buttonLeft.setOnAction(this);
        buttonRight.setOnAction(this);

        HBox buttonBox = new HBox(buttonUp, buttonDown, buttonLeft, buttonRight);
        StackPane buttonPane = new StackPane(buttonBox);
        buttonPane.setTranslateY(GRID_SIZE * TILE_SIZE + 10);
        root.getChildren().addAll(buttonPane);
    }

    private void createPointsLabel() {
        pointsLabel = new Label("Points: 0");
        pointsLabel.setTranslateY(GRID_SIZE * TILE_SIZE + 40);
        root.getChildren().add(pointsLabel);
    }

    private void moveSnake(ActionEvent event) {
       
        switch (currentDirection) {
            case UP:
                moveSnake(0, -1);
                break;
            case DOWN:
                moveSnake(0, 1);
                break;
            case LEFT:
                moveSnake(-1, 0);
                break;
            case RIGHT:
                moveSnake(1, 0);
                break;
        }

        pointsLabel.setText("Points: " + points);
    }

    private void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();

        switch (code) {
            case UP:
                currentDirection = Direction.UP;
                break;
            case DOWN:
                currentDirection = Direction.DOWN;
                break;
            case LEFT:
                currentDirection = Direction.LEFT;
                break;
            case RIGHT:
                currentDirection = Direction.RIGHT;
                break;
        }
    }

    private void moveSnake(int deltaX, int deltaY) {
        int newSnakeX = (snakeX + deltaX + GRID_SIZE) % GRID_SIZE;
        int newSnakeY = (snakeY + deltaY + GRID_SIZE) % GRID_SIZE;

        // collision avec un segment
        if (isCollisionWithSnake(newSnakeX, newSnakeY)) {
            endGame();
            return;
        }

        //collision avec le pixel rouge
        if (newSnakeX == (int) redPixel.getTranslateX() / TILE_SIZE &&
                newSnakeY == (int) redPixel.getTranslateY() / TILE_SIZE) {
            points++;
            placeRedPixelRandomly();
            addSnakeSegment();  
        }

        //position du serpent
        snakeX = newSnakeX;
        snakeY = newSnakeY;

        
        for (int i = snakeSegments.size() - 1; i > 0; i--) {
            Rectangle currentSegment = snakeSegments.get(i);
            Rectangle previousSegment = snakeSegments.get(i - 1);

            currentSegment.setTranslateX(previousSegment.getTranslateX());
            currentSegment.setTranslateY(previousSegment.getTranslateY());
        }

        //position du premier segment
        Rectangle headSegment = snakeSegments.get(0);
        headSegment.setTranslateX(snakeX * TILE_SIZE);
        headSegment.setTranslateY(snakeY * TILE_SIZE);
    }

    private boolean isCollisionWithSnake(int x, int y) {
        for (int i = 1; i < snakeSegments.size(); i++) {
            Rectangle segment = snakeSegments.get(i);
            if (x == (int) segment.getTranslateX() / TILE_SIZE && y == (int) segment.getTranslateY() / TILE_SIZE) {
                return true;
            }
        }
        return false;
    }

    private void endGame() {
        System.out.println("Game Over! Score: " + points);
        System.exit(0); 
    }

    @Override
    public void handle(ActionEvent event) {
        Button source = (Button) event.getSource();

        switch (source.getText()) {
            case "Haut":
                currentDirection = Direction.UP;
                break;
            case "Bas":
                currentDirection = Direction.DOWN;
                break;
            case "Gauche":
                currentDirection = Direction.LEFT;
                break;
            case "Droite":
                currentDirection = Direction.RIGHT;
                break;
        }
    }
}


