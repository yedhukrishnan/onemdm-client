package com.multunus.one_mdm_client;

import com.bugsnag.android.Bugsnag;

import java.io.IOException;

public class ScriptExecutor {

    int exitValue = 0;
    ScriptExecutionOutput output;

    public ScriptExecutionOutput execute(String command) {
        try {
            Process process = executeCommand(command);
            process.waitFor();
            exitValue = process.exitValue();

        } catch (IOException e) {
            e.printStackTrace();
            Bugsnag.notify(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        output = new ScriptExecutionOutput(exitValue);

        return output;
    }

    protected Process executeCommand(String command) throws IOException {
        return this.getRuntime().exec(command);
    }

    protected Runtime getRuntime() {
        return Runtime.getRuntime();
    }

}
