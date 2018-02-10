/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.worker;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.fuzzy.safepunch.Context;
import org.fuzzy.safepunch.domain.PunchSession;
import org.fuzzy.safepunch.domain.RawData;
import org.fuzzy.safepunch.operation.StoreSessionFile;
import org.fuzzy.utils.DateFormatter;

/**
 *
 * @author matteo
 */
public class DataAnalyzer extends Worker{
    
    private final static int MEASURE_STATE_LOW=0;
    private final static int MEASURE_STATE_HIGH=1;
    
    private List<Double> buffer=new ArrayList<>();
    private int measureState;
    private final double measureThreshold;
    private final int bufferMaxSize;
    private PunchSession session;
    private int adcMeasureSampler;
    
    public DataAnalyzer(Context context, String identifier, BlockingQueue<RawData> queue){
        super(context, identifier, queue);
        this.measureThreshold=this.context.getPunchThreshold();
        this.bufferMaxSize=this.context.getAnalyzerBufferSize();
        this.measureState=MEASURE_STATE_LOW;
        this.adcMeasureSampler=this.context.getMeasureAdcSampler();
    }

    @Override
    protected boolean init() throws Exception {
        this.session=new PunchSession();
        session.setSessionDate(new Date());
        session.setName(this.identifier);
        return true;
    }
    
    @Override
    public void work() throws Exception{
        RawData data=this.queue.take();
        while(this.session.size()>=this.bufferMaxSize){
            this.session.pop();
        }
        this.session.pushMeasure(data);
    }
    
    /*private void doMeasure(double measure){
        while(this.buffer.size()>=this.bufferMaxSize){
            this.buffer.remove(0);
        }
        this.buffer.add(measure);
        if(measure>this.measureThreshold){
            if(this.measureState==MEASURE_STATE_LOW)
                this.measureState=MEASURE_STATE_HIGH;
        } else {
            if(this.measureState==MEASURE_STATE_HIGH)
                this.storeValues();
        }
    }*/

    @Override
    protected boolean finish() throws Exception {
        while (this.queue.size()>0) {            
            RawData data=this.queue.take();
            while(this.session.size()>=this.bufferMaxSize){
                this.session.pop();
            }
            this.session.pushMeasure(data);
        }
        
        //StoreSessionDatabase store=new StoreSessionDatabase(session);
        String dateText=DateFormatter.toLocaleDateTimeLog(this.session.getSessionDate());
        String outputPath=String.format(this.context.getDataOuput(), this.identifier, dateText);
        StoreSessionFile store=new StoreSessionFile(session, outputPath);
        boolean success=store.execute();
        return true;
    }
    
    /*private void storeValues(){
        if(!this.buffer.isEmpty()){
            Iterator<Double> it=this.buffer.iterator();
            while (it.hasNext()) {
                Double value = it.next();
                session.pushMeasure(value);
            }
        }
        this.measureState=MEASURE_STATE_LOW;
        this.buffer.clear();
    }*/
    
    
    
}
