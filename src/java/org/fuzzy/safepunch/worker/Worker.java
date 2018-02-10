/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.worker;

import java.util.concurrent.BlockingQueue;
import org.fuzzy.safepunch.Context;
import org.fuzzy.safepunch.domain.RawData;

/**
 *
 * @author matteo
 */
public abstract class Worker implements Runnable{
    
    protected final BlockingQueue<RawData> queue;    
    protected final String identifier;    
    protected final Context context;
    private boolean working;
    
    public Worker(Context context, String identifier, BlockingQueue<RawData> queue){
        super();
        this.context=context;
        this.identifier=identifier;
        this.queue=queue;
        this.working=false;
        
    }
    
    protected abstract boolean init() throws Exception;
    protected abstract void work() throws Exception;
    protected abstract boolean finish() throws Exception;

    @Override
    public void run() {
        try {
            this.working=this.init();
            while (this.working) {
                this.work();
                //this.sleep(100);
            }
            this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stop(){
        this.working=false;
    }
    
    private void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
        }
    }
}
