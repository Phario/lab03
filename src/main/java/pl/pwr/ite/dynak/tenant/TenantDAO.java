package pl.pwr.ite.dynak.tenant;

import pl.pwr.ite.dynak.dataUtils.DueBillInfo;

import java.util.List;

public interface TenantDAO {
    void createPayment(int billId, String paymentDate);
    List<DueBillInfo> readDueBills();
    void updateCounter(int heatingTime);
    void destroyBills();
}
