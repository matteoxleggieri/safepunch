/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.domain;

import org.fuzzy.utils.MathUtility;

/**
 *
 * @author matteo
 */
public class SessionSettings {

    private int gain;
    private int fullscale;
    private int idle;
    private int sampler;
    
    public SessionSettings() {
        this.gain=2;
        this.fullscale=32767;
        this.idle=100;
        this.sampler=2;
    }

    public int getGain() {
        return gain;
    }

    public void setGain(int gain) {
        this.gain = gain;
    }

    public int getFullscale() {
        return fullscale;
    }

    public void setFullscale(int fullscale) {
        this.fullscale = fullscale;
    }

    public int getIdle() {
        return idle;
    }

    public void setIdle(int idle) {
        this.idle = idle;
    }

    public int getSampler() {
        return sampler;
    }

    public void setSampler(int sampler) {
        this.sampler = sampler;
    }   
    
    public void pushSettings(String sampler, String gain, String fullscale, String idle){
        Double value=MathUtility.parseDouble(sampler);
        if(value!=null){
            this.sampler=value.intValue();
        }
        value=MathUtility.parseDouble(gain);
        if(value!=null){
            this.gain=value.intValue();
        }
        value=MathUtility.parseDouble(fullscale);
        if(value!=null){
            this.fullscale=value.intValue();
        }
        value=MathUtility.parseDouble(idle);
        if(value!=null){
            this.idle=value.intValue();
        }
    }
    
    public String getParameters(){
        StringBuilder buffer=new StringBuilder();
        buffer.append(this.sampler).append(",")
                .append(this.gain).append(",")
                .append(this.fullscale).append(",")
                .append(this.idle);
        return buffer.toString();
    }
    
}
