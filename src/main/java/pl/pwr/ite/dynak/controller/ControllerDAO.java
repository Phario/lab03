package pl.pwr.ite.dynak.controller;

import pl.pwr.ite.dynak.dataUtils.ControlOrderData;
public interface ControllerDAO {
    public void createReport(String date, int orderId);
    public ControlOrderData readControlOrder();
    public void updateCounters();
    public void deleteControlOrder(int orderId);
}
