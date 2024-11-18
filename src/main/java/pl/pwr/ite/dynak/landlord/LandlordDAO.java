package pl.pwr.ite.dynak.landlord;

public interface LandlordDAO {
    public void createFlat(int heaterPower);
    public void createTenant(String name);
    public void createHeater(int heaterPower);
    public void createControlRequest(int date);
    public void createBill(int flatId, int tenantId, int counterState, int date);
    public void readControlResults(int reportId);
    public void updateFlatTenant(int tenantId);
    public void destroyFlat(int flatId);
    public void destroyTenant(int tenantId);

}
