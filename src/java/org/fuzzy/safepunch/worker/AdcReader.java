/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.worker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import org.fuzzy.safepunch.Context;
import org.fuzzy.safepunch.domain.RawData;
import org.fuzzy.utils.DateFormatter;
import org.fuzzy.utils.MathUtility;
import org.fuzzy.utils.StringUtility;

/**
 *
 * @author matteo
 */
public class AdcReader extends Worker{
    
    private Process adcProcess;
    private BufferedReader adcReader;
    private int maxCapacity;
//    private final GpioControll;er gpioController = GpioFactory.getInstance();
//    private PinState readerState;
    
    private double gain;
    private int fullscale;
    
    public AdcReader(Context context, String identifier, BlockingQueue<RawData> queue){
        super(context, identifier, queue);
        this.maxCapacity=this.context.getAdcReaderMaxCapacity();    
        this.gain=this.context.getGain();
        this.fullscale=this.context.getFullscale();
        //this.adcReader = (PyInstance) python.eval("AdcReader()");
        /*this.readerState=PinState.LOW;
        Pin pin=RaspiPin.getPinByName("GPIO "+this.context.getInterruptPin());
        PinPullResistance pullResistance=this.context.getInterruptPullResistance();
        final GpioPinDigitalInput remoteGpio = this.gpioController.provisionDigitalInputPin(pin, pullResistance);
        remoteGpio.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                AdcReader.this.readerState=event.getState();
            }
        });*/
    }

    @Override
    protected boolean init() throws Exception {
        String adcScript=this.context.getAdcScript();
        String[] command = {"python", adcScript};
        this.adcProcess=Runtime.getRuntime().exec(command);
        this.adcReader=new BufferedReader(new InputStreamReader(this.adcProcess.getInputStream()));
        return true;
    }
    
    @Override
    public void work() throws Exception{
        //if(this.readerState.isLow())
        //    return;
        
        
        String line=this.adcReader.readLine();
        if(line!=null){
            String[] elements=StringUtility.splitText(line, '|', true);
            if(elements!=null && elements.length>1){
                Date datetime=this.parseDatetime(line);
                if(datetime!=null){
                    RawData data=new RawData();
                    data.setDatetime(datetime);
                    for (int i = 0; i < elements.length; i++) {
                        String element = elements[i];
                        Double value=MathUtility.parseDouble(element);
                        if(value!=null){
                            value=(value/this.fullscale)*this.gain;
                            data.pushValue(value); 
                        }                            
                    }
                    if(data.size()>0){
                        while((this.queue.size()+data.size())>this.maxCapacity){
                            RawData lost=this.queue.take();
                        }
                        this.queue.add(data);
                    }
                }
            }
        }
    }

    @Override
    protected boolean finish() throws Exception {
        this.adcReader.close();
        this.adcProcess.destroyForcibly();
        return true;
    }
    
    private Date parseDatetime(String text){
        Date result=null;
        try {
            result=DateFormatter.parseUnixDateTimeMilliseconds(text);
        } catch (Exception e) {
        }
        return result;
    }
}
