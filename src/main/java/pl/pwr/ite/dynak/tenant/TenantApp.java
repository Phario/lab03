package pl.pwr.ite.dynak.tenant;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
public class TenantApp extends Application implements TenantDAO {
    @Override
    public void start(Stage stage) {
        var grid = new GridPane();
        var buttonTenant = new Button("Tenant");
        var buttonLandlord = new Button("Landlord");
        var buttonController = new Button("Controller");
        //grid.setGridLinesVisible(true);
        var scene = new Scene(grid, 400, 400);
        grid.addRow(1, buttonTenant, buttonLandlord, buttonController);
        stage.setScene(scene);
        stage.setTitle("Property Managing Application");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
