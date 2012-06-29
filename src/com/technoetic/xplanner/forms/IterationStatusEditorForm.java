package com.technoetic.xplanner.forms;

import javax.servlet.ServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * User: Mateusz Prokopowicz
 * Date: Jan 10, 2005
 * Time: 3:05:49 PM
 */
public class IterationStatusEditorForm extends ActionForm{
    private String operation;
    private String oid;
    public static final String SAVE_TIME_ATTR = "saveTime";
    private boolean closeIterations;
    private boolean iterationStartConfirmed;

   public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public boolean isCloseIterations() {
       return closeIterations;
    }

    public void setCloseIterations(boolean closeIterations) {
       this.closeIterations = closeIterations;
    }

    public boolean isIterationStartConfirmed() {
       return iterationStartConfirmed;
    }

    public void setIterationStartConfirmed(boolean iterationStartConfirmed) {
       this.iterationStartConfirmed = iterationStartConfirmed;
    }

    public void reset(ActionMapping mapping, ServletRequest request) {
       super.reset(mapping, request);
       this.closeIterations = false;
       this.iterationStartConfirmed =false;
    }
}
