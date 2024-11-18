package pl.pwr.ite.dynak.landlord;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.EventHandler;

public class LandlordApp extends Application{

    public void start(Stage stage) {
        GridPane grid = new GridPane();
        var createFlatButton = new Button("Create Flat");
        grid.add(createFlatButton, 0, 0);
        var scene =new Scene(grid, 400, 300);
        var Landlord = new Landlord();
        createFlatButton.setOnAction(event -> Landlord.createFlat(20));
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
