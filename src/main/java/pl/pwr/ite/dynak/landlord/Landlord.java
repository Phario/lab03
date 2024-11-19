package pl.pwr.ite.dynak.landlord;

import pl.pwr.ite.dynak.dataRecords.CounterStatesResults;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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
    public void createTenant(String name, int flatId) {
        var tenantCreation = "INSERT INTO tenants(name, flatId) VALUES(?,?)";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmt = conn.prepareStatement(tenantCreation)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, flatId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createHeater(int heaterPower) {
        //empty for now, implement once I figure out how databases work lmao
    }

    @Override
    public void createControlOrder(String date) {
        var controlOrderCreation = "INSERT INTO controlOrder(date) VALUES(?)";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmt = conn.prepareStatement(controlOrderCreation)) {
            pstmt.setString(1, date);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createBills(int reportId, String date) {
        //TODO: Implement adding bills to billing history
        //sends bills to database
        double amount = 0.0;
        var billCreation = "INSERT INTO dueBills(amount, date) VALUES(?,?)";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmt = conn.prepareStatement(billCreation)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, date);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public CounterStatesResults readControlResults(int reportId) {
        var sql = "SELECT * FROM counterStatesReport WHERE reportId = ?";
        //takes the flatId as key and amount as value
        var counterStatesReport = new HashMap<Integer, Integer>();
        //attempt to read data
        CounterStatesResults counterStatesResults = null;
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);

            ResultSet rs = pstmt.executeQuery(sql);
            //saves date to variable
            String date = rs.getString("date");
            //saves amount of used heat in a flat and the flat's ID to a hashmap
            while (rs.next()) {
                counterStatesReport.put(rs.getInt("flatId"), rs.getInt("amount"));
            }
            //creates record of the hashmap and date
            counterStatesResults = new CounterStatesResults(counterStatesReport, date);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return counterStatesResults;
    }

    @Override
    public ArrayList<Integer> readLazyBunsIds() {
        var sqlUnpaidBills = "SELECT tenantId FROM dueBills";
        ArrayList<Integer> lazyBunsIdList = null;
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmt1 = conn.prepareStatement(sqlUnpaidBills)) {
            ResultSet rsLazyBunsIds = pstmt1.executeQuery();
            lazyBunsIdList = new ArrayList<>();
            while (rsLazyBunsIds.next()) {
                lazyBunsIdList.add(rsLazyBunsIds.getInt("tenantId"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lazyBunsIdList;
    }

    @Override
    public void readFlats() {

    }

    @Override
    public void readTenants() {

    }

    @Override
    public void updateFlatTenant(int tenantId) {

    }

    @Override
    public void destroyFlat(int flatId) {
        //TODO: add cascade Delete for tenant and his children
        var sqlFindFlatTenant = "SELECT tenantId FROM flats WHERE flatId = ?";
        var sqlUpdateTenantsFlat = "UPDATE tenants SET flatId = null WHERE flatId = ?";
        var sqlDestroyFlat = "DELETE FROM flats WHERE flatId = ?";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtDestroy = conn.prepareStatement(sqlDestroyFlat);
             var pstmtFindFlatTenant = conn.prepareStatement(sqlFindFlatTenant);
             var pstmtUpdateTenantsFlat = conn.prepareStatement(sqlUpdateTenantsFlat)) {
            //finds tenants in flats to be destroyed
            pstmtFindFlatTenant.setInt(1, flatId);
            ResultSet rsTenants = pstmtFindFlatTenant.executeQuery();
            //evicts the tenant before the flat is destroyed (sets his flatId to null)
            //if there was a tenant in it
            if (rsTenants.first()) {
                pstmtUpdateTenantsFlat.setInt(1, rsTenants.getInt("tenantId"));
                pstmtUpdateTenantsFlat.executeQuery();
            }
            //destroys flat with given id
            pstmtDestroy.setInt(1, flatId);
            pstmtDestroy.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void destroyTenant(int tenantId) {
        var sqlFindTenantsFlat = "SELECT tenantId FROM flats WHERE flatId = ?";
        var sqlUpdateFlatsTenant = "UPDATE flats SET tenantId = null WHERE tenantId = ?";
        var sqlDestroyTenant = "DELETE FROM tenants WHERE tenantId = ?";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtDestroyTenant = conn.prepareStatement(sqlDestroyTenant);
             var pstmtUpdateFlatsTenant = conn.prepareStatement(sqlUpdateFlatsTenant);
             var pstmtFindTenantsFlat = conn.prepareStatement(sqlFindTenantsFlat)) {
            pstmtDestroyTenant.setInt(1, tenantId);
            pstmtDestroyTenant.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static double calculateBill(double heatingRate, int counterState) {
        return (double)Math.round(heatingRate * counterState*100)/100;
    }
}
