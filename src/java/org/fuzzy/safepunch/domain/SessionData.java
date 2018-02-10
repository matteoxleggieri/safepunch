/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.domain;

import java.util.List;

/**
 *
 * @author matteo
 */
public class SessionData {  
    
    private List<Double> measures;
    private int sessionId;
    private int id;
    
    public SessionData(){
        super();
    }

    public List<Double> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Double> measures) {
        this.measures = measures;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
