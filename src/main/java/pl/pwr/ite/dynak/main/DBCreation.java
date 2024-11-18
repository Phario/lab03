package pl.pwr.ite.dynak.main;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBCreation {
    public static void main(String[] args) {
        var url = "jdbc:sqlite:propertyData.sqlite";
        var tenantsTable = "CREATE TABLE IF NOT EXISTS tenants ("
                + "tenantId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "flatId INTEGER NOT NULL REFERENCES flats(flatId))";
        var flatsTable = "CREATE TABLE IF NOT EXISTS flats ("
                + "flatId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "counter INTEGER NOT NULL,"
                + "tenantId INTEGER,"
                + "heaterPower INTEGER)";
        var counterStates = "CREATE TABLE IF NOT EXISTS counterStates ("
                + "flatId INTEGER PRIMARY KEY REFERENCES flats(flatId),"
                + "amount INTEGER NOT NULL)";
        var counterStatesReport = "CREATE TABLE IF NOT EXISTS counterStatesReport ("
                + "reportId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "flatId INTEGER REFERENCES flats(flatId),"
                + "amount INTEGER NOT NULL,"
                + "date DATE NOT NULL)";
        var controlOrder = "CREATE TABLE IF NOT EXISTS controlOrder ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "flatId INTEGER NOT NULL REFERENCES flats(flatId),"
                + "date DATE NOT NULL)";
        var dueBills = "CREATE TABLE IF NOT EXISTS dueBills ("
                + "billId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "amount FLOAT NOT NULL,"
                + "date DATE NOT NULL)";
        var billingHistory = "CREATE TABLE IF NOT EXISTS billingHistory ("
                + "tenantId INTEGER PRIMARY KEY AUTOINCREMENT REFERENCES tenants(tenantId),"
                + "amount FLOAT NOT NULL,"
                + "date DATE NOT NULL,"
                + "billId INTEGER NOT NULL REFERENCES bills(billId))";
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(tenantsTable);
            stmt.execute(flatsTable);
            stmt.execute(counterStates);
            stmt.execute(counterStatesReport);
            stmt.execute(controlOrder);
            stmt.execute(dueBills);
            stmt.execute(billingHistory);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
