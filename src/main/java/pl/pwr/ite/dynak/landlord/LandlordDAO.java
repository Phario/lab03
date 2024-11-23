package pl.pwr.ite.dynak.landlord;
import pl.pwr.ite.dynak.dataUtils.CounterStatesResults;
import pl.pwr.ite.dynak.dataUtils.TenantInfo;

import java.util.ArrayList;
import java.util.List;

public interface LandlordDAO {
    public void createFlat(int heaterPower);
    public void createTenant(String name, int tenantId);
    public void createHeater(int heaterPower);
    public void createControlOrder(String date);
    public void createBills(int reportId, String date, int rate);
    public List<CounterStatesResults> readControlResults(int reportId);
    //Finds tenants who haven't paid their bills
    public List<Integer> readLazyBunsIds();
    public List<Integer[]> readFlats();
    public List<TenantInfo> readTenants();
    public void updateMainCounter(int mainCounterState);
    public void destroyFlat(int flatId);
    public void destroyTenant(int tenantId);

}
