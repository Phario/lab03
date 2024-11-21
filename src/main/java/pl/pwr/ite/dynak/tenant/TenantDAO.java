package pl.pwr.ite.dynak.tenant;

import pl.pwr.ite.dynak.dataRecords.DueBillInfo;

import java.util.List;

public interface TenantDAO {
    public void createBribe();
    public List<DueBillInfo> readDueBills();
    public void updateCounter(int heatingTime);
    public void destroyBills();
}
