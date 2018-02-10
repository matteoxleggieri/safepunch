/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fuzzy.safepunch.operation;

import org.fuzzy.contact.operation.JdbcOperation;

/**
 *
 * @author matteo
 */
public abstract class SafePunchOperation  extends JdbcOperation {
    
    public SafePunchOperation(){
        super("safe-punch-db.properties");
    }
}
