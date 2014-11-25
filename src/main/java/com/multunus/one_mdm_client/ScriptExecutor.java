package com.multunus.one_mdm_client;

import android.util.Log;

import com.bugsnag.android.Bugsnag;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ScriptExecutor {

    int exitValue = 0;
    ScriptExecutionOutput output;
    String scriptResult = "";
    String terminalSession = "";

    public ScriptExecutionOutput execute(String commands, boolean rootPermissionRequired) {
        Log.d("one-mdm", "Executing script: " + commands);
        try {
            terminalSession = getTerminalSession(rootPermissionRequired);
            Process process = executeCommand(terminalSession);

            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            InputStream inputStream = new ByteArrayInputStream(commands.getBytes());
            BufferedReader commandReader = new BufferedReader(new InputStreamReader(inputStream));

            String command;
            try {
                while((command = commandReader.readLine()) != null){
                    os.writeBytes(command + "\n");
                    os.flush();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            os.writeBytes("exit" + "\n");
            os.flush();

            process.waitFor();

            scriptResult += getExecutionResult(process.getInputStream());
            exitValue = scriptResult.isEmpty()? 1 : 0;
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

    private String getTerminalSession(boolean rootPermissionRequired) {
        return (rootPermissionRequired? "su" : "sh");
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