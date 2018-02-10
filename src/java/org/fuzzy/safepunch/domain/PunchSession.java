/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author matteo
 */
public class PunchSession {
 
    private int id;
    private String name;
    private Date sessionDate;
    //private List<SessionData> values;
    private List<RawData> values;
    
    public PunchSession(){
        super();
        this.values=new ArrayList<>();
    }
    
    public void pushMeasure(RawData data){
        this.values.add(data);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public List<RawData> getValues() {
        return values;
    }

    public void setValues(List<RawData> values) {
        this.values = values;
    }
    
    public int size(){
        return this.values.size();
    }

    public RawData pop() {
        RawData result=null;
        if(this.size()>0)
            result=this.values.remove(0);
        return result;
    }
}
