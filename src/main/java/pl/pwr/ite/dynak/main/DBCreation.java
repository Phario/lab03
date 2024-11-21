package pl.pwr.ite.dynak.main;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBCreation {
    public static void main(String[] args) {
        var url = "jdbc:sqlite:propertyData.sqlite";
        var tenantsTable = "CREATE TABLE IF NOT EXISTS tenants ("
                + "tenantId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "flatId INTEGER NOT NULL,"
                + "FOREIGN KEY (flatId) REFERENCES flats(flatId) ON DELETE CASCADE)";
        var flatsTable = "CREATE TABLE IF NOT EXISTS flats ("
                + "flatId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "counter INTEGER NOT NULL,"
                + "tenantId INTEGER,"
                + "heaterPower INTEGER)";
        var counterStates = "CREATE TABLE IF NOT EXISTS counterStates ("
                + "flatId INTEGER PRIMARY KEY,"
                + "amount INTEGER NOT NULL,"
                + "FOREIGN KEY (flatId) REFERENCES flats(flatId) ON DELETE CASCADE)";
        var counterStatesReport = "CREATE TABLE IF NOT EXISTS counterStatesReport ("
                + "reportId INTEGER REFERENCES controlOrder(orderId),"
                + "tenantId INTEGER,"
                + "amount INTEGER NOT NULL,"
                + "date TEXT NOT NULL,"
                + "FOREIGN KEY (tenantId) REFERENCES tenants(tenantId) ON DELETE CASCADE)";
        var controlOrder = "CREATE TABLE IF NOT EXISTS controlOrder ("
                + "orderId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "date TEXT NOT NULL)";
        var dueBills = "CREATE TABLE IF NOT EXISTS dueBills ("
                + "billId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tenantId INTEGER NOT NULL,"
                + "amount REAL NOT NULL,"
                + "date TEXT NOT NULL,"
                + "FOREIGN KEY (tenantId) REFERENCES tenants(tenantId) ON DELETE CASCADE)";
        var billingHistory = "CREATE TABLE IF NOT EXISTS billingHistory ("
                + "tenantId INTEGER,"
                + "amount REAL NOT NULL,"
                + "date TEXT NOT NULL,"
                + "billId INTEGER NOT NULL)";
        var mainCounter = "CREATE TABLE IF NOT EXISTS mainCounter ("
                + "counter INTEGER NOT NULL)";
        var mainCounterInitialise = "INSERT INTO mainCounter (counter) VALUES (0)";
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(tenantsTable);
            stmt.execute(flatsTable);
            stmt.execute(counterStates);
            stmt.execute(counterStatesReport);
            stmt.execute(controlOrder);
            stmt.execute(dueBills);
            stmt.execute(billingHistory);
            stmt.execute(mainCounter);
            stmt.execute(mainCounterInitialise);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
