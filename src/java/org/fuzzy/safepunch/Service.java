/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.fuzzy.safepunch.domain.RawData;
import org.fuzzy.safepunch.worker.AdcReader;
import org.fuzzy.safepunch.worker.DataAnalyzer;

/**
 *
 * @author matteo
 */
public class Service {
   
    private static final int STATE_STARTED=0;
    private static final int STATE_STOPPED=1;
    
    private int state;
    private AdcReader adcReader;
    private DataAnalyzer dataAnalyzer;
    
    public Service(){
        super();
    }
    
    public void execute(){
        this.state=STATE_STOPPED;
        Context context=new Context();
        BlockingQueue<RawData> queue=new LinkedBlockingQueue<>();
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Insert an identifier for the session:");
        String identifier=keyboard.nextLine();
        System.out.println("Choose an option:");
        System.out.println("1 - Start mesurement");
        System.out.println("2 - Stop measurement");
        System.out.println("3 - Exit");       
        while (true) {          
            int option = keyboard.nextInt();
            switch(option){
                case 1:
                    if(this.state==STATE_STOPPED)
                        this.start(context, identifier, queue);
                    break;
                case 2:
                    if(this.state==STATE_STARTED)
                        this.stop();
                    break;
                case 3:                    
                    if(this.state==STATE_STARTED)
                        this.stop();
                    System.exit(0);
                    break;
            }
            this.sleep(1000);
        }
    }
    
    private void start(Context context, String identifier, BlockingQueue<RawData> queue){
        System.out.println("Measurement started. Press 2 to stop or 3 to exit session");
        this.state=STATE_STARTED;
        this.adcReader=new AdcReader(context, identifier, queue);
        Thread thread=new Thread(this.adcReader);
        thread.start();
        this.dataAnalyzer=new DataAnalyzer(context, identifier, queue);
        thread=new Thread(this.dataAnalyzer);
        thread.start();
    }
    
    private void stop(){
        System.out.println("Measurement stopped. Press 1 to start or 3 to exit");
        this.state=STATE_STOPPED;
        this.adcReader.stop();
        this.dataAnalyzer.stop();
    }
    
    private void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
        }
    }
    
    public static void main(String[] args){
        Service service=new Service();
        service.execute();
    }
}
