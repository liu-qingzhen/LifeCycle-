package com.example.lifecycle.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Time {
    @Id
    private Long id;
    @Property
    private String date;
    @Property
    private String name;
    @Property
    private int time;
    @Generated(hash = 44786854)
    public Time(Long id, String date, String name, int time) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.time = time;
    }
    @Generated(hash = 37380482)
    public Time() {
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTime() {
        return this.time;
    }
    public void setTime(int time) {
        this.time = time;
    }
}
