import java.util.Collections;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GameRunner extends Application {


    //Public variables
    public double scalingConstant;

    //Private variables
    private Pane root;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Welcome to the snake game");

        int n = 50;
        int m = 50;

        root = new Pane();
        root.setPrefSize(n, m);

        drawGrid(n, m);

        Snake snake = new Snake(n, m, scalingConstant, Direction.Stop);
        drawSnake(snake);

        double width = scalingConstant * n;
        double height = scalingConstant * m;
        Scene scene = new Scene(root, width, height);


        Runnable snakeStepper = () ->{
            try {
                while (true) {
                    stepHandler(snake);
                    Collections.rotate(snake, 1);
                    Thread.sleep(80);
                }

            } catch (InterruptedException ie) {
            }
        };

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case UP:
                    if (snake.getDirr() != Direction.Down) {
                        snake.setCurrentDirection(Direction.Up);
                    }
                    break;
                case DOWN:
                    if (snake.getDirr() != Direction.Up) {
                        snake.setCurrentDirection(Direction.Down);
                    }
                    break;
                case LEFT:
                    if (snake.getDirr() != Direction.Right) {
                        snake.setCurrentDirection(Direction.Left);
                    }
                    break;
                case RIGHT:
                    if (snake.getDirr() != Direction.Left) {
                        snake.setCurrentDirection(Direction.Right);
                    }
                    break;

                case SPACE:

                    break;
                default:
                    break;
            }
        });

        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        Thread gameThread = new Thread(snakeStepper);
        gameThread.setDaemon(true);
        gameThread.start();
    }


    public void drawGrid(int x, int y) { // Colours background
        if (x > y) {
            scalingConstant = 800 / (x);
        } else {
            scalingConstant = 800 / (y);
        }
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Rectangle back = new Rectangle(i * scalingConstant, j * scalingConstant, scalingConstant,
                        scalingConstant);
                if (((i % 2 == 0) && (j % 2 == 0)) || ((i % 2 != 0) && (j % 2 != 0))) {
                    back.setFill(Color.rgb(136, 91, 242));
                } else {
                    back.setFill(Color.rgb(109, 74, 191));
                }
                root.getChildren().add(back);
            }
        }
    }

    public void drawSnake(Snake snake){
        for(int i = 0; i < snake.getLength()-1; i++){}
        root.getChildren().addAll(snake);
    }

    public void stepHandler(Snake snake){
        Platform.runLater(() -> {
            snake.moveSnake(snake.getDirr());
        });
    }
}
