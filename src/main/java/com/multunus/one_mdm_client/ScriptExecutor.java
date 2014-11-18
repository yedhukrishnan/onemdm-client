package com.multunus.one_mdm_client;

import android.util.Log;

import com.bugsnag.android.Bugsnag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ScriptExecutor {

    int exitValue = 0;
    ScriptExecutionOutput output;
    String scriptResult = "";

    public ScriptExecutionOutput execute(String command) {
        Log.d("one-mdm", "Executing script: " + command);
        try {
            Process process = executeCommand(command);
            process.waitFor();
            exitValue = process.exitValue();

            scriptResult += getExecutionResult(process.getInputStream());
            if(scriptResult.isEmpty()) {
                exitValue = 1;
            }
            scriptResult += getExecutionResult(process.getErrorStream());

            Log.d("one-mdm", "Execution finished with status: " + exitValue);

        } catch (IOException e) {
            e.printStackTrace();
            Bugsnag.notify(e);
        } catch (Exception e) {
            e.printStackTrace();
            Bugsnag.notify(e);
        }

        output = new ScriptExecutionOutput(exitValue, scriptResult);


        return output;
    }

    protected String getExecutionResult(InputStream stream) throws IOException {
        StringBuffer result = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        int read;
        char[] buffer = new char[4096];
        while ((read = reader.read(buffer)) > 0) {
            result.append(buffer, 0, read);
        }
        reader.close();
        return result.toString();
    }

    protected Process executeCommand(String command) throws IOException {
        return this.getRuntime().exec(command);
    }

    protected Runtime getRuntime() {
        return Runtime.getRuntime();
    }

}