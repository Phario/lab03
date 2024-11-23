package pl.pwr.ite.dynak.landlord;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import pl.pwr.ite.dynak.dataUtils.ControlOrderData;
import pl.pwr.ite.dynak.dataUtils.CounterStatesResults;
import pl.pwr.ite.dynak.dataUtils.TenantInfo;

import java.util.ArrayList;
import java.util.List;

public class LandlordApp extends Application{

    public void lazyBunsIdListReadout(Stage stage, List<Integer> idList) {
        var vbox = new VBox();
        vbox.setSpacing(5);
        for (Integer integer : idList) {
            Label idLabel = new Label(String.valueOf(integer));
            vbox.getChildren().add(idLabel);
        }
        var scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Lazybuns ID list readout");
        stage.show();
    }
    public void flatsReadout(Stage stage, List<Integer[]> idList) {
        var vbox = new VBox();
        vbox.setSpacing(10);
        for (Integer[] integer : idList) {
            Label idLabel = new Label(" ID: " + integer[0] +
                    " Counter state: " + " " + integer[1] +
                    " Tenant's ID: "+ " " + integer[2] +
                    " Heater Power: "+ " " + integer[3]);
            vbox.getChildren().add(idLabel);
        }
        var scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Flat info readout");
        stage.show();
    }
    public void tenantsReadout(Stage stage, List<TenantInfo> tenantList) {
        var vbox = new VBox();
        vbox.setSpacing(10);
        for (TenantInfo tenantInfo : tenantList) {
            Label tenantInfoLabel = new Label("ID: " + tenantInfo.tenantId()
                    + " Name: " + tenantInfo.name()
                    + " Flat Id: " + tenantInfo.flatId());
            vbox.getChildren().add(tenantInfoLabel);
        }
        var scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Tenant info readout");
        stage.show();
    }
    public void controlResultsReadout(Stage stage, List<CounterStatesResults> resultsList) {
        var vbox = new VBox();
        vbox.setSpacing(10);
        for (CounterStatesResults results : resultsList) {
            Label resultsLabel = new Label("Report ID: " + results.reportId()
                    + " Flat Id: " + results.flatId()
                    + " Tenant Id: " + results.tenantId()
                    + " Amount: " + results.amount()
                    + " Date: " + results.date());
            vbox.getChildren().add(resultsLabel);
        }
        var scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Control results readout");
        stage.show();
    }
    public void start(Stage stage){
        GridPane grid = new GridPane();
        var createFlatButton = new Button("Create Flat");
        var createTenantButton = new Button("Create Tenant");
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
        var fieldCODate = new TextField();
        var fieldCFHeatingPower = new TextField();
        var fieldCTFlatId = new TextField();
        var fieldCBReportId = new TextField();
        var fieldCBDate = new TextField();
        var fieldCRReportId = new TextField();
        var fieldAmount = new TextField();
        var fieldDFFlatId = new TextField();
        var fieldDTTenantId = new TextField();
        fieldName.setPromptText("Name");
        fieldRate.setPromptText("Rate");
        fieldCODate.setPromptText("Control Order Date");
        fieldCFHeatingPower.setPromptText("Heating Power");
        fieldCTFlatId.setPromptText("Flat ID");
        fieldCBReportId.setPromptText("Report ID");
        fieldCBDate.setPromptText("Date");
        fieldCRReportId.setPromptText("Report ID");
        fieldAmount.setPromptText("Amount");
        fieldDTTenantId.setPromptText("Tenant ID");
        fieldDFFlatId.setPromptText("Flat ID");
        grid.setHgap(5);
        grid.setVgap(5);
        grid.add(createFlatButton, 0, 0);           grid.add(fieldCFHeatingPower, 1, 0);
        grid.add(createTenantButton, 0, 1);         grid.add(fieldName, 1, 1); grid.add(fieldCTFlatId, 2, 1);
        grid.add(createControlOrderButton, 0, 2);   grid.add(fieldCODate, 1, 2);
        grid.add(createBillsButton, 0, 3);          grid.add(fieldCBReportId, 1, 3);grid.add(fieldCBDate, 2, 3);grid.add(fieldRate, 2, 3);
        grid.add(readControlResultsButton, 0, 4);   grid.add(fieldCRReportId, 1, 4);
        grid.add(readLazyBunsIdListButton, 0, 5);
        grid.add(readFlatsButton, 0, 6);
        grid.add(readTenantsButton, 0, 7);
        grid.add(updateMainCounterButton, 0, 8);    grid.add(fieldAmount, 1, 8);
        grid.add(destroyFlatButton, 0, 9);          grid.add(fieldDFFlatId, 1, 9);
        grid.add(destroyTenantButton, 0, 10);       grid.add(fieldDTTenantId, 1, 10);
        var scene =new Scene(grid, 500, 400);
        var landlord = new Landlord();
        createFlatButton.setOnAction(actionEvent -> {
            try {
                int cfHeatingPower = Integer.parseInt(fieldCFHeatingPower.getText());
                landlord.createFlat(cfHeatingPower);
                fieldCFHeatingPower.clear();
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                fieldCFHeatingPower.clear();
            }
        });
        createTenantButton.setOnAction(actionEvent -> {
            String name = fieldName.getText();
            int ctFlatId = Integer.parseInt(fieldCTFlatId.getText());
            landlord.createTenant(name, ctFlatId);
            fieldName.clear();
            fieldCTFlatId.clear();
        });
        createControlOrderButton.setOnAction(actionEvent -> {
            String coDate = fieldCODate.getText();
            landlord.createControlOrder(coDate);
            fieldCODate.clear();
        });
        createBillsButton.setOnAction(actionEvent -> {
            int cbReportId = Integer.parseInt(fieldCBReportId.getText());
            String cbDate = fieldCBDate.getText();
            int rate = Integer.parseInt(fieldRate.getText());
            landlord.createBills(cbReportId, cbDate, rate);
            fieldCBReportId.clear();
            fieldCBDate.clear();
            fieldRate.clear();
        });
        readControlResultsButton.setOnAction(actionEvent -> {
            int crReportId = Integer.parseInt(fieldCRReportId.getText());
            Stage second1Stage = new Stage();
            controlResultsReadout(second1Stage, landlord.readControlResults(crReportId));
            fieldCRReportId.clear();
        });
        readLazyBunsIdListButton.setOnAction(actionEvent -> {
            Stage secondStage = new Stage();
            lazyBunsIdListReadout(secondStage, landlord.readLazyBunsIds());
        });
        readFlatsButton.setOnAction(actionEvent -> {
            Stage second2Stage = new Stage();
            flatsReadout(second2Stage, landlord.readFlats());
        });
        readTenantsButton.setOnAction(actionEvent -> {
            Stage second3Stage = new Stage();
            tenantsReadout(second3Stage, landlord.readTenants());
        });
        updateMainCounterButton.setOnAction(actionEvent -> {
            int amount = Integer.parseInt(fieldAmount.getText());
            landlord.updateMainCounter(amount);
            fieldAmount.clear();
        });
        destroyFlatButton.setOnAction(actionEvent -> {
            int dfFlatId = Integer.parseInt(fieldDFFlatId.getText());
            landlord.destroyFlat(dfFlatId);
            fieldDFFlatId.clear();
        });
        destroyTenantButton.setOnAction(actionEvent -> {
            int dtTenantId = Integer.parseInt(fieldDTTenantId.getText());
            landlord.destroyTenant(dtTenantId);
            fieldDTTenantId.clear();
        });
        stage.setScene(scene);
        stage.setTitle("Landlord Application");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
