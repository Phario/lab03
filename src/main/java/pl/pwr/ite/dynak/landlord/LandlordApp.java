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
        var createTenantButton = new Button("Create Tenant");
        //var createHeaterButton = new Button("Create Flat");
        var createControlOrderButton = new Button("Create Control Order");
        var createBillsButton = new Button("Create Bills");
        var readControlResultsButton = new Button("Read Control Results");
        var readLazyBunsIdListButton = new Button("Read Lazy Buns");
        var readFlatsButton = new Button("Read Flats");
        var readTenantsButton = new Button("Read Tenants");
        var updateMainCounterButton = new Button("Update Main Counter");
        var destroyFlatButton = new Button("Destroy Flat");
        var destroyTenantButton = new Button("Destroy Tenant");
        var fieldName = new TextField();
        var fieldRate = new TextField();
        var fieldCODate
        grid.add(createFlatButton, 0, 0);
        grid.add(createTenantButton, 0, 1);
        grid.add(createControlOrderButton, 0, 2);
        grid.add(createBillsButton, 0, 3);
        grid.add(readControlResultsButton, 0, 4);
        grid.add(readLazyBunsIdListButton, 0, 5);
        grid.add(readFlatsButton, 0, 6);
        grid.add(readTenantsButton, 0, 7);
        grid.add(updateMainCounterButton, 0, 8);
        grid.add(destroyFlatButton, 0, 9);
        grid.add(destroyTenantButton, 0, 10);
        var scene =new Scene(grid, 400, 300);
        var Landlord = new Landlord();
        createFlatButton.setOnAction(event -> Landlord.createFlat(fieldCFHeatingPower));
        createTenantButton.setOnAction(actionEvent -> Landlord.createTenant(fieldName, fieldCTFlatId));
        createControlOrderButton.setOnAction(actionEvent -> Landlord.createControlOrder(fieldCODate));
        createBillsButton.setOnAction(actionEvent -> Landlord.createBills(fieldCBReportId, fieldCBDate, fieldRate));
        readControlResultsButton.setOnAction(actionEvent -> Landlord.readControlResults(fieldCRReportId));
        readLazyBunsIdListButton.setOnAction(actionEvent -> Landlord.readLazyBunsIds());
        readFlatsButton.setOnAction(actionEvent -> Landlord.readFlats());
        readTenantsButton.setOnAction(actionEvent -> Landlord.readTenants());
        updateMainCounterButton.setOnAction(actionEvent -> Landlord.updateMainCounter(fieldAmount));
        destroyFlatButton.setOnAction(actionEvent -> Landlord.destroyFlat(fieldDFFlatId));
        destroyTenantButton.setOnAction(actionEvent -> Landlord.destroyTenant(fieldDTTenantId));
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
