/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.operation;

import java.util.Iterator;
import java.util.List;
import org.fuzzy.contact.jdbc.JdbcConnection;
import org.fuzzy.contact.jdbc.domain.Record;
import org.fuzzy.safepunch.domain.PunchSession;
import org.fuzzy.safepunch.domain.SessionData;

/**
 *
 * @author matteo
 */
public class StoreSessionDatabase extends SafePunchOperation {

    private final PunchSession session;
    
    public StoreSessionDatabase(PunchSession session) {
        super();
        this.session=session;
    }
    
    @Override
    public boolean doExecute(JdbcConnection connection) throws Exception 
    {
        Record record=new Record();
        record.pushValue("name", this.session.getName());
        record.pushValue("sessiondate", this.session.getSessionDate());
        int insert=connection.insert("punch_session", record, "id");
        boolean result=(insert>0);
        if(result){
            this.session.setId(insert);
            /*List<SessionData> listData=this.session.getValues();
            if(listData!=null && !listData.isEmpty()){
                Iterator<SessionData> it=listData.iterator();
                while (it.hasNext()) {
                    SessionData data = it.next();
                    data.setSessionId(this.session.getId());
                    record=new Record();
                    record.pushValue("measure", data.getMeasure());
                    record.pushValue("sessionId", data.getSessionId());
                    insert=connection.insert("session_data", record, "id");
                    boolean success=(insert>0);
                    if(success)
                        data.setId(insert);
                    result=success && result;
                }
            }*/
        }
        return result;        
    }
}
