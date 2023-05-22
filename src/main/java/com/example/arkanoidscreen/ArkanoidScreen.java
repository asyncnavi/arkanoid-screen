package com.example.arkanoidscreen;

import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Represents the Arkanoid game screen.
 */
public class ArkanoidScreen extends Application {
    private int rows;
    private int columns;
    private int currentScore;
    private int highScore;
    private int level;
    private double ballX;
    private double ballY;
    private double paddleX;
    private final int PADDLE_WIDTH = 80;
    private final int PADDLE_HEIGHT = 15;
    private final int SCREEN_PADDING = 10;
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;
    private static final double BALL_RADIUS = 10.0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        showUserDialog();

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawGame(gc);


        StackPane pane = new StackPane();
        pane.getChildren().add(canvas);
        // Set up the scene and show the stage
        stage.setScene(new Scene(pane));
        stage.setTitle("Arkanoid");
        stage.show();
    }

    /**
     * Shows a dialog box to get user input for game parameters.
     */
    private void showUserDialog() {
        // Prompt for rows

        TextInputDialog rowDialog = new TextInputDialog();
        rowDialog.setTitle("Arkanoid Setup");
        rowDialog.setHeaderText("Enter Game Parameters");
        rowDialog.setContentText("Rows (1-10):");
        Optional<String> result = rowDialog.showAndWait();

        result.ifPresent(rowsInput -> {
            try {
                rows = Integer.parseInt(rowsInput);
                if (rows < 1 || rows > 10) {
                    showInputError("Invalid numbers of rows. Please enter a value between 1 and 10");
                    showUserDialog();
                    return;
                }
            } catch (NumberFormatException e) {
                showInputError("Invalid input. Please enter a valid number");
                showUserDialog();
                return;
            }

        });

        // Prompt for columns
        TextInputDialog columnDialog = new TextInputDialog();
        columnDialog.setTitle("Arkanoid Setup");
        columnDialog.setHeaderText("Enter Game Parameters");
        columnDialog.setContentText("Columns (1-10):");
        Optional<String> columnResult = columnDialog.showAndWait();
        columnResult.ifPresent(columnsInput -> {
            try {
                columns = Integer.parseInt(columnsInput);
                if (columns < 1 || columns > 10) {
                    showInputError("Invalid number of columns. Please enter a value between 1 and 10.");
                    showUserDialog();
                    return;
                }
            } catch (NumberFormatException e) {
                showInputError("Invalid input. Please enter a valid number.");
                showUserDialog();
                return;
            }
        });

        // Prompt for scores
        TextInputDialog scoreDialog = new TextInputDialog();
        scoreDialog.setTitle("Arkanoid Setup");
        scoreDialog.setHeaderText("Enter Game Parameters");
        scoreDialog.setContentText("Current Score:");
        Optional<String> scoreResult = scoreDialog.showAndWait();
        scoreResult.ifPresent(scoreInput -> {
            try {
                currentScore = Integer.parseInt(scoreInput);
            } catch (NumberFormatException e) {
                showInputError("Invalid input. Please enter a valid number.");
                showUserDialog();
                return;
            }

        });


        // Prompt for high score
        TextInputDialog highScoreDialog = new TextInputDialog();
        highScoreDialog.setTitle("Arkanoid Setup");
        highScoreDialog.setHeaderText("Enter Game Parameters");
        highScoreDialog.setContentText("High Score:");
        Optional<String> highScoreResult = highScoreDialog.showAndWait();
        highScoreResult.ifPresent(highScoreInput -> {
            try {
                highScore = Integer.parseInt(highScoreInput);
                if (currentScore > highScore) {
                    highScore = currentScore;
                }
            } catch (NumberFormatException e) {
                showInputError("Invalid input. Please enter a valid number.");
                showUserDialog();
                return;
            }
        });


        // Prompt for level
        TextInputDialog levelDialog = new TextInputDialog();
        levelDialog.setTitle("Arkanoid Setup");
        levelDialog.setHeaderText("Enter Game Parameters");
        levelDialog.setContentText("Level:");
        Optional<String> levelResult = levelDialog.showAndWait();
        levelResult.ifPresent(levelInput -> {
            try {
                level = Integer.parseInt(levelInput);
            } catch (NumberFormatException e) {
                showInputError("Invalid input. Please enter a valid number.");
                showUserDialog();
                return;
            }

        });

        // Prompt for ball position
        TextInputDialog ballPositionDialog = new TextInputDialog();
        ballPositionDialog.setTitle("Arkanoid Setup");
        ballPositionDialog.setHeaderText("Enter Game Parameters");
        ballPositionDialog.setContentText("Ball Position (X Y):");
        Optional<String> ballPositionResult = ballPositionDialog.showAndWait();
        ballPositionResult.ifPresent(ballPositionInput -> {
            try {
                String[] position = ballPositionInput.split(" ");
                ballX = Double.parseDouble(position[0]);
                ballY = Double.parseDouble(position[1]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                showInputError("Invalid input. Please enter valid X and Y positions.");
                showUserDialog();
                return;
            }
        });


        TextInputDialog paddlePositionDialog = new TextInputDialog();
        paddlePositionDialog.setTitle("Arkanoid Setup");
        paddlePositionDialog.setHeaderText("Enter Game Parameters");
        paddlePositionDialog.setContentText("Paddle Position (X):");
        Optional<String> paddlePositionResult = paddlePositionDialog.showAndWait();
        paddlePositionResult.ifPresent(paddlePositionInput -> {
            try {
                paddleX = Double.parseDouble(paddlePositionInput);
            } catch (NumberFormatException e) {
                showInputError("Invalid input. Please enter a valid number.");
                showUserDialog();
                return;
            }

            // All inputs are valid, proceed to draw the game

        });
    }

    /**
     * Shows an error message dialog.
     *
     * @param message the error message to display
     */
    private void showInputError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Draws the game on the provided GraphicsContext.
     *
     * @param gc the GraphicsContext to draw on
     */
    private void drawGame(GraphicsContext gc) {
        // Set the background color
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Calculate widths based on ratios
        double gameRegionWidth = WINDOW_WIDTH * 0.8;
        double displayRegionWidth = WINDOW_WIDTH * 0.2;

        // Calculate heights based on ratios
        double gameRegionHeight = WINDOW_HEIGHT;
        double displayRegionHeight = WINDOW_HEIGHT;

        // Draw display region
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(gameRegionWidth, 0, displayRegionWidth, displayRegionHeight);

        // Calculate heights for scores and level
        double scoreLevelHeight = displayRegionHeight * 0.2;
        double scoreLevelPadding = displayRegionHeight * 0.05;

        // Draw scores and level
        gc.setFill(Color.WHITE);
        gc.fillText("Current Score: " + currentScore, gameRegionWidth + SCREEN_PADDING, scoreLevelPadding + scoreLevelHeight);
        gc.fillText("High Score: " + highScore, gameRegionWidth + SCREEN_PADDING, scoreLevelPadding + scoreLevelHeight * 2);
        gc.fillText("Level: " + level, gameRegionWidth + SCREEN_PADDING, scoreLevelPadding + scoreLevelHeight * 3);

        // Adjust game region dimensions
        gameRegionHeight -= scoreLevelHeight * 3 + scoreLevelPadding * 4;
        gameRegionWidth -= SCREEN_PADDING * 2;

        // Draw game region
        int brickPadding = 5;
        int brickStartY = SCREEN_PADDING * 2;
        int maxBrickWidth = (int) (gameRegionWidth / columns) - brickPadding;
        int maxBrickHeight = (int) ((gameRegionHeight - BALL_RADIUS) / rows) - brickPadding;

        for (int i = 0; i < rows; i++) {
            int brickY = brickStartY + i * (maxBrickHeight + brickPadding);
            Color brickColor = i % 2 == 0 ? Color.BLUE : Color.YELLOW; // Change color based on row index
            for (int j = 0; j < columns; j++) {
                int brickX = SCREEN_PADDING + j * (maxBrickWidth + brickPadding);
                gc.setFill(brickColor);
                gc.fillRect(brickX, brickY, maxBrickWidth, maxBrickHeight);
            }
        }


        // Draw ball
        gc.setFill(Color.RED);
        gc.fillOval(SCREEN_PADDING + ballX, SCREEN_PADDING + ballY, BALL_RADIUS, BALL_RADIUS);

        // Draw paddle
        gc.setFill(Color.GREEN);
        double paddleY = WINDOW_HEIGHT - PADDLE_HEIGHT - SCREEN_PADDING;
        double paddleYPosition = paddleY;
        gc.fillRect(paddleX, paddleYPosition, PADDLE_WIDTH, PADDLE_HEIGHT);
    }
}
