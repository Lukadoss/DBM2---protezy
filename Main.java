import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Lukado on 23. 11. 2016.
 */
public class Main extends Application {

    /** Sirka okna */
    private static final int WIDTH = 1600;
    /** Vyska okna */
    private static final int HEIGHT = 900;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Leg bone cutting advisor");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setMinHeight(450);
        primaryStage.setMinWidth(700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
