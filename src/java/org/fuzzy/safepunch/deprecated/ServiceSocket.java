/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.deprecated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author matteo
 */
public class ServiceSocket {
    
    //private final String OUTPUT_PATH="/home/pi/";
    private final String OUTPUT_PATH="D:\\workspace\\Java\\SafePunch\\output\\";
    
    private boolean finish;
    
    public ServiceSocket(){
        super();
    }
    
    public void start(){
        this.finish=false;
        ServerSocket server=null;
        try {
            server=new ServerSocket(50005);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        while (!this.finish) {            
            Socket pythonClient=this.accept(server);
            if(pythonClient!=null){
                List<String> data=this.readData(pythonClient);
                System.out.println("READ DATA");
                this.writeDataOnFile(data);
                System.out.println("WROTE DATA");
            } else {
                System.err.println("CLIENT NULL ERROR");
            }            
            this.sleep(100);
        }
    }
    
    private Socket accept(ServerSocket server){
        Socket result=null;
        try {
            result=server.accept();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private List<String> readData(Socket client){
        List<String> result=new ArrayList<>();
        List<String> temp=new ArrayList<>();
        try {
            InputStreamReader stream=new InputStreamReader(client.getInputStream());
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
        ServiceSocket service=new ServiceSocket();
        service.start();
    }
    
}
