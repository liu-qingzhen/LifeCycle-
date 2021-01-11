package com.example.lifecycle.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Location {
    @Id
    private Long id;
    @Property
    private String date;
    @Property
    private double x;
    @Property
    private double y;
    @Generated(hash = 921917947)
    public Location(Long id, String date, double x, double y) {
        this.id = id;
        this.date = date;
        this.x = x;
        this.y = y;
    }
    @Generated(hash = 375979639)
    public Location() {
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
    public double getX() {
        return this.x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return this.y;
    }
    public void setY(double y) {
        this.y = y;
    }
}
