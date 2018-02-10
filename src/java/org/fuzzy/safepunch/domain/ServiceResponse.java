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
public class ServiceResponse {
    
    private double value;
    private Date date;
    private String text;
    
    public ServiceResponse(){
        
    }
    
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }   

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
