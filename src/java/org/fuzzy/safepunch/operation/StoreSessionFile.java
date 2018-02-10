/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.operation;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.fuzzy.safepunch.domain.PunchSession;
import org.fuzzy.safepunch.domain.RawData;
import org.fuzzy.safepunch.domain.SessionData;
import org.fuzzy.utils.DateFormatter;
import org.fuzzy.utils.NumericFormatter;
import org.fuzzy.utils.io.FileWriter;

/**
 *
 * @author matteo
 */
public class StoreSessionFile {
    
    private final PunchSession session;
    private final String output;
    
    public StoreSessionFile(PunchSession session, String output){
        super();
        this.session=session;
        this.output=output;
    }
    
    public boolean execute(){
        boolean result=false;
        try {
            List<String> listRows=new ArrayList<>();
            listRows.add(session.getName());
            listRows.add(DateFormatter.toLocaleDateTime(session.getSessionDate()));
            Iterator<RawData> it=this.session.getValues().iterator();
            while (it.hasNext()) {
                RawData data = it.next();
                listRows.add(data.toString());
            }
            FileWriter writer=new FileWriter(this.output, false, listRows);
            writer.execute();
            result=!writer.isErrorOnLastResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
