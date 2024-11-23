package pl.pwr.ite.dynak.landlord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwr.ite.dynak.dataUtils.*;

import java.sql.*;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Landlord implements LandlordDAO {
    private static final Logger logger = LoggerFactory.getLogger(Landlord.class);
    String databaseURL = "jdbc:sqlite:propertyData.sqlite";
    private static int calculateBill(int heatingRate, int counterState, int mainCounterState, int tenantAmount) {
        return heatingRate * (counterState + mainCounterState/tenantAmount);
    }
    @Override
    public void createFlat(int heatingPower) throws NumberFormatException {
        var flatCreation = "INSERT INTO flats(Counter,heaterPower) VALUES(?,?)";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmt = conn.prepareStatement(flatCreation)) {
            pstmt.setInt(1, 0);
            pstmt.setInt(2, heatingPower);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void createTenant(String name, int flatId) {
        var sqlTenantCreation = "INSERT INTO tenants(name, flatId) VALUES(?,?)";
        var sqlGetTenantId = "SELECT tenantId FROM tenants WHERE name = ?";
        var sqlUpdateFlat = "UPDATE flats SET tenantId = ? WHERE flatId = ?";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtTenantCreation = conn.prepareStatement(sqlTenantCreation);
             var pstmtUpdateFlat = conn.prepareStatement(sqlUpdateFlat);
             var pstmtGetTenantId = conn.prepareStatement(sqlGetTenantId)) {
            //create tenant
            pstmtTenantCreation.setString(1, name);
            pstmtTenantCreation.setInt(2, flatId);
            pstmtTenantCreation.execute();
            //retrieve new tenant's ID based on his name
            pstmtGetTenantId.setString(1, name);
            ResultSet tenantIdRS = pstmtGetTenantId.executeQuery();
            tenantIdRS.next();
            int tenantId = tenantIdRS.getInt(1);
            //update tenant's flat's tenantId
            pstmtUpdateFlat.setInt(1, tenantId);
            pstmtUpdateFlat.setInt(2, flatId);
            pstmtUpdateFlat.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
        }
    }

    @Override
    public void createBills(int reportId, String date, int rate) {
        var sqlBillCreation = "INSERT INTO dueBills(tenantId, amount, date) VALUES(?,?,?)";
        var sqlGetCounterStatesReportData = "SELECT tenantId, amount FROM counterStatesReport WHERE reportId = ?";
        var sqlAddBillToHistory = "INSERT INTO billingHistory VALUES (?,?,?,?)";
        var sqlGetBillId = "SELECT billId FROM dueBills WHERE tenantId = ? AND date = ?";
        var sqlGetMainCounterState = "SELECT counter FROM mainCounter";
        var sqlGetAmountOfTenants = "SELECT COUNT(*) AS tenantAmount FROM tenants";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtBillCreation = conn.prepareStatement(sqlBillCreation);
             var pstmtGetCounterStatesReportData = conn.prepareStatement(sqlGetCounterStatesReportData);
             var pstmtAddBillToHistory = conn.prepareStatement(sqlAddBillToHistory);
             var pstmtGetBillId = conn.prepareStatement(sqlGetBillId);
             var pstmtGetMainCounterState = conn.prepareStatement(sqlGetMainCounterState);
             var pstmtGetAmountOfTenants = conn.prepareStatement(sqlGetAmountOfTenants)) {
            //get ResultSet from counterStatesReportData
            pstmtGetCounterStatesReportData.setInt(1, reportId);
            ResultSet rsCounterStatesReportData = pstmtGetCounterStatesReportData.executeQuery();
            ResultSet rsGetMainCounterState = pstmtGetMainCounterState.executeQuery();
            int mainCounterState = rsGetMainCounterState.getInt("counter");
            //set dates to statements
            pstmtBillCreation.setString(3, date);
            pstmtAddBillToHistory.setString(3, date);
            pstmtGetBillId.setString(2, date);
            ResultSet rsTenantAmount = pstmtGetAmountOfTenants.executeQuery();
            int tenantAmount = rsTenantAmount.getInt("tenantAmount");
            //loop that creates bills per flat
            while (rsCounterStatesReportData.next()) {
                int tenantId = rsCounterStatesReportData.getInt("tenantId");
                int billAmount = calculateBill(rate, rsCounterStatesReportData.getInt("amount"), mainCounterState, tenantAmount);

                //fills bills sql statement with info
                pstmtBillCreation.setInt(1, tenantId);
                pstmtBillCreation.setInt(2, billAmount);
                //submit bill to database
                pstmtBillCreation.execute();

                //get new bill's id:
                pstmtGetBillId.setInt(1, tenantId);
                ResultSet rsGetBillId = pstmtGetBillId.executeQuery();

                //fills billHistory with freshly created bill
                pstmtAddBillToHistory.setInt(1, tenantId);
                pstmtAddBillToHistory.setInt(2, billAmount);
                pstmtAddBillToHistory.setInt(4, rsGetBillId.getInt("billId"));
                pstmtAddBillToHistory.addBatch();
                //submits bill to database and history
            }
            pstmtAddBillToHistory.executeBatch();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public ArrayList<CounterStatesResults> readControlResults(int reportId) {
        var sql = "SELECT * FROM counterStatesReport WHERE reportId = ?";
        var counterStatesReport = new ArrayList<CounterStatesResults>();
        //attempt to read data
        CounterStatesResults counterStatesResults;
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                counterStatesResults = new CounterStatesResults(rs.getInt("reportId"),
                                                                rs.getInt("tenantId"),
                                                                rs.getInt("amount"),
                                                                rs.getInt("flatId"),
                                                                rs.getString("date"));
                counterStatesReport.add(counterStatesResults);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return counterStatesReport;
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
            logger.error(e.getMessage());
        }
        return lazyBunsIdList;
    }

    @Override
    public ArrayList<Integer[]> readFlats() {
        var sqlFlats = "SELECT flatId, counter, tenantId, heaterPower FROM flats";
        ArrayList<Integer[]> flatInfoList = null;
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtSqlFlats = conn.prepareStatement(sqlFlats)) {
            ResultSet rsFlatInfo = pstmtSqlFlats.executeQuery();
            flatInfoList = new ArrayList<>();
            while (rsFlatInfo.next()) {
                Integer[] flatInfo = new Integer[4];
                flatInfo[0] = rsFlatInfo.getInt("flatId");
                flatInfo[1] = rsFlatInfo.getInt("counter");
                flatInfo[2] = rsFlatInfo.getInt("tenantId");
                flatInfo[3] = rsFlatInfo.getInt("heaterPower");
                flatInfoList.add(flatInfo);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return flatInfoList;
    }

    @Override
    public ArrayList<TenantInfo> readTenants() {
        var sqlFlats = "SELECT tenantId, name, flatId FROM tenants";
        ArrayList<TenantInfo> tenantInfoList = null;
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtSqlFlats = conn.prepareStatement(sqlFlats)) {
            ResultSet rsTenantInfo = pstmtSqlFlats.executeQuery();
            tenantInfoList = new ArrayList<>();
            TenantInfo tenantInfo;
            while (rsTenantInfo.next()) {
                tenantInfo = new TenantInfo(rsTenantInfo.getInt("tenantId"),
                                            rsTenantInfo.getString("name"),
                                            rsTenantInfo.getInt("flatId"));
                tenantInfoList.add(tenantInfo);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return tenantInfoList;
    }

    @Override
    public void updateMainCounter(int mainCounterState) {
        var sqlUpdateMainCounter = "UPDATE mainCounter SET counter = ?";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtUpdateMainCounter = conn.prepareStatement(sqlUpdateMainCounter)) {
            pstmtUpdateMainCounter.setInt(1, mainCounterState);
            pstmtUpdateMainCounter.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void destroyFlat(int flatId) {
        var sqlDestroyFlat = "DELETE FROM flats WHERE flatId = ?";
        var sqlPRAGMA = "PRAGMA foreign_keys=ON";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtDestroyFlat = conn.prepareStatement(sqlDestroyFlat);
             var pstmtPRAGMA = conn.prepareStatement(sqlPRAGMA)) {
            pstmtPRAGMA.execute();
            pstmtDestroyFlat.setInt(1, flatId);
            pstmtDestroyFlat.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void destroyTenant(int tenantId) {
        var sqlFindTenantsFlat = "SELECT tenantId FROM flats WHERE flatId = ?";
        var sqlUpdateFlatsTenant = "UPDATE flats SET tenantId = null WHERE tenantId = ?";
        var sqlDestroyTenant = "DELETE FROM tenants WHERE tenantId = ?";
        var sqlPRAGMA = "PRAGMA foreign_keys=ON";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtDestroyTenant = conn.prepareStatement(sqlDestroyTenant);
             var pstmtFindTenantsFlat = conn.prepareStatement(sqlFindTenantsFlat);
             var pstmtUpdateFlatsTenant = conn.prepareStatement(sqlUpdateFlatsTenant);
             var pstmtPRAGMA = conn.prepareStatement(sqlPRAGMA)) {
            //find tenant's flat
            pstmtDestroyTenant.setInt(1, tenantId);
            ResultSet tenantsFlatIdRS = pstmtFindTenantsFlat.executeQuery();
            int tenantsFlatId = tenantsFlatIdRS.getInt(1);
            //update tenant's flat's id to NULL
            pstmtUpdateFlatsTenant.setInt(1, tenantsFlatId);
            pstmtUpdateFlatsTenant.executeUpdate();
            //remove tenant
            pstmtPRAGMA.execute();
            pstmtDestroyTenant.setInt(1, tenantId);
            pstmtDestroyTenant.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

}
