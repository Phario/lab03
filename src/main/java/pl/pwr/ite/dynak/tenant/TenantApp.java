package pl.pwr.ite.dynak.tenant;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import pl.pwr.ite.dynak.dataUtils.DueBillInfo;
import pl.pwr.ite.dynak.dataUtils.InvalidIdException;
import pl.pwr.ite.dynak.dataUtils.TenantInfo;

import java.util.List;

import static pl.pwr.ite.dynak.tenant.Tenant.checkIdValidity;
public class TenantApp extends Application {
    public void dueBillsReadout(Stage stage, List<DueBillInfo> dueBillInfoList) {
        var vbox = new VBox();
        vbox.setSpacing(10);
        for (DueBillInfo dueBillInfo : dueBillInfoList) {
            Label tenantInfoLabel = new Label("Bill ID: " + dueBillInfo.billId()
                    + " Amount: " + dueBillInfo.amount()
                    + " Date: " + dueBillInfo.date());
            vbox.getChildren().add(tenantInfoLabel);
        }
        var scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Due bills info readout");
        stage.show();
    }
    public void launchTenantApp(Stage stage, int tenantId) {
        var tenant = new Tenant(tenantId);
        var grid = new GridPane();
        var createPaymentButton = new Button("Create payment");
        var readDueBillsButton = new Button("Read due bills");
        var updateCounterButton = new Button("Update counter");
        var destroyBillsButton = new Button("Remove old bills");
        var fieldBillId = new TextField();
        var fieldPaymentDate = new TextField();
        var fieldHeatingTime = new TextField();
        var appScene = new Scene(grid, 500, 400);
        fieldHeatingTime.setPromptText("Heating time");
        fieldPaymentDate.setPromptText("Payment date");
        fieldBillId.setPromptText("Bill ID");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(createPaymentButton, 0, 0); grid.add(fieldBillId, 1, 0); grid.add(fieldPaymentDate, 2, 0);
        grid.add(readDueBillsButton, 0, 1);
        grid.add(updateCounterButton, 0, 2); grid.add(fieldHeatingTime, 1, 2);
        grid.add(destroyBillsButton, 0, 3);
        createPaymentButton.setOnAction(actionEvent -> {
            int billId = Integer.parseInt(fieldBillId.getText());
            String paymentDate = fieldPaymentDate.getText();
            tenant.createPayment(billId, paymentDate);
            fieldBillId.clear();
            fieldPaymentDate.clear();
        });
        readDueBillsButton.setOnAction(actionEvent -> {
            Stage secondStage = new Stage();
            dueBillsReadout(secondStage, tenant.readDueBills());
        });
        updateCounterButton.setOnAction(actionEvent -> {
            int heatingTime = Integer.parseInt(fieldHeatingTime.getText());
            tenant.updateCounter(heatingTime);
            fieldHeatingTime.clear();
        });
        destroyBillsButton.setOnAction(actionEvent -> tenant.destroyBills());
        stage.setScene(appScene);
        stage.setTitle("Tenant Application, ID: " + tenantId);
        stage.show();
    }
    @Override
    public void start(Stage stage) {
        var loginGrid = new GridPane();
        var loginScene = new Scene(loginGrid, 300, 100);
        var tenantIdButton = new Button("Submit");
        var fieldSubmitId = new TextField();
        var loginLabel = new Label("Enter your ID:");
        loginGrid.setHgap(5);
        loginGrid.setVgap(5);
        loginGrid.add(loginLabel, 0, 0);
        loginGrid.add(fieldSubmitId, 1, 0);
        loginGrid.add(tenantIdButton, 0, 1);
        tenantIdButton.setOnAction(event -> {
            int tenantId = Integer.parseInt(fieldSubmitId.getText());
            try {
                checkIdValidity(tenantId);
                launchTenantApp(stage, tenantId);
            } catch (InvalidIdException e) {
                System.out.println(e.getMessage());
            }

        });
        stage.setTitle("Tenant Login");
        stage.setScene(loginScene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
