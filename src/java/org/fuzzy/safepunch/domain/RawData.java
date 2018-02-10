/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.fuzzy.utils.DateFormatter;
import org.fuzzy.utils.NumericFormatter;

/**
 *
 * @author matteo
 */
public class RawData {
    
    private Date datetime;
    private List<Double> listValues=new ArrayList<>();
    
    public RawData(){
        super();
    }

    public List<Double> getListValues() {
        return listValues;
    }

    public void setListValues(List<Double> listValues) {
        this.listValues = listValues;
    }
    
    public void pushValues(List<Double> values){
        this.listValues.addAll(values);
    }
    
    public void pushValue(double value){
        this.listValues.add(value);
    }

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        if(!this.listValues.isEmpty()){
            Iterator<Double> it=this.listValues.iterator();
            builder.append(DateFormatter.toLocaleDateTimeMilliseconds(this.datetime)).append(";");
            while (it.hasNext()) {
                Double value = it.next();
                builder.append(NumericFormatter.formatDouble6(value)).append(";");
            }
        }
        return builder.toString();
    }
    
    public int size(){
        return this.listValues.size();
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
    
     
    
}
