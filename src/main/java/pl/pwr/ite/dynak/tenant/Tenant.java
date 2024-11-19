package pl.pwr.ite.dynak.tenant;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Tenant implements TenantDAO {
    private static int tenantId = 1;

    @Override
    public void createBribe() {

    }

    @Override
    public void readBills() {

    }

    @Override
    public void updateCounter(float amount, int counterId) {
        var url = "jdbc:sqlite:propertyData.sqlite";
        //var sql = "UPDATE counter SET ? = ? WHERE ";
    }

    @Override
    public void destroyBills() {

    }
}
