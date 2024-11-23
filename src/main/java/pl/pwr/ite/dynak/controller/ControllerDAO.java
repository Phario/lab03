package pl.pwr.ite.dynak.controller;

import pl.pwr.ite.dynak.dataUtils.ControlOrderData;
public interface ControllerDAO {
    void createReport(String date, int orderId);
    ControlOrderData readControlOrder();
    void updateCounters();
    void deleteControlOrder(int orderId);
}
