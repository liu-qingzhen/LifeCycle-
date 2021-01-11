package com.example.lifecycle.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Step {
    @Id
    private Long id;
    @Property
    private String date;
    @Property
    private String step;
    @Generated(hash = 413462766)
    public Step(Long id, String date, String step) {
        this.id = id;
        this.date = date;
        this.step = step;
    }
    @Generated(hash = 561308863)
    public Step() {
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
    public String getStep() {
        return this.step;
    }
    public void setStep(String step) {
        this.step = step;
    }
}
