package pl.pwr.ite.dynak.landlord;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Landlord implements LandlordDAO {
    String databaseURL = "jdbc:sqlite:propertyData.sqlite";

    @Override
    public void createFlat(int heatingPower) {
        var flatCreation = "INSERT INTO flats(Counter,heaterPower) VALUES(?,?)";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmt = conn.prepareStatement(flatCreation)) {
            pstmt.setInt(1, 0);
            pstmt.setInt(2, heatingPower);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createTenant(String name) {

    }

    @Override
    public void createHeater(int heaterPower) {

    }

    @Override
    public void createControlRequest(int date) {

    }

    @Override
    public void createBill(int flatId, int tenantId, int counterState, int date) {

    }

    @Override
    public void readControlResults(int reportId) {

    }

    @Override
    public void updateFlatTenant(int tenantId) {

    }

    @Override
    public void destroyFlat(int flatId) {

    }

    @Override
    public void destroyTenant(int tenantId) {

    }
}
