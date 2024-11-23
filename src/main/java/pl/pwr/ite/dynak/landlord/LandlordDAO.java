package pl.pwr.ite.dynak.landlord;
import pl.pwr.ite.dynak.dataUtils.CounterStatesResults;
import pl.pwr.ite.dynak.dataUtils.TenantInfo;

import java.util.List;

public interface LandlordDAO {
    void createFlat(int heaterPower);
    void createTenant(String name, int tenantId);
    void createHeater(int heaterPower);
    void createControlOrder(String date);
    void createBills(int reportId, String date, int rate);
    List<CounterStatesResults> readControlResults();
    List<Integer> readLazyBunsIds();
    List<Integer[]> readFlats();
    List<TenantInfo> readTenants();
    void updateMainCounter(int mainCounterState);
    void destroyFlat(int flatId);
    void destroyTenant(int tenantId);

}
