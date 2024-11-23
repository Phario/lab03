package pl.pwr.ite.dynak.tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pwr.ite.dynak.dataUtils.DueBillInfo;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import pl.pwr.ite.dynak.dataUtils.InvalidIdException;

@Setter
@Getter
public class Tenant implements TenantDAO {
    private static final Logger logger = LoggerFactory.getLogger(Tenant.class);
    private int tenantId;

    public Tenant(int tenantId) {
        this.tenantId = tenantId;
    }

    private static String databaseURL = "jdbc:sqlite:propertyData.sqlite";

    @Override
    public void createPayment(int billId, String paymentDate) {
        var sqlCreatePayment = "INSERT INTO paymentHistory(tenantId, amount, date, billId) VALUES (?,?,?,?)";
        var sqlGetDueBillAmount = "SELECT amount FROM dueBills WHERE tenantId = ?";
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtCreatePayment = conn.prepareStatement(sqlCreatePayment);
             var pstmtGetDueBillAmount = conn.prepareStatement(sqlGetDueBillAmount)) {
            ResultSet rsDueBillAmount = pstmtGetDueBillAmount.executeQuery();
            int amount = rsDueBillAmount.getInt("amount");
            pstmtCreatePayment.setInt(1, tenantId);
            pstmtCreatePayment.setInt(2, amount);
            pstmtCreatePayment.setString(3, paymentDate);
            pstmtCreatePayment.setInt(4, billId);
            pstmtCreatePayment.execute();
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
                                              rs.getInt("amount"),
                                              rs.getString("date"));
                dueBillInfoList.add(dueBillInfo);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
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
            int heaterPower = rs.getInt("heaterPower");
            int counterUpdateAmount = heatingTime*heaterPower;
            pstmtUpdateCounter.setInt(1, counterUpdateAmount);
            pstmtUpdateCounter.setInt(2, tenantId);
            pstmtUpdateCounter.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
        }
    }

    public static void checkIdValidity(int tenantId) throws InvalidIdException {
        var sqlGetIds = "SELECT tenantId FROM tenants";
        boolean tenantIdValidity = false;
        try (var conn = DriverManager.getConnection(databaseURL);
             var pstmtGetIds = conn.prepareStatement(sqlGetIds)) {
            ResultSet rs = pstmtGetIds.executeQuery();
            while (rs.next()) {
                if (tenantId == rs.getInt("tenantId")) {
                    tenantIdValidity = true;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        if (!tenantIdValidity) {
            throw new InvalidIdException();
        }
    }
}
