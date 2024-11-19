package pl.pwr.ite.dynak.landlord;
import pl.pwr.ite.dynak.dataRecords.CounterStatesResults;

import java.util.ArrayList;

public interface LandlordDAO {
    public void createFlat(int heaterPower);
    public void createTenant(String name, int tenantId);
    public void createHeater(int heaterPower);
    public void createControlOrder(String date);
    public void createBills(int reportId, String date);
    public CounterStatesResults readControlResults(int reportId);
    //Finds tenants who haven't paid their bills
    public ArrayList<Integer> readLazyBunsIds();
    public void readFlats();
    public void readTenants();
    public void updateFlatTenant(int tenantId);
    public void destroyFlat(int flatId);
    public void destroyTenant(int tenantId);

}
