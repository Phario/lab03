package pl.pwr.ite.dynak.landlord;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LandlordApp extends Application implements LandlordDAO{
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
