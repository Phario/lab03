package pl.pwr.ite.dynak.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwr.ite.dynak.dataUtils.ControlOrderData;
import pl.pwr.ite.dynak.dataUtils.InvalidIdException;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller implements ControllerDAO{
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    static String databaseURL = "jdbc:sqlite:propertyData.sqlite";

    public static void checkReportIdValidity(int reportId) throws InvalidIdException {
        var sqlGetIds = "SELECT reportId FROM counterStatesReport";
        boolean reportIdValidity = false;
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtGetIds = conn.prepareStatement(sqlGetIds)) {
            ResultSet rs = pstmtGetIds.executeQuery();
            while (rs.next()) {
                if (reportId == rs.getInt("reportId")) {
                    reportIdValidity = true;
                }
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        if (!reportIdValidity) {
            throw new InvalidIdException();
        }
    }
    @Override
    public void createReport(String date, int orderId) {
        var sqlGetCounterStates = "SELECT flatId, tenantId, counter FROM flats";
        var sqlCreateReport = "INSERT INTO counterStatesReport(reportId, tenantId, amount, date, flatId) VALUES (?,?,?,?,?)";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtGetCounterStates = conn.prepareStatement(sqlGetCounterStates);
             var pstmtCreateReport = conn.prepareStatement(sqlCreateReport)) {
            ResultSet rsCounterStates = pstmtGetCounterStates.executeQuery();
            pstmtCreateReport.setInt(1, orderId);
            pstmtCreateReport.setString(4, date);
            while (rsCounterStates.next()) {
                pstmtCreateReport.setInt(2, rsCounterStates.getInt("tenantId"));
                pstmtCreateReport.setInt(3, rsCounterStates.getInt("counter"));
                pstmtCreateReport.setInt(5, rsCounterStates.getInt("flatId"));
                pstmtCreateReport.addBatch();
            }
            pstmtCreateReport.executeBatch();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }
    @Override
    public ControlOrderData readControlOrder() {
        var sql = "SELECT * FROM controlOrder";
        //attempt to read data
        ControlOrderData controlOrderData = null;
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            int orderId = rs.getInt("orderId");
            String date = rs.getString("date");
            controlOrderData = new ControlOrderData(orderId, date);
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return controlOrderData;
    }
    @Override
    public void updateCounters() {
        var sql = "UPDATE flats SET counter = 0";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }
    @Override
    public void deleteControlOrder(int orderId) {
        var sql = "DELETE FROM controlOrder WHERE orderId = ?";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.execute();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }
}
