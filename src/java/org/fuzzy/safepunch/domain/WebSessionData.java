/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.domain;

import java.util.Date;

/**
 *
 * @author matteo
 */
public class WebSessionData {  
    
    private Double measure;
    private Date date;
    
    public WebSessionData(){
        super();
    }

    public Double getMeasure() {
        return measure;
    }

    public void setMeasure(Double measure) {
        this.measure = measure;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        builder.append(this.date).append(" | ").append(this.measure);
        return builder.toString();
    }
}
