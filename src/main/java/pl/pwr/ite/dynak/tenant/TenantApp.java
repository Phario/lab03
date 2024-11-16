package pl.pwr.ite.dynak.tenant;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
public class TenantApp extends Application {
    @Override
    public void start(Stage stage) {
        var grid = new GridPane();
        var updateCounterButton = new Button("Update Counters");
        var payDueBillsButton = new Button("Pay due bills");
        var buttonController = new Button("Controller");
        grid.setGridLinesVisible(true);
        var scene = new Scene(grid, 400, 400);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(1, updateCounterButton, payDueBillsButton, buttonController);
        stage.setScene(scene);
        stage.setTitle("Property Managing Application");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
