package pl.pwr.ite.dynak.controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwr.ite.dynak.dataUtils.ControlOrderData;
import pl.pwr.ite.dynak.dataUtils.InvalidIdException;

import static pl.pwr.ite.dynak.controller.Controller.checkReportIdValidity;
import static pl.pwr.ite.dynak.landlord.Landlord.checkFlatIdValidity;

public class ControllerApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(ControllerApp.class);
    public void orderReadout(Stage stage, ControlOrderData controlOrderData) {
        int orderId = controlOrderData.orderId();
        String orderDate = controlOrderData.date();
        var borderPane = new BorderPane();
        var orderData = new Label("Order ID:" + orderId + "\nOrder date:" +orderDate);
        var scene = new Scene(borderPane, 200, 50);
        borderPane.setCenter(orderData);
        stage.setScene(scene);
        stage.setTitle("Order Readout");
        stage.show();
    }
    @Override
    public void start(Stage stage) {
        var controller = new Controller();
        GridPane grid = new GridPane();
        var scene = new Scene(grid, 460, 200);
        grid.setHgap(5);
        grid.setVgap(5);
        var createReportButton = new Button("Create Report");
        var readControlOrderButton = new Button("Read control order");
        var updateCountersButton = new Button("Update counters");
        var deleteControlOrderButton = new Button("Delete control order");
        var fieldDate = new TextField();
        var fieldCRReportId = new TextField();
        var fieldDRReportId = new TextField();
        fieldDate.setPromptText("Date");
        fieldCRReportId.setPromptText("Order ID");
        fieldDRReportId.setPromptText("Order ID");
        grid.add(createReportButton, 0, 0); grid.add(fieldDate, 1, 0); grid.add(fieldCRReportId, 2, 0);
        grid.add(readControlOrderButton, 0, 1);
        grid.add(updateCountersButton, 0, 2);
        grid.add(deleteControlOrderButton, 0, 3); grid.add(fieldDRReportId, 1, 3);
        createReportButton.setOnAction(actionEvent -> {
            String date = fieldDate.getText();
            int reportId = Integer.parseInt(fieldCRReportId.getText());
            controller.createReport(date, reportId);
            fieldDate.clear();
            fieldCRReportId.clear();
        });
        readControlOrderButton.setOnAction(actionEvent -> {
            Stage secondStage = new Stage();
            orderReadout(secondStage, controller.readControlOrder());
        });
        updateCountersButton.setOnAction(actionEvent -> controller.updateCounters());
        deleteControlOrderButton.setOnAction(actionEvent -> {
            int orderId = Integer.parseInt(fieldDRReportId.getText());
            controller.deleteControlOrder(orderId);
            fieldDRReportId.clear();
        });
        stage.setTitle("Controller Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
