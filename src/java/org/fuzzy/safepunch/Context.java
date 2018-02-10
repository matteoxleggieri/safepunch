/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch;

//import com.pi4j.io.gpio.PinPullResistance;
import java.util.HashMap;
import java.util.Map;
import org.fuzzy.utils.FileProperties;

/**
 *
 * @author matteo
 */
public class Context {
    
    private final FileProperties properties;    
    private Map<Double,Double> GAIN_MAP=new HashMap<>();
    
    public Context(){
        super();
        this.properties=new FileProperties("SafePunch.properties", false);
        this.properties.execute();
        GAIN_MAP.put(1.0, 4.096);
        GAIN_MAP.put(2.0, 2.048);
        GAIN_MAP.put(4.0, 1.024);
        GAIN_MAP.put(8.0, 0.512);
        GAIN_MAP.put(16.0, 0.256);
    }
    
    public int getInterruptPin(){
        return this.properties.getIntegerKey("InterruptPin", 12);
    }
    
  /*  public PinPullResistance getInterruptPullResistance(){
        int value=this.properties.getIntegerKey("InterruptPullResistance", -1);
        PinPullResistance result=PinPullResistance.OFF;
        switch(value){
            case 0:
                result=PinPullResistance.PULL_DOWN;
                break;
            case 1:
                result=PinPullResistance.PULL_UP;
                break;
        }
        return result;
    }
    */
    public String getAdcScript(){
        return this.properties.getStringKey("AdcScript", "./adc_reader.py");
    }
    
    public String getDataOuput(){
        return this.properties.getStringKey("DataOuput", "./output/output_%s.txt");
    }
    
    public double getPunchThreshold(){
        return this.properties.getDoubleKey("PunchThreshold", 2.3);
    }

    public int getAnalyzerBufferSize() {
        return this.properties.getIntegerKey("AnalyzerBufferSize", 10000);
    }

    public int getAdcReaderMaxCapacity() {
        return this.properties.getIntegerKey("AdcReaderMaxCapacity", 10000);
    }
    
    public int getMeasureAdcSampler(){
        return this.properties.getIntegerKey("AdcSampler", 3);
    }
    
    public double getGain(){
        double rawGain=this.properties.getDoubleKey("Gain", 1);
        double result=GAIN_MAP.get(rawGain);
        return result;
    }
    
    public int getFullscale(){
        return this.properties.getIntegerKey("Fullscale", 32767);
    }

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        builder.append("");
        return builder.toString();
    }
    
    
}
