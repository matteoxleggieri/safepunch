/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.deprecated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

/**
 *
 * @author matteo
 */
public class ServiceProcess {
    
    //private final String OUTPUT_PATH="/home/pi/";
    private final String OUTPUT_PATH="D:\\workspace\\Java\\SafePunch\\output\\";
    private final String[] CLIENT_COMMAND=new String[]{"python", "D:\\workspace\\Java\\SafePunch\\test_stdout.py"};
    
    private boolean finish;
    
    public ServiceProcess(){
        super();
    }
    
    public void start(){
        this.finish=false;                
        while (!this.finish) {            
            Process process=this.executeCommand(CLIENT_COMMAND);
            if(process==null){
                System.out.println("ERRORS WHILE EXECUTING PROCESS:");
                StringBuilder builder=new StringBuilder();
                for (int i = 0; i < CLIENT_COMMAND.length; i++) {
                    String command = CLIENT_COMMAND[i];
                    builder.append(command).append(" ");
                }
                System.out.println(builder.toString());
                continue;
            }
            try {       
                InputStream input=process.getInputStream();
                List<String> data=this.readData(input);
                System.out.println("READ DATA");
                this.writeDataOnFile(data);
                System.out.println("WROTE DATA");
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.sleep(1000);
        }
    }
    
    private List<String> readData(InputStream input){
        List<String> result=new ArrayList<>();
        List<String> temp=new ArrayList<>();
        try {
            InputStreamReader stream=new InputStreamReader(input);
            BufferedReader reader=new BufferedReader(stream);
            String line=reader.readLine();
            while (!line.equalsIgnoreCase("START-DATA")) {                
                line=reader.readLine();
            }
            line=reader.readLine();
            while (!line.equalsIgnoreCase("END-DATA")) {                
                temp.add(line);
                line=reader.readLine();
            }
            result.addAll(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private void writeDataOnFile(List<String> data){
        try {
            SimpleDateFormat format=new SimpleDateFormat("ddMMyyyy");
            String date=format.format(new Date());
            FileOutputStream output=new FileOutputStream(OUTPUT_PATH+"data_"+date+".txt", true);
            OutputStreamWriter stream=new OutputStreamWriter(output);
            BufferedWriter writer=new BufferedWriter(stream);
            Iterator<String> it=data.iterator();
            while (it.hasNext()) {
                String line = it.next();
                writer.write(line);
                writer.write("\r\n");
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Process executeCommand(String[] command)
    {
        Process result=null;
        try {
            result=Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            e.printStackTrace();
            result=null;
        }
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ServiceProcess service=new ServiceProcess();
        service.start();
    }
    
}
