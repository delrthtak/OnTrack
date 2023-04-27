package com.example.ontrack;

import java.util.List;

public class Health {

    private String healthDate;
    private int dwGoal;
    private boolean dwgb;
    private int daGoal;
    private boolean dagb;
    private int water;
    private List<ActivityLog> dailyActivityLog;

    Health(){ super(); }

    public void setHealthDate(String healthDate) { this.healthDate = healthDate; }
    public void setDwgb(boolean dwgb) { this.dwgb = dwgb; }
    public void setDwGoal(int dwGoal) { this.dwGoal = dwGoal; }
    public void setDaGoal(int daGoal) { this.daGoal = daGoal; }
    public void setDagb(boolean dagb) { this.dagb = dagb; }
    public void setWater(int water) { this.water = water; }
    public void setDailyActivityLog(List<ActivityLog> dailyActivityLog) { this.dailyActivityLog = dailyActivityLog; }
    public void addActivityLog(ActivityLog activityLog) { this.dailyActivityLog.add(activityLog); }

    public String getHealthDate() { return healthDate; }
    public boolean isDwgb() { return dwgb; }
    public int getDwGoal() { return dwGoal; }
    public int getDaGoal() { return daGoal; }
    public boolean isDagb() { return dagb; }
    public int getWater() { return water; }
    public List<ActivityLog> getDailyActivityLog() { return dailyActivityLog; }

}
