package com.multunus.one_mdm_client;

public class ScriptExecutionOutput {

    final static String FAILED = "Failed";
    final static String SUCCEEDED = "Success";
    private int scriptID;
    private int exitValue;
    private String result;

    public ScriptExecutionOutput(int exitValue, String result) {
        this.exitValue = exitValue;
        this.result = result;
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

    public String getResult() {
        return result;
    }


}
