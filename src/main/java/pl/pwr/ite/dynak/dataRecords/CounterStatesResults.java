package pl.pwr.ite.dynak.dataRecords;

import java.util.HashMap;

public record CounterStatesResults(HashMap<Integer, Integer> flatIdAmount, String date) {}
