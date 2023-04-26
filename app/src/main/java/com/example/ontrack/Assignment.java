package com.example.ontrack;

import java.util.Date;

public class Assignment {
    private String name;
    private String dueDate;
    private String className;
    private String notes;
    private int priority;

    Assignment(String name, String dueDate, String className, String notes, int priority) {
        super();
        this.name = name;
        this.dueDate = dueDate;
        this.className = className;
        this.notes = notes;
        this.priority = priority;
    }

    Assignment() {
        super();
        this.name = "";
        this.dueDate = "";
        this.className = "";
        this.notes = "";
        this.priority = 0;
    }

    public void setName(String name) { this.name = name; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setClassName(String className) { this.className = className; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setPriority(int priority) { this.priority = priority; }

    public String getName() { return name; }
    public String getDueDate() { return dueDate; }
    public String getClassName() { return className; }
    public String getNotes() { return notes; }
    public int getPriority() { return priority; }

}
