package com.multunus.one_mdm_client;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import static org.robolectric.util.Strings.fromStream;

@RunWith(RobolectricTestRunner.class)
public class StatusUpdaterTest {

    private String jsonResponse;
    private int id;
    private int exitValue;
    private ScriptExecutionOutput scriptExecutionOutput;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jsonResponse = "{\"id\": 123}";
        id = 123;
        exitValue = 0;
    }

    @Test
    public void shouldUpdateTheStatusOfScriptExecutionToTheServer() throws IOException, JSONException {
        scriptExecutionOutput = generateScriptExecutionOutput(id, exitValue);

        HttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, "OK");
        httpResponse.setEntity(new StringEntity(jsonResponse));
        Robolectric.addPendingHttpResponse(httpResponse);

        new StatusUpdater().execute(scriptExecutionOutput);
        Robolectric.runBackgroundTasks();

        HttpPost postRequest = (HttpPost) Robolectric.getLatestSentHttpRequest();
        String postBody = fromStream(postRequest.getEntity().getContent());
        JSONObject registrationData = new JSONObject(postBody);

        Assert.assertEquals(ScriptExecutionOutput.SUCCEEDED, registrationData.get("status"));
    }

    @Test
    public void shouldGenerateURLForUpdatingStatus() {
        String expectedURL = Config.SERVER + "script/" + id;
        junit.framework.Assert.assertEquals(expectedURL, (new StatusUpdater()).generateUrl(id));
    }

    private ScriptExecutionOutput generateScriptExecutionOutput(int id, int exitValue) {
        return new ScriptExecutionOutput(id, exitValue);
    }

}
