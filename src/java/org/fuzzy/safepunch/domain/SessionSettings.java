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
    private int idleServer;
    private int idleDevice;
    private int sampler;
    
    public SessionSettings() {
        this.gain=2;
        this.fullscale=32767;
        this.idleServer=100;
        this.idleDevice=100;
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

    public int getIdleServer() {
        return idleServer;
    }

    public void setIdleServer(int idleServer) {
        this.idleServer = idleServer;
    }

    public int getIdleDevice() {
        return idleDevice;
    }

    public void setIdleDevice(int idleDevice) {
        this.idleDevice = idleDevice;
    }

    public int getSampler() {
        return sampler;
    }

    public void setSampler(int sampler) {
        this.sampler = sampler;
    }   
    
    public void pushSettings(String sampler, String gain, String fullscale, String idleServer, String idleDevice){
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
        value=MathUtility.parseDouble(idleServer);
        if(value!=null){
            this.idleServer=value.intValue();
        }
        value=MathUtility.parseDouble(idleDevice);
        if(value!=null){
            this.idleDevice=value.intValue();
        }
    }
    
    public String getDeviceParameters(){
        StringBuilder buffer=new StringBuilder();
        buffer.append(this.sampler).append(",")
                .append(this.gain).append(",")
                .append(this.fullscale).append(",")
                .append(this.idleDevice);
        return buffer.toString();
    }
    
}
