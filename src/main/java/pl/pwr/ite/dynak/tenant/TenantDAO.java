package pl.pwr.ite.dynak.tenant;

import pl.pwr.ite.dynak.dataUtils.DueBillInfo;

import java.util.List;

public interface TenantDAO {
    public void createPayment(int billId, String paymentDate);
    public List<DueBillInfo> readDueBills();
    public void updateCounter(int heatingTime);
    public void destroyBills();
}
