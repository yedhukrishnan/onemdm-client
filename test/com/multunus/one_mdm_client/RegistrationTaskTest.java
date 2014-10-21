package com.multunus.one_mdm_client;
import static org.robolectric.util.Strings.fromStream;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.multunus.one_mdm_client.RegistrationTask;


@RunWith(RobolectricTestRunner.class)
public class RegistrationTaskTest {
	
	@Test
	public void shouldRegisterTheDeviceToTheServer() throws Exception {
		String deviceName = "Test Device 123";
		HttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, "OK");
		Robolectric.addPendingHttpResponse(httpResponse);
		
		new RegistrationTask().execute(deviceName);
		Robolectric.runBackgroundTasks();
		
		HttpPost postRequest = (HttpPost)Robolectric.getLatestSentHttpRequest();	
		String postBody = fromStream(postRequest.getEntity().getContent());
		JSONObject registrationData = new JSONObject(postBody);
		
		Assert.assertEquals(deviceName, registrationData.get("name"));
	}
}
