package pl.pwr.ite.dynak.tenant;
import pl.pwr.ite.dynak.dataRecords.DueBillInfo;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class Tenant implements TenantDAO {
    private int tenantId;
    @Getter
    @Setter
    private String databaseURL = "jdbc:sqlite:propertyData.sqlite";
    @Override
    public void createBribe(double amount, int billId) {
        var sqlCreateBribe = "";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtCreateBribe = conn.prepareStatement(sqlCreateBribe)) {

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<DueBillInfo> readDueBills() {
        DueBillInfo dueBillInfo;
        ArrayList<DueBillInfo> dueBillInfoList = new ArrayList<>();
        var sqlGetDueBillData = "SELECT billId, amount, date FROM dueBills WHERE tenantId = ?";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtUpdateCounter = conn.prepareStatement(sqlGetDueBillData)) {
            pstmtUpdateCounter.setInt(1, tenantId);
            ResultSet rs = pstmtUpdateCounter.executeQuery();
            while (rs.next()) {
                dueBillInfo = new DueBillInfo(rs.getInt("billId"),
                                              rs.getDouble("amount"),
                                              rs.getString("date"));
                dueBillInfoList.add(dueBillInfo);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dueBillInfoList;
    }

    @Override
    public void updateCounter(int heatingTime) {
        var sqlUpdateCounter = "UPDATE flats SET counter = ? WHERE  tenantId = ? ";
        var sqlGetHeaterPower = "SELECT heaterPower FROM flats WHERE tenantId = ?";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtUpdateCounter = conn.prepareStatement(sqlUpdateCounter);
             var pstmtGetHeaterPower = conn.prepareStatement(sqlGetHeaterPower)) {
            pstmtGetHeaterPower.setInt(1, tenantId);
            ResultSet rs = pstmtGetHeaterPower.executeQuery();
            //rs.next(); check if code doesn't work
            int heaterPower = rs.getInt("heaterPower");
            int counterUpdateAmount = heatingTime*heaterPower;
            pstmtUpdateCounter.setInt(1, counterUpdateAmount);
            pstmtUpdateCounter.setInt(2, tenantId);
            pstmtUpdateCounter.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void destroyBills() {
        var sqlDestroyDueBill = "DELETE FROM dueBills WHERE tenantId = ?";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtUpdateCounter = conn.prepareStatement(sqlDestroyDueBill)) {
            pstmtUpdateCounter.setInt(1, tenantId);
            pstmtUpdateCounter.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
