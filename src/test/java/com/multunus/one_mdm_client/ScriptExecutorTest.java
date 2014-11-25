package com.multunus.one_mdm_client;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.io.InputStream;

@RunWith(RobolectricTestRunner.class)
public class ScriptExecutorTest {

    @Mock Runtime runTime;
    @Spy ScriptExecutor scriptExecutor;
    @Mock Process process;
    private String command;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        runTime = Mockito.mock(Runtime.getRuntime().getClass());
        process = Mockito.mock(Process.class);
        scriptExecutor = Mockito.spy(new ScriptExecutor());
        Mockito.doReturn(runTime).when(scriptExecutor).getRuntime();
        command = "df";
    }

    @Test
    public void shouldExecuteCommandOnTheShell() throws IOException {
        scriptExecutor.executeCommand(command);
        Mockito.verify(runTime, Mockito.times(1)).exec(Mockito.eq(command));
    }

    @Test
    public void shouldExecuteScriptAndReturnTheStatusAndResult() throws IOException, InterruptedException {
        Mockito.doReturn(process).when(scriptExecutor).executeCommand(Mockito.anyString());
        Mockito.doReturn(0).when(process).waitFor();
        Mockito.doReturn(1).when(process).exitValue();
        Mockito.doReturn("output").when(scriptExecutor).getExecutionResult(Mockito.any(InputStream.class));
        ScriptExecutionOutput output = scriptExecutor.execute(command);

        Mockito.verify(scriptExecutor, Mockito.times(1)).executeCommand(Mockito.eq("sh"));
        Assert.assertEquals(ScriptExecutionOutput.SUCCEEDED, output.getStatus());
        Assert.assertEquals("outputoutput", output.getResult());
    }

}
