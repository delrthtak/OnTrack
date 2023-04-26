package com.example.ontrack;

public class WaterLog {

    private String date;
    private int total;
    private boolean goalMet;

    WaterLog() {
        super();
    }

    WaterLog(String date, int total, boolean goalMet) {
        super();
        this.date = date;
        this.total = total;
        this.goalMet = goalMet;
    }

    public void setDate(String date) { this.date = date; }
    public void setTotal(int total) { this.total = total; }
    public void setGoalMet(boolean goalMet) { this.goalMet = goalMet; }

    public String getDate() { return date; }
    public int getTotal() { return total; }
    public boolean isGoalMet() { return goalMet; }

}
