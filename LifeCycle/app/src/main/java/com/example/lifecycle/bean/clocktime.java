package com.example.lifecycle.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class clocktime {
    @Id
    private Long id;
    @Property
    private String date;
    @Property
    private String getUptime;
    @Property
    private String sleeptime;
    @Generated(hash = 28748830)
    public clocktime(Long id, String date, String getUptime, String sleeptime) {
        this.id = id;
        this.date = date;
        this.getUptime = getUptime;
        this.sleeptime = sleeptime;
    }
    @Generated(hash = 182660512)
    public clocktime() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getGetUptime() {
        return this.getUptime;
    }
    public void setGetUptime(String getUptime) {
        this.getUptime = getUptime;
    }
    public String getSleeptime() {
        return this.sleeptime;
    }
    public void setSleeptime(String sleeptime) {
        this.sleeptime = sleeptime;
    }

}
