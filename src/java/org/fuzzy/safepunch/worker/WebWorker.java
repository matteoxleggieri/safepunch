/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.worker;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;
import org.fuzzy.logger.Logger;
import org.fuzzy.safepunch.domain.SessionSettings;
import org.fuzzy.safepunch.domain.WebSessionData;
import org.fuzzy.utils.DateFormatter;
import org.fuzzy.utils.MathUtil;

/**
 *
 * @author matteo
 */
public class WebWorker implements Runnable{
    
    private final String hostname="192.168.1.219";
    private final int port=8888;
    private final String username="pi";
    private final String password="d3p3ch3m0d3";
    private final String command="python /home/pi/safepunch/adc_reader.py";
    private final Logger logger=new Logger("WebWorker");
    
    protected final BlockingQueue<WebSessionData> queue;   
    private boolean working;
    private SessionSettings settings;
    
    public WebWorker(BlockingQueue<WebSessionData> queue){
        this.queue=queue;
        this.working=false;
        this.settings=new SessionSettings();
    }
    
    public void setSettings(SessionSettings settings){
        this.settings=settings;
    }
/*
    @Override
    public void run() {
        try {
            this.logger.info("Connecting to host %s with credentials [%s, %s]", hostname, username, password);
            System.out.println();
            JSch jsch=new JSch();
            Session session=jsch.getSession(username, hostname, 22);
            session.setPassword(password);
            session.setUserInfo(new SPUserInfo());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            this.logger.info("Executing command %s", command);
            Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);
            InputStream in=channel.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
            channel.connect();
            this.working=true;
            String line=reader.readLine();
            while (this.working && line!=null) {
                this.logger.debug("Pushing data  %s", line);
                this.queue.add(line);
                Thread.sleep(100);
                line=reader.readLine();
            }
            this.logger.info("Disconnectiong from host");
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
    @Override
    public void run() {
        try {
            this.logger.info("Connecting to host %s with credentials [%s, %s]", hostname, username, password);
            Socket client=new Socket(this.hostname, this.port);
            String parameters=this.settings.getDeviceParameters();
            OutputStream out=client.getOutputStream();
            out.write(parameters.getBytes(Charset.forName("UTF-8")));
            out.flush();
            InputStream in=client.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
            this.working=true;
            String date=reader.readLine();
            String value=reader.readLine();
            while (this.working && date!=null && value!=null) {
                this.logger.debug("Pushing data  [%s, %s]", date, value);
                Double measure=MathUtil.parseDouble(value);
                if(measure==null){
                    measure=0.0;
                }
                WebSessionData data=new WebSessionData();
                data.setDate(DateFormatter.parseISODateTimeMilliseconds(date));
                data.setMeasure(measure);
                this.queue.add(data);
                Thread.sleep(this.settings.getIdleServer());
                date=reader.readLine();
                value=reader.readLine();
            }
            this.logger.info("Disconnectiong from host");
            String stop="Stop";
            out.write(stop.getBytes(Charset.forName("UTF-8")));
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stop(){
        this.working=false;
    }
}
