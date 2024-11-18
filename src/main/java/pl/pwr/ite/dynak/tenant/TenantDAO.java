package pl.pwr.ite.dynak.tenant;

public interface TenantDAO {
    public void createBribe();
    public void updateCounter(float amount, int counterId);
    public void readBills();
    public void deleteBills();
}
