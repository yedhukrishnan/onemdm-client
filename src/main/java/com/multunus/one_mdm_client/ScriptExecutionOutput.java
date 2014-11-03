package com.multunus.one_mdm_client;

public class ScriptExecutionOutput {

    final static String FAILED = "failed";
    final static String SUCCEEDED = "succeeded";
    private int scriptID;
    private int exitValue;

    public ScriptExecutionOutput(int exitValue) {
        this.exitValue = exitValue;
    }

    public ScriptExecutionOutput(int scriptID, int exitValue) {
        this.scriptID = scriptID;
        this.exitValue = exitValue;
    }


    public void setScriptID(int scriptID) {
        this.scriptID = scriptID;
    }

    public int getScriptID() {
        return scriptID;
    }

    public String getStatus() {
        return (exitValue == 0) ? FAILED : SUCCEEDED;
    }

}
