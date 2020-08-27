package com.alam.birthdayreminder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Person implements Serializable {
    private long id;
    private String name;
    private Date dob;
    private Boolean notify;

    public Person(String name, Date dob, Boolean notify) {
        this.name = name;
        this.dob = dob;
        this.notify = notify;
    }

    public long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getDOB() {
        return dob;
    }

    public Calendar getDOBAsCalender() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dob.getTime());
        return calendar;
    }

    public Boolean isNotify() {
        return notify;
    }
    public void setDob(Date dob) { this.dob = dob; }
    public void setNotify(Boolean notify) { this.notify = notify; }
}