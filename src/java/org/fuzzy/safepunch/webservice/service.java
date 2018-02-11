/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.webservice;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fuzzy.logger.Logger;
import org.fuzzy.safepunch.domain.ServiceResponse;
import org.fuzzy.safepunch.domain.SessionSettings;
import org.fuzzy.safepunch.domain.WebSessionData;
import org.fuzzy.safepunch.worker.WebWorker;

/**
 *
 * @author matteo
 */
@WebServlet(name = "service", urlPatterns = {"/service"})
public class service extends HttpServlet {

    private final BlockingQueue<WebSessionData> queue=new LinkedBlockingQueue<>();   
    private final WebWorker worker=new WebWorker(this.queue);
    private final Logger logger=new Logger("Service");
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        String command=request.getParameter("command");
        ServiceResponse output=new ServiceResponse();
        try (PrintWriter out = response.getWriter()) {
            switch(command){
                case "start":                    
                    this.startReader(request);
                    output.setText("OK");
                    break;
                case "stop":
                    this.stopReader();
                    output.setText("OK");           
                    break;
                case "popData":
                    this.popData(output);
                    break;
            }
            String json = new Gson().toJson(output);
            out.println(json);
        }
    }
    
    private void startReader(HttpServletRequest request){
        String sampler=request.getParameter("sampler");
        String gain=request.getParameter("gain");
        String fullscale=request.getParameter("fullscale");
        String idleServer=request.getParameter("idleServer");
        String idleDevice=request.getParameter("idleDevice");
        SessionSettings settings=new SessionSettings();
        settings.pushSettings(sampler, gain, fullscale, idleServer, idleDevice);
        this.logger.info("Started WebWorker");
        this.worker.stop();
        this.worker.setSettings(settings);
        Thread thread=new Thread(this.worker);
        thread.start();
    }
    
    private void stopReader(){
        this.logger.info("Stopped WebWorker");
        this.worker.stop();
    }
    
    private void popData(ServiceResponse output){        
        WebSessionData data=null;
        try {
            data=this.queue.poll(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.logger.debug("Pop data %s", data);
        output.setValue(data.getMeasure());
        output.setTimestamp(data.getDate().getTime());
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
